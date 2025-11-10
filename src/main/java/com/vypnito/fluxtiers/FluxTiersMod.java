package com.vypnito.fluxtiers;

import com.vypnito.fluxtiers.api.TierApiClient;
import com.vypnito.fluxtiers.api.TierCache;
import com.vypnito.fluxtiers.clan.ClanManager;
import com.vypnito.fluxtiers.commands.ClanCommand;
import com.vypnito.fluxtiers.commands.TierCommand;
import com.vypnito.fluxtiers.commands.TierRefreshCommand;
import com.vypnito.fluxtiers.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FluxTiersMod implements ClientModInitializer {
    public static final String MOD_ID = "fluxtiers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static TierApiClient apiClient;
    private static TierCache tierCache;
    private static ModConfig config;
    private static ClanManager clanManager;

    @Override
    public void onInitializeClient() {
        LOGGER.info("FluxTiers mod initializing...");

        config = ModConfig.load();
        apiClient = new TierApiClient(config.getApiUrl());
        tierCache = new TierCache(apiClient);
        clanManager = new ClanManager();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            TierCommand.register(dispatcher);
            TierRefreshCommand.register(dispatcher);
            ClanCommand.register(dispatcher);
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            onPlayerJoin(client);
        });

        LOGGER.info("FluxTiers mod initialized successfully!");
    }

    private void onPlayerJoin(MinecraftClient client) {
        if (client.player == null) return;

        String playerName = client.player.getName().getString();

        tierCache.getTierAsync(playerName, tier -> {
            if (tier == null || !tier.isVerified()) {
                client.execute(() -> {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§c§l[FluxTiers] §7You are not ranked yet!"), false);
                        client.player.sendMessage(Text.literal("§7Get tested and earn your tier at §b§nfluxsmp.fun"), false);
                    }
                });
            }
        });

        tierCache.refreshPlayerTier(playerName);
    }

    public static TierApiClient getApiClient() {
        return apiClient;
    }

    public static TierCache getTierCache() {
        return tierCache;
    }

    public static ModConfig getConfig() {
        return config;
    }

    public static ClanManager getClanManager() {
        return clanManager;
    }
}
