package io.mikaple.endertech.packet;

import io.mikaple.endertech.Endertech;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record EditEnderBookPacket(List<String> pages) implements CustomPacketPayload {
    public static final Identifier PAYLOAD_ID = Endertech.id("edit_ender_book");

    public static final CustomPacketPayload.Type<EditEnderBookPacket> TYPE =
            new CustomPacketPayload.Type<>(PAYLOAD_ID);

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, EditEnderBookPacket> CODEC =
            StreamCodec.composite(
                    net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8.apply(
                            net.minecraft.network.codec.ByteBufCodecs.list()),
                    EditEnderBookPacket::pages,
                    EditEnderBookPacket::new
            );
}
