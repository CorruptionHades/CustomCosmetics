package me.corruptionhades.customcosmetics.cosmetic.impl;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.objfile.ObjFile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Path;

public class Test extends Cosmetic {

    private ObjFile obj;

    public Test() {
        super("Test", BodyPart.LEFT_ARM);
        try {
            obj = new ObjFile("model.obj", ObjFile.ResourceProvider.ofPath(Path.of("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/shield/Lightning Shield Blue")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(AbstractClientPlayerEntity player, MatrixStack matrices) {
        if(obj != null) {
            Matrix4f matrix4f = new Matrix4f();
            matrix4f.scale(0.15f, 0.15f, 0.15f);
            obj.draw(matrices, matrix4f, new Vec3d(0, 100, 0));
        }
    }
}
