package io.mikaple.endertech.datagen.lang;

import io.mikaple.endertech.index.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModChineseLangProvider extends FabricLanguageProvider {
    public ModChineseLangProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, FabricLanguageProvider.@NonNull TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.WRITABLE_ENDER_BOOK, "末影之书");
    }
}
