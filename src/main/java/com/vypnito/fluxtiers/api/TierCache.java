package com.vypnito.fluxtiers.api;

import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.models.PlayerTier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class TierCache {
    private final TierApiClient apiClient;
    private final Map<String, CachedTier> cache = new ConcurrentHashMap<>();
    private final long cacheExpiry;

    public TierCache(TierApiClient apiClient) {
        this.apiClient = apiClient;
        this.cacheExpiry = FluxTiersMod.getConfig().getCacheExpiry() * 1000L;

        startAutoRefreshTask();
    }

    public void getTierAsync(String username, Consumer<PlayerTier> callback) {
        String key = username.toLowerCase();

        if (cache.containsKey(key)) {
            CachedTier cached = cache.get(key);
            if (!cached.isExpired()) {
                callback.accept(cached.getTier());
                return;
            }
        }

        apiClient.fetchTierAsync(username, tier -> {
            if (tier != null) {
                cache.put(key, new CachedTier(tier));
            }
            callback.accept(tier);
        });
    }

    public PlayerTier getTierSync(String username) {
        String key = username.toLowerCase();

        if (cache.containsKey(key)) {
            CachedTier cached = cache.get(key);
            if (!cached.isExpired()) {
                return cached.getTier();
            }
        }

        PlayerTier tier = apiClient.fetchTierSync(username);
        if (tier != null) {
            cache.put(key, new CachedTier(tier));
        }
        return tier;
    }

    public void refreshPlayerTier(String username) {
        apiClient.fetchTierAsync(username, tier -> {
            if (tier != null) {
                cache.put(username.toLowerCase(), new CachedTier(tier));
                FluxTiersMod.LOGGER.debug("Refreshed tier for {}", username);
            }
        });
    }

    public void clearCache() {
        cache.clear();
        FluxTiersMod.LOGGER.info("Tier cache cleared");
    }

    public Set<String> getCachedPlayerNames() {
        return new HashSet<>(cache.keySet());
    }

    private void startAutoRefreshTask() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cleanExpiredEntries();
            }
        }, cacheExpiry, cacheExpiry);
    }

    private void cleanExpiredEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private class CachedTier {
        private final PlayerTier tier;
        private final long timestamp;

        public CachedTier(PlayerTier tier) {
            this.tier = tier;
            this.timestamp = System.currentTimeMillis();
        }

        public PlayerTier getTier() {
            return tier;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > cacheExpiry;
        }
    }
}
