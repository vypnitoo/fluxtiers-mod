package com.vypnito.fluxtiers.mixin;

import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.models.PlayerTier;
import com.vypnito.fluxtiers.util.TierFormatter;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void addTierToPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        String playerName = entry.getProfile().getName();
        PlayerTier tier = FluxTiersMod.getTierCache().getTierSync(playerName);

        if (tier != null && tier.isVerified() && !tier.getTiers().isEmpty()) {
            PlayerTier.GamemodeTier highestTier = tier.getHighestTier();
            if (highestTier != null) {
                String originalText = cir.getReturnValue().getString();
                String tierDisplay = TierFormatter.formatTierDisplay(highestTier.getTier());
                cir.setReturnValue(Text.literal(originalText + " " + tierDisplay));
            }
        }
    }
}
