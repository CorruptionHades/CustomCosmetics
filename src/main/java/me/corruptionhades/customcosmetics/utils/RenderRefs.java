package me.corruptionhades.customcosmetics.utils;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RenderRefs {

    public static Matrix4f projectionMatrix, positionMatrix;
    public static Vec3d cameraPos;

    public static Matrix4f lastProjMat, lastModelMat, lastWorldSpaceMatrix;

    public static void onRender(MatrixStack matrices, Matrix4f positionMatrix, Matrix4f projectionMatrix) {
        CustomCosmeticsClient.getInstance().blub.draw(matrices, new Matrix4f(), new Vec3d(0, 200, 0));
    }
}
