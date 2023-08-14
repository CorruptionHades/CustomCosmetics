package me.corruptionhades.customcosmetics.ui.comp;

import me.corruptionhades.customcosmetics.utils.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.io.IOException;

public class Preset {

    protected double x, y, width, height;
    protected GuiUtils guiUtils = new GuiUtils();

    public void render(DrawContext context, int mouseX, int mouseY, double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void handleMouseInput(double wheel) throws IOException {}

    public void render(DrawContext context, int mouseX, int mouseY) {}

    public void onClick(double mouseX, double mouseY, int button) {}

    public void onRelease(double mouseX, double mouseY, int button) {}

    public void keyTyped(char typedChar, int keyCode) {}

    public final boolean isInside(double mouseX, double mouseY, double x, double y, double x2, double y2) {
        double sf = MinecraftClient.getInstance().getWindow().getScaleFactor();
        x /= sf;
        y = y / sf;
        x2 = x2 / sf;
        y2 = y2 / sf;

        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
