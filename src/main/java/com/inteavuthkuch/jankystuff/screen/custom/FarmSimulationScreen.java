package com.inteavuthkuch.jankystuff.screen.custom;

import com.inteavuthkuch.jankystuff.common.Texture;
import com.inteavuthkuch.jankystuff.common.UserInterface;
import com.inteavuthkuch.jankystuff.menu.custom.FarmSimulationMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FarmSimulationScreen extends AbstractContainerScreen<FarmSimulationMenu> {
    private final Texture texture = UserInterface.FARM_SIMULATION.getTexture();
    private final Texture progressTexture = UserInterface.FARM_SIMULATION_PROGRESS.getTexture();

    public FarmSimulationScreen(FarmSimulationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = this.texture.imageWidth();
        this.imageHeight = this.texture.imageHeight();
    }

    private void renderProgress(GuiGraphics pGuiGraphics, int x, int y) {
        if(menu.isCrafting()){
            int progressTextureWidth = progressTexture.textureWidth();
            int progressTextureHeight = progressTexture.textureHeight();
            pGuiGraphics.blit(progressTexture.location(), x + 25, y + 35, 0, 0, menu.getScaledProgress(),
                    progressTextureHeight, progressTextureWidth, progressTextureHeight);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture.location());

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        pGuiGraphics.blit(this.texture.location(), x, y, 0,0, imageWidth, imageHeight, this.texture.textureWidth(), this.texture.textureHeight());
        renderProgress(pGuiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
