package me.corruptionhades.customcosmetics.cosmetic.impl;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.objfile.ObjFile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
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
    public void render(PlayerEntityModel<?> model, AbstractClientPlayerEntity player, MatrixStack matrices) {
        if(obj != null) {
            Matrix4f matrix4f = new Matrix4f();
            matrix4f.scale(0.12f, 0.12f, 0.12f);
            matrix4f.translate(3.95f, 3f, 0.2f);
            matrix4f.rotate(-190, 0, 1, 0);
            obj.draw(matrices, matrix4f);
        }
    }
}
