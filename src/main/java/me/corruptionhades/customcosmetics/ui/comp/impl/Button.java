package me.corruptionhades.customcosmetics.ui.comp.impl;

import me.corruptionhades.customcosmetics.animation.Animation;
import me.corruptionhades.customcosmetics.animation.Easing;
import me.corruptionhades.customcosmetics.ui.comp.Callback;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Button extends Preset {

    private Callback.ClickCallback onClick;
    private final String text;
    private final Color bg;

    private boolean clickable = true;

    public Button(String text, Color bg) {
        this.text = text;
        this.bg = bg;
    }

    public void addClickListener(Callback.ClickCallback clickListener) {
        onClick = clickListener;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);

        guiUtils.drawRoundedRect(context, x, y, x + width, y + height, 15, bg.getRGB());

        int middleX = (int) ((width) / 2) - (FontUtil.normal.getStringWidth(text) / 2);
        int middleY = (int) ((height / 2) - (FontUtil.normal.getHeight() / 2));

        FontUtil.normal.drawString(context, text, x + middleX, y + middleY, -1);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int mouseButton) {
        if(!clickable) {
            return;
        }

        if(isInside(mouseX, mouseY, x, y, x + width, y + height) && mouseButton == 0) {
            if(onClick != null) {
                onClick.onClick();
            }
        }
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}
