package me.corruptionhades.customcosmetics.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import me.corruptionhades.customcosmetics.ping.Pinger;
import me.corruptionhades.customcosmetics.utils.FastMStack;
import me.corruptionhades.customcosmetics.utils.RenderRefs;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    public abstract void render(RenderTickCounter tickCounter, boolean tick);

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld")
    private void renderer_postWorldRender(RenderTickCounter renderTickCounter, CallbackInfo ci) {
        Pinger.lastProjMat.set(RenderSystem.getProjectionMatrix());
        Pinger.lastModMat.set(RenderSystem.getModelViewMatrix());
        //  Pinger.lastWorldSpaceMatrix.set(renderTickCounter..peek().getPositionMatrix());
    }

    @WrapOperation(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"))
    void renderer_postWorldRender(WorldRenderer instance, ObjectAllocator allocator, RenderTickCounter tickCounter,
                                  boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                                  LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix,
                                  Matrix4f projectionMatrix, Operation<Void> original) {

        original.call(instance, allocator, tickCounter, renderBlockOutline, camera, gameRenderer, lightmapTextureManager,
                positionMatrix, projectionMatrix);


        RenderRefs.projectionMatrix = projectionMatrix;
        RenderRefs.positionMatrix = positionMatrix;

        MatrixStack matrix = new FastMStack();
        matrix.multiplyPositionMatrix(positionMatrix);

        RenderRefs.lastProjMat = RenderSystem.getProjectionMatrix();
        RenderRefs.lastModelMat = RenderSystem.getModelViewMatrix();
        RenderRefs.lastWorldSpaceMatrix = matrix.peek().getPositionMatrix();

        RenderRefs.onRender(matrix, positionMatrix, projectionMatrix);
    }
}