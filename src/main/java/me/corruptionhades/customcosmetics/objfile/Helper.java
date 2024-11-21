package me.corruptionhades.customcosmetics.objfile;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUsage;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Helper {

    public static VertexBuffer createVbo(BuiltBuffer builder, GlUsage expectedUsage) {
        VertexBuffer buffer = new VertexBuffer(expectedUsage);
        buffer.bind();
        buffer.upload(builder);
        VertexBuffer.unbind();
        return buffer;
    }

    public static void setupRender() {
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
       // RenderSystem.depthFunc(GL12.GL_LEQUAL);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void endRender() {
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableDepthTest();
    }

    public static void registerBufferedImageTexture(Identifier i, BufferedImage bi) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", out);
            byte[] bytes = out.toByteArray();

            ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
            data.flip();
            NativeImageBackedTexture tex = new NativeImageBackedTexture(NativeImage.read(data));
            MinecraftClient.getInstance()
                    .execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(i, tex));
        } catch (Exception e) { // should never happen, but just in case
            e.printStackTrace();
        }
    }

    @Contract(value = "-> new", pure = true)
    public static Identifier randomIdentifier() {
        return Identifier.of("customcosmetic", "temp/" + randomString(32));
    }

    private static String randomString(int length) {
        Random RND = ThreadLocalRandom.current();
        return IntStream.range(0, length)
                .mapToObj(operand -> String.valueOf((char) RND.nextInt('a', 'z' + 1)))
                .collect(Collectors.joining());
    }
}
