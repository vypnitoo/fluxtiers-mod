package com.vypnito.fluxtiers.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.vypnito.fluxtiers.FluxTiersMod;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class TierRefreshCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("tierrefresh")
                .executes(context -> {
                    FluxTiersMod.getTierCache().clearCache();
                    context.getSource().sendFeedback(Text.literal("§a§l[FluxTiers] §7Tier cache cleared and will refresh on next lookup!"));
                    return 1;
                }));
    }
}
