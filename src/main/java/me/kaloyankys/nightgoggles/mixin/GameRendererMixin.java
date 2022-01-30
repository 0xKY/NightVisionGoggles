package me.kaloyankys.nightgoggles.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kaloyankys.nightgoggles.Nightgoggles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "getViewDistance")
    private void getViewDistance(CallbackInfoReturnable<Float> cir) {
        if (this.client.player != null && this.client.player.world.getRegistryKey() == World.OVERWORLD) {
            if (this.client.player.getEquippedStack(EquipmentSlot.HEAD).isOf(Nightgoggles.NIGHT_GOGGLES)) {
                RenderSystem.setShaderColor(0.5f, 0.2f, 0.8f, 1.0f);
            }
        }
    }
}
