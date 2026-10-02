package io.mikaple.endertech.client.gui;

import io.mikaple.endertech.EnderTech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.Filterable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mojang.blaze3d.platform.InputConstants.KEYCODE_PAGEDOWN;
import static com.mojang.blaze3d.platform.InputConstants.KEYCODE_PAGEUP;
import static net.minecraft.client.gui.TextAlignment.RIGHT;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED;
import static net.minecraft.network.chat.CommonComponents.EMPTY;

public class EnderBookEditScreen extends Screen {
    public static final Component TITLE = Component.empty();
    public static final Identifier BOOK = EnderTech.id("textures/gui/ender_book.png");

    public static final int EDITOR_Y = 28;
    public static final int EDITOR_GAP = 16;
    public static final int EDITOR_WIDTH = 122;
    public static final int EDITOR_HEIGHT = 134;
    public static final int EDITOR_OFFSET_LEFT = -EDITOR_GAP / 2 - EDITOR_WIDTH;
    public static final int EDITOR_OFFSET_RIGHT = EDITOR_GAP / 2;

    public static final int IMAGE_Y = 2;
    public static final int IMAGE_WIDTH = 288;
    public static final int IMAGE_HEIGHT = 192;
    public static final int IMAGE_OFFSET = -IMAGE_WIDTH / 2;

    public static final int BUTTON_Y = 196;
    public static final int BUTTON_GAP = 4;
    public static final int BUTTON_WIDTH = 98;
    public static final int BUTTON_OFFSET_LEFT = -BUTTON_GAP / 2 - BUTTON_WIDTH;
    public static final int BUTTON_OFFSET_RIGHT = BUTTON_GAP / 2;

    public static final int PAGE_BUTTON_Y = 157;
    public static final int PAGE_BUTTON_GAP = 18;
    public static final int PAGE_BUTTON_WIDTH = 23;
    public static final int PAGE_BUTTON_OFFSET_LEFT = -IMAGE_WIDTH / 2 + PAGE_BUTTON_GAP;
    public static final int PAGE_BUTTON_OFFSET_RIGHT = IMAGE_WIDTH / 2 - PAGE_BUTTON_GAP - PAGE_BUTTON_WIDTH;

    public static final int PAGE_INDICATOR_Y = 18;
    public static final int PAGE_INDICATOR_OFFSET = 4;
    public static final int PAGE_INDICATOR_OFFSET_LEFT = -EDITOR_GAP / 2 - PAGE_INDICATOR_OFFSET;
    public static final int PAGE_INDICATOR_OFFSET_RIGHT = EDITOR_GAP / 2 - PAGE_INDICATOR_OFFSET + EDITOR_WIDTH;

    private final Player owner;
    private final ItemStack book;
    private final List<String> pages;
    private final InteractionHand hand;

    private PageButton buttonNext;
    private PageButton buttonPrev;
    private MultiLineEditBox editorLeft;
    private MultiLineEditBox editorRight;

    private int currentPageLeft = 0;
    private int currentPageRight = 1;

    public EnderBookEditScreen(
            Player owner,
            ItemStack book,
            InteractionHand hand,
            WritableBookContent content
    ) {
        super(TITLE);
        this.pages = content.getPages(Minecraft.getInstance().isTextFilteringEnabled()).collect(Collectors.toCollection(ArrayList::new));
        this.owner = owner;
        this.book = book;
        this.hand = hand;
    }

    @Override
    public void init() {
        this.buttonPrev = new PageButton(this.width / 2 + PAGE_BUTTON_OFFSET_LEFT, PAGE_BUTTON_Y, false, _ -> this.prevPage(), true);
        this.buttonNext = new PageButton(this.width / 2 + PAGE_BUTTON_OFFSET_RIGHT, PAGE_BUTTON_Y, true, _ -> this.nextPage(), true);
        this.editorLeft = newEditor(false, this.width / 2 + EDITOR_OFFSET_LEFT);
        this.editorRight = newEditor(true, this.width / 2 + EDITOR_OFFSET_RIGHT);

        this.addRenderableWidget(this.buttonPrev);
        this.addRenderableWidget(this.buttonNext);
        this.addRenderableWidget(this.editorLeft);
        this.addRenderableWidget(this.editorRight);

        var cancelButton = Button.builder(CommonComponents.GUI_CANCEL, _ -> this.onClose()).pos(this.width / 2 + BUTTON_OFFSET_LEFT, BUTTON_Y).width(BUTTON_WIDTH).build();
        var doneButton = Button.builder(CommonComponents.GUI_DONE, _ -> this.saveChanges()).pos(this.width / 2 + BUTTON_OFFSET_RIGHT, BUTTON_Y).width(BUTTON_WIDTH).build();

        this.addRenderableWidget(cancelButton);
        this.addRenderableWidget(doneButton);
        this.ensureEnoughPages();
        this.updateEditorContent();
        this.updateButtonVisibility();
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        return switch (event.shortcutKey()) {
            case KEYCODE_PAGEUP -> {
                this.buttonPrev.onPress(event);
                yield true;
            }
            case KEYCODE_PAGEDOWN -> {
                this.buttonNext.onPress(event);
                yield true;
            }
            default -> super.keyPressed(event);
        };
    }

    @Override
    public void extractBackground(
            @NonNull GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float a
    ) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(
                GUI_TEXTURED,
                BOOK,
                IMAGE_OFFSET + width / 2, IMAGE_Y, 0, 0,
                IMAGE_WIDTH, IMAGE_HEIGHT,
                IMAGE_WIDTH, IMAGE_HEIGHT
        );
    }

    @Override
    public void extractRenderState(
            @NonNull GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float a
    ) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.textRenderer().accept(RIGHT, PAGE_INDICATOR_OFFSET_LEFT + width / 2, PAGE_INDICATOR_Y, getPageNumberMessage(currentPageLeft));
        graphics.textRenderer().accept(RIGHT, PAGE_INDICATOR_OFFSET_RIGHT + width / 2, PAGE_INDICATOR_Y, getPageNumberMessage(currentPageRight));
    }

    private Component getPageNumberMessage(int page) {
        return Component
                .translatable("book.pageIndicator", page + 1, this.pages.size())
                .withColor(0xFF000000)
                .withoutShadow();
    }

    private void prevPage() {
        if (this.currentPageLeft > 0) {
            this.currentPageLeft -= 2;
            this.currentPageRight -= 2;
            this.ensureEnoughPages();
            this.updateEditorContent();
            this.updateButtonVisibility();
        }
    }

    private void nextPage() {
        if (this.currentPageRight < WritableBookContent.MAX_PAGES - 1) {
            this.currentPageLeft += 2;
            this.currentPageRight += 2;
            this.ensureEnoughPages();
            this.updateEditorContent();
            this.updateButtonVisibility();
        }
    }

    private void ensureEnoughPages() {
        if (this.pages.size() < currentPageRight + 1) this.pages.add("");
        if (this.pages.size() < currentPageRight + 1) this.pages.add("");
    }

    private void updatePageContent(boolean rightSide, String value) {
        if (!rightSide) {
            this.pages.set(this.currentPageLeft, value);
        } else {
            this.pages.set(this.currentPageRight, this.editorRight.getValue());
        }
    }

    private void updateEditorContent() {
        this.editorLeft.setValue(this.pages.get(currentPageLeft));
        this.editorRight.setValue(this.pages.get(currentPageRight));
    }

    private void updateButtonVisibility() {
        this.buttonPrev.visible = this.currentPageLeft > 0;
    }

    private void saveChanges() {
        var connection = this.minecraft.getConnection();
        if (connection == null) return;

        var iter = this.pages.listIterator(this.pages.size());
        var slot = this.hand == InteractionHand.MAIN_HAND
                ? this.owner.getInventory().getSelectedSlot()
                : Inventory.SLOT_OFFHAND;

        while (iter.hasPrevious() && iter.previous().isEmpty()) iter.remove();
        var packet = new ServerboundEditBookPacket(slot, this.pages, Optional.empty());
        var writableBookContent = new WritableBookContent(this.pages.stream().map(Filterable::passThrough).toList());
        this.book.set(DataComponents.WRITABLE_BOOK_CONTENT, writableBookContent);
        this.owner.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
        this.minecraft.gui.setScreen(null);
        connection.send(packet);
    }

    private MultiLineEditBox newEditor(boolean rightSide, int x) {
        MultiLineEditBox editor = MultiLineEditBox.builder()
                .setTextShadow(false)
                .setShowBackground(false)
                .setShowDecorations(false)
                .setTextColor(0xFF000000)
                .setCursorColor(0xFF000000)
                .setX(x)
                .setY(EDITOR_Y)
                .build(font, EDITOR_WIDTH, EDITOR_HEIGHT, EMPTY);

        editor.setValueListener(value -> this.updatePageContent(rightSide, value));
        editor.setCharacterLimit(1024);
        editor.setLineLimit(14);
        return editor;
    }
}
