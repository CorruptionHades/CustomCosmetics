package me.corruptionhades.customcosmetics.ui.comp.impl.animation;

import me.corruptionhades.customcosmetics.cosmetic.custom.CustomAnimation;
import me.corruptionhades.customcosmetics.ui.CreateScreen;
import me.corruptionhades.customcosmetics.ui.MainScreen;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class AnimationComp extends Preset {

    private final CreateScreen parent;
    private final CustomAnimation canimation;

    public AnimationComp(CreateScreen parent, CustomAnimation canimation) {
        this.parent = parent;
        this.canimation = canimation;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);
        int sf = guiUtils.getGuiScale();
        guiUtils.drawRoundedRect(context, x, y, x + width, y + height, 10, new Color(97, 187, 95).getRGB());

        int middleY = (int) ((height / 2) - (FontUtil.normal.getHeight() / 2));
        FontUtil.normal.drawString(context, canimation.getName() + ": " + canimation.getEasing().name() + ", " + canimation.getDisplayStart() + "-" + canimation.getDisplayEnd(), x + (5 * sf), y + middleY, Color.WHITE.getRGB());

        guiUtils.drawScaledImage(context,x + width - (15 * sf), y + (2 * sf), 0.027f,"/edit.png");
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        int sf = guiUtils.getGuiScale();

        // Edit
        if(isInside(mouseX, mouseY, x + width - (15 * sf), y, x + width, y + height)) {
            parent.edit(null);
            parent.getEditComp().setCustomAnimation(canimation);
            parent.getParent().openSettings();
        }

        // Play
        if(isInside(mouseX, mouseY, x + width - (35 * sf), y, x + width - (20 * sf), y + height)) {

        }
    }
}
