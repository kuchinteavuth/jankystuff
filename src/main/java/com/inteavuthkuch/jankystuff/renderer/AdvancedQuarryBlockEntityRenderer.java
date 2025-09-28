package com.inteavuthkuch.jankystuff.renderer;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.block.AdvancedQuarryBlock;
import com.inteavuthkuch.jankystuff.blockentity.AdvancedQuarryBlockEntity;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;


public class AdvancedQuarryBlockEntityRenderer implements BlockEntityRenderer<AdvancedQuarryBlockEntity> {
    public AdvancedQuarryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(AdvancedQuarryBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        MutableComponent status = Component.translatable("state.jankystuff.running");
        int color = 0xFFFFFF;

        if(pBlockEntity.isDisabled()){
            status = Component.translatable("state.jankystuff.disable");
            color = 0xFF0000;
        } else if(pBlockEntity.isPause()){
            status = Component.translatable("state.jankystuff.pause");
            color = 0xFFFF00;
        } else if(pBlockEntity.isCompletedWork()){
            status = Component.translatable("state.jankystuff.completed");
            color = 0x00FF00;
        } else if(pBlockEntity.isMissionInventory()) {
            status = Component.translatable("state.jankystuff.missing_inventory");
            color = 0xFF0000;
        } else if(pBlockEntity.isNoInventory()){
            status = Component.translatable("state.jankystuff.no_inventory");
            color = 0xFF0000;
        }

        Font font = Minecraft.getInstance().font;
        Direction facing = pBlockEntity.getBlockState().getValue(AdvancedQuarryBlock.FACING);
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 0.9, 0.5);

        switch (facing) {
            case NORTH -> {}
            case SOUTH -> pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
            case EAST -> pPoseStack.mulPose(Axis.YP.rotationDegrees(-90));
        }

        pPoseStack.translate(0, 0, -0.501);
        pPoseStack.scale(0.01f, 0.01f, 0.01f);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(180));

        float x = -font.width(status) / 2f;
        int light = LightTexture.pack(12, 12); // softer but still clear
        font.drawInBatch(status,
                x,
                0,
                color,
                false,
                pPoseStack.last().pose(),
                pBufferSource,
                Font.DisplayMode.NORMAL,
                OverlayTexture.NO_OVERLAY,
                light);
        pPoseStack.popPose();
    }
}
