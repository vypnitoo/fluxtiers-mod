package com.vypnito.fluxtiers.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.clan.Clan;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ClanApiClient {
    private static final Gson GSON = new Gson();
    private final OkHttpClient httpClient;
    private final String baseUrl;

    public ClanApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient.Builder()
                .build();
    }

    public void fetchAllClansAsync(Consumer<Map<String, Clan>> callback) {
        CompletableFuture.runAsync(() -> {
            Map<String, Clan> clans = fetchAllClansSync();
            callback.accept(clans);
        });
    }

    public Map<String, Clan> fetchAllClansSync() {
        String url = baseUrl + "/all";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Map<String, Clan> clans = new HashMap<>();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                FluxTiersMod.LOGGER.warn("Failed to fetch clans: HTTP {}", response.code());
                return clans;
            }

            String body = response.body().string();
            JsonObject json = GSON.fromJson(body, JsonObject.class);

            if (json.has("clans") && json.get("clans").isJsonArray()) {
                JsonArray clansArray = json.getAsJsonArray("clans");
                for (int i = 0; i < clansArray.size(); i++) {
                    JsonObject clanObj = clansArray.get(i).getAsJsonObject();

                    String name = clanObj.get("name").getAsString();
                    String tag = clanObj.get("tag").getAsString();
                    String leaderDiscordId = clanObj.get("leader_discord_id").getAsString();
                    String color = clanObj.has("color") ? clanObj.get("color").getAsString() : "#00FFFF";
                    String description = clanObj.has("description") && !clanObj.get("description").isJsonNull()
                            ? clanObj.get("description").getAsString() : "";
                    String icon = clanObj.has("icon") ? clanObj.get("icon").getAsString() : "🛡️";

                    Clan clan = new Clan(name, tag, UUID.randomUUID());
                    clan.setColor(color);
                    clan.setDescription(description);
                    clan.setIcon(icon);

                    if (clanObj.has("members") && clanObj.get("members").isJsonArray()) {
                        JsonArray membersArray = clanObj.getAsJsonArray("members");
                        for (int j = 0; j < membersArray.size(); j++) {
                            JsonObject memberObj = membersArray.get(j).getAsJsonObject();
                            String minecraftUuid = memberObj.has("minecraft_uuid") && !memberObj.get("minecraft_uuid").isJsonNull()
                                    ? memberObj.get("minecraft_uuid").getAsString() : null;
                            String rank = memberObj.get("rank").getAsString();

                            if (minecraftUuid != null) {
                                try {
                                    UUID uuid = UUID.fromString(minecraftUuid);
                                    Clan.ClanRank clanRank = Clan.ClanRank.valueOf(rank);
                                    clan.getMembers().put(uuid, clanRank);

                                    if (rank.equals("LEADER")) {
                                        clan.setLeaderId(uuid);
                                    }
                                } catch (IllegalArgumentException e) {
                                    FluxTiersMod.LOGGER.warn("Invalid UUID or rank for clan member: {}", minecraftUuid);
                                }
                            }
                        }
                    }

                    clans.put(tag.toUpperCase(), clan);
                }
            }

            FluxTiersMod.LOGGER.info("Fetched {} clans from API", clans.size());
            return clans;

        } catch (IOException e) {
            FluxTiersMod.LOGGER.error("Error fetching clans", e);
            return clans;
        }
    }

    public Clan fetchClanSync(String tag) {
        String url = baseUrl + "/" + tag;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                FluxTiersMod.LOGGER.warn("Failed to fetch clan {}: HTTP {}", tag, response.code());
                return null;
            }

            String body = response.body().string();
            JsonObject json = GSON.fromJson(body, JsonObject.class);

            if (json.has("clan")) {
                JsonObject clanObj = json.getAsJsonObject("clan");

                String name = clanObj.get("name").getAsString();
                String clanTag = clanObj.get("tag").getAsString();
                String color = clanObj.has("color") ? clanObj.get("color").getAsString() : "#00FFFF";
                String description = clanObj.has("description") && !clanObj.get("description").isJsonNull()
                        ? clanObj.get("description").getAsString() : "";
                String icon = clanObj.has("icon") ? clanObj.get("icon").getAsString() : "🛡️";

                Clan clan = new Clan(name, clanTag, UUID.randomUUID());
                clan.setColor(color);
                clan.setDescription(description);
                clan.setIcon(icon);

                if (clanObj.has("members") && clanObj.get("members").isJsonArray()) {
                    JsonArray membersArray = clanObj.getAsJsonArray("members");
                    for (int i = 0; i < membersArray.size(); i++) {
                        JsonObject memberObj = membersArray.get(i).getAsJsonObject();
                        String minecraftUuid = memberObj.has("minecraft_uuid") && !memberObj.get("minecraft_uuid").isJsonNull()
                                ? memberObj.get("minecraft_uuid").getAsString() : null;
                        String rank = memberObj.get("rank").getAsString();

                        if (minecraftUuid != null) {
                            try {
                                UUID uuid = UUID.fromString(minecraftUuid);
                                Clan.ClanRank clanRank = Clan.ClanRank.valueOf(rank);
                                clan.getMembers().put(uuid, clanRank);

                                if (rank.equals("LEADER")) {
                                    clan.setLeaderId(uuid);
                                }
                            } catch (IllegalArgumentException e) {
                                FluxTiersMod.LOGGER.warn("Invalid UUID or rank for clan member: {}", minecraftUuid);
                            }
                        }
                    }
                }

                return clan;
            }

            return null;

        } catch (IOException e) {
            FluxTiersMod.LOGGER.error("Error fetching clan {}", tag, e);
            return null;
        }
    }
}
