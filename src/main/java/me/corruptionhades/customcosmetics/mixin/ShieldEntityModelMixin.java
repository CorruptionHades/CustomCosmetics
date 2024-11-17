package me.corruptionhades.customcosmetics.mixin;

import me.corruptionhades.customcosmetics.cosmetic.CosmeticItemRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShieldEntityModel.class)
public class ShieldEntityModelMixin {

  /*  @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderMixin(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if(CosmeticItemRenderer.render(new ItemStack(Items.SHIELD), matrices)) {
            ci.cancel();
        }
    } */
}
