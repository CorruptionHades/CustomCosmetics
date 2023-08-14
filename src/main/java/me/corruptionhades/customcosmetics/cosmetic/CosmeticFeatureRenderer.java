package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.text.DecimalFormat;

public class CosmeticFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final PlayerEntityModel<?> model;

    public static String text = "Test";

    public CosmeticFeatureRenderer(PlayerEntityModel<?> model, FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if(entity.isSneaking()) {
            matrices.translate(0, 0.2, 0);
        }

     //   matrices.translate(-model.leftArm.roll, 0, 0);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(model.leftArm.roll));

        for(Cosmetic cosmetic : CustomCosmeticsClient.getInstance().getCosmeticManager().getCosmetics()) {

            if(cosmetic.getType() == BodyPart.LEFT_ARM) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.leftArm.pitch));
            }
            else if(cosmetic.getType() == BodyPart.RIGHT_ARM) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.rightArm.pitch));
            }

            cosmetic.render(model, entity, matrices);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        text = df.format(model.leftArm.pitch) + ": " + df.format(model.leftArm.roll) + ", " + df.format(model.leftArm.roll);




    }


}
