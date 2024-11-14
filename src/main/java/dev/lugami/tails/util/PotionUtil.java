//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.util;

import org.bukkit.potion.PotionEffectType;

public class PotionUtil {
    public PotionUtil() {
    }

    public static String getName(PotionEffectType potionEffectType) {
        if (potionEffectType.getName().equalsIgnoreCase("fire_resistance")) {
            return "Fire Resistance";
        } else if (potionEffectType.getName().equalsIgnoreCase("speed")) {
            return "Speed";
        } else if (potionEffectType.getName().equalsIgnoreCase("weakness")) {
            return "Weakness";
        } else {
            return potionEffectType.getName().equalsIgnoreCase("slowness") ? "Slowness" : "Unknown";
        }
    }
}
