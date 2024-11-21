package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.item.SwordPreset;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class CosmeticItemRenderer {

    public static boolean render(ItemStack stack, MatrixStack matrices) {

        for (ItemCosmetic itemCosmetic : CustomCosmeticsClient.getInstance().getCosmeticManager().getItemCosmetics()) {

            if(itemCosmetic instanceof SwordPreset sp) {
                if(sp.getItems().contains(stack.getItem())) {
                    sp.render(stack, matrices);
                    return true;
                }
            }

            if(stack.getItem() == itemCosmetic.getItem()) {
                itemCosmetic.render(stack, matrices);
                return true;
            }
        }

        return false;
    }
}
