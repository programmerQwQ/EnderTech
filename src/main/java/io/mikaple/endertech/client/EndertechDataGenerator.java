package io.mikaple.endertech.client;

import io.mikaple.endertech.client.langs.ModChineseLangProvider;
import io.mikaple.endertech.client.langs.ModEnglishLangProvider;
import io.mikaple.endertech.client.models.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EndertechDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModEnglishLangProvider::new);
        pack.addProvider(ModChineseLangProvider::new);
        pack.addProvider(ModModelProvider::new);
    }
}
