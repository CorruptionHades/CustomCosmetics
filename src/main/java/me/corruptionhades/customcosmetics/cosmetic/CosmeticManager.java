package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.cosmetic.impl.Test;

import java.util.ArrayList;
import java.util.List;

public class CosmeticManager {

    private final List<Cosmetic> cosmetics = new ArrayList<>();

    public CosmeticManager() {
        init();
    }

    public void init() {
        registerCosmetic(new Test());
    }

    public void registerCosmetic(Cosmetic cosmetic) {
        cosmetics.add(cosmetic);
    }

    public void unregisterCosmetic(Cosmetic cosmetic) {
        cosmetics.remove(cosmetic);
    }

    public List<Cosmetic> getCosmetics() {
        return cosmetics;
    }

    public List<CustomCosmetic> getCustomCosmetics() {
        List<CustomCosmetic> customCosmetics = new ArrayList<>();
        for(Cosmetic cosmetic : cosmetics) {
            if(cosmetic instanceof CustomCosmetic) {
                customCosmetics.add((CustomCosmetic) cosmetic);
            }
        }
        return customCosmetics;
    }
}
