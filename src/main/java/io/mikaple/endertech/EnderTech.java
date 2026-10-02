package io.mikaple.endertech;

import io.mikaple.endertech.index.ModAttachments;
import io.mikaple.endertech.index.ModEvents;
import io.mikaple.endertech.index.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class EnderTech implements ModInitializer {
    public static String MOD_ID = "endertech";
    public static String MOD_NAME = "EnderTech";

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        ModAttachments.register();
        ModEvents.register();
        ModItems.register();
    }
}
