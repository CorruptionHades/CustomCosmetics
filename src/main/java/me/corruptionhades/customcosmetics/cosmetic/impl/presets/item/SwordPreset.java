package me.corruptionhades.customcosmetics.cosmetic.impl.presets.item;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.ItemCosmetic;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomResourceLocation;
import me.corruptionhades.customcosmetics.objfile.TextureObjFile;
import me.corruptionhades.customcosmetics.utils.render.TextureUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwordPreset extends ItemCosmetic {

    private TextureObjFile obj;

    @Nullable
    private final Map<Item, File> texturePath;

    private Map<Item, CustomResourceLocation> crls;

    public SwordPreset(String name, File modelFile, @Nullable Map<Item, File> texturePath) {
        super(name, BodyPart.RIGHT_ARM, Items.NETHERITE_SWORD);
        try {
            obj = new TextureObjFile(modelFile.getName(), TextureObjFile.ResourceProvider.ofPath(modelFile.toPath().getParent()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.texturePath = texturePath;
    }

    int wee = 0;

    @Override
    public void render(ItemStack stack, MatrixStack matrices) {
        if(obj != null) {
            Matrix4f matrix4f = new Matrix4f();
            matrix4f.scale(0.5f, 0.5f, 0.5f);
            matrix4f.translate(1.1f, 1.1f, 1f);

            matrix4f.rotate(RotationAxis.NEGATIVE_Y.rotationDegrees(90));
            matrix4f.rotate(RotationAxis.NEGATIVE_X.rotationDegrees(45));

            CustomResourceLocation crl = crls.get(stack.getItem());
            obj.draw(matrices, matrix4f, crl);
        }
    }

    @Override
    public void onTick() {

        if(crls != null) {
            crls.forEach((item, crl) -> {
                if(crl != null) {
                    crl.update();
                }
            });
        }

        wee++;

        if(wee > 360) {
            wee = 0;
        }
    }

    @Override
    public void loadTexture() {
        if(texturePath != null) {
            crls = new HashMap<>();
            texturePath.forEach((item, file) -> {
                crls.put(item, TextureUtil.loadTexture(file));
            });
        }
    }

    public List<Item> getItems() {
        return new ArrayList<>(texturePath.keySet());
    }
}
