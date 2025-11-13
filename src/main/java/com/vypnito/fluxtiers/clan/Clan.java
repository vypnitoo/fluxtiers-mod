package com.vypnito.fluxtiers.clan;

import java.util.*;

public class Clan {
    private final String name;
    private final String tag;
    private UUID leaderId;
    private final Map<UUID, ClanRank> members;
    private final Map<UUID, Long> invites;
    private String color;
    private String description;
    private String icon;
    private final long createdAt;

    public Clan(String name, String tag, UUID leaderId) {
        this.name = name;
        this.tag = tag;
        this.leaderId = leaderId;
        this.members = new HashMap<>();
        this.invites = new HashMap<>();
        this.color = "#FFFFFF";
        this.description = "";
        this.icon = "🛡️";
        this.createdAt = System.currentTimeMillis();

        if (leaderId != null) {
            this.members.put(leaderId, ClanRank.LEADER);
        }
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public UUID getLeaderId() {
        return leaderId;
    }

    public Map<UUID, ClanRank> getMembers() {
        return members;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setLeaderId(UUID leaderId) {
        this.leaderId = leaderId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void addMember(UUID playerId, ClanRank rank) {
        members.put(playerId, rank);
        invites.remove(playerId);
    }

    public void removeMember(UUID playerId) {
        members.remove(playerId);
    }

    public void setMemberRank(UUID playerId, ClanRank rank) {
        if (members.containsKey(playerId)) {
            members.put(playerId, rank);
        }
    }

    public ClanRank getMemberRank(UUID playerId) {
        return members.get(playerId);
    }

    public boolean isMember(UUID playerId) {
        return members.containsKey(playerId);
    }

    public boolean isLeader(UUID playerId) {
        return leaderId.equals(playerId);
    }

    public boolean isOfficer(UUID playerId) {
        ClanRank rank = members.get(playerId);
        return rank == ClanRank.OFFICER || rank == ClanRank.LEADER;
    }

    public void invitePlayer(UUID playerId) {
        invites.put(playerId, System.currentTimeMillis());
    }

    public void removeInvite(UUID playerId) {
        invites.remove(playerId);
    }

    public boolean hasInvite(UUID playerId) {
        return invites.containsKey(playerId);
    }

    public void cleanupExpiredInvites() {
        long now = System.currentTimeMillis();
        long expiryTime = 24 * 60 * 60 * 1000; // 24 hours
        invites.entrySet().removeIf(entry -> now - entry.getValue() > expiryTime);
    }

    public enum ClanRank {
        LEADER,
        OFFICER,
        MEMBER
    }
}
