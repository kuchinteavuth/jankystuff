package com.inteavuthkuch.jankystuff.screen;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.common.Texture;
import com.inteavuthkuch.jankystuff.menu.BlockBreakerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BlockBreakerScreen extends AbstractContainerScreen<BlockBreakerMenu> implements MenuAccess<BlockBreakerMenu> {
    private final Texture texture;

    public BlockBreakerScreen(BlockBreakerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.texture = ContainerType.BLOCK_BREAKER.getGui().getTexture();
        this.imageHeight = this.texture.imageHeight();
        this.imageWidth = this.texture.imageWidth();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture.location());

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        pGuiGraphics.blit(this.texture.location(), x, y, 0,0, imageWidth, imageHeight, this.texture.textureWidth(), this.texture.textureHeight());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, 8, (this.imageHeight - 96 + 4), 4210752, false);
    }
}
