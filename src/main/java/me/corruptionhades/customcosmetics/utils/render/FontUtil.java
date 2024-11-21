package me.corruptionhades.customcosmetics.utils.render;

import me.corruptionhades.customcosmetics.interfaces.IMinecraftInstance;
import net.minecraft.client.gui.DrawContext;

public class FontUtil implements IMinecraftInstance {

    public static FontRenderer normal = new FontRenderer();
    public static FontRenderer big = new FontRenderer();

    public static class FontRenderer {
        public void drawString(DrawContext context, String text, double x, double y, int color) {
            int sf = GuiUtils.static_GetGuiScale();
            context.drawText(mc.textRenderer, text, (int) (x / sf), (int) (y / sf), color, false);
        }

        public int getHeight() {
            return mc.textRenderer.fontHeight * GuiUtils.static_GetGuiScale();
        }

        public int getStringWidth(String text) {
            return mc.textRenderer.getWidth(text) * GuiUtils.static_GetGuiScale();
        }
    }
}
