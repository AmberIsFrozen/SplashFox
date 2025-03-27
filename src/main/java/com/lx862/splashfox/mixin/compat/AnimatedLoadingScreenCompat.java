package com.lx862.splashfox.mixin.compat;

import com.bawnorton.mixinsquared.TargetHandler;
import com.lx862.splashfox.SplashFox;
import com.lx862.splashfox.data.ImagePosition;
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
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"), cancellable = true)
    private void cancelAnimatedLoadingScreen(CallbackInfo ci) {
        if(SplashFox.config.position == ImagePosition.REPLACE_MOJANG) {
            ci.cancel();
        }
    }
}
