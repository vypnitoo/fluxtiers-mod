package com.vypnito.fluxtiers.models;

import java.util.List;

public class PlayerTier {
    private final String username;
    private final List<GamemodeTier> tiers;
    private final boolean verified;

    public PlayerTier(String username, List<GamemodeTier> tiers, boolean verified) {
        this.username = username;
        this.tiers = tiers;
        this.verified = verified;
    }

    public String getUsername() {
        return username;
    }

    public List<GamemodeTier> getTiers() {
        return tiers;
    }

    public boolean isVerified() {
        return verified;
    }

    public GamemodeTier getHighestTier() {
        if (tiers == null || tiers.isEmpty()) {
            return null;
        }

        GamemodeTier highest = tiers.get(0);
        for (GamemodeTier tier : tiers) {
            if (compareTiers(tier.getTier(), highest.getTier()) > 0) {
                highest = tier;
            }
        }
        return highest;
    }

    private int compareTiers(String tier1, String tier2) {
        return getTierValue(tier1) - getTierValue(tier2);
    }

    private int getTierValue(String tier) {
        tier = tier.toUpperCase();
        int baseValue = 0;

        if (tier.startsWith("S")) baseValue = 90;
        else if (tier.startsWith("A")) baseValue = 75;
        else if (tier.startsWith("B")) baseValue = 60;
        else if (tier.startsWith("C")) baseValue = 45;
        else if (tier.startsWith("D")) baseValue = 30;
        else if (tier.startsWith("E")) baseValue = 15;
        else if (tier.startsWith("F")) baseValue = 0;

        if (tier.endsWith("+")) baseValue += 2;
        else if (tier.endsWith("-")) baseValue -= 2;
        else baseValue += 1;

        return baseValue;
    }

    public static class GamemodeTier {
        private final String gamemode;
        private final String tier;
        private final String icon;

        public GamemodeTier(String gamemode, String tier, String icon) {
            this.gamemode = gamemode;
            this.tier = tier;
            this.icon = icon;
        }

        public String getGamemode() {
            return gamemode;
        }

        public String getTier() {
            return tier;
        }

        public String getIcon() {
            return icon;
        }
    }
}
