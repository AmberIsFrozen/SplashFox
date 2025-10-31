package com.lx862.splashfox.screen.widget;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ChooseButton extends Button {
    public static final int PADDING = 4;
    private final ResourceLocation buttonTextureId;
    private boolean selected;
    private int baseY;

    public ChooseButton(int x, int y, int width, int height, boolean selected, ResourceLocation buttonTextureId, Button.OnPress pressAction, Component text) {
        super(x, y, width, height, text, pressAction, DEFAULT_NARRATION);
        this.baseY = y;
        this.buttonTextureId = buttonTextureId;
        this.selected = selected;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int startX = getX() - PADDING;
        int startY = getY() - PADDING;
        int sizeWidth = getWidth() + PADDING + PADDING;
        int sizeHeight = getHeight() + PADDING + PADDING;
        int endX = getX() + getWidth() + PADDING;
        int endY = getY() + getHeight() + PADDING;
        if(selected) {
            guiGraphics.fill(startX-1, startY-1, startX + sizeWidth+1, startY + sizeHeight+1, 0xFFFFFFFF);
            guiGraphics.fill(startX, startY, endX, endY, 0xFF000000);
        } else if (this.isHovered() || this.isFocused()) {
            int eX = startX + sizeWidth;
            int eY = startY + sizeHeight;
            // top bottom
            guiGraphics.fill(startX, startY-1, eX, startY, 0x66FFFFFF);
            guiGraphics.fill(startX, eY, eX, eY+1, 0x66FFFFFF);

            // left right (includ. corner)
            guiGraphics.fill(startX-1, startY-1, startX, eY+1, 0x66FFFFFF);
            guiGraphics.fill(eX, startY-1, eX+1, eY+1, 0x66FFFFFF);
        }
        if (this.isHovered()) {
            guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
        }
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, buttonTextureId, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
    }

    public void setSelected(boolean bl) {
        this.selected = bl;
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.baseY = y;
    }

    public void setYOffset(int offset) {
        super.setY(baseY + offset);
    }
}
