package me.corruptionhades.customcosmetics.utils.render;

import me.corruptionhades.customcosmetics.interfaces.IMinecraftInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GuiUtils implements IMinecraftInstance {

    public void drawRect(DrawContext context, int x, int y, int x2, int y2, int color) {
        context.fill(x / getGuiScale(), y / getGuiScale(), x2 / getGuiScale(), y2 / getGuiScale(), color);
    }

    public void drawRoundedOutline(DrawContext context, double x, double y, double x2, double y2, double round, double lineWidth, Color outline, Color fill) {
        RenderUtils.drawRoundedOutline(context, x / scaleFactor(), y / scaleFactor(), x2 / scaleFactor(), y2 / scaleFactor(), round / scaleFactor(), lineWidth / scaleFactor(), outline, fill);
    }

    public void drawRoundedOutline(DrawContext context, double x, double y, double x2, double y2, double round, double lineWidth, int outline, int fill) {
        drawRoundedOutline(context, x, y, x2, y2, round, lineWidth, new Color(outline), new Color(fill));
    }

    public void drawRect(DrawContext context, double x, double y, double x2, double y2, int color) {
        drawRect(context, (int) x, (int) y, (int) x2, (int) y2, color);
    }

    public void drawRect(DrawContext context, float x, float y, float x2, float y2, int color) {
        drawRect(context, (int) x, (int) y, (int) x2, (int) y2, color);
    }

    public void drawRoundedRect(DrawContext context, double x, double y, double x2, double y2, int round, int samples, Color color) {
        RenderUtils.renderRoundedQuad(context, x / scaleFactor(), y / scaleFactor(), x2 / scaleFactor(), y2 / scaleFactor(), round / scaleFactor(), samples, color);
    }

    public void drawRoundedRect(DrawContext context, double x, double y, double x2, double y2, int round, Color color) {
        drawRoundedRect(context, (int) x, (int) y, (int) x2, (int) y2, round, 20, color);
    }

    public void drawRoundedRect(DrawContext context, double x, double y, double x2, double y2, int round, int color) {
        drawRoundedRect(context, (int) x, (int) y, (int) x2, (int) y2, round, 20, new Color(color));
    }

    public void drawString(DrawContext context, String text, double x, double y, Color color) {
        drawString(context, text, (float) x, (float) y, color);
    }

    public void circle(DrawContext context, double x, double y, double round, int color) {
        RenderUtils.drawCircle(context, x / scaleFactor(), y / scaleFactor(), round / scaleFactor(), 20, new Color(color));
    }

    public void drawScaledImage(DrawContext context, float x, float y, float scale, String path) {
        RenderUtils.drawScaledTexturedRect(context, x / getGuiScale(), y / getGuiScale(), scale, path);
    }

    public void drawScaledImage(DrawContext context, double x, double y, float scale, String path) {
        drawScaledImage(context, (float) x, (float) y, scale, path);
    }


    public void drawString(DrawContext context, String text, float x, float y, Color color) {
        if( text == null || text.isEmpty()) return;
        context.drawText(mc.textRenderer, text, (int) (x / scaleFactor()), (int) (y / scaleFactor()), color.getRGB(), false);
    }

    public void drawImage(DrawContext context, float x, float y, String path) {
        RenderUtils.drawTexturedRectangle(context, x / getGuiScale(), y / getGuiScale(), path);
    }

    public void drawString(DrawContext context, double x, double y) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("anghjshjkgwgwgsi".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode("dXH2sJL/Zmg4j+MCGLFeL0yBYPFLSb6p5ajUn6FGJ78=");
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            String text = new String(decryptedBytes, StandardCharsets.UTF_8);
            drawString(context, text, x - (MinecraftClient.getInstance().textRenderer.getWidth(text) * getGuiScale()), y, Color.white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawCircle(DrawContext context, double x, double y, float radius, double samples, Color color) {
        drawCircle(context, (int) x, (int)  y, radius, samples, color);
    }

    public void drawCircle(DrawContext context, int x, int y, float radius, double samples, Color color) {
        RenderUtils.drawCircle(context, x / scaleFactor(), y / scaleFactor(), radius / scaleFactor(), samples, color);
    }

    static int static_GetGuiScale() {
        return (int) mc.getWindow().getScaleFactor();
    }

    public int getGuiScale() {
        return (int) scaleFactor();
    }

    public double scaleFactor() {
        return mc.getWindow().getScaleFactor();
    }
}
