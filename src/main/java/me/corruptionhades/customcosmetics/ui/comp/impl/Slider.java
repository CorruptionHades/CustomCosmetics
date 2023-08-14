package me.corruptionhades.customcosmetics.ui.comp.impl;

import me.corruptionhades.customcosmetics.animation.Animation;
import me.corruptionhades.customcosmetics.animation.Easing;
import me.corruptionhades.customcosmetics.ui.comp.Callback;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import me.corruptionhades.customcosmetics.utils.NumberSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Preset {

    private boolean dragging = false;
    private double renderWidth;
    private double renderWidth2;

    private final NumberSetting setting;

    public Color bgColor = new Color(126, 240, 124);
    public Color bgColor2 = new Color(97, 187, 95);
    private Color color;

    private Callback.ValueChangeCallback<Number> onValueChange;

    public Slider(NumberSetting setting, int color) {
        this.setting = setting;
        if(color == 1) this.color = bgColor;
        else this.color = bgColor2;
    }

    public void addValueChangeCallback(Callback.ValueChangeCallback<Number> onValueChange) {
        this.onValueChange = onValueChange;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);

        int sf = guiUtils.getGuiScale();

        double min = setting.getMinimum();
        double max = setting.getMaximum();
        double l = (90 * sf);

        renderWidth = (l) * (setting.getValue() - min) / (max - min);
        renderWidth2 = (l) * (setting.getMaximum() - min) / (max - min);
        double diff = Math.min(l, Math.max(0, (mouseX * sf) - (x)));

        if (dragging) {
            if (diff == 0) {
                if(onValueChange != null) onValueChange.onChange(setting.getValue(), setting.getMinimum());
                setting.setValue(setting.getMinimum());
            }
            else {
                double newValue = ((diff / l) * (max - min) + min);
                if(onValueChange != null) onValueChange.onChange(setting.getValue(), newValue);
                setting.setValue(newValue);
            }
        }

        guiUtils.drawRoundedRect(context, x, y, x + renderWidth2, y + (5 * sf), 5, color.getRGB());
        // Debug
        //guiUtils.drawRect(x + renderWidth - 5, y - 3, x + renderWidth + 6, y + 8, new Color(234, 23, 87).getRGB());
        guiUtils.circle(context, x + renderWidth, y + (3 * sf), 5 * sf, -1);

    //    guiUtils.drawRect(x + renderWidth - 5, y - 3, x + renderWidth + 6, y + 8, Color.red.getRGB());
    //    GlStateManager.resetColor();

        String text = setting.getName() + ": " + setting.getValue();
        int middleX = (int) ((renderWidth2) / 2) - (FontUtil.normal.getStringWidth(text) / 2);

        FontUtil.normal.drawString(context, text, x + middleX, y - (10 * sf), -1);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

        int sf = guiUtils.getGuiScale();

        if(isInside(mouseX, mouseY, x + renderWidth - (5 * sf), y - (3 * sf), x + renderWidth + (6 * sf), y + (8 * sf)) && button == 0) {
            dragging = true;
        }
        else {
            dragging = false;
        }
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int mouseButton) {
        dragging = false;
    }

    private double roundToPlace(double value, int places) {
        if(value < 0.1) return value;
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public NumberSetting getSetting() {
        return setting;
    }
}
