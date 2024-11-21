package me.corruptionhades.customcosmetics.cosmetic;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Cosmetic {

    private final String name;
    protected BodyPart bodyPart;
    public final boolean isItem;

    public Cosmetic(String name, BodyPart bodyPart, boolean isItem) {
        this.name = name;
        this.bodyPart = bodyPart;
        this.isItem = isItem;
    }

    public abstract void render(PlayerEntityModel model, MatrixStack matrices, PlayerEntityRenderState state);

    public void onTick() {}

    public String getName() {
        return name;
    }

    public BodyPart getType() {
        return bodyPart;
    }

    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    public abstract void loadTexture();
}
