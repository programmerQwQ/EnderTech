package io.mikaple.endertech;

import io.mikaple.endertech.datagen.lang.ModChineseLangProvider;
import io.mikaple.endertech.datagen.lang.ModEnglishLangProvider;
import io.mikaple.endertech.datagen.model.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EnderTechDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModEnglishLangProvider::new);
        pack.addProvider(ModChineseLangProvider::new);
        pack.addProvider(ModModelProvider::new);
    }
}
