package io.mikaple.endertech.packet;

import io.mikaple.endertech.Endertech;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record OpenEnderBookPacket() implements CustomPacketPayload {

    public static final Identifier PAYLOAD_ID = Endertech.id("open_ender_book");

    public static final CustomPacketPayload.Type<OpenEnderBookPacket> TYPE =
            new CustomPacketPayload.Type<>(PAYLOAD_ID);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenEnderBookPacket> CODEC =
            StreamCodec.unit(new OpenEnderBookPacket());
}
