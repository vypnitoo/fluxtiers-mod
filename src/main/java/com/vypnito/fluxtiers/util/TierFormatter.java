package com.vypnito.fluxtiers.util;

public class TierFormatter {

    public static String getTierColor(String tier) {
        if (tier == null || tier.isEmpty()) {
            return "§7";
        }

        String tierUpper = tier.toUpperCase();

        if (tierUpper.startsWith("S")) {
            return "§6";
        } else if (tierUpper.startsWith("A")) {
            return "§a";
        } else if (tierUpper.startsWith("B")) {
            return "§9";
        } else if (tierUpper.startsWith("C")) {
            return "§e";
        } else if (tierUpper.startsWith("D")) {
            return "§c";
        } else if (tierUpper.startsWith("E") || tierUpper.startsWith("F")) {
            return "§7";
        }

        return "§7";
    }

    public static String formatTierDisplay(String tier) {
        if (tier == null || tier.isEmpty()) {
            return "";
        }
        String color = getTierColor(tier);
        return color + "[" + tier + "]";
    }

    public static String formatPlayerNameWithTier(String playerName, String tier) {
        if (tier == null || tier.isEmpty()) {
            return playerName;
        }
        return playerName + " " + formatTierDisplay(tier);
    }
}
