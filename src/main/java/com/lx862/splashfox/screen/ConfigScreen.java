package com.lx862.splashfox.screen;

import com.lx862.splashfox.config.Config;
import com.lx862.splashfox.data.ImagePosition;
import com.lx862.splashfox.data.ScreenAlignment;
import com.lx862.splashfox.SplashFox;
import com.lx862.splashfox.render.FoxRenderer;
import com.lx862.splashfox.screen.widget.SplashFoxSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigScreen extends Screen {
    private static final int MAX_WIDTH = 320;
    private final SplashFoxSlider dropHeightSlider;
    private final SplashFoxSlider foxSizeSlider;
    private final SplashFoxSlider speedSlider;
    private final Checkbox flippedCheckbox;
    private final Checkbox wobblyCheckbox;
    private final Button chooseImageButton;
    private final Button positionButton;
    private final Button discardButton;
    private final Button saveButton;
    private final List<Tuple<String, Integer>> labels;
    private final Config tmpConfigInstance;
    private final FoxRenderer foxRenderer;
    private final Screen parentScreen;
    private double elapsed;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("splashfox.gui.config_title"));
        this.parentScreen = parent;
        // Make another instance of config so changes only apply if the user click save
        tmpConfigInstance = Config.readConfig();
        foxRenderer = new FoxRenderer();
        labels = new ArrayList<>();

        int curY = 40;

        chooseImageButton = new Button.Builder(Component.translatable("splashfox.gui.choose"), (d) -> {
            ChooseImageScreen chooseImageScreen = new ChooseImageScreen(this, tmpConfigInstance);
            minecraft.setScreen(chooseImageScreen);
        }).build();
        chooseImageButton.setY(curY);
        labels.add(new Tuple<>("splashfox.gui.choose_img", curY));

        curY += 20;

        speedSlider = new SplashFoxSlider(0, curY, 100, 20, Component.literal(String.valueOf(tmpConfigInstance.speed)), tmpConfigInstance.speed, 2, (slider) -> {
            tmpConfigInstance.speed = slider.getValue();
        });
        labels.add(new Tuple<>("splashfox.gui.speed", curY));

        curY += 20;

        dropHeightSlider = new SplashFoxSlider(0, curY, 100, 20, Component.literal(String.valueOf(tmpConfigInstance.dropHeight)), tmpConfigInstance.dropHeight, 3, (slider) -> {
            tmpConfigInstance.dropHeight = slider.getValue();
        });
        labels.add(new Tuple<>("splashfox.gui.drop_height", curY));

        curY += 20;

        foxSizeSlider = new SplashFoxSlider(0, curY, 100, 20, Component.literal(String.valueOf(tmpConfigInstance.foxSize)), tmpConfigInstance.foxSize, 2, (slider) -> {
            tmpConfigInstance.foxSize = slider.getValue();
        });
        labels.add(new Tuple<>("splashfox.gui.blobfox_size", curY));

        curY += 20;

        flippedCheckbox = Checkbox.builder(Component.literal(""), Minecraft.getInstance().font)
                .selected(tmpConfigInstance.flipped)
                .pos(0, curY)
                .onValueChange((btn, checked) -> {
                    tmpConfigInstance.flipped = checked;
                }).build();
        labels.add(new Tuple<>("splashfox.gui.flipped", curY));

        curY += 20;

        wobblyCheckbox = Checkbox.builder(Component.literal(""), Minecraft.getInstance().font)
                .selected(tmpConfigInstance.wobbly)
                .pos(0, curY)
                .onValueChange((btn, checked) -> {
                    tmpConfigInstance.wobbly = checked;
                }).build();
        labels.add(new Tuple<>("splashfox.gui.wobbly", curY));

        curY += 20;

        positionButton = new Button.Builder(Component.translatable("splashfox.gui.position." + tmpConfigInstance.position.toString()), (d) -> {
            int index = tmpConfigInstance.position.ordinal();
            ImagePosition[] imagePositions = Arrays.stream(ImagePosition.values()).filter(e -> e.selectable).toArray(ImagePosition[]::new);
            tmpConfigInstance.position = imagePositions[(index + 1) % imagePositions.length];
            d.setMessage(Component.translatable("splashfox.gui.position." + tmpConfigInstance.position.toString()));
        }).build();

        positionButton.setY(curY);
        labels.add(new Tuple<>("splashfox.gui.position", curY));

        curY += 20;

        discardButton = new Button.Builder(Component.translatable("splashfox.gui.discard_config"), (d) -> {
            onClose();
        }).build();

        saveButton = new Button.Builder(Component.translatable("splashfox.gui.save_config"), (d) -> {
            Config.needUpdateTexture = true;
            Config.writeConfig(tmpConfigInstance);
            SplashFox.config = Config.readConfig();
            onClose();
        }).build();
    }

    @Override
    protected void init() {
        super.init();

        chooseImageButton.setWidth(100);
        chooseImageButton.setX(getX(chooseImageButton, ScreenAlignment.RIGHT));

        speedSlider.setX(getX(speedSlider, ScreenAlignment.RIGHT));
        dropHeightSlider.setX(getX(dropHeightSlider, ScreenAlignment.RIGHT));
        foxSizeSlider.setX(getX(foxSizeSlider, ScreenAlignment.RIGHT));
        flippedCheckbox.setX(getX(flippedCheckbox, ScreenAlignment.RIGHT));
        wobblyCheckbox.setX(getX(wobblyCheckbox, ScreenAlignment.RIGHT));

        positionButton.setWidth(120);
        positionButton.setX(getX(positionButton, ScreenAlignment.RIGHT));

        discardButton.setWidth(MAX_WIDTH / 2);
        discardButton.setX(getX(saveButton, ScreenAlignment.LEFT));
        discardButton.setY(this.height - saveButton.getHeight() - 10);

        saveButton.setWidth(MAX_WIDTH / 2);
        saveButton.setX(getX(saveButton, ScreenAlignment.RIGHT));
        saveButton.setY(this.height - saveButton.getHeight() - 10);

        addRenderableWidget(chooseImageButton);
        addRenderableWidget(speedSlider);
        addRenderableWidget(dropHeightSlider);
        addRenderableWidget(foxSizeSlider);
        addRenderableWidget(flippedCheckbox);
        addRenderableWidget(wobblyCheckbox);
        addRenderableWidget(positionButton);
        addRenderableWidget(discardButton);
        addRenderableWidget(saveButton);

        for(Tuple<String, Integer> label : labels) {
            StringWidget tw = new StringWidget(Component.translatable(label.getA()), font);
            tw.setX(getStartX());
            tw.setY(label.getB() + font.lineHeight);
            addRenderableWidget(tw);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if(minecraft == null) return;
        super.render(guiGraphics, mouseX, mouseY, delta);
        elapsed += delta;

        // Render fox preview :D
        foxRenderer.render(minecraft, guiGraphics, ImagePosition.GUI_PREVIEW, tmpConfigInstance, mouseX, mouseY, elapsed, 1.0f);

        guiGraphics.drawCenteredString(font, title, this.width / 2, 12, CommonColors.WHITE);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Screen.HEADER_SEPARATOR, 0, 30, 0.0F, 0.0F, this.width, 2, 32, 2);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - 40, 0.0F, 0.0F, this.width, 2, 32, 2);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parentScreen);
    }

    private int getX(int width, ScreenAlignment type) {
        if(minecraft != null) {
            int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
            int startX = (scaledWidth - MAX_WIDTH) / 2;

            if(type == ScreenAlignment.LEFT) {
                return startX;
            }

            if(type == ScreenAlignment.CENTERED) {
                return (scaledWidth / 2) - (width / 2);
            }

            if(type == ScreenAlignment.RIGHT) {
                return scaledWidth - startX - width;
            }
        }

        return 0;
    }

    private int getX(AbstractWidget widget, ScreenAlignment type) {
        return getX(widget.getWidth(), type);
    }

    private int getStartX() {
        return getX(0, ScreenAlignment.LEFT);
    }
}
