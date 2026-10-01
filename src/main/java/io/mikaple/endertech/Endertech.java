package io.mikaple.endertech;

import com.mojang.serialization.Codec;
import io.mikaple.endertech.components.EnderBookData;
import io.mikaple.endertech.components.ModComponents;
import io.mikaple.endertech.items.EnderBookItem;
import io.mikaple.endertech.items.ModItems;
import io.mikaple.endertech.packet.EditEnderBookPacket;
import io.mikaple.endertech.packet.OpenEnderBookPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class Endertech implements ModInitializer {
    public static String nameSpace = "ender_tech";
    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(nameSpace,name);
    }

    public static final AttachmentType<Boolean> GOT_STARTER_KIT = AttachmentRegistry.create(
            id("got_starter_kit"),
            builder -> builder
                    .persistent(Codec.BOOL)
                    .initializer(() -> false)
                    .copyOnDeath()
    );

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModComponents.initialize();

        PayloadTypeRegistry.clientboundPlay().register(
                OpenEnderBookPacket.TYPE,
                OpenEnderBookPacket.CODEC
        );
        PayloadTypeRegistry.serverboundPlay().register(
                EditEnderBookPacket.TYPE,
                EditEnderBookPacket.CODEC
        );

        ServerPlayConnectionEvents.JOIN.register((handler, _, _) -> {
            ServerPlayer player = handler.getPlayer();
            Boolean played = player.getAttachedOrCreate(GOT_STARTER_KIT);
            if (!played) {
                player.getInventory().add(0,new ItemStack(ModItems.ENDER_BOOK));
                player.setAttached(GOT_STARTER_KIT,true);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(EditEnderBookPacket.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (stack.getItem() instanceof EnderBookItem) {
                    EnderBookData data = EnderBookItem.getEnderBookData(stack);
                    if (data.canEdit()) stack.set(ModComponents.ENDER_BOOK_DATA, new EnderBookData(true, payload.pages()));
                }
            });
        });
    }
}
