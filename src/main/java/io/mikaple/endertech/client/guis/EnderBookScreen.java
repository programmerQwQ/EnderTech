package io.mikaple.endertech.client.guis;

import io.mikaple.endertech.Endertech;
import io.mikaple.endertech.components.EnderBookData;
import io.mikaple.endertech.packet.EditEnderBookPacket;
import io.mikaple.endertech.utils.EnderBookContextTransformer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EnderBookScreen extends Screen {
    public static boolean opening = false;
    private static final Identifier ENDER_BOOK_LOCATION = Endertech.id("ender_book");
    private static final int drawWidth = 384;
    private static final int drawHeight = 256;
    private MultiLineEditBox page1 = null;
    private MultiLineEditBox page2 = null;
    private boolean canEdit = false;
    private final EnderBookData enderBookData;
    private int currentPage = 1;
    private final List<String> pages;
    private EnderIconButton prevButton;
    private EnderIconButton nextButton;

    public EnderBookScreen(Component title, EnderBookData enderBookData) {
        this.enderBookData = enderBookData;
        super(title);
        this.pages = new ArrayList<>(enderBookData.pages());
    }

    @Override
    protected void init() {
        canEdit = this.enderBookData.canEdit();
        opening = true;
        if (canEdit) {
            page1 = createPage(this.width / 2 - drawWidth / 2 + 15,currentPage);
            page2 = createPage(this.width / 2 + 15,currentPage + 1);
            this.addRenderableWidget(page1);
            this.addRenderableWidget(page2);
            this.addRenderableWidget(new EnderButton(
                    this.width / 2 - drawWidth / 2 + 15,this.height-backgroundTop(),48,32,
                    Component.translatable("gui.ender_tech.ender_book.save") ,button -> {
                        saveCurrentPages();
                        eraseEmptyTrailingPages();
                        ClientPlayNetworking.send(new EditEnderBookPacket(this.pages));
                        onClose();
            }));
            this.addRenderableWidget(new EnderButton(
                    this.width / 2 + drawWidth / 2 - 63,this.height-backgroundTop(),48,32,
                    Component.translatable("gui.ender_tech.ender_book.cancel") ,button -> {
                        onClose();
            }));
            this.setInitialFocus(page1);
        }
        prevButton = new EnderIconButton(
                this.width / 2 - drawWidth / 2 - 13,this.height / 2 - 30,16,64,
                Endertech.id("widget/prev_page"),Endertech.id("widget/prev_page_hovered"),
                button -> {
                    prevPage();
                });
        nextButton = new EnderIconButton(
                this.width / 2 + drawWidth / 2 - 3,this.height / 2 - 30,16,64,
                Endertech.id("widget/next_page"),Endertech.id("widget/next_page_hovered"),
                button -> {
                    nextPage();
                }
        );
        this.addRenderableWidget(prevButton);
        this.addRenderableWidget(nextButton);
        updateButtonVisibility();
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        if (!canEdit) {
            renderComponentPage(graphics,this.width / 2 - drawWidth / 2 + 15, currentPage);
            renderComponentPage(graphics,this.width / 2 + 15, currentPage + 1);
        }
        renderPageNumbers(graphics);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        this.minecraft.gui.hud.extractDeferredSubtitles();
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                ENDER_BOOK_LOCATION,
                this.backgroundLeft(), this.backgroundTop(),
                drawWidth, drawHeight);
    }

    private int backgroundLeft() {
        return (this.width - drawWidth) / 2;
    }

    private int backgroundTop() {
        return (this.height - drawHeight) / 2 - 5;
    }

    @Override
    public void removed() {
        opening = false;
    }

    private MultiLineEditBox createPage(int x, int page) {
        MultiLineEditBox editBox = MultiLineEditBox.builder()
                .setX(x)
                .setY(this.backgroundTop() + 10)
                .setShowBackground(false)
                .setShowDecorations(false)
                .setTextColor(0xFFFEFEFE)
                .setCursorColor(0xFFFEFEFE)
                .setTextShadow(false)
                .build(this.font, 160, 235, CommonComponents.EMPTY);
        if (enderBookData.pages().size() >= page) {
            String string = enderBookData.pages().get(page-1);
            if (string != null) {
                editBox.setValue(string);
            }
        }
        editBox.setLineLimit(25);
        editBox.setCharacterLimit(2048);
        return editBox;
    }

    private void renderComponentPage(GuiGraphicsExtractor graphics, int x, int page) {
        int innerPadding = 4;
        int lineHeight = 9;
        int startX = x + innerPadding;
        int startY = this.backgroundTop() + 10 + innerPadding;
        List<FormattedCharSequence> lines = new ArrayList<>();
        List<String> pages = enderBookData.pages();
        if (pages.size() >= page) {
            Component component = EnderBookContextTransformer.fromString(pages.get(page -1));
            lines = this.font.split(component,152);
        }
        for (int i = 0; i < lines.size(); i++) {
            graphics.text(this.font, lines.get(i), startX, startY + i * lineHeight, 0xFFFEFEFE, false);
        }
    }

    private void saveCurrentPages() {
        if (!canEdit) return;
        while (pages.size() < currentPage + 1) {
            pages.add("");
        }
        pages.set(currentPage - 1, page1.getValue());
        pages.set(currentPage,     page2.getValue());
    }

    private void refreshPages() {
        if (!canEdit) return;

        page1.setValue(currentPage <= pages.size() ? pages.get(currentPage - 1) : "");
        page2.setValue(currentPage + 1 <= pages.size() ? pages.get(currentPage) : "");
    }

    private void nextPage() {
        if (canEdit) {
            saveCurrentPages();
            currentPage += 2;
            refreshPages();
        }
        if (!canEdit && currentPage + 2 <= enderBookData.pages().size()) {
            currentPage += 2;
        }
        updateButtonVisibility();
    }

    private void prevPage() {
        if (currentPage - 2 >= 1) {
            if (canEdit) {
                saveCurrentPages();
            }
            currentPage -= 2;
            refreshPages();
        }
        updateButtonVisibility();
    }
    private void updateButtonVisibility() {
        prevButton.visible = currentPage - 2 >= 1;
        if (!canEdit) nextButton.visible = currentPage + 2 <= enderBookData.pages().size();
    }

    private void eraseEmptyTrailingPages() {
        ListIterator<String> it = pages.listIterator(pages.size());
        while (it.hasPrevious() && it.previous().isEmpty()) {
            it.remove();
        }
    }

    private void renderPageNumbers(GuiGraphicsExtractor graphics) {
        int total = pages.size();
        renderPageNumber(graphics, this.width / 2 - drawWidth / 2 + 15, currentPage, total);
        renderPageNumber(graphics, this.width / 2 + 15, currentPage + 1, total);
    }

    private void renderPageNumber(GuiGraphicsExtractor graphics, int pageContentLeft, int page, int total) {
        if (page > total) {
            if (!canEdit) return;
            total = page;;
        }
        String text = page + "/" + total;
        int textWidth = this.font.width(text);
        int centerX = pageContentLeft + 160 / 2; // 与内容框宽度一致
        int x = centerX - textWidth / 2;
        int y = this.backgroundTop() + drawHeight;
        graphics.text(this.font, text, x, y, 0xFFFEFEFE, false);
    }
}
