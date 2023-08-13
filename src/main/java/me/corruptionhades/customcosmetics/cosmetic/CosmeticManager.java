package me.corruptionhades.customcosmetics.cosmetic;

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
}
