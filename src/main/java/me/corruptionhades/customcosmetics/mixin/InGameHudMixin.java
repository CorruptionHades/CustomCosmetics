package me.corruptionhades.customcosmetics.mixin;

import me.corruptionhades.customcosmetics.cosmetic.CosmeticFeatureRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At("RETURN"))
    public void renderHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        context.drawText(client.textRenderer, CosmeticFeatureRenderer.text, 10, 10, -1, false);
    }
}
