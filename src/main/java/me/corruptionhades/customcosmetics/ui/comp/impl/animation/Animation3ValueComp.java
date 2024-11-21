package me.corruptionhades.customcosmetics.ui.comp.impl.animation;

import me.corruptionhades.customcosmetics.cosmetic.custom.anim.Custom3ValueAnimation;
import me.corruptionhades.customcosmetics.ui.CreateScreen;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.utils.render.FontUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.io.IOException;

public class Animation3ValueComp extends Preset {

    private final CreateScreen parent;
    private final AnimationComp xAnimation, yAnimation, zAnimation;
    private final Custom3ValueAnimation valueAnimation;

    public Animation3ValueComp(CreateScreen parent, Custom3ValueAnimation valueAnimation) {
        this.xAnimation = new AnimationComp(parent, valueAnimation.getxAnimation());
        this.yAnimation = new AnimationComp(parent, valueAnimation.getyAnimation());
        this.zAnimation = new AnimationComp(parent, valueAnimation.getzAnimation());
        this.valueAnimation = valueAnimation;
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);

        int sf = guiUtils.getGuiScale();

        guiUtils.drawRoundedRect(context, x, y, x + width, y + height, 5, new Color(97, 187, 95).getRGB());
        FontUtil.normal.drawString(context,valueAnimation.getName() + ", " + valueAnimation.getType().name(), x + (5 * sf), y + (5 * sf), -1);

        guiUtils.drawScaledImage(context, x + width - (15 * sf), y + (2 * sf), 0.027f, "edit.png");
        guiUtils.drawScaledImage(context,x + width - (35 * sf), y + (2 * sf), 0.027f, "play.png");

        xAnimation.render(context, mouseX, mouseY, x + (30 * sf), y + (30 * sf), width + (30 * sf), (20) * sf);
        yAnimation.render(context,mouseX, mouseY, x + (30 * sf), y + (60 * sf), width + (30 * sf), (20 * sf));
        zAnimation.render(context,mouseX, mouseY, x + (30 * sf), y + (90 * sf), width + (30 * sf), (20 * sf));
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        int sf = guiUtils.getGuiScale();
        if(isInside(mouseX, mouseY, x + width - (16 * sf), y, x + width, y + (20 * sf))) {
            parent.getEditComp().setCustomAnimation(null);
            parent.getParent().openSettings();
            parent.edit(valueAnimation);
        }

        if(isInside(mouseX, mouseY, x + width - (35 * sf), y, x + width - (20 * sf), y + (20 * sf))) {
            parent.removeAnimation(valueAnimation, this);
        }

        xAnimation.onClick(mouseX, mouseY, button);
        yAnimation.onClick(mouseX, mouseY, button);
        zAnimation.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int button) {
        xAnimation.onRelease(mouseX, mouseY, button);
        yAnimation.onRelease(mouseX, mouseY, button);
        zAnimation.onRelease(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        xAnimation.keyTyped(typedChar, keyCode);
        yAnimation.keyTyped(typedChar, keyCode);
        zAnimation.keyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput(double amount) throws IOException {
        xAnimation.handleMouseInput(amount);
        yAnimation.handleMouseInput(amount);
        zAnimation.handleMouseInput(amount);
    }

    public Custom3ValueAnimation getValueAnimation() {
        return valueAnimation;
    }
}
