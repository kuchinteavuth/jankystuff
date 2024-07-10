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
}
