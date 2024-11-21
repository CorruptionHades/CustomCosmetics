package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import me.corruptionhades.customcosmetics.a.objfile.AObjFile;
import me.corruptionhades.customcosmetics.objfile.TextureObjFile;
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

import java.io.IOException;
import java.nio.file.Path;

public class CosmeticFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {

    private final PlayerEntityModel model;

    public static String text = "Test";

    public CosmeticFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
        this.model = context.getModel();

        try {
            objj = new AObjFile("angel_wings.obj", AObjFile.ResourceProvider.ofPath(Path.of("/home/mitarbeiter/Downloads/"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

            cosmetic.render(model, matrices, state);
            matrices.pop();
        }
    }

    private AObjFile objj;
}
