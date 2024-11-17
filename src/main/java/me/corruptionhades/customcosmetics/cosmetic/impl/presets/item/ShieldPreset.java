package me.corruptionhades.customcosmetics.cosmetic.impl.presets.item;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.cosmetic.ItemCosmetic;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomResourceLocation;
import me.corruptionhades.customcosmetics.objfile.TextureObjFile;
import me.corruptionhades.customcosmetics.utils.TextureUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.File;

public class ShieldPreset extends ItemCosmetic {

    private TextureObjFile obj;

    @Nullable
    private final File texturePath;

    private CustomResourceLocation crl;

    public ShieldPreset(String name, File modelFile, @Nullable File texturePath) {
        super(name, BodyPart.RIGHT_ARM, Items.SHIELD);
        try {
            obj = new TextureObjFile(modelFile.getName(), TextureObjFile.ResourceProvider.ofPath(modelFile.toPath().getParent()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.texturePath = texturePath;
    }

    @Override
    public void render(ItemStack stack, MatrixStack matrices) {
        if (obj != null) {
            Matrix4f matrix4f = new Matrix4f();
            matrix4f.scale(0.12f, 0.12f, 0.12f);
            matrix4f.translate(0, 1, 0);
            matrix4f.rotate(RotationAxis.POSITIVE_X.rotationDegrees(180));
            matrix4f.rotate(RotationAxis.POSITIVE_Z.rotationDegrees(180));

            obj.draw(matrices, matrix4f, crl);
        }
    }

    @Override
    public void onTick() {
        if(crl != null) {
            crl.update();
        }
    }

    @Override
    public void loadTexture() {
        if(texturePath != null) {
            crl = TextureUtil.loadTexture(texturePath);
        }
    }
}
