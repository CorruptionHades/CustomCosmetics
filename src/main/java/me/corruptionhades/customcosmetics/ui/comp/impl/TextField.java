package me.corruptionhades.customcosmetics.ui.comp.impl;

import me.corruptionhades.customcosmetics.ui.comp.Callback;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.utils.render.FontUtil;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextField extends Preset {

    private String text;

    private final List<Character> chars = new ArrayList<>();
    private boolean typing;

    private boolean changed = false;
    private boolean editable = true;

    private Callback.ValueChangeCallback<String> onValueChange;

    public TextField(String text) {
        this.text = text;
    }

    public void addValueChangeCallback(Callback.ValueChangeCallback<String> onValueChange) {
        this.onValueChange = onValueChange;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        super.render(context, mouseX, mouseY, x, y, width, height);
        int sf = guiUtils.getGuiScale();
        guiUtils.drawRoundedRect(context, x, y, x + width, y + height, 15, new Color(126, 240, 124).getRGB());

        int middleY = (int) ((height / 2) - (FontUtil.normal.getHeight() / 2));
        FontUtil.normal.drawString(context, text, x + (5 * sf), y + middleY, -1);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

        if(!editable) return;

        if(isInside(mouseX, mouseY, x, y, x + width, y + height)) {
            text = "";
            chars.clear();
            typing = true;
            changed = true;
        }
        else {
            typing = false;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(!editable) return;

        if(typing) {
            if(keyCode == GLFW.GLFW_KEY_ENTER) {
                typing = false;
            }
            if(keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if(!chars.isEmpty()) {
                    int index = chars.size() - 1;
                    chars.remove(index);

                    StringBuilder sb = new StringBuilder();

                    for (Character c : chars) {
                        sb.append(c);
                    }

                    text = sb.toString();
                }
            }
            else {
                if(keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT || keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL /*|| keyCode == 15 || keyCode == 58 || keyCode == 56 || keyCode == 219 || keyCode == 203 || keyCode == 200 || keyCode == 208 || keyCode == 205 || keyCode == 210 || keyCode == 199 || keyCode == 184*/) {
                    return;
                }

                if(typedChar != '§') chars.add(typedChar);

                StringBuilder sb = new StringBuilder();

                for(Character c : chars) {
                    sb.append(c);
                }

                System.out.println(typedChar);

                if(onValueChange != null) onValueChange.onChange(text, sb.toString().replace("§", ""));
                text = sb.toString().replace("§", "");
            }
        }
    }

    public String getText() {
        return text;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
