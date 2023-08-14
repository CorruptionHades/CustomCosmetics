package me.corruptionhades.customcosmetics.ui.comp.impl;

import me.corruptionhades.customcosmetics.animation.Animation;
import me.corruptionhades.customcosmetics.animation.Easing;
import me.corruptionhades.customcosmetics.ui.comp.Callback;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.List;

public class DropDown extends Preset {

    private final String text;
    private final String[] options;
    private final Color bg;
    private boolean expanded;
    private final Animation animation;
    private float customHeight;

    private String selectedOption;
    private Callback.ValueChangeCallback<String> changeCallback;

    private int scrollPosition = 0;
    private final int visibleOptions = 6;

    public DropDown(String text, Color bg, String...options) {
        this.text = text;
        this.options = options;
        this.bg = bg;
        this.animation = new Animation(200, 0, 25 * guiUtils.getGuiScale(), Easing.EASE_OUT_CUBIC);
        selectedOption = options[0];
    }

    public DropDown(String text, Color bg, List<String> options) {
        this(text, bg, options.toArray(new String[0]));
    }

    public void addValueChangeListener(Callback.ValueChangeCallback<String> changeCallback) {
        this.changeCallback = changeCallback;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);

        guiUtils.drawRoundedRect(context, x, y, x + width, y + customHeight, 15, bg.getRGB());
        

        int middleX = (int) (((width) / 2) - (FontUtil.normal.getStringWidth(text + ": " + selectedOption) / 2));
        int middleY = (int) ((height / 2) - (FontUtil.normal.getHeight() / 2));

        FontUtil.normal.drawString(context, text + ": " + selectedOption, x + middleX, y + middleY, -1);

        customHeight = animation.getValue();

        int sf = guiUtils.getGuiScale();

        if(animation.getEnd() != height) {
            int yOff = (15 * sf) + FontUtil.normal.getHeight() + (5 * sf);
            for (int i = scrollPosition; i < Math.min(scrollPosition + visibleOptions, options.length); i++) {
                String option = options[i];

                if(option.equals(selectedOption)) {
                    FontUtil.normal.drawString(context, option, x + (10 * sf), y + yOff, -1);
                }
                else FontUtil.normal.drawString(context, option, x + (10 * sf), y + yOff, Color.gray.getRGB());

                
                yOff += FontUtil.normal.getHeight() + (5 * sf);
            }
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int mouseButton) {
        int sf = guiUtils.getGuiScale();
        if(isInside(mouseX, mouseY, x, y, x + width, y + height) && mouseButton == 0) {
            expanded = !expanded;
            int yOff = (15 * sf) + FontUtil.normal.getHeight() + (5 * sf);
            for (int i = scrollPosition; i < Math.min(scrollPosition + visibleOptions, options.length); i++) {
                yOff += FontUtil.normal.getHeight() + (5 * sf);
            }
            yOff += 5 * sf;

            if(animation.isDone()) {
                if(animation.getEnd() == height) {
                    animation.setEnd(yOff);
                    expanded = true;
                } else {
                    animation.setEnd((float) height);
                    expanded = false;
                }
            }
        }


        if(expanded) {
            int yOff = (15 * sf) + FontUtil.normal.getHeight() + (5 * sf);
            for (int i = scrollPosition; i < Math.min(scrollPosition + visibleOptions, options.length); i++) {
                String option = options[i];
                if (isInside(mouseX, mouseY, x, y + yOff, x + width, y + yOff + FontUtil.normal.getHeight() + (5 * sf)) && mouseButton == 0) {
                    String oldOption = selectedOption;
                    selectedOption = option;
                    if(changeCallback != null) {
                        changeCallback.onChange(oldOption, selectedOption);
                    }
                }
                yOff += FontUtil.normal.getHeight() + (5 * sf);
            }
        }
    }

    @Override
    public void handleMouseInput(double wheel) {

        if (wheel != 0 && expanded) {
            scrollPosition -= wheel; // Adjust this value as needed

            if (scrollPosition < 0) {
                scrollPosition = 0;
            } else if (scrollPosition > options.length - visibleOptions) {
                scrollPosition = options.length - visibleOptions;
            }
        }
    }

    public float getCustomHeight() {
        return customHeight;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
}
