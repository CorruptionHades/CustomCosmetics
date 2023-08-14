package me.corruptionhades.customcosmetics.cosmetic.custom;

import me.corruptionhades.customcosmetics.animation.Animation;
import me.corruptionhades.customcosmetics.animation.Easing;
import me.corruptionhades.customcosmetics.cosmetic.custom.anim.Custom3ValueAnimation;

import java.util.ArrayList;
import java.util.List;

public class CustomAnimation {

    private double displayStart, displayEnd;
    private float start, end;

    private Easing easing;
    private AnimationType animationType;
    private long duration;

    private final Custom3ValueAnimation parent;
    private Animation animation;

    public CustomAnimation(Custom3ValueAnimation parent) {
        this.parent = parent;
        this.easing = Easing.LINEAR;
        this.animationType = AnimationType.TranslateX;
        this.duration = 1000L;

        this.start = (float) displayStart;
        this.end = (float) displayEnd;
        animation = new Animation(duration, start, end, easing);
    }

    public void reverse() {
        float oldEnd = this.end;
        this.end = this.start;
        this.start = oldEnd;

        animation.setEnd(end);
    }

    public double getDisplayStart() {
        // round it to #.####
        return Math.round(displayStart * 10000.0) / 10000.0;
    }

    public void setDisplayStart(double displayStart) {
        this.displayStart = displayStart;
        updateAnimation();
    }

    public String getName() {
        return switch (animationType) {
            case TranslateX, ScaleX, RotateX -> "X";
            case TranslateY, ScaleY, RotateY -> "Y";
            case TranslateZ, ScaleZ, RotateZ -> "Z";
        };
    }

    public double getDisplayEnd() {
        return Math.round(displayEnd * 10000.0) / 10000.0;
    }

    public void setDisplayEnd(double displayEnd) {
        this.displayEnd = displayEnd;
        updateAnimation();
    }

    public Easing getEasing() {
        return easing;
    }

    public void setEasing(Easing easing) {
        this.easing = easing;
        updateAnimation();
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        updateAnimation();
    }

    private void updateAnimation() {
        this.start = (float) displayStart;
        this.end = (float) displayEnd;
        animation = new Animation(duration, start, end, easing);

        parent.getxAnimation().getAnimation().reset();
        parent.getyAnimation().getAnimation().reset();
        parent.getzAnimation().getAnimation().reset();
    }

    public enum AnimationType {
        TranslateX, TranslateY, TranslateZ, ScaleX, ScaleY, ScaleZ, RotateX, RotateY, RotateZ;

        public static List<String> getValues() {
            List<String> values = new ArrayList<>();
            for (AnimationType value : AnimationType.values()) {
                values.add(value.name());
            }
            return values;
        }
    }

    public Animation getAnimation() {
        return animation;
    }
}
