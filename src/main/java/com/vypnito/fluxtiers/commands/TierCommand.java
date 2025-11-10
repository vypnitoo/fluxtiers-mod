package com.vypnito.fluxtiers.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.models.PlayerTier;
import com.vypnito.fluxtiers.util.TierFormatter;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class TierCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("tier")
                .then(argument("player", StringArgumentType.string())
                        .suggests(playerSuggestions())
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "player");
                            return executeTierCommand(context.getSource(), playerName);
                        }))
                .executes(context -> {
                    MinecraftClient client = context.getSource().getClient();
                    if (client.player != null) {
                        return executeTierCommand(context.getSource(), client.player.getName().getString());
                    }
                    return 0;
                }));
    }

    private static SuggestionProvider<FabricClientCommandSource> playerSuggestions() {
        return (context, builder) -> {
            MinecraftClient client = context.getSource().getClient();

            Stream<String> onlinePlayers = Stream.empty();
            if (client.getNetworkHandler() != null) {
                Collection<PlayerListEntry> players = client.getNetworkHandler().getPlayerList();
                onlinePlayers = players.stream()
                        .map(entry -> entry.getProfile().getName());
            }

            Set<String> cachedPlayers = FluxTiersMod.getTierCache().getCachedPlayerNames();

            Stream<String> allPlayers = Stream.concat(onlinePlayers, cachedPlayers.stream())
                    .distinct()
                    .filter(name -> name.toLowerCase().startsWith(builder.getRemaining().toLowerCase()));

            return CommandSource.suggestMatching(allPlayers, builder);
        };
    }

    private static int executeTierCommand(FabricClientCommandSource source, String playerName) {
        source.sendFeedback(Text.literal("§7Fetching tier data for §b" + playerName + "§7..."));

        FluxTiersMod.getTierCache().getTierAsync(playerName, tier -> {
            MinecraftClient client = source.getClient();
            client.execute(() -> {
                if (tier == null || !tier.isVerified()) {
                    source.sendFeedback(Text.literal("§c§l[FluxTiers] §7" + playerName + " is not ranked yet!"));
                    source.sendFeedback(Text.literal("§7Get tested at §b§nfluxsmp.fun"));
                    return;
                }

                source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
                source.sendFeedback(Text.literal("§e§lFluxTiers §7- §f" + tier.getUsername()));
                source.sendFeedback(Text.literal(""));

                if (tier.getTiers().isEmpty()) {
                    source.sendFeedback(Text.literal("  §7No tiers found"));
                } else {
                    for (PlayerTier.GamemodeTier gmt : tier.getTiers()) {
                        String color = TierFormatter.getTierColor(gmt.getTier());
                        source.sendFeedback(Text.literal("  §7" + gmt.getGamemode() + ": " + color + gmt.getTier()));
                    }
                }

                source.sendFeedback(Text.literal(""));
                source.sendFeedback(Text.literal("§6§l═══════════════════════════════"));
            });
        });

        return 1;
    }
}
