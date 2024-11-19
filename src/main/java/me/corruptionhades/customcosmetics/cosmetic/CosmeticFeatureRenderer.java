package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import me.corruptionhades.customcosmetics.a.objfile.AObjFile;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.file.Path;

public class CosmeticFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {

    private final PlayerEntityModel model;

    public static String text = "Test";

    public CosmeticFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
        this.model = context.getModel();
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if(state.sneaking) {
            matrices.translate(0, 0.2, 0);
        }

        for(Cosmetic cosmetic : CustomCosmeticsClient.getInstance().getCosmeticManager().getCosmetics()) {

            if(cosmetic instanceof ItemCosmetic) continue;

            matrices.push();
            if(cosmetic.getType() == BodyPart.LEFT_ARM) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(model.leftArm.roll));
                matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.leftArm.pitch));
            }
            else if(cosmetic.getType() == BodyPart.RIGHT_ARM) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(model.rightArm.roll));
                matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.rightArm.pitch));
            }
            else if(cosmetic.getType() == BodyPart.BODY) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(model.body.roll));
                matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.body.pitch));
            }

            cosmetic.render(model, matrices);
            matrices.pop();
        }

        // test cosmetic
        matrices.push();

        // Translate to center above the player's head
        matrices.translate(-0.5F, -state.height + 0.25F, -0.5F);
        // Render a diamond block above the player's head
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                Blocks.DIAMOND_BLOCK.getDefaultState(), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();

   //     CustomCosmeticsClient.getInstance().blub.draw(matrices, new Matrix4f(), new Vec3d(state.x, state.y, state.z));
    }
}
