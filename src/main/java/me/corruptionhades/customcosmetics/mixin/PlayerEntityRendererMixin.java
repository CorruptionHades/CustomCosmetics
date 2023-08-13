package me.corruptionhades.customcosmetics.mixin;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.cosmetic.CosmeticFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    private CosmeticFeatureRenderer cosmeticFeatureRenderer;

    @Inject(method = "<init>", at  = @At("TAIL"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(cosmeticFeatureRenderer = new CosmeticFeatureRenderer(this));
    }

    @Inject(at = @At("TAIL"), method = "renderLeftArm")
    private void renderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci) {
        cosmeticFeatureRenderer.renderLeftArm(matrices, player);
    }

    @Inject(at = @At("TAIL"), method = "renderRightArm")
    private void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci) {
        cosmeticFeatureRenderer.renderRightArm(matrices, player);
    }
}
