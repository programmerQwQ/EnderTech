package io.mikaple.endertech.client.langs;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.mikaple.endertech.items.ModItems.ENDER_BOOK;

public class ModChineseLangProvider extends FabricLanguageProvider {
    public ModChineseLangProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, "zh_cn" ,registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, FabricLanguageProvider.@NonNull TranslationBuilder translationBuilder) {
        translationBuilder.add(ENDER_BOOK, "末影之书");
        translationBuilder.add("gui.ender_tech.ender_book.save", "保存");
        translationBuilder.add("gui.ender_tech.ender_book.cancel", "取消");
    }
}
