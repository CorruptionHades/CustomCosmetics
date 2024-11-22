package me.corruptionhades.customcosmetics.cosmetic.impl.presets;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.md5.MD5FileReader;
import me.corruptionhades.customcosmetics.md5.MD5Renderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.File;

public class MD5PresetTest extends Cosmetic {

    /**
     * The MD5 renderer, can be null when initialization fails
     */
    @Nullable
    private MD5Renderer renderer;

    public MD5PresetTest(File file) {
        super("md5cosemticteicj", BodyPart.BODY, false);

        try {
            renderer = new MD5Renderer(new MD5FileReader().parse(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(PlayerEntityModel model, MatrixStack matrices, PlayerEntityRenderState state) {
        if (renderer != null) {
            renderer.render(matrices, new Matrix4f(), null);
        }
    }

    @Override
    public void loadTexture() {

    }
}
