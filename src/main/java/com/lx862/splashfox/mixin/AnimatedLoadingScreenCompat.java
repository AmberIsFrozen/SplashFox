package com.lx862.splashfox.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SplashOverlay.class, priority = 1500)
public class AnimatedLoadingScreenCompat {
    @TargetHandler(
            mixin = "com.cyao.animatedLogo.mixin.SplashOverlayMixin",
            name = "onAfterRenderLogo"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
    private void b(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo originalCi, int scaledWidth, int scaledHeight, long now, float fadeOutProgress, float fadeInProgress, float alpha, int x, int y, double height, int halfHeight, double width, int halfWidth, CallbackInfo ci) {
        // We should conditionally cancel here
        ci.cancel();
    }
}
