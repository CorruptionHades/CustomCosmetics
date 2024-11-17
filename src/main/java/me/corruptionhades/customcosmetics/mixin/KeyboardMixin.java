package me.corruptionhades.customcosmetics.mixin;

import me.corruptionhades.customcosmetics.CustomCosmeticsClient;
import me.corruptionhades.customcosmetics.ping.Pinger;
import me.corruptionhades.customcosmetics.ui.MainScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if(key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            MinecraftClient.getInstance().setScreen(new MainScreen());
        }
        else if(key == GLFW.GLFW_KEY_R) {
            Pinger.ping();
        }
        else if(key == GLFW.GLFW_KEY_J) {
            CustomCosmeticsClient.open();
        }
    }
}
