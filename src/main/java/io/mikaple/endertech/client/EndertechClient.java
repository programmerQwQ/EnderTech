package io.mikaple.endertech.client;

import io.mikaple.endertech.client.guis.EnderBookScreen;
import io.mikaple.endertech.components.EnderBookData;
import io.mikaple.endertech.items.EnderBookItem;
import io.mikaple.endertech.packet.OpenEnderBookPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class EndertechClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenEnderBookPacket.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (item.typeHolder().value() instanceof EnderBookItem ) {
                        EnderBookData enderBookData = EnderBookItem.getEnderBookData(item);
                        context.client().setScreenAndShow(new EnderBookScreen(Component.literal("title"), enderBookData));
                    }
                }
            });
        });
    }
}
