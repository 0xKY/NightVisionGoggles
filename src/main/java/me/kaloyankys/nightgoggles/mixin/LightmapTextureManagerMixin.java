package me.kaloyankys.nightgoggles.mixin;

import me.kaloyankys.nightgoggles.Nightgoggles;
import me.kaloyankys.nightgoggles.item.NightGoggles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "getBrightness", cancellable = true)
    private void getBrightness(World world, int lightLevel, CallbackInfoReturnable<Float> cir) {
        if (this.client.player != null && this.client.player.world.getRegistryKey() == World.OVERWORLD) {
            if (this.client.player.getEquippedStack(EquipmentSlot.HEAD).isOf(Nightgoggles.NIGHT_GOGGLES)) {
                cir.setReturnValue((cir.getReturnValueF() + 30) + (this.client.player.world.getLightLevel(LightType.BLOCK, this.client.player.getBlockPos())) * -1);
            }
        }
    }
}

