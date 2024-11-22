package me.corruptionhades.customcosmetics.cosmetic;

import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.BandanaPreset;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.CapePreset;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.MD5PresetTest;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.WingPreset;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.item.ShieldPreset;
import me.corruptionhades.customcosmetics.cosmetic.impl.presets.item.SwordPreset;
import me.corruptionhades.customcosmetics.utils.TesterUtil;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CosmeticManager {

    private final List<Cosmetic> cosmetics = new ArrayList<>();

    public CosmeticManager() {
        init();
    }

    public void init() {
        if(TesterUtil.isUbuntu()) {
            // /home/mitarbeiter/Downloads
            registerCosmetic(new WingPreset("angel_wings", BodyPart.BODY,
                    new File("/home/mitarbeiter/Downloads/angel_wings.obj"),
                    new File("/home/mitarbeiter/Downloads/wing_6.png")));

            registerCosmetic(new SwordPreset(
                    "katana",
                    new File("/home/mitarbeiter/Downloads/katana_sword.obj"),
                    new HashMap<>() {
                        {
                            put(Items.DIAMOND_SWORD, new File("/home/mitarbeiter/Downloads/diamond.png"));
                        }
                    }
            ));

            registerCosmetic(new ShieldPreset(
                    "shield_zickzackv5_bastighg",
                    new File("/home/mitarbeiter/Downloads/shield_zickzackv5_bastighg.obj"),
                    new File("/home/mitarbeiter/Downloads/shield_333-47.png")
            ));

            registerCosmetic(new MD5PresetTest(new File("/home/mitarbeiter/Downloads/pet_26.md5mesh")));
        }
        else {

            String path = "H:/IntelijProjects/CustomCosmetics/assets/";

            registerCosmetic(new WingPreset("wichtiger_dark_wings", BodyPart.BODY,
                    new File(path + "wings/wichtiger.obj"),
                    new File(path + "wings/frames/")
            ));

            registerCosmetic(new SwordPreset("dragon_sword",
                    new File(path + "sword/dragon_sword.obj"),
                    new HashMap<>() {
                        {
                            put(Items.NETHERITE_SWORD, new File(path + "sword/netherite.png"));
                            put(Items.DIAMOND_SWORD, new File(path + "sword/diamond.png"));
                            put(Items.GOLDEN_SWORD, new File(path + "sword/gold.png"));
                            put(Items.IRON_SWORD, new File(path + "sword/iron.png"));
                            put(Items.STONE_SWORD, new File(path + "sword/stone.png"));
                            put(Items.WOODEN_SWORD, new File(path + "sword/wood.png"));
                        }
                    }
            ));

            registerCosmetic(new ShieldPreset(
                    "basti_shield",
                    new File(path + "/shield/roundshield.obj"),
                    new File(path + "/shield/frames/")
            ));

            registerCosmetic(new BandanaPreset("blue_lightning_bandana",
                    new File(path + "/bandana/bandana.obj"),
                    new File(path + "/bandana/frames/")
            ));

            registerCosmetic(new CapePreset("cape",
                    new File(path + "/cape.png")
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

    public @Nullable CapePreset getCapeCosmetic() {
        for(Cosmetic cosmetic : cosmetics) {
            if(cosmetic instanceof CapePreset) {
                return (CapePreset) cosmetic;
            }
        }
        return null;
    }
}
