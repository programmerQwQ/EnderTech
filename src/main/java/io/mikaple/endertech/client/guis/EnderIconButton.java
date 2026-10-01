package io.mikaple.endertech.client.guis;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class EnderIconButton extends Button {
    private final Identifier normalSprite;
    private final Identifier hoverSprite;

    public EnderIconButton(int x, int y, int width, int height,
                           Identifier normalSprite, Identifier hoverSprite,
                           Button.OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.normalSprite = normalSprite;
        this.hoverSprite = hoverSprite;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Identifier sprite = this.isHovered() ? this.hoverSprite : this.normalSprite;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite,
                this.getX(), this.getY(), this.width, this.height);
    }
}