package com.lx862.splashfox.data;

import com.lx862.splashfox.SplashFox;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

public class BuiltinResourceTexture extends ResourceTexture {
    public BuiltinResourceTexture(Identifier location) {
        super(location);
    }

    @Override
    public TextureContents loadContents(ResourceManager resourceManager) {
        final Identifier textureId = getId();
        final String path = "assets/" + textureId.getNamespace() + "/" + textureId.getPath();

        try(InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if(input != null) {
                return new TextureContents(NativeImage.read(input), new TextureResourceMetadata(true, true));
            }
        } catch (IOException e) {
            SplashFox.LOGGER.error("Failed to read internal path {}", path, e);
        }
        return TextureContents.createMissing();
    }
}
