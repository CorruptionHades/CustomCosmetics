package me.corruptionhades.customcosmetics.cosmetic.impl.presets;

import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomResourceLocation;
import me.corruptionhades.customcosmetics.utils.render.TextureUtil;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class CapePreset extends Cosmetic {

    @Nullable
    private final File texturePath;

    private CustomResourceLocation crl;

    public CapePreset(String name, @Nullable File texturePath) {
        super(name, BodyPart.BODY, false);
        this.texturePath = texturePath;
    }

    @Override
    public void render(PlayerEntityModel model, MatrixStack matrices, PlayerEntityRenderState state) {}

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

    public CustomResourceLocation getCrl() {
        return crl;
    }
}
