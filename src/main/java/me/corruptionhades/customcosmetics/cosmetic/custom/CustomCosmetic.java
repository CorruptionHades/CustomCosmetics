package me.corruptionhades.customcosmetics.cosmetic.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.Cosmetic;
import me.corruptionhades.customcosmetics.cosmetic.custom.anim.Custom3ValueAnimation;
import me.corruptionhades.customcosmetics.objfile.ObjFile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CustomCosmetic extends Cosmetic {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    // Saving
    private File modelFile, textureFile;

    // Render
    private ObjFile model;
    private float transX, transY, transZ;
    private float rotX, rotY, rotZ;
    private float scaleX, scaleY, scaleZ;
    private Color color;
    private CustomResourceLocation textureLocation;

    private final List<Custom3ValueAnimation> customAnimations = new ArrayList<>();

    public CustomCosmetic(String name, BodyPart type, boolean item) {
        super(name, type, item);
        CustomCosmeticsClient.getInstance().getCosmeticManager().registerCosmetic(this);
    }

    /*
    I am making a custom cosmetic creator for my minecraft client and have some code here to handle cosmetics be ajacent to the left and right arm, but I also want the player to set custom rotations/scale/transformation, I tried the comments but
    **bruh it does work**
     */

    /**
     * @see CustomCosmeticsClient#onTick() for animated texture updating
     */
    @Override
    public void render(PlayerEntityModel model, MatrixStack matrices) {

        Matrix4f matrix4f = new Matrix4f();


        matrix4f.translate(transX, transY, transZ);
        matrix4f.rotate(rotX, 1, 0, 0);
        matrix4f.rotate(rotY, 0, 1, 0);
        matrix4f.rotate(rotZ, 0, 0, 1);
        matrix4f.scale(scaleX, scaleY, scaleZ);
        //matrix4f.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());


        if(textureLocation != null) {
       //     mc.getTextureManager().bindTexture(textureLocation.getTexture());
            RenderSystem.setShaderTexture(0, textureLocation.getTexture());
        }

        try {
            for (Custom3ValueAnimation customAnimation : customAnimations) {

                customAnimation.update();

                switch (customAnimation.getType()) {
                    case Translate: {
                        matrix4f.translate(customAnimation.getxAnimation().getAnimation().getValue(), customAnimation.getyAnimation().getAnimation().getValue(), customAnimation.getzAnimation().getAnimation().getValue());
                        break;
                    }
                    case Scale: {
                        matrix4f.scale(customAnimation.getxAnimation().getAnimation().getValue(), customAnimation.getyAnimation().getAnimation().getValue(), customAnimation.getzAnimation().getAnimation().getValue());
                    }
                    case Rotate: {
                        matrix4f.rotate(customAnimation.getxAnimation().getAnimation().getValue(), 1, 0, 0);
                        matrix4f.rotate(customAnimation.getyAnimation().getAnimation().getValue(), 0, 1, 0);
                        matrix4f.rotate(customAnimation.getzAnimation().getAnimation().getValue(), 0, 0, 1);
                    }
                }
            }
        }
        catch (ConcurrentModificationException ignored) {}

        this.model.draw(matrices, matrix4f, textureLocation == null ? null : textureLocation.getTexture());
    }

    @Override
    public void loadTexture() {

    }

    public void addCustomAnimation(Custom3ValueAnimation customAnimation) {
        customAnimations.add(customAnimation);
    }

    public void removeCustomAnimation(Custom3ValueAnimation customAnimation) {
        customAnimations.remove(customAnimation);
    }

    public void setModel(ObjFile model) {
        this.model = model;
    }

    public void setModelFile(File modelFile) {
        this.modelFile = modelFile;
    }

    public void setTransX(float transX) {
        this.transX = transX;
    }

    public void setTransY(float transY) {
        this.transY = transY;
    }

    public void setTransZ(float transZ) {
        this.transZ = transZ;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ObjFile getModel() {
        return model;
    }

    public void setTextureLocation(CustomResourceLocation textureFile) {
        this.textureLocation = textureFile;
    }

    public CustomResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public File getModelFile() {
        return modelFile;
    }

    public void setTextureFile(File textureFile) {
        this.textureFile = textureFile;
    }

    public File getTextureFile() {
        return textureFile;
    }

    public double getTransX() {
        return transX;
    }

    public double getTransY() {
        return transY;
    }

    public double getTransZ() {
        return transZ;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getScaleZ() {
        return scaleZ;
    }

    public Color getColor() {
        return color;
    }

    public List<Custom3ValueAnimation> getCustomAnimations() {
        return customAnimations;
    }
}
