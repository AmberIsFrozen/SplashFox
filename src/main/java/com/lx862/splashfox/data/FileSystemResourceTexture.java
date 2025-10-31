package com.lx862.splashfox.data;

import com.lx862.splashfox.config.Config;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * A texture that reads an image on the user's filesystem.
 */
public class FileSystemResourceTexture extends SimpleTexture {
    private final String relativePath;

    public FileSystemResourceTexture(String relativePath, ResourceLocation id) {
        super(id);
        this.relativePath = relativePath;
    }

    @Override
    public TextureContents loadContents(ResourceManager resourceManager) {
        try(InputStream input = Files.newInputStream(Config.CUSTOM_IMG_PATH.resolve(relativePath))) {
            return new TextureContents(NativeImage.read(input), new TextureMetadataSection(true, true));
        } catch (IOException exception) {
            return TextureContents.createMissing();
        }
    }
}