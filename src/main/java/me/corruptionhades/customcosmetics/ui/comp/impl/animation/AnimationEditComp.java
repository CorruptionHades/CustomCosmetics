package me.corruptionhades.customcosmetics.ui.comp.impl.animation;

import me.corruptionhades.customcosmetics.animation.Easing;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomAnimation;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.ui.comp.impl.DropDown;
import me.corruptionhades.customcosmetics.ui.comp.impl.Slider;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import me.corruptionhades.customcosmetics.utils.NumberSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.io.IOException;

public class AnimationEditComp extends Preset {

    private CustomAnimation customAnimation;

    private Slider durationSlider, startSlider, endSlider;
    private DropDown easingDropDown;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);
        int sf = guiUtils.getGuiScale();
        if(customAnimation == null) return;

        FontUtil.normal.drawString(context, "Editing " + customAnimation.getName(), x + (25 * sf), y - (30 * sf), -1);

        startSlider.render(context, mouseX, mouseY, x + (10 * sf), y, width, height);
        endSlider.render(context, mouseX, mouseY, x + (10 * sf), y + startSlider.getHeight() + (10 * sf), width, height);
        durationSlider.render(context, mouseX, mouseY, x + (10 * sf), y + startSlider.getHeight() + endSlider.getHeight() + (20 * sf), width, height);
        easingDropDown.render(context, mouseX, mouseY, x, y + startSlider.getHeight() + endSlider.getHeight() + durationSlider.getHeight() + (15 * sf), width, height);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if(customAnimation == null) return;

        startSlider.onClick(mouseX, mouseY, button);
        endSlider.onClick(mouseX, mouseY, button);
        durationSlider.onClick(mouseX, mouseY, button);
        easingDropDown.onClick(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(customAnimation == null) return;

        startSlider.keyTyped(typedChar, keyCode);
        endSlider.keyTyped(typedChar, keyCode);
        durationSlider.keyTyped(typedChar, keyCode);
        easingDropDown.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int button) {
        if(customAnimation == null) return;

        startSlider.onRelease(mouseX, mouseY, button);
        endSlider.onRelease(mouseX, mouseY, button);
        durationSlider.onRelease(mouseX, mouseY, button);
        easingDropDown.onRelease(mouseX, mouseY, button);
    }

    public void setCustomAnimation(CustomAnimation customAnimation) {
        this.customAnimation = customAnimation;

        if(customAnimation == null) return;

        durationSlider = new Slider(new NumberSetting("Duration", customAnimation.getDuration(), 0, 3000, 1), 1);
        durationSlider.addValueChangeCallback(((oldVal, newVal) -> customAnimation.setDuration(newVal.longValue())));

        easingDropDown = new DropDown("Easing", new Color(126, 240, 124), Easing.getValues());
        easingDropDown.addValueChangeListener(((oldVal, newVal) -> customAnimation.setEasing(Easing.valueOf(newVal))));

        makeSliders(customAnimation.getAnimationType().name());
    }

    private void makeSliders(String mode) {
        if(mode.contains("Translate")) {
            this.startSlider = new Slider(new NumberSetting("Start", customAnimation.getDisplayStart(), -7, 7, 0.01), 1);
            this.endSlider = new Slider(new NumberSetting("End", customAnimation.getDisplayEnd(), -7, 7, 0.01), 1);
        }
        else if(mode.contains("Scale")) {
            this.startSlider = new Slider(new NumberSetting("Start", customAnimation.getDisplayStart(), 1, 5, 0.0001), 1);
            this.endSlider = new Slider(new NumberSetting("End", customAnimation.getDisplayEnd(), 1, 5, 0.0001), 1);
        }
        else if(mode.contains("Rotate")) {
            this.startSlider = new Slider(new NumberSetting("Start", customAnimation.getDisplayStart(), 0, 360, 1), 1);
            this.endSlider = new Slider(new NumberSetting("End", customAnimation.getDisplayEnd(), 0, 360, 1), 1);
        }

        startSlider.addValueChangeCallback(((oldVal, newVal) -> customAnimation.setDisplayStart(newVal.floatValue())));
        endSlider.addValueChangeCallback(((oldVal, newVal) -> customAnimation.setDisplayEnd(newVal.floatValue())));
    }

    public CustomAnimation getCustomAnimation() {
        return customAnimation;
    }

    @Override
    public void handleMouseInput(double amount) throws IOException {

        if(customAnimation == null) return;

        startSlider.handleMouseInput(amount);
        endSlider.handleMouseInput(amount);
        durationSlider.handleMouseInput(amount);
        easingDropDown.handleMouseInput(amount);
    }
}
