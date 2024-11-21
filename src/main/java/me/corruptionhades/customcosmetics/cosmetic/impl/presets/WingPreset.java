package me.corruptionhades.customcosmetics.cosmetic.impl.presets;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomResourceLocation;
import me.corruptionhades.customcosmetics.objfile.TextureObjFile;
import me.corruptionhades.customcosmetics.utils.render.TextureUtil;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.File;

public class WingPreset extends Cosmetic {

    private TextureObjFile obj;
    int wee = 0;
    boolean reached = false;

    @Nullable
    private final File texturePath;

    private CustomResourceLocation crl;

    public WingPreset(String name, BodyPart bodyPart, File file, @Nullable File texture) {
        super(name, bodyPart, false);
        try {
            obj = new TextureObjFile(file.getName(), TextureObjFile.ResourceProvider.ofPath(file.toPath().getParent()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.texturePath = texture;
    }

    @Override
    public void onTick() {

        if(crl != null) {
            crl.update();
        }

        int incr = 6;
        if(obj != null) {
            if(wee < 91 && !reached) {
                wee+=incr;
            }
            else if(wee >= 91 && !reached) {
                reached = true;
            }
            else if(wee > 0) {
                wee-=incr;
            }
            else {
                reached = false;
            }
        }
    }

    @Override
    public void render(PlayerEntityModel model, MatrixStack matrices, PlayerEntityRenderState state) {
        if(obj != null) {

            float scale = 0.1f;
            float yOff = -1.6f, zOff = 2f;

            Matrix4f matrix = new Matrix4f();
            matrix.scale(scale, -scale, scale);
            matrix.translate(0, yOff, zOff);

            matrix.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(wee));

            obj.draw(matrices, matrix, crl);

            Matrix4f matrix2 = new Matrix4f();

            // mirror
            matrix2.scale(-scale, -scale, scale);
            matrix2.translate(0, yOff, zOff);

            matrix2.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(wee));

            obj.draw(matrices, matrix2, crl);
        }
    }

    @Override
    public void loadTexture() {
        if(texturePath != null) {
            crl = TextureUtil.loadTexture(texturePath);
        }
    }
}
