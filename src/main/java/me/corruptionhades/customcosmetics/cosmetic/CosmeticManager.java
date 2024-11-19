package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.WingPreset;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.item.ShieldPreset;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.item.SwordPreset;
import me.corruptionhades.customcosmetics.utils.TesterUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CosmeticManager {

    private final List<Cosmetic> cosmetics = new ArrayList<>();

    public CosmeticManager() {
        init();
    }

    public void init() {
        if(TesterUtil.isUbuntu()) {
            registerCosmetic(new WingPreset("angel_wings", BodyPart.BODY,
                    new File("/home/mitarbeiter/Downloads/angel_wings.obj"),
                    new File("/home/mitarbeiter/Downloads/wing_6.png")));
        }
        else {
            registerCosmetic(new WingPreset("wichtiger_dark_wings", BodyPart.BODY,
                    new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/wing/Wichtiger Dark Wings/wichtiger.obj"),
                    new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/wing/Wichtiger Dark Wings/frames/")));


            // /home/mitarbeiter/Downloads

            registerCosmetic(new SwordPreset(
                    "obsidian_sword", new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/sword/Dragon Obsidian Sword/dragon_sword_ob.obj"),
                    new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/sword/Dragon Obsidian Sword/frames")
            ));

            registerCosmetic(new ShieldPreset(
                    "no_risk_black_shield",
                    new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/shield/NoRisk Black Shield/roundshield.obj"),
                    new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/shield/NoRisk Black Shield/frames/")
            ));
        }


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

    public List<ItemCosmetic> getItemCosmetics() {
        List<ItemCosmetic> itemCosmetics = new ArrayList<>();
        for(Cosmetic cosmetic : cosmetics) {
            if(cosmetic instanceof ItemCosmetic) {
                itemCosmetics.add((ItemCosmetic) cosmetic);
            }
        }
        return itemCosmetics;
    }
}
