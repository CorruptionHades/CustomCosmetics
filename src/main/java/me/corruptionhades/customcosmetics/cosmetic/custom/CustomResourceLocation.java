package me.corruptionhades.customcosmetics.cosmetic.custom;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomResourceLocation {

    private final List<Identifier> textures = new ArrayList<>();
    private final boolean animated;

    private int fpt;

    private int currentTick;
    private int currentFrame;

    public CustomResourceLocation(boolean animated, Identifier...locations) {
        this(animated, 1, locations);
    }

    public CustomResourceLocation(boolean animated, int fpt, Identifier...locations) {
        this.animated = animated;
        this.fpt = fpt;
        currentFrame = 0;
        currentTick = 0;
        if(!animated) {
            textures.add(locations[0]);
        }
        else textures.addAll(Arrays.asList(locations));
    }

    /**
     * @see CustomCosmeticsClient#onTick() for animated texture updating
     */
    public void update() {
        if(currentTick > fpt) {
            currentTick = 0;
            currentFrame++;
            if(currentFrame > textures.size() - 1) {
                currentFrame = 0;
            }
        }
        currentTick++;
    }

    public Identifier getTexture() {
        return textures.get(currentFrame);
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setFpt(int fpt) {
        this.fpt = fpt;
    }
}
