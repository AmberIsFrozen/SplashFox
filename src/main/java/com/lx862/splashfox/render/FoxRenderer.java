package com.lx862.splashfox.render;

import com.lx862.splashfox.config.Config;
import com.lx862.splashfox.data.ImagePosition;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FoxRenderer {
    private final Config config;
    private final MinecraftClient client;

    public FoxRenderer(MinecraftClient client, Config config) {
        this.config = config;
        this.client = client;
    }
    public void render(DrawContext drawContext, ImagePosition imagePosition, int mouseX, int mouseY, double elapsed, float alpha) {
        MatrixStack matrices = drawContext.getMatrices();
        double scaleBasis = Math.min((double)client.getWindow().getScaledWidth() * 0.75, client.getWindow().getScaledHeight()) * 0.25;
        int splashScreenScale = (int)(scaleBasis * 0.5);

        double size = config.foxSize * splashScreenScale;
        double dropHeight = config.dropHeight * splashScreenScale;
        double speedFactor = config.speed;
        boolean wobbly = config.wobbly;
        boolean flipped = config.flipped;
        Identifier foxImage = config.getImageIdentifier();

        double animationProgress = getBounceProgress(speedFactor, elapsed / 10, wobbly);
        final double offsetY = (dropHeight * Math.min(0, -animationProgress)) + dropHeight;
        final double centeredScreenWidth = (client.getWindow().getScaledWidth() / 2.0) - (size / 2);
        final double centeredScreenHeight = (client.getWindow().getScaledHeight() / 2.0) - dropHeight;
        final double x;
        final double y;

        switch(imagePosition) {
            case LEFT_TO_MOJANG -> {
                x = centeredScreenWidth - (6 * splashScreenScale);
                y = centeredScreenHeight + offsetY;
            }
            case RIGHT_TO_MOJANG -> {
                x = centeredScreenWidth + (6 * splashScreenScale);
                y = centeredScreenHeight + offsetY;
            }
            case ABOVE_MOJANG -> {
                x = centeredScreenWidth;
                y = centeredScreenHeight - (splashScreenScale) - size + offsetY;
            }
            case REPLACE_MOJANG -> {
                x = centeredScreenWidth;
                y = centeredScreenHeight + offsetY;
            }
            case FOLLOW_MOUSE -> {
                x = mouseX;
                y = mouseY + offsetY;
            }
            case GUI_PREVIEW -> {
                x = client.getWindow().getScaledHeight() > 450 ? centeredScreenWidth : (flipped ? 0 : client.getWindow().getScaledWidth() - size);
                y = centeredScreenHeight + offsetY;
            }
            default -> {
                x = centeredScreenWidth;
                y = offsetY;
            }
        }

        matrices.push();
        matrices.translate(x, y, 0);

        if(flipped) {
            matrices.scale(-1, 1, 1);
            matrices.translate(-size, 0, 0);
        }

        if(wobbly && animationProgress <= 0) {
            // Deform the fox
            float deformScale = (float)(animationProgress * (config.dropHeight * 0.75));
            matrices.translate(size / 2.0, size, 0);
            matrices.scale(1 - deformScale, 1 + deformScale, 1);
            matrices.translate(-(size / 2.0), -size, 0);
        }

        RenderSystem.enableBlend();

        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        drawContext.drawTexture(foxImage, 0, 0, 0, 0, (int)size, (int)size, (int)size, (int)size);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        matrices.pop();
    }

    private double getBounceProgress(double speedFactor, double x, boolean wobbly) {
        if(wobbly) {
            return Math.abs(Math.sin(x * speedFactor) * 1.2) - 0.2;
        } else {
            return Math.abs(Math.sin(x * speedFactor));
        }
    }
}
