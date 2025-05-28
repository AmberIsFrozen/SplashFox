package com.lx862.splashfox.data;

import com.lx862.splashfox.config.Config;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * A texture that reads an image on the user's filesystem.
 */
public class FileSystemResourceTexture extends ResourceTexture {
    private final String relativePath;

    public FileSystemResourceTexture(String relativePath, Identifier location) {
        super(location);
        this.relativePath = relativePath;
    }

    @Override
    public TextureContents loadContents(ResourceManager resourceManager) {
        try(InputStream input = Files.newInputStream(Config.CUSTOM_IMG_PATH.resolve(relativePath))) {
            return new TextureContents(NativeImage.read(input), new TextureResourceMetadata(true, true));
        } catch (IOException exception) {
            return TextureContents.createMissing();
        }
    }
}