package io.mikaple.endertech.index;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.world.item.ItemStack;

public class ModEvents {
    public static void register() {
        ServerPlayerEvents.JOIN.register(player -> {
            if (!player.getAttachedOrCreate(ModAttachments.GOT_STARTER_KIT)) {
                player.getInventory().add(new ItemStack(ModItems.WRITABLE_ENDER_BOOK));
                player.setAttached(ModAttachments.GOT_STARTER_KIT, true);
            }
        });
    }
}
