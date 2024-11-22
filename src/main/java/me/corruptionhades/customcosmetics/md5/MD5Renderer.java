package me.corruptionhades.customcosmetics.md5;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlUsage;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class MD5Renderer {

    private final MD5File md5File;
    private final Map<Mesh, VertexBuffer> buffers = new HashMap<>();
    private boolean baked = false;
    private boolean closed = false;

    public MD5Renderer(MD5File md5File) {
        this.md5File = md5File;
    }

    private void bake() {
        for (Mesh mesh : md5File.getMeshes()) {
            BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_TEXTURE_COLOR);

            for (Tri tri : mesh.getTris()) {
                for (int vertIndex : tri.getVertIndices()) {
                    Vert vert = mesh.getVerts().get(vertIndex);
                    float[] texCoord = vert.getTexCoord();
                    float[] finalPosition = new float[]{0, 0, 0};

                    for (int i = 0; i < vert.getCountWeight(); i++) {
                        Weight weight = mesh.getWeights().get(vert.getStartWeight() + i);
                        Joint joint = md5File.getJoints().get(weight.getJoint());

                        Vector3f jointPos = new Vector3f(joint.getPosition());
                        Quaternionf jointOrient = new Quaternionf(joint.getOrientation()[0], joint.getOrientation()[1], joint.getOrientation()[2], calculateW(joint.getOrientation()));

                        Vector3f weightPos = new Vector3f(weight.getPosition());
                        Quaternionf weightQuat = new Quaternionf(weightPos.x, weightPos.y, weightPos.z, 0);

                        Quaternionf rotatedPos = jointOrient.mul(weightQuat).mul(jointOrient.conjugate());
                        Vector3f rotatedVec = new Vector3f(rotatedPos.x, rotatedPos.y, rotatedPos.z);

                        rotatedVec.add(jointPos).mul(weight.getBias());
                        finalPosition[0] += rotatedVec.x;
                        finalPosition[1] += rotatedVec.y;
                        finalPosition[2] += rotatedVec.z;
                    }

                    builder.vertex(finalPosition[0], finalPosition[1], finalPosition[2])
                            .texture(texCoord[0], texCoord[1])
                            .color(1f, 1f, 1f, 1f);
                }
            }

            BuiltBuffer builtBuffer = builder.end();
            VertexBuffer vbo = new VertexBuffer(GlUsage.DYNAMIC_WRITE);
            vbo.upload(builtBuffer);
            buffers.put(mesh, vbo);
        }

        baked = true;
    }

    private float calculateW(float[] orientation) {
        float t = 1.0f - (orientation[0] * orientation[0]) - (orientation[1] * orientation[1]) - (orientation[2] * orientation[2]);
        return t < 0.0f ? 0.0f : (float) -Math.sqrt(t);
    }

    public void render(MatrixStack stack, Matrix4f viewMatrix, @Nullable Identifier textureIdentifier) {
        if (closed) {
            throw new IllegalStateException("Closed");
        }
        if (!baked) {
            bake();
        }

        Matrix4fStack modelMatrix = RenderSystem.getModelViewStack();
        modelMatrix.pushMatrix();
        modelMatrix.mul(stack.peek().getPositionMatrix());
        modelMatrix.mul(viewMatrix);

        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        if (textureIdentifier != null) {
            RenderSystem.setShaderTexture(0, textureIdentifier);
        }

        for (VertexBuffer vbo : buffers.values()) {
            vbo.bind();
            vbo.draw(modelMatrix, RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        }

        modelMatrix.popMatrix();
        VertexBuffer.unbind();
    }

    public void close() {
        for (VertexBuffer buffer : buffers.values()) {
            buffer.close();
        }
        buffers.clear();
        closed = true;
    }
}