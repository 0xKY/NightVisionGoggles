package me.kaloyankys.nightgoggles.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.kaloyankys.nightgoggles.Nightgoggles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Shadow @Final private static Identifier VIGNETTE_TEXTURE;

    @Shadow protected abstract void renderSpyglassOverlay(float scale);

    @Inject(at = @At(value = "FIELD", ordinal = 0,
            target = "Lnet/minecraft/client/MinecraftClient;interactionManager:Lnet/minecraft/client/network/ClientPlayerInteractionManager;"), method = "render")
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (this.client.player != null && this.client.player.world.getRegistryKey() == World.OVERWORLD) {
            if (this.client.player.getEquippedStack(EquipmentSlot.HEAD).isOf(Nightgoggles.NIGHT_GOGGLES)) {
                ClientPlayerEntity player = this.client.player;
                if (player != null && this.client.interactionManager != null) {
                    this.renderGreenVignette(1.0F);
                    this.renderGreenVignette(2.0F);
                    this.renderGreenVignette(150.0F);
                    this.renderSpyglassOverlay(2.0F);
                }
            }
        }
    }

    private void renderGreenVignette(float multiplication) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(1.0F, 0.0F, 0.8F, 2.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0D, this.scaledHeight, -90.0D).texture(0.0F, multiplication).next();
        bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -90.0D).texture(1.0F, multiplication).next();
        bufferBuilder.vertex(this.scaledWidth, 0.0D, -90.0D).texture(multiplication, 0.0F).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}
