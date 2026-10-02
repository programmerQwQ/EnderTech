package io.mikaple.endertech.mixins;

import io.mikaple.endertech.client.gui.EnderBookEditScreen;
import io.mikaple.endertech.index.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Inject(method = "openItemGui", at = @At("HEAD"), cancellable = true)
    private void enderTech$openEnderBook(ItemStack itemStack, InteractionHand hand, CallbackInfo ci) {
        WritableBookContent content = itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT);
        if (!itemStack.is(ModItems.WRITABLE_ENDER_BOOK)) return;
        if (content == null) return;
        EnderBookEditScreen screen = new EnderBookEditScreen((Player) (Object) this, itemStack, hand, content);
        this.minecraft.gui.setScreen(screen);
        ci.cancel();
    }
}
