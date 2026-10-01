package io.mikaple.endertech.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record EnderBookData(boolean canEdit, List<String> pages) {
    public static final EnderBookData EMPTY = new EnderBookData(false, List.of(""));

    public static final Codec<EnderBookData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("can_edit").forGetter(EnderBookData::canEdit),
                    Codec.STRING.listOf().fieldOf("pages").forGetter(EnderBookData::pages)
            ).apply(instance, EnderBookData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, EnderBookData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, EnderBookData::canEdit,
                    ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), EnderBookData::pages,
                    EnderBookData::new
            );
}