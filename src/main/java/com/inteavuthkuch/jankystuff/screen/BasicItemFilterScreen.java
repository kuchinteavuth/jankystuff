package com.inteavuthkuch.jankystuff.screen;

import com.inteavuthkuch.jankystuff.common.ContainerType;
import com.inteavuthkuch.jankystuff.common.Texture;
import com.inteavuthkuch.jankystuff.inventory.GhostSlot;
import com.inteavuthkuch.jankystuff.menu.BasicItemFilterMenu;
import com.inteavuthkuch.jankystuff.network.packet.GhostSlotUpdatePacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class BasicItemFilterScreen extends AbstractContainerScreen<BasicItemFilterMenu> {
    private final Texture texture;

    public BasicItemFilterScreen(BasicItemFilterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.texture = ContainerType.BASIC_ITEM_FILTER.getGui().getTexture();
        this.imageWidth = this.texture.imageWidth();
        this.imageHeight = this.texture.imageHeight();
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
    protected void renderSlot(GuiGraphics pGuiGraphics, Slot pSlot) {
        super.renderSlot(pGuiGraphics, pSlot);
        if(pSlot instanceof GhostSlot && pSlot.hasItem()){
            ItemStack stack = pSlot.getItem();

            // Render the item with transparency
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0, 0, 100); // render above other elements

            pGuiGraphics.renderItem(
                    stack,
                    pSlot.x,
                    pSlot.y,
                    0x80FFFFFF // semi-transparent white overlay
            );

            pGuiGraphics.pose().popPose();

        }
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType clickType) {
        if (slot instanceof GhostSlot) {
            ItemStack held = menu.getCarried();
            ItemStack ghostCopy = ItemStack.EMPTY;

            if (!held.isEmpty() && slot.mayPlace(held)) {
                ghostCopy = held.copy();
                ghostCopy.setCount(1);
            }

            PacketDistributor.sendToServer(new GhostSlotUpdatePacket(slot.index, ghostCopy));
        } else {
            super.slotClicked(slot, slotId, mouseButton, clickType);
        }
    }
}
