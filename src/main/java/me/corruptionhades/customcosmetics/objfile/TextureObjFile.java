package me.corruptionhades.customcosmetics.objfile;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import de.javagl.obj.*;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomResourceLocation;
import me.corruptionhades.customcosmetics.utils.CustomSounds;
import me.corruptionhades.customcosmetics.utils.RenderRefs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUsage;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

/**
 * A wavefront obj file parser and renderer with explicit texture management.
 */
public class TextureObjFile implements Closeable {
    private final ResourceProvider provider;
    private final String name;
    private final Map<Obj, VertexBuffer> buffers = new HashMap<>();
    private boolean baked = false;
    private boolean closed = false;

    /**
     * Creates a new ObjFile
     *
     * @param name     Filename of the target .obj, resolved by the {@link ResourceProvider} {@code provider}
     * @param provider The resource provider to use
     * @throws IOException When reading the .obj fails
     */
    public TextureObjFile(String name, ResourceProvider provider) throws IOException {
        this.name = name;
        this.provider = provider;
      //  bake();
    }

    private static Vec3d transformVec3d(Vec3d in) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        return in.subtract(camPos);
    }

    private void bake() throws IOException {

        try (InputStream reader = provider.open(name)) {
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(reader));

            BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES,
                    VertexFormats.POSITION_TEXTURE_COLOR);

            for (int i = 0; i < obj.getNumFaces(); i++) {
                ObjFace face = obj.getFace(i);
                boolean hasUV = face.containsTexCoordIndices();
                boolean hasNormals = face.containsNormalIndices();

                for (int j = 0; j < face.getNumVertices(); j++) {
                    FloatTuple vertex = obj.getVertex(face.getVertexIndex(j));
                    VertexConsumer vertexConsumer = builder.vertex(vertex.getX(), vertex.getY(), vertex.getZ());

                    if (hasUV) {
                        FloatTuple texCoord = obj.getTexCoord(face.getTexCoordIndex(j));
                        vertexConsumer.texture(texCoord.getX(), 1 - texCoord.getY());
                    }
                    else {
                        vertexConsumer.texture(0, 0);
                    }

                    vertexConsumer.color(1f, 1f, 1f, 1f);

               /*     if (hasNormals) {
                        FloatTuple normal = obj.getNormal(face.getNormalIndex(j));
                        vertexConsumer.normal(normal.getX(), normal.getY(), normal.getZ());
                    }
                    else {
                        vertexConsumer.normal(0, 0, 0);
                    } */

                    //vertexConsumer.color(1f, 1f, 1f, 1f); // Default white color
             //       vertexConsumer.next();
                }
            }

            BuiltBuffer builtBuffer = builder.end();
            VertexBuffer vbo = Helper.createVbo(builtBuffer, GlUsage.DYNAMIC_WRITE);
            buffers.put(obj, vbo);
        }

        baked = true;
    }

    public void draw(MatrixStack stack, Matrix4f viewMatrix, @Nullable CustomResourceLocation crl) {
        draw(stack, viewMatrix, crl == null ? null : crl.getTexture());
    }

    /**
     * Draws this ObjFile. Calls {@link #bake()} if necessary.
     *
     * @param stack      MatrixStack
     * @param viewMatrix View matrix to apply to this ObjFile, independent of any other matrix.
     * @param textureIdentifier Path to the texture image to use.
     */

    public void draw(MatrixStack stack, Matrix4f viewMatrix, @Nullable Identifier textureIdentifier) {
        if (closed) {
            throw new IllegalStateException("Closed");
        }
        if (!baked) {
            try {
                bake();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
        Matrix4f modelMatrix = new Matrix4f(stack.peek().getPositionMatrix());

        modelMatrix.mul(viewMatrix);

        Helper.setupRender();

        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        if (textureIdentifier != null) {
            RenderSystem.setShaderTexture(0, textureIdentifier);
        }

        for (Map.Entry<Obj, VertexBuffer> entry : buffers.entrySet()) {
            VertexBuffer vbo = entry.getValue();
            vbo.bind();
            vbo.draw(modelMatrix, projectionMatrix, RenderSystem.getShader());
        }

        VertexBuffer.unbind();
        Helper.endRender();
    }

    private Matrix4f getCameraRotationMatrix() {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Matrix4f rotationMatrix = new Matrix4f();

        // Fetch camera rotation as a quaternion
        float pitch = camera.getPitch();
        float yaw = camera.getYaw();

        // Apply rotations (order matters: yaw first, then pitch)
      //  rotationMatrix.rotateY((float) Math.toRadians(-yaw)); // Negate to align with view
     //   rotationMatrix.rotateX((float) Math.toRadians(-pitch));

        return rotationMatrix;
    }

    private Identifier loadTexture(Path texturePath) {
        try (InputStream stream = Files.newInputStream(texturePath)) {
            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
                throw new IllegalArgumentException("Invalid image file: " + texturePath);
            }

            Identifier textureId = Helper.randomIdentifier();
            Helper.registerBufferedImageTexture(textureId, image);
            return textureId;
        } catch (IOException e) {
            e.printStackTrace();
            return Identifier.of("minecraft", "textures/missingno.png");
        }
    }

    @Override
    public void close() {
        for (VertexBuffer buffer : buffers.values()) {
            buffer.close();
        }
        buffers.clear();
        closed = true;
    }

    /**
     * A function, which maps a resource name found in an .obj file to an InputStream
     */
    @FunctionalInterface
    public interface ResourceProvider {
        /**
         * Appends the filename to read to a Path, then tries to load the resulting file.
         *
         * @param parent Parent path of all files
         * @return New ResourceProvider
         */
        static TextureObjFile.ResourceProvider ofPath(Path parent) {
            return name -> {
                Path resolve = parent.resolve(name);
                return Files.newInputStream(resolve);
            };
        }

        /**
         * Opens {@code name} as InputStream
         *
         * @param name Filename to open
         * @return The opened InputStream. Closed by the library when done.
         */
        InputStream open(String name) throws IOException;
    }
}
