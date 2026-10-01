package io.mikaple.endertech.client.guis;

import io.mikaple.endertech.Endertech;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class EnderButton extends Button {
    protected EnderButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Identifier sprite = this.isHovered()
                ? Endertech.id("widget/ender_button_hovered")
                : Endertech.id("widget/ender_button");
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite,
                this.getX(), this.getY(), this.width, this.height);

        // 画文字（复用原版逻辑）
        this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
    }
}
