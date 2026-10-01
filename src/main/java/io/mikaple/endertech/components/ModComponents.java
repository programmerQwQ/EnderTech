package io.mikaple.endertech.components;

import io.mikaple.endertech.Endertech;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModComponents {
    public static final DataComponentType<EnderBookData> ENDER_BOOK_DATA = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Endertech.id("ender_book_data"),
            DataComponentType.<EnderBookData>builder()
                    .persistent(EnderBookData.CODEC)
                    .networkSynchronized(EnderBookData.STREAM_CODEC)
                    .build()
    );

    public static void initialize() {
    }
}
