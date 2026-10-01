package io.mikaple.endertech.items;

import io.mikaple.endertech.components.EnderBookData;
import io.mikaple.endertech.components.ModComponents;
import io.mikaple.endertech.packet.OpenEnderBookPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

public class EnderBookItem extends Item {
    public static final ResourceKey<Item> KEY = ModItemIds.create("ender_book");
    public static final Item.Properties properties = new Item.Properties()
            .stacksTo(1)
            .component(ModComponents.ENDER_BOOK_DATA,new EnderBookData(true,new ArrayList<>()));

    public EnderBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(final @NonNull Level level, final @NonNull Player player, final @NonNull InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer,new OpenEnderBookPacket());
        }
        return InteractionResult.SUCCESS;
    }

    public EnderBookData getEnderBookData() {
        return this.components().getOrDefault(
                ModComponents.ENDER_BOOK_DATA,
                new EnderBookData(true, new ArrayList<>())
        );
    }

    public static EnderBookData getEnderBookData(ItemStack stack) {
        return stack.getOrDefault(ModComponents.ENDER_BOOK_DATA, EnderBookData.EMPTY);
    }
}
