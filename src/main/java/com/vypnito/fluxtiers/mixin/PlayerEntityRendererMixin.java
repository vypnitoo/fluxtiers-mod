package com.vypnito.fluxtiers.mixin;

import com.vypnito.fluxtiers.FluxTiersMod;
import com.vypnito.fluxtiers.models.PlayerTier;
import com.vypnito.fluxtiers.util.TierFormatter;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void addTierAboveName(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        String playerName = player.getName().getString();
        PlayerTier tier = FluxTiersMod.getTierCache().getTierSync(playerName);

        if (tier != null && tier.isVerified() && !tier.getTiers().isEmpty()) {
            PlayerTier.GamemodeTier highestTier = tier.getHighestTier();
            if (highestTier != null) {
                String tierDisplay = TierFormatter.formatTierDisplay(highestTier.getTier());

                matrices.push();
                matrices.translate(0.0D, player.getHeight() + 0.5D, 0.0D);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);

                this.dispatcher.getTextRenderer().draw(
                    Text.literal(tierDisplay),
                    -(this.dispatcher.getTextRenderer().getWidth(tierDisplay) / 2.0F),
                    0,
                    0xFFFFFF,
                    false,
                    matrices.peek().getPositionMatrix(),
                    vertexConsumers,
                    false,
                    0,
                    light
                );

                matrices.pop();
            }
        }
    }
}
