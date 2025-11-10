package com.vypnito.fluxtiers.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.models.PlayerTier;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TierApiClient {
    private static final Gson GSON = new Gson();
    private final OkHttpClient httpClient;
    private final String baseUrl;

    public TierApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient.Builder()
                .build();
    }

    public void fetchTierAsync(String username, Consumer<PlayerTier> callback) {
        CompletableFuture.runAsync(() -> {
            PlayerTier tier = fetchTierSync(username);
            callback.accept(tier);
        });
    }

    public PlayerTier fetchTierSync(String username) {
        String url = baseUrl + "/" + username;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                FluxTiersMod.LOGGER.warn("Failed to fetch tier for {}: HTTP {}", username, response.code());
                return null;
            }

            String body = response.body().string();
            JsonObject json = GSON.fromJson(body, JsonObject.class);

            String playerName = json.get("username").getAsString();
            boolean verified = json.has("verified") && json.get("verified").getAsBoolean();

            List<PlayerTier.GamemodeTier> tiers = new ArrayList<>();

            if (json.has("tiers") && json.get("tiers").isJsonArray()) {
                JsonArray tiersArray = json.getAsJsonArray("tiers");
                for (int i = 0; i < tiersArray.size(); i++) {
                    JsonObject tierObj = tiersArray.get(i).getAsJsonObject();
                    String gamemode = tierObj.get("gamemode").getAsString();
                    String tier = tierObj.get("tier").getAsString();
                    String icon = tierObj.has("icon") ? tierObj.get("icon").getAsString() : "";

                    tiers.add(new PlayerTier.GamemodeTier(gamemode, tier, icon));
                }
            }

            return new PlayerTier(playerName, tiers, verified);

        } catch (IOException e) {
            FluxTiersMod.LOGGER.error("Error fetching tier for {}", username, e);
            return null;
        }
    }
}
