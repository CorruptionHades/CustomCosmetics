package me.corruptionhades.customcosmetics.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.corruptionhades.customcosmetics.utils.FastMStack;
import me.corruptionhades.customcosmetics.utils.RenderRefs;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        //RenderRefs.projectionMatrix = projectionMatrix;
      //  RenderRefs.positionMatrix = positionMatrix;
    }
}
