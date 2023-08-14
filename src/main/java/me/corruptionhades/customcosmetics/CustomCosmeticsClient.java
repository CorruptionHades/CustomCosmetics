package me.corruptionhades.customcosmetics;

import me.corruptionhades.customcosmetics.cosmetic.CosmeticManager;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.interfaces.IMinecraftInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class CustomCosmeticsClient implements net.fabricmc.api.ClientModInitializer, IMinecraftInstance {

    private static CustomCosmeticsClient instance;

    private CosmeticManager cosmeticManager;

    @Override
    public void onInitializeClient() {
        instance = this;
        cosmeticManager = new CosmeticManager();
    }

    public void onTick() {
        for (CustomCosmetic customCosmetic : cosmeticManager.getCustomCosmetics()) {
            if(customCosmetic.getTextureLocation() != null) {
                customCosmetic.getTextureLocation().update();
            }
        }
    }

    public static CustomCosmeticsClient getInstance() {
        return instance;
    }

    public CosmeticManager getCosmeticManager() {
        return cosmeticManager;
    }

    public static void open() {
        MinecraftClient.getInstance().setScreen(new Screen(Text.of("<vdxhrt")) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
                if(client.player != null) {
                    InventoryScreen.drawEntity(context, 10, 100, 40, 0, 0, client.player);
                }
                else {
                    context.drawText(client.textRenderer, "null", 10, 10, -1, true);
                }
            }
        });
    }
}
