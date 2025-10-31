package com.lx862.splashfox.screen.widget;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ChooseButton extends ButtonWidget {
    public static final int PADDING = 4;
    private final Identifier buttonTexture;
    private boolean selected;
    private int baseY;

    public ChooseButton(int x, int y, int width, int height, boolean selected, Identifier buttonTexture, ButtonWidget.PressAction pressAction, Text text) {
        super(x, y, width, height, text, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.baseY = y;
        this.buttonTexture = buttonTexture;
        this.selected = selected;
    }

    @Override
    public void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        int startX = getX() - PADDING;
        int startY = getY() - PADDING;
        int sizeWidth = getWidth() + PADDING + PADDING;
        int sizeHeight = getHeight() + PADDING + PADDING;
        int endX = getX() + getWidth() + PADDING;
        int endY = getY() + getHeight() + PADDING;
        if(selected) {
            drawContext.fill(startX-1, startY-1, startX + sizeWidth+1, startY + sizeHeight+1, 0xFFFFFFFF);
            drawContext.fill(startX, startY, endX, endY, 0xFF000000);
        } else if (this.isHovered() || this.isFocused()) {
            int eX = startX + sizeWidth;
            int eY = startY + sizeHeight;
            // top bottom
            drawContext.fill(startX, startY-1, eX, startY, 0x66FFFFFF);
            drawContext.fill(startX, eY, eX, eY+1, 0x66FFFFFF);

            // left right (includ. corner)
            drawContext.fill(startX-1, startY-1, startX, eY+1, 0x66FFFFFF);
            drawContext.fill(eX, startY-1, eX+1, eY+1, 0x66FFFFFF);
        }
        if (this.isHovered()) {
            drawContext.setCursor(StandardCursors.POINTING_HAND);
        }
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, buttonTexture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
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
