package me.corruptionhades.customcosmetics.ui.comp;

import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.ui.MainScreen;
import me.corruptionhades.customcosmetics.ui.ViewMode;
import me.corruptionhades.customcosmetics.ui.comp.impl.Slider;
import me.corruptionhades.customcosmetics.ui.comp.impl.Button;
import me.corruptionhades.customcosmetics.utils.NumberSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SettingsView extends Preset {

    private final List<Slider> sliders = new ArrayList<>();

    private CustomCosmetic cosmetic;

    NumberSetting x, y, z;

    NumberSetting scaleX, scaleY, scaleZ;

    NumberSetting rotationX, rotationY, rotationZ;

    private final Button animationButton;

    private final MainScreen parent;

    public SettingsView(MainScreen parent, CustomCosmetic cosmetic) {

        this.x = new NumberSetting("X", cosmetic == null ? 0 : cosmetic.getTransX(), -7, 7, 0.001);
        this.y = new NumberSetting("Y", cosmetic == null ? 0 : cosmetic.getTransY(), -7, 7, 0.001);
        this.z = new NumberSetting("Z", cosmetic == null ? 0 : cosmetic.getTransZ(), -7, 7, 0.001);

        this.scaleX = new NumberSetting("Scale X", cosmetic == null ? 0.01 : cosmetic.getScaleX(), 0, 0.2, 0.0001);
        this.scaleY = new NumberSetting("Scale Y", cosmetic == null ? 0.01 : cosmetic.getScaleY(), 0, 0.2, 0.0001);
        this.scaleZ = new NumberSetting("Scale Z", cosmetic == null ? 0.01 : cosmetic.getScaleZ(), 0, 0.2, 0.0001);

        this.rotationX = new NumberSetting("Rotation X", cosmetic == null ? 0 : cosmetic.getRotX(), 0, 360, 1);
        this.rotationY = new NumberSetting("Rotation Y", cosmetic == null ? 0 : cosmetic.getRotY(), 0, 360, 1);
        this.rotationZ = new NumberSetting("Rotation Z", cosmetic == null ? 0 : cosmetic.getRotZ(), 0, 360, 1);

        List<NumberSetting> settings = new ArrayList<>();
        settings.add(x);
        settings.add(y);
        settings.add(z);
        settings.add(scaleX);
        settings.add(scaleY);
        settings.add(scaleZ);
        settings.add(rotationX);
        settings.add(rotationY);
        settings.add(rotationZ);

        for (NumberSetting setting : settings) {
            sliders.add(new Slider(setting, 2));
        }

        this.cosmetic = cosmetic;
        this.animationButton = new Button("Animations", new Color(97, 187, 95));
        this.parent = parent;

        if(cosmetic != null) {
            animationButton.setClickable(true);
            animationButton.addClickListener(() -> {
                if(cosmetic != null) {
                    parent.setViewMode(ViewMode.Animation);
                    parent.getCustomAnimationScreen().setCosmetic(cosmetic);
                    parent.closeSettings();
                }
            });
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);

        int sf = guiUtils.getGuiScale();

        int yOffset = 0;
        int xOffset = 0;
        int amount = 0;

        for (Slider ignored : sliders) {

            if(amount == 3) {
                xOffset += 100 * guiUtils.getGuiScale();
                yOffset = 0;
                amount = 0;
            }

            yOffset += 50 * guiUtils.getGuiScale();
            amount++;
        }

        guiUtils.drawRoundedOutline(context, x - (5 * sf), y - (20 * sf), x + xOffset + (100 * sf), y + yOffset, 20, 5, new Color(97, 187, 95), new Color(126, 240, 124));

        yOffset = 0;
        xOffset = 0;
        amount = 0;

        for (Slider slider : sliders) {

            if(amount == 3) {
                xOffset += 100 * guiUtils.getGuiScale();
                yOffset = 0;
                amount = 0;
            }

            slider.render(context, mouseX, mouseY, x + xOffset, y + yOffset, 0, 0);
            yOffset += 50 * guiUtils.getGuiScale();
            amount++;
        }

        animationButton.render(context, mouseX, mouseY, x + (100 * sf), y + yOffset - (35 * sf), 100 * guiUtils.getGuiScale(), 25 * guiUtils.getGuiScale());



        if(cosmetic != null) {
            cosmetic.setTransX(this.x.getValue().floatValue());
            cosmetic.setTransY(this.y.getValue().floatValue());
            cosmetic.setTransZ(z.getValue().floatValue());

            cosmetic.setScaleX(scaleX.getValue().floatValue());
            cosmetic.setScaleY(scaleY.getValue().floatValue());
            cosmetic.setScaleZ(scaleZ.getValue().floatValue());

            cosmetic.setRotX(rotationX.getValue().floatValue());
            cosmetic.setRotY(rotationY.getValue().floatValue());
            cosmetic.setRotZ(rotationZ.getValue().floatValue());
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int mouseButton) {
        super.onClick(mouseX, mouseY, mouseButton);
        for (Slider slider : sliders) {
            slider.onClick(mouseX, mouseY, mouseButton);
        }
        animationButton.onClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int mouseButton) {
        super.onRelease(mouseX, mouseY, mouseButton);
        for (Slider slider : sliders) {
            slider.onRelease(mouseX, mouseY, mouseButton);
        }
        animationButton.onRelease(mouseX, mouseY, mouseButton);
    }

    public CustomCosmetic getCosmetic() {
        return cosmetic;
    }

    public void setCosmetic(CustomCosmetic cosmetic) {
        this.cosmetic = cosmetic;
        animationButton.setClickable(true);
        animationButton.addClickListener(() -> {
            if(cosmetic != null) {
                parent.setViewMode(ViewMode.Animation);
                parent.getCustomAnimationScreen().setCosmetic(cosmetic);
                parent.closeSettings();
            }
        });
    }
}
