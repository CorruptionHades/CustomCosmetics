package me.corruptionhades.customcosmetics;

import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.cosmetic.CosmeticManager;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.interfaces.IMinecraftInstance;
import me.corruptionhades.customcosmetics.utils.CustomSounds;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class CustomCosmeticsClient implements net.fabricmc.api.ClientModInitializer, IMinecraftInstance {

    private static CustomCosmeticsClient instance;

    private CosmeticManager cosmeticManager;

    @Override
    public void onInitializeClient() {
        instance = this;
        cosmeticManager = new CosmeticManager();

        Registry.register(Registries.SOUND_EVENT, CustomSounds.PING, CustomSounds.PING_EVENT);

        new Thread(() -> {
            try {
                Thread.sleep(1000);

                for (Cosmetic cosmetic : cosmeticManager.getCosmetics()) {
                    cosmetic.loadTexture();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void onTick() {
        for (Cosmetic cosmetic : cosmeticManager.getCosmetics()) {

            if(cosmetic instanceof CustomCosmetic customCosmetic) {
                if(customCosmetic.getTextureLocation() != null) {
                    customCosmetic.getTextureLocation().update();
                }
            }

            cosmetic.onTick();
        }
    }

    public static CustomCosmeticsClient getInstance() {
        return instance;
    }

    public CosmeticManager getCosmeticManager() {
        return cosmeticManager;
    }

    public static void open() {
      /*  MinecraftClient.getInstance().setScreen(new Screen(Text.of("<vdxhrt")) {

            double moveX = 0, moveY = 0;
            double sizeX = 10;

            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                sizeX = 100;

                super.render(context, mouseX, mouseY, delta);
                if(client.player != null) {
                    // rotate player
                    context.getMatrices().push();

                    context.getMatrices().translate(240, 0, 0);
                    context.getMatrices().multiply(new Quaternionf().rotateY((float) moveX));
                    context.getMatrices().translate(-240, 0, 0);
                  //
                   // context.getMatrices().translate(-(240 + sizeX), -100, 0);
                    InventoryScreen.drawEntity(context, 240, 100, 40, 0, 0, client.player);

               //
                    context.getMatrices().pop();

                    context.fill((int) (240 - 10), (int) (100 - 40), (int) (240 + 10), (int) (100 + 40), 0x80000000);

                    moveX += 0.005;
                }
                else {
                    context.drawText(client.textRenderer, "null", 10, 10, -1, true);
                }

                context.drawText(client.textRenderer, "" + moveX, 10, 30, -1, true);
            }

            @Override
            public void mouseMoved(double mouseX, double mouseY) {
                super.mouseMoved(mouseX, mouseY);
            }
        }); */
    }
}
