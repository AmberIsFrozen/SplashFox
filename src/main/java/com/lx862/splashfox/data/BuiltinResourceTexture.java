package com.lx862.splashfox.data;

import com.lx862.splashfox.SplashFox;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;

public class BuiltinResourceTexture extends SimpleTexture {
    public BuiltinResourceTexture(Identifier id) {
        super(id);
    }

    @Override
    public TextureContents loadContents(ResourceManager resourceManager) {
        final Identifier textureId = resourceId();
        final String path = "assets/" + textureId.getNamespace() + "/" + textureId.getPath();

        try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if(input != null) {
                return new TextureContents(NativeImage.read(input), new TextureMetadataSection(true, true, MipmapStrategy.MEAN, TextureMetadataSection.DEFAULT_ALPHA_CUTOFF_BIAS));
            }
        } catch (IOException e) {
            SplashFox.LOGGER.error("Failed to read internal path {}", path, e);
        }
        return TextureContents.createMissing();
    }
}
