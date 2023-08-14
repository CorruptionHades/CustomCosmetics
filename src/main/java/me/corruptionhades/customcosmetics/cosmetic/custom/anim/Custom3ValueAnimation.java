package me.corruptionhades.customcosmetics.cosmetic.custom.anim;

import me.corruptionhades.customcosmetics.cosmetic.custom.CustomAnimation;

import java.util.ArrayList;
import java.util.List;

public class Custom3ValueAnimation {

    private String name;
    private CustomAnimation xAnimation, yAnimation, zAnimation;
    private Type type;
    private boolean reverse;

    public Custom3ValueAnimation(String name) {
        this.name = name;
        this.type = Type.Translate;
        this.reverse = false;
    }

    public void setxAnimation(CustomAnimation xAnimation) {
        this.xAnimation = xAnimation;
    }

    public void setyAnimation(CustomAnimation yAnimation) {
        this.yAnimation = yAnimation;
    }

    public void setzAnimation(CustomAnimation zAnimation) {
        this.zAnimation = zAnimation;
    }

    public CustomAnimation getxAnimation() {
        return xAnimation;
    }

    public CustomAnimation getyAnimation() {
        return yAnimation;
    }

    public CustomAnimation getzAnimation() {
        return zAnimation;
    }

    public Type getType() {
        return type;
    }

    public boolean shouldReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public void setType(Type type) {
        this.type = type;

        switch (type) {
            case Translate -> {
                xAnimation.setAnimationType(CustomAnimation.AnimationType.TranslateX);
                xAnimation.setDisplayStart(0);
                xAnimation.setDisplayEnd(0);
                yAnimation.setAnimationType(CustomAnimation.AnimationType.TranslateY);
                yAnimation.setDisplayStart(0);
                yAnimation.setDisplayEnd(0);
                zAnimation.setAnimationType(CustomAnimation.AnimationType.TranslateZ);
                zAnimation.setDisplayStart(0);
                zAnimation.setDisplayEnd(0);
            }
            case Scale -> {
                xAnimation.setAnimationType(CustomAnimation.AnimationType.ScaleX);
                xAnimation.setDisplayStart(1);
                xAnimation.setDisplayEnd(1);
                yAnimation.setAnimationType(CustomAnimation.AnimationType.ScaleY);
                yAnimation.setDisplayStart(1);
                yAnimation.setDisplayEnd(1);
                zAnimation.setAnimationType(CustomAnimation.AnimationType.ScaleZ);
                zAnimation.setDisplayStart(1);
                zAnimation.setDisplayEnd(1);
            }
            case Rotate -> {
                xAnimation.setAnimationType(CustomAnimation.AnimationType.RotateX);
                xAnimation.setDisplayStart(0);
                xAnimation.setDisplayEnd(0);
                yAnimation.setAnimationType(CustomAnimation.AnimationType.RotateY);
                yAnimation.setDisplayStart(0);
                yAnimation.setDisplayEnd(0);
                zAnimation.setAnimationType(CustomAnimation.AnimationType.RotateZ);
                zAnimation.setDisplayStart(0);
                zAnimation.setDisplayEnd(0);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void update() {
        if(xAnimation.getAnimation().isDone()) {
            if(reverse) {
                xAnimation.reverse();
            } else {
                xAnimation.getAnimation().reset();
            }
        }

        if(yAnimation.getAnimation().isDone()) {
            if(reverse) {
                yAnimation.reverse();
            } else {
                yAnimation.getAnimation().reset();
            }
        }

        if(zAnimation.getAnimation().isDone()) {
            if(reverse) {
                zAnimation.reverse();
            } else {
                zAnimation.getAnimation().reset();
            }
        }
    }

    public enum Type {
        Translate, Scale, Rotate;

        public static List<String> getValues() {
            List<String> values = new ArrayList<>();
            for(Type type : values()) {
                values.add(type.name());
            }
            return values;
        }
    }
}
