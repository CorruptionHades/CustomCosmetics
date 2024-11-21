package me.corruptionhades.customcosmetics.cosmetic.impl.presets;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomResourceLocation;
import me.corruptionhades.customcosmetics.objfile.TextureObjFile;
import me.corruptionhades.customcosmetics.utils.render.TextureUtil;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.File;

public class BandanaPreset extends Cosmetic {

    private TextureObjFile obj;

    @Nullable
    private final File texturePath;

    private CustomResourceLocation crl;

    public BandanaPreset(String name, File objFile, @Nullable File texturePath) {
        super(name, BodyPart.HEAD, false);

        try {
            obj = new TextureObjFile(objFile.getName(), TextureObjFile.ResourceProvider.ofPath(objFile.toPath().getParent()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.texturePath = texturePath;
    }

    @Override
    public void render(PlayerEntityModel model, MatrixStack matrices, PlayerEntityRenderState state) {
        if(obj == null) {
            return;
        }

        float scale = 0.065f;

        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.scale(scale, -scale, -scale);
        viewMatrix.translate(0, 6.2f, 0);

        obj.draw(matrices, viewMatrix, crl);
    }

    @Override
    public void loadTexture() {
        if(texturePath != null) {
            crl = TextureUtil.loadTexture(texturePath);
        }
    }
}
