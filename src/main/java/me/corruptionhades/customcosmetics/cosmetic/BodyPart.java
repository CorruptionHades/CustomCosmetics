package me.corruptionhades.customcosmetics.cosmetic;

public enum BodyPart {
    HEAD,
    BODY,
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG;

    public static String[] bodyParts() {
        String[] bodyParts = new String[BodyPart.values().length];
        for(int i = 0; i < BodyPart.values().length; i++) {
            bodyParts[i] = BodyPart.values()[i].name();
        }
        return bodyParts;
    }
}
