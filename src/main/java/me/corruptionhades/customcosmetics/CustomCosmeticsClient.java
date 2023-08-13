package me.corruptionhades.customcosmetics;

import me.corruptionhades.customcosmetics.cosmetic.CosmeticManager;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class CustomCosmeticsClient implements net.fabricmc.api.ClientModInitializer{

    private static CustomCosmeticsClient instance;

    private CosmeticManager cosmeticManager;

    @Override
    public void onInitializeClient() {
        instance = this;
        cosmeticManager = new CosmeticManager();
    }

    public static CustomCosmeticsClient getInstance() {
        return instance;
    }

    public CosmeticManager getCosmeticManager() {
        return cosmeticManager;
    }
}
