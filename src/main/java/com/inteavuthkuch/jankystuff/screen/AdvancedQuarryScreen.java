package com.inteavuthkuch.jankystuff.screen;

import com.inteavuthkuch.jankystuff.JankyStuff;
import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.common.JankyBlockEntityType;
import com.inteavuthkuch.jankystuff.common.QuarryActionCode;
import com.inteavuthkuch.jankystuff.common.Texture;
import com.inteavuthkuch.jankystuff.config.JankyStuffCommonConfig;
import com.inteavuthkuch.jankystuff.menu.AdvancedQuarryMenu;
import com.inteavuthkuch.jankystuff.network.packet.BlockEntityPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class AdvancedQuarryScreen extends AbstractContainerScreen<AdvancedQuarryMenu> {
    private final Texture texture;
    private Button pauseButton;
    private boolean lastPauseState = false;
    private boolean lastDisableState = false;

    public AdvancedQuarryScreen(AdvancedQuarryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.texture = ContainerType.ADVANCED_QUARRY.getGui().getTexture();
        this.imageWidth = this.texture.imageWidth();
        this.imageHeight = this.texture.imageHeight();
    }

    @Override
    protected void init() {
        super.init();

        pauseButton = Button.builder(Component.translatable("button.jankystuff.pause"), this::onButtonPress)
                .bounds(leftPos + 133, topPos + 36, 36, 16)
                .createNarration(narration -> Component.translatable("button.jankystuff.pause"))
                .tooltip(Tooltip.create(Component.translatable("button.jankystuff.pause")))
                .build();

        this.addRenderableWidget(pauseButton);

        pauseButton.active = !JankyStuffCommonConfig.DISABLE_QUARRY.get();
        if(JankyStuffCommonConfig.DISABLE_QUARRY.get()){
            pauseButton.setMessage(Component.translatable("state.jankystuff.disable"));
            pauseButton.setTooltip(Tooltip.create(Component.translatable("text.jankystuff.disable_in_config")));
        } else {
            pauseButton.setMessage(Component.translatable("button.jankystuff.pause"));
            pauseButton.setTooltip(Tooltip.create(Component.translatable("button.jankystuff.pause")));
        }

        lastDisableState = JankyStuffCommonConfig.DISABLE_QUARRY.get();
    }


    private void onButtonPress(Button button) {
        int action = menu.isPause() ? QuarryActionCode.RESUME : QuarryActionCode.PAUSE;
        PacketDistributor.sendToServer(new BlockEntityPacket(
                menu.blockEntity.getBlockPos(),
                action,
                JankyBlockEntityType.ADVANCED_QUARRY.getTypeCode()
        ));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture.location());

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(this.texture.location(), x, y, 0,0, imageWidth, imageHeight, this.texture.textureWidth(), this.texture.textureHeight());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);

        MutableComponent status = Component.translatable("text.jankystuff.status").append(CommonComponents.space());

        boolean isPaused = menu.isPause();
        boolean isCompleted = menu.isCompletedWork();
        boolean noInventory = menu.isNoInventory();
        boolean missingInventory = menu.isMissionInventory();
        boolean isDisabled = menu.isDisabled();

        if (isCompleted) {
            status.append(Component.translatable("state.jankystuff.completed"));
        } else if (isDisabled) {
            status.append(Component.translatable("state.jankystuff.disable").withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        } else if (isPaused) {
            status.append(Component.translatable("state.jankystuff.pause"));
        } else if (noInventory) {
            status.append(Component.translatable("state.jankystuff.no_inventory"));
        } else if(missingInventory){
            status.append(Component.translatable("state.jankystuff.missing_inventory"));
        } else {
            status.append(Component.translatable("state.jankystuff.running"));
        }

        pGuiGraphics.drawWordWrap(this.font, status, 8, 18, 150, 4210752);

        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();
        MutableComponent progressComponent = Component.translatable("text.jankystuff.mined", progress, maxProgress);
        pGuiGraphics.drawWordWrap(this.font, progressComponent, 8, 28, 100, 4210752);

        if(pauseButton != null){
            if(lastDisableState != isDisabled) {
                pauseButton.active = !isDisabled;
                if (isDisabled) {
                    pauseButton.setMessage(Component.translatable("state.jankystuff.disable"));
                    pauseButton.setTooltip(Tooltip.create(Component.translatable("text.jankystuff.disable_in_config")));
                } else {
                    pauseButton.setMessage(Component.translatable("button.jankystuff.pause"));
                    pauseButton.setTooltip(Tooltip.create(Component.translatable("button.jankystuff.pause")));
                }

                lastDisableState = isDisabled;
                lastPauseState = isPaused;

            } else if (!isDisabled && lastPauseState != isPaused) {
                pauseButton.setFocused(false);
                pauseButton.active = true;
                if (isPaused) {
                    pauseButton.setMessage(Component.translatable("text.jankystuff.start"));
                    pauseButton.setTooltip(Tooltip.create(Component.translatable("text.jankystuff.start")));
                } else {
                    pauseButton.setMessage(Component.translatable("button.jankystuff.pause"));
                    pauseButton.setTooltip(Tooltip.create(Component.translatable("button.jankystuff.pause")));
                }
                lastPauseState = isPaused;
            }
        }
    }
}
