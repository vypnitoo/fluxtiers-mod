package com.vypnito.fluxtiers.mixin;

import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.clan.Clan;
import com.vypnito.fluxtiers.models.PlayerTier;
import com.vypnito.fluxtiers.util.TierFormatter;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void addClanAndTierToPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        String playerName = entry.getProfile().getName();
        UUID playerId = entry.getProfile().getId();
        String originalText = cir.getReturnValue().getString();
        StringBuilder displayName = new StringBuilder();

        Clan clan = FluxTiersMod.getClanManager().getPlayerClan(playerId);
        if (clan != null) {
            displayName.append("§7[§b").append(clan.getTag()).append("§7] ");
        }

        displayName.append(originalText);

        PlayerTier tier = FluxTiersMod.getTierCache().getTierSync(playerName);
        if (tier != null && tier.isVerified() && !tier.getTiers().isEmpty()) {
            PlayerTier.GamemodeTier highestTier = tier.getHighestTier();
            if (highestTier != null) {
                String tierDisplay = TierFormatter.formatTierDisplay(highestTier.getTier());
                displayName.append(" ").append(tierDisplay);
            }
        }

        cir.setReturnValue(Text.literal(displayName.toString()));
    }
}
