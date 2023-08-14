package me.corruptionhades.customcosmetics.ui.comp.impl;

import me.corruptionhades.customcosmetics.animation.Animation;
import me.corruptionhades.customcosmetics.animation.Easing;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class ToggleButton extends Preset {

    private final String text;
    private boolean enabled;
    private final Color bg;

    private final Animation animation;

    public ToggleButton(String text, boolean enabled, Color bg) {
        this.text = text;
        this.enabled = enabled;
        this.bg = bg;
        int sf = guiUtils.getGuiScale();
        this.animation = new Animation(10,  enabled ? (16 * sf) : 1, enabled ? (17 * sf) : 0, Easing.EASE_IN_OUT_BOUNCE);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);
        int sf = guiUtils.getGuiScale();
        guiUtils.drawRoundedRect(context, x, y, x + width, y + height, 10, bg);
        FontUtil.normal.drawString(context, text, x + (5 * sf), y + (height / 2) - (FontUtil.normal.getHeight() / 2f), -1);

        guiUtils.drawRoundedRect(context,x + (83 * sf), y + (5 * sf), x + width - (5 * sf), y + height - (5 * sf), 10, bg.darker().getRGB());
        guiUtils.circle(context, x + (90 * sf) + animation.getValue(), y + (12.5 * sf), 5.5 * sf, -1);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        int sf = guiUtils.getGuiScale();
        if(isInside(mouseX, mouseY, x + (83 * sf), y + (5 * sf), x + width - (5 * sf), y + height - (5 * sf))) {
            enabled = !enabled;

            if(animation.isDone()) {
                if(animation.getEnd() == 0) {
                    animation.setEnd(17 * sf);
                }
                else {
                    animation.setEnd(0);
                }
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
