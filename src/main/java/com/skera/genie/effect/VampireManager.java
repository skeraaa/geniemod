package com.skera.genie.effect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class VampireManager {
    
    public enum Rank {
        NONE, LOW, MID, HIGH
    }

    public static Rank getRank(PlayerEntity player) {
        NbtCompound nbt = player.getPersistentData();
        if (!nbt.contains("genie_vampire_rank")) return Rank.NONE;
        return Rank.values()[nbt.getInt("genie_vampire_rank")];
    }

    public static void setRank(PlayerEntity player, Rank rank) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putInt("genie_vampire_rank", rank.ordinal());
        
        // Сброс крови при повышении/понижении
        if (rank != Rank.NONE) {
            nbt.putInt("genie_blood_level", 0);
            nbt.putInt("genie_blood_max", 100 * (rank.ordinal() + 1));
        }
    }

    public static void addBlood(PlayerEntity player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int current = nbt.getInt("genie_blood_level");
        int max = nbt.getInt("genie_blood_max");
        nbt.putInt("genie_blood_level", Math.min(current + amount, max));
    }

    public static boolean hasThirst(PlayerEntity player) {
        return getRank(player) != Rank.NONE;
    }

    // Проверка солнца
    public static boolean canStandInSun(PlayerEntity player) {
        if (getRank(player) == Rank.HIGH) return true;
        // Проверка кожаной брони (упрощенно)
        return player.getInventory().getArmorStack(2).getName().getString().contains("Leather") &&
               player.getInventory().getArmorStack(3).getName().getString().contains("Leather");
    }
}
