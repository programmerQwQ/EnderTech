package io.mikaple.endertech.index;

import com.mojang.serialization.Codec;
import io.mikaple.endertech.EnderTech;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class ModAttachments {
    public static final AttachmentType<Boolean> GOT_STARTER_KIT = AttachmentRegistry.create(
            EnderTech.id("got_starter_kit"),
            builder -> builder
                    .initializer(() -> false)
                    .persistent(Codec.BOOL)
                    .copyOnDeath()
    );

    public static void register() {
    }
}
