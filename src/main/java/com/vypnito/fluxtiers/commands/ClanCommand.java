package com.vypnito.fluxtiers.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.clan.Clan;
import com.vypnito.fluxtiers.clan.ClanManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.UUID;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClanCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("clan")
                .then(literal("create")
                        .then(argument("name", StringArgumentType.string())
                                .then(argument("tag", StringArgumentType.string())
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "name");
                                            String tag = StringArgumentType.getString(context, "tag");
                                            return createClan(context.getSource(), name, tag);
                                        }))))
                .then(literal("disband")
                        .executes(context -> disbandClan(context.getSource())))
                .then(literal("invite")
                        .then(argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    return invitePlayer(context.getSource(), playerName);
                                })))
                .then(literal("kick")
                        .then(argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    return kickPlayer(context.getSource(), playerName);
                                })))
                .then(literal("leave")
                        .executes(context -> leaveClan(context.getSource())))
                .then(literal("accept")
                        .executes(context -> acceptInvite(context.getSource())))
                .then(literal("decline")
                        .executes(context -> declineInvite(context.getSource())))
                .then(literal("promote")
                        .then(argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    return promotePlayer(context.getSource(), playerName);
                                })))
                .then(literal("demote")
                        .then(argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    return demotePlayer(context.getSource(), playerName);
                                })))
                .then(literal("info")
                        .executes(context -> showClanInfo(context.getSource())))
                .then(literal("list")
                        .executes(context -> listClans(context.getSource())))
                .executes(context -> showHelp(context.getSource())));
    }

    private static int createClan(FabricClientCommandSource source, String name, String tag) {
        MinecraftClient client = source.getClient();
        if (client.player == null) return 0;

        ClanManager manager = FluxTiersMod.getClanManager();
        UUID playerId = client.player.getUuid();

        if (manager.getPlayerClan(playerId) != null) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7You are already in a clan!"));
            return 0;
        }

        if (tag.length() > 6) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7Clan tag must be 6 characters or less!"));
            return 0;
        }

        Clan clan = manager.createClan(name, tag, playerId);
        if (clan == null) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7A clan with that tag already exists!"));
            return 0;
        }

        source.sendFeedback(Text.literal("§a§l[Clan] §7Successfully created clan §e" + name + " §7[" + tag + "]"));
        return 1;
    }

    private static int disbandClan(FabricClientCommandSource source) {
        MinecraftClient client = source.getClient();
        if (client.player == null) return 0;

        ClanManager manager = FluxTiersMod.getClanManager();
        UUID playerId = client.player.getUuid();
        Clan clan = manager.getPlayerClan(playerId);

        if (clan == null) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7You are not in a clan!"));
            return 0;
        }

        if (!manager.disbandClan(clan.getTag(), playerId)) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7Only the clan leader can disband the clan!"));
            return 0;
        }

        source.sendFeedback(Text.literal("§a§l[Clan] §7Successfully disbanded clan!"));
        return 1;
    }

    private static int invitePlayer(FabricClientCommandSource source, String playerName) {
        MinecraftClient client = source.getClient();
        if (client.player == null) return 0;

        ClanManager manager = FluxTiersMod.getClanManager();
        UUID playerId = client.player.getUuid();
        Clan clan = manager.getPlayerClan(playerId);

        if (clan == null) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7You are not in a clan!"));
            return 0;
        }

        if (!clan.isOfficer(playerId)) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7Only officers and leaders can invite players!"));
            return 0;
        }

        source.sendFeedback(Text.literal("§e§l[Clan] §7Invite feature requires the player to be online. Share your clan tag with them!"));
        source.sendFeedback(Text.literal("§7They can join using: §b/clan join " + clan.getTag()));
        return 1;
    }

    private static int kickPlayer(FabricClientCommandSource source, String playerName) {
        source.sendFeedback(Text.literal("§c§l[Clan] §7This feature is under development."));
        return 1;
    }

    private static int leaveClan(FabricClientCommandSource source) {
        MinecraftClient client = source.getClient();
        if (client.player == null) return 0;

        ClanManager manager = FluxTiersMod.getClanManager();
        UUID playerId = client.player.getUuid();
        Clan clan = manager.getPlayerClan(playerId);

        if (clan == null) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7You are not in a clan!"));
            return 0;
        }

        if (clan.isLeader(playerId)) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7Leaders cannot leave! Use §e/clan disband§7 instead."));
            return 0;
        }

        manager.removeMemberFromClan(clan.getTag(), playerId, playerId);
        source.sendFeedback(Text.literal("§a§l[Clan] §7You have left the clan."));
        return 1;
    }

    private static int acceptInvite(FabricClientCommandSource source) {
        source.sendFeedback(Text.literal("§c§l[Clan] §7This feature is under development."));
        return 1;
    }

    private static int declineInvite(FabricClientCommandSource source) {
        source.sendFeedback(Text.literal("§c§l[Clan] §7This feature is under development."));
        return 1;
    }

    private static int promotePlayer(FabricClientCommandSource source, String playerName) {
        source.sendFeedback(Text.literal("§c§l[Clan] §7This feature is under development."));
        return 1;
    }

    private static int demotePlayer(FabricClientCommandSource source, String playerName) {
        source.sendFeedback(Text.literal("§c§l[Clan] §7This feature is under development."));
        return 1;
    }

    private static int showClanInfo(FabricClientCommandSource source) {
        MinecraftClient client = source.getClient();
        if (client.player == null) return 0;

        ClanManager manager = FluxTiersMod.getClanManager();
        UUID playerId = client.player.getUuid();
        Clan clan = manager.getPlayerClan(playerId);

        if (clan == null) {
            source.sendFeedback(Text.literal("§c§l[Clan] §7You are not in a clan!"));
            return 0;
        }

        source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
        source.sendFeedback(Text.literal("§e§lClan Info §7- §f" + clan.getName() + " [" + clan.getTag() + "]"));
        source.sendFeedback(Text.literal(""));
        source.sendFeedback(Text.literal("§7Members: §f" + clan.getMembers().size()));
        source.sendFeedback(Text.literal("§7Your Rank: §f" + clan.getMemberRank(playerId).name()));
        source.sendFeedback(Text.literal(""));
        source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
        return 1;
    }

    private static int listClans(FabricClientCommandSource source) {
        ClanManager manager = FluxTiersMod.getClanManager();

        source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
        source.sendFeedback(Text.literal("§e§lAll Clans"));
        source.sendFeedback(Text.literal(""));

        if (manager.getAllClans().isEmpty()) {
            source.sendFeedback(Text.literal("§7No clans exist yet!"));
        } else {
            for (Clan clan : manager.getAllClans()) {
                source.sendFeedback(Text.literal("§7[" + clan.getTag() + "] §f" + clan.getName() + " §7(" + clan.getMembers().size() + " members)"));
            }
        }

        source.sendFeedback(Text.literal(""));
        source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
        return 1;
    }

    private static int showHelp(FabricClientCommandSource source) {
        source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
        source.sendFeedback(Text.literal("§e§lClan Commands"));
        source.sendFeedback(Text.literal(""));
        source.sendFeedback(Text.literal("§b/clan create <name> <tag> §7- Create a clan"));
        source.sendFeedback(Text.literal("§b/clan info §7- View your clan info"));
        source.sendFeedback(Text.literal("§b/clan list §7- List all clans"));
        source.sendFeedback(Text.literal("§b/clan invite <player> §7- Invite a player"));
        source.sendFeedback(Text.literal("§b/clan leave §7- Leave your clan"));
        source.sendFeedback(Text.literal("§b/clan disband §7- Disband your clan (leader only)"));
        source.sendFeedback(Text.literal(""));
        source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
        return 1;
    }
}
