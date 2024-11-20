package me.corruptionhades.customcosmetics.utils;

import me.corruptionhades.customcosmetics.cosmetic.custom.CustomResourceLocation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class TextureUtil {

    public static Identifier saveBufferedImageAsIdentifier(File file) throws IOException {
        Identifier identifier = Identifier.of("customcosmetics", "texture_" + System.currentTimeMillis());
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        String[] split = file.getName().split("\\.");

        ImageIO.write(bufferedImage, split[split.length - 1], stream);
        byte[] bytes = stream.toByteArray();

        ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
        data.flip();
        NativeImage img = NativeImage.read(data);
        NativeImageBackedTexture texture = new NativeImageBackedTexture(img);

        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, texture));
        return identifier;
    }

    public static @Nullable String readContents(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    builder.append(line);
                }
            }

            return new String(Base64.getEncoder().encode(builder.toString().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static @Nullable CustomResourceLocation loadTexture(File texturePath) {

        CustomResourceLocation crl = null;

        try {
            List<Identifier> textures = new ArrayList<>();

            File[] listFiles = texturePath.listFiles();

            if (listFiles == null) {

                Identifier ident = saveBufferedImageAsIdentifier(texturePath);

                crl = new CustomResourceLocation(false, ident);
                crls.add(crl);
                return crl;
            }

            for (File file : listFiles) {
                textures.add(TextureUtil.saveBufferedImageAsIdentifier(file));
                System.out.println("Loaded texture: " + file.getName());
            }

            crl = new CustomResourceLocation(textures.size() > 1, textures.toArray(new Identifier[0]));

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(crl != null) {
            crls.add(crl);
        }

        return crl;
    }

    public static List<CustomResourceLocation> crls = new ArrayList<>();
}
