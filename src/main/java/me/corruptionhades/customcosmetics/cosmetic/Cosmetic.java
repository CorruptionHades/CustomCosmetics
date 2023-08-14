package me.corruptionhades.customcosmetics.cosmetic;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Cosmetic {

    private final String name;
    protected BodyPart bodyPart;

    public Cosmetic(String name, BodyPart bodyPart) {
        this.name = name;
        this.bodyPart = bodyPart;
    }

    public abstract void render(PlayerEntityModel<?> model, AbstractClientPlayerEntity player, MatrixStack matrices);

    public String getName() {
        return name;
    }

    public BodyPart getType() {
        return bodyPart;
    }

    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }
}
