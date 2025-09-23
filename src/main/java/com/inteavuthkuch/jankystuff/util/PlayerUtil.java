package com.inteavuthkuch.jankystuff.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlayerUtil {

    @Nullable
    private static MenuProvider getMenuProvider(@NotNull Level pLevel, BlockPos pPos) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof MenuProvider ? (MenuProvider)blockentity : null;
    }

    public static void tryOpenMenu(Player player, Level pLevel, BlockPos pPos) {
        MenuProvider menu = PlayerUtil.getMenuProvider(pLevel, pPos);
        PlayerUtil.tryOpenMenu(player, menu);
    }

    public static void tryOpenMenu(Player player, @Nullable MenuProvider pMenu) {
        if(pMenu != null) {
            player.openMenu(pMenu);
        }
    }

    public static int getPlayerTotalXP(Player player) {
        int level = player.experienceLevel;
        float progress = player.experienceProgress;
        int xp = 0;

        for (int i = 0; i < level; i++) {
            xp += getXpNeededForLevel(i);
        }

        xp += Math.round(progress * getXpNeededForLevel(level));
        return xp;
    }

    public static int getXpNeededForLevel(int level) {
        if (level >= 30) return 9 * level - 158;
        if (level >= 16) return 5 * level - 38;
        return 2 * level + 7;
    }

    public static int getLevelFromXP(int xp) {
        int level = 0;

        while (true) {
            int xpToNext;
            if (level >= 30) {
                xpToNext = 9 * level - 158;
            } else if (level >= 16) {
                xpToNext = 5 * level - 38;
            } else {
                xpToNext = 2 * level + 7;
            }

            if (xp < xpToNext) {
                break;
            }

            xp -= xpToNext;
            level++;
        }

        return level;
    }
}
