package io.mikaple.endertech.index;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.component.WritableBookContent;

import java.util.function.Function;

public class ModItems {
    public static final Item WRITABLE_ENDER_BOOK = register(
            ModItemIds.WRITABLE_ENDER_BOOK,
            WritableBookItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.WRITABLE_BOOK_CONTENT, WritableBookContent.EMPTY)
    );

    private static Item register(
            ResourceKey<Item> itemKey,
            Function<Item.Properties, Item> itemFactory,
            Item.Properties properties
    ) {
        Item item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void register() {
    }
}
