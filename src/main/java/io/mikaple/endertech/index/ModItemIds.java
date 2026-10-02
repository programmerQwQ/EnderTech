package io.mikaple.endertech.index;

import io.mikaple.endertech.EnderTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static final ResourceKey<Item> WRITABLE_ENDER_BOOK = create("writable_ender_book");

    private static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, EnderTech.id(name));
    }
}
