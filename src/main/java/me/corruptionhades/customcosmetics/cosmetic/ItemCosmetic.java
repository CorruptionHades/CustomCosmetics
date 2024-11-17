package me.corruptionhades.customcosmetics.cosmetic;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemCosmetic extends Cosmetic {

    private final Item item;

    public ItemCosmetic(String name, BodyPart bodyPart, Item item) {
        super(name, bodyPart, true);
        this.item = item;
    }

    @Override
    public void render(PlayerEntityModel model, MatrixStack matrices) {
        // NO-OP
    }

    public abstract void render(ItemStack stack, MatrixStack matrices);

    public final Item getItem() {
        return item;
    }
}
