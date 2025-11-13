package com.vypnito.fluxtiers.clan;

import com.google.gson.*;
import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.api.ClanApiClient;

import java.io.*;
import java.util.*;

public class ClanManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CLAN_FILE = new File("config/fluxtiers_clans.json");

    private final Map<String, Clan> clans = new HashMap<>();
    private final Map<UUID, String> playerClans = new HashMap<>();
    private final ClanApiClient apiClient;
    private long lastRefresh = 0;
    private static final long REFRESH_INTERVAL = 5 * 60 * 1000;

    public ClanManager(String apiUrl) {
        this.apiClient = new ClanApiClient(apiUrl);
        load();
        refreshFromApi();
    }

    public Clan createClan(String name, String tag, UUID leaderId) {
        if (clans.containsKey(tag.toUpperCase())) {
            return null;
        }

        if (playerClans.containsKey(leaderId)) {
            return null;
        }

        Clan clan = new Clan(name, tag, leaderId);
        clans.put(tag.toUpperCase(), clan);
        playerClans.put(leaderId, tag.toUpperCase());
        save();
        return clan;
    }

    public boolean disbandClan(String tag, UUID requesterId) {
        Clan clan = clans.get(tag.toUpperCase());
        if (clan == null) return false;

        if (!clan.isLeader(requesterId)) {
            return false;
        }

        clan.getMembers().keySet().forEach(playerClans::remove);
        clans.remove(tag.toUpperCase());
        save();
        return true;
    }

    public Clan getClan(String tag) {
        return clans.get(tag.toUpperCase());
    }

    public Clan getPlayerClan(UUID playerId) {
        refreshIfNeeded();
        String tag = playerClans.get(playerId);
        return tag != null ? clans.get(tag) : null;
    }

    public void refreshFromApi() {
        apiClient.fetchAllClansAsync(fetchedClans -> {
            if (fetchedClans != null && !fetchedClans.isEmpty()) {
                clans.clear();
                playerClans.clear();
                clans.putAll(fetchedClans);

                for (Clan clan : fetchedClans.values()) {
                    for (UUID memberId : clan.getMembers().keySet()) {
                        playerClans.put(memberId, clan.getTag().toUpperCase());
                    }
                }

                lastRefresh = System.currentTimeMillis();
                FluxTiersMod.LOGGER.info("Refreshed {} clans from API", clans.size());
            }
        });
    }

    private void refreshIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastRefresh > REFRESH_INTERVAL) {
            refreshFromApi();
        }
    }

    public boolean addMemberToClan(String tag, UUID playerId, Clan.ClanRank rank) {
        Clan clan = clans.get(tag.toUpperCase());
        if (clan == null) return false;

        if (playerClans.containsKey(playerId)) {
            return false;
        }

        clan.addMember(playerId, rank);
        playerClans.put(playerId, tag.toUpperCase());
        save();
        return true;
    }

    public boolean removeMemberFromClan(String tag, UUID playerId, UUID requesterId) {
        Clan clan = clans.get(tag.toUpperCase());
        if (clan == null) return false;

        if (!clan.isOfficer(requesterId) && !playerId.equals(requesterId)) {
            return false;
        }

        if (clan.isLeader(playerId)) {
            return false;
        }

        clan.removeMember(playerId);
        playerClans.remove(playerId);
        save();
        return true;
    }

    public boolean setMemberRank(String tag, UUID playerId, Clan.ClanRank newRank, UUID requesterId) {
        Clan clan = clans.get(tag.toUpperCase());
        if (clan == null) return false;

        if (!clan.isLeader(requesterId)) {
            return false;
        }

        if (clan.isLeader(playerId)) {
            return false;
        }

        clan.setMemberRank(playerId, newRank);
        save();
        return true;
    }

    public Collection<Clan> getAllClans() {
        return clans.values();
    }

    private void save() {
        try {
            CLAN_FILE.getParentFile().mkdirs();
            JsonObject root = new JsonObject();
            JsonArray clansArray = new JsonArray();

            for (Clan clan : clans.values()) {
                JsonObject clanObj = new JsonObject();
                clanObj.addProperty("name", clan.getName());
                clanObj.addProperty("tag", clan.getTag());
                clanObj.addProperty("leaderId", clan.getLeaderId().toString());
                clanObj.addProperty("color", clan.getColor());
                clanObj.addProperty("description", clan.getDescription());
                clanObj.addProperty("createdAt", clan.getCreatedAt());

                JsonArray membersArray = new JsonArray();
                clan.getMembers().forEach((uuid, rank) -> {
                    JsonObject memberObj = new JsonObject();
                    memberObj.addProperty("uuid", uuid.toString());
                    memberObj.addProperty("rank", rank.name());
                    membersArray.add(memberObj);
                });
                clanObj.add("members", membersArray);

                clansArray.add(clanObj);
            }

            root.add("clans", clansArray);

            try (FileWriter writer = new FileWriter(CLAN_FILE)) {
                GSON.toJson(root, writer);
            }
        } catch (IOException e) {
            FluxTiersMod.LOGGER.error("Failed to save clans", e);
        }
    }

    private void load() {
        if (!CLAN_FILE.exists()) return;

        try (FileReader reader = new FileReader(CLAN_FILE)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null || !root.has("clans")) return;

            JsonArray clansArray = root.getAsJsonArray("clans");
            for (int i = 0; i < clansArray.size(); i++) {
                JsonObject clanObj = clansArray.get(i).getAsJsonObject();

                String name = clanObj.get("name").getAsString();
                String tag = clanObj.get("tag").getAsString();
                UUID leaderId = UUID.fromString(clanObj.get("leaderId").getAsString());

                Clan clan = new Clan(name, tag, leaderId);

                if (clanObj.has("color")) {
                    clan.setColor(clanObj.get("color").getAsString());
                }
                if (clanObj.has("description")) {
                    clan.setDescription(clanObj.get("description").getAsString());
                }

                if (clanObj.has("members")) {
                    JsonArray membersArray = clanObj.getAsJsonArray("members");
                    for (int j = 0; j < membersArray.size(); j++) {
                        JsonObject memberObj = membersArray.get(j).getAsJsonObject();
                        UUID uuid = UUID.fromString(memberObj.get("uuid").getAsString());
                        Clan.ClanRank rank = Clan.ClanRank.valueOf(memberObj.get("rank").getAsString());

                        clan.getMembers().put(uuid, rank);
                        playerClans.put(uuid, tag.toUpperCase());
                    }
                }

                clans.put(tag.toUpperCase(), clan);
            }

            FluxTiersMod.LOGGER.info("Loaded {} clans", clans.size());
        } catch (IOException e) {
            FluxTiersMod.LOGGER.error("Failed to load clans", e);
        }
    }
}
