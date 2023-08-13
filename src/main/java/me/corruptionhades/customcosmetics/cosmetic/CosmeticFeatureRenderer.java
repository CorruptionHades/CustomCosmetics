package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class CosmeticFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public CosmeticFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.translate(0, 0.25, 0.13);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        for(Cosmetic cosmetic : CustomCosmeticsClient.getInstance().getCosmeticManager().getCosmetics()) {
            cosmetic.render(entity, matrices);
        }
    }

    public void renderLeftArm(MatrixStack matrices, AbstractClientPlayerEntity player) {

    }

    public void renderRightArm(MatrixStack matrices, AbstractClientPlayerEntity player) {

    }
}
