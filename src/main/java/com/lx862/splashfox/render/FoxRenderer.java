package com.lx862.splashfox.render;

import com.lx862.splashfox.config.Config;
import com.lx862.splashfox.data.ImagePosition;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;

import java.util.function.Function;

public class FoxRenderer {
    private static final RenderPipeline GUI_NO_CULL = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET).withLocation("pipeline/gui_textured").withCull(false).build());
    public static final Function<Identifier, RenderLayer> SplashFoxRenderLayer = Util.memoize((texture) -> RenderLayer.of("splashfox_gui_textured", 786432, GUI_NO_CULL, RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, TriState.FALSE, false)).build(false)));
    private double shiftY = 0;
    private double animationProgress = 0;

    public void render(MinecraftClient client, DrawContext drawContext, ImagePosition imagePosition, Config config, int mouseX, int mouseY, double elapsed, float alpha) {
        MatrixStack matrices = drawContext.getMatrices();
        double scaleBasis = Math.min((double)client.getWindow().getScaledWidth() * 0.75, client.getWindow().getScaledHeight()) * 0.25;
        int splashScreenScale = (int)(scaleBasis * 0.5);

        double size = config.foxSize * splashScreenScale;
        double dropHeight = config.dropHeight * splashScreenScale;
        double speedFactor = config.speed;
        boolean wobbly = config.wobbly;
        boolean flipped = config.flipped;
        Identifier foxImage = config.getImageIdentifier();

        final double centeredScreenWidth = (client.getWindow().getScaledWidth() / 2.0) - (size / 2);
        final double centeredScreenHeight = (client.getWindow().getScaledHeight() / 2.0) - dropHeight;
        final double x;
        final double y;

        switch(imagePosition) {
            case LEFT_TO_MOJANG -> {
                x = centeredScreenWidth - (6 * splashScreenScale);
                y = centeredScreenHeight + shiftY;
            }
            case RIGHT_TO_MOJANG -> {
                x = centeredScreenWidth + (6 * splashScreenScale);
                y = centeredScreenHeight + shiftY;
            }
            case ABOVE_MOJANG -> {
                x = centeredScreenWidth;
                y = centeredScreenHeight - (splashScreenScale) - size + shiftY;
            }
            case REPLACE_MOJANG -> {
                x = centeredScreenWidth;
                y = centeredScreenHeight + shiftY;
            }
            case FOLLOW_MOUSE -> {
                x = mouseX;
                y = mouseY + shiftY;
            }
            case GUI_PREVIEW -> {
                x = client.getWindow().getScaledHeight() > 450 ? centeredScreenWidth : (flipped ? 0 : client.getWindow().getScaledWidth() - size);
                y = centeredScreenHeight + shiftY;
            }
            default -> {
                x = centeredScreenWidth;
                y = shiftY;
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

        drawContext.drawTexture(SplashFoxRenderLayer, foxImage, 0, 0, 0, 0, (int)size, (int)size, (int)size, (int)size, 0xFFFFFF | ((int)(alpha * 255)) << 24);
        matrices.pop();

        animationProgress = getBounceProgress(speedFactor, elapsed / 10, wobbly);
        shiftY = (dropHeight * Math.min(0, -animationProgress)) + dropHeight;
    }

    private double getBounceProgress(double speedFactor, double x, boolean wobbly) {
        if(wobbly) {
            return Math.abs(Math.sin(x * speedFactor) * 1.2) - 0.2;
        } else {
            return Math.abs(Math.sin(x * speedFactor));
        }
    }
}
