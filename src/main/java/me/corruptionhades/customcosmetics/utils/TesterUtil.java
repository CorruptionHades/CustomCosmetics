package me.corruptionhades.customcosmetics.utils;

public class TesterUtil {

    public static boolean isUbuntu() {
        String osName = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        System.out.println(osName + " " + arch);
        return osName.contains("nix") || osName.contains("nux") || osName.contains("aix") || osName.contains("mac") || arch.contains("arm");
    }
}
