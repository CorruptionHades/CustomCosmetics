package me.corruptionhades.customcosmetics.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.corruptionhades.customcosmetics.ping.Pinger;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
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

    @Shadow public abstract void render(RenderTickCounter tickCounter, boolean tick);

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld")
    private void renderer_postWorldRender(RenderTickCounter renderTickCounter, CallbackInfo ci) {
        Pinger.lastProjMat.set(RenderSystem.getProjectionMatrix());
        Pinger.lastModMat.set(RenderSystem.getModelViewMatrix());
      //  Pinger.lastWorldSpaceMatrix.set(renderTickCounter..peek().getPositionMatrix());
    }
}
