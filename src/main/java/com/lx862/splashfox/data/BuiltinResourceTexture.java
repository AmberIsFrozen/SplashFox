package com.lx862.splashfox.data;

import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

public class BuiltinResourceTexture extends ResourceTexture {
    public BuiltinResourceTexture(Identifier location) {
        super(location);
    }

    @Override
    protected TextureData loadTextureData(ResourceManager resourceManager) {
        final Identifier textureId = location;

        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/" + textureId.getNamespace() + "/" + textureId.getPath());
            TextureData texture = null;

            if(input != null) {
                try {
                    texture = new TextureData(new TextureResourceMetadata(true, true), NativeImage.read(input));
                } finally {
                    input.close();
                }
            }

            return texture;
        } catch (IOException exception) {
            return new TextureData(exception);
        }
    }
}
