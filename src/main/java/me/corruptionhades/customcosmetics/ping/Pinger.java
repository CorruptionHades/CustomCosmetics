package me.corruptionhades.customcosmetics.ping;

import me.corruptionhades.customcosmetics.utils.CustomSounds;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class Pinger {

    static final MinecraftClient mc = MinecraftClient.getInstance();
    static final List<PingResult> pings = new ArrayList<>();

    static long lastTime = System.currentTimeMillis();

    public static void ping() {
        if(mc.player == null || mc.world == null) {
            return;
        }

        if(System.currentTimeMillis() - lastTime <= 500) {
            return;
        }

        BlockHitResult bhr = getTargetBlock(100);
        Entity target = getTarget(100);

        if(bhr != null && target == null) {

            try {
                List<PingResult> toRemove = new ArrayList<>();
                for(PingResult ping : pings) {
                    if(ping.pos.distanceTo(bhr.getPos()) <= 5) {
                        toRemove.add(ping);
                    }
                }

                for(PingResult pos : toRemove) {
                    pings.remove(pos);
                }
            }
            catch (ConcurrentModificationException ignored) {}

            if(pings.size() >= 5) {
                pings.removeFirst();
            }

            BlockPos pos = new BlockPos(((int) Math.round(bhr.getPos().x)),
                    (int) Math.round(bhr.getPos().y),
                    (int) Math.round(bhr.getPos().z));

            Block block = mc.world.getBlockState(pos).getBlock();

            if(block instanceof AirBlock) {
                block = mc.world.getBlockState(pos.add(0, -1, 0)).getBlock();
            }

            Identifier ident = getIdentifier(block);

            pings.add(new PingResult(bhr.getPos(), ident, System.currentTimeMillis(), null));
        }
        if (target != null) {
            try {
                List<PingResult> toRemove = new ArrayList<>();
                for(PingResult ping : pings) {
                    if(ping.tagged != null && ping.tagged == target) {
                        toRemove.add(ping);
                    }
                }

                for(PingResult pos : toRemove) {
                    pings.remove(pos);
                }
            }
            catch (ConcurrentModificationException ignored) {}

            if(pings.size() >= 5) {
                pings.removeFirst();
            }

            pings.add(new PingResult(target.getPos(), null, System.currentTimeMillis(), target));

            lastTime = System.currentTimeMillis();

            mc.player.playSound(CustomSounds.PING_EVENT, 1, 1);
        }

        if(bhr != null || target != null) {
            lastTime = System.currentTimeMillis();
            mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }

    public static Entity getTarget(int max) {
        Entity entity2 = mc.getCameraEntity();
        Predicate<Entity> predicate = entity -> true; // Do entity checking here(ex: preventing specific entities from triggering)
        Vec3d eyePos = entity2.getEyePos();
        Vec3d vec3d2 = entity2.getRotationVec(1).multiply(max);
        Vec3d vec3d3 = eyePos.add(vec3d2);
        Box box = entity2.getBoundingBox().stretch(vec3d2).expand(1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity2, eyePos, vec3d3, box, predicate, max * max);

        if(entityHitResult == null) return null;

        return entityHitResult.getEntity();
    }

    public static BlockHitResult getTargetBlock(int max) {
        HitResult hitResult = mc.cameraEntity.raycast(max, 1, false);
        if(hitResult instanceof BlockHitResult hit) {
            return hit;
        }

        return null;
    }

    public static void render(DrawContext context) {
        if(mc.player == null) {
            return;
        }

        try {
            for(PingResult ping : pings) {

                long time = ping.creationTime;
                if(System.currentTimeMillis() - time >= 15000) {
                    pings.remove(ping);
                    continue;
                }

                // update position
                if(ping.tagged != null) {

                    if(!ping.tagged.isAlive()) {
                        pings.remove(ping);
                        continue;
                    }

                    if(mc.player.canSee(ping.tagged)) {
                        ping.pos = ping.tagged.getPos();
                    }
                }

                double x = ping.pos.getX();
                double y = ping.pos.getY();
                double z = ping.pos.getZ();

                float size = 10;

                Vec3d position = worldSpaceToScreenSpace(new Vec3d(x, y, z));

                if(!isOnScreen(position)) {
                    continue;
                }

                if(ping.tagged != null) {
                    context.fill((int) (position.x - size / 2),
                            (int) (position.y - size / 2),
                            (int) (position.x + size / 2),
                            (int) (position.y + size / 2),
                            new Color(255, 0, 0, 100).getRGB());
                }
                else if(ping.particle != null) {
                    context.drawTexture(RenderLayer::getGuiTextured, ping.particle, (int) (position.x - size / 2),
                            (int) (position.y - size / 2), 0, 0, (int) size, (int) size, (int) size, (int) size, 0xFFFFF);
                }
                else {
                    // light blue
                    int color = new Color(0, 255, 255, 100).getRGB();
                    context.fill((int) (position.x - size / 2),
                            (int) (position.y - size / 2),
                            (int) (position.x + size / 2),
                            (int) (position.y + size / 2),
                            color);
                }
            }
        }
        catch (ConcurrentModificationException ignored) {}
    }

    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();
    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();

    public static Vec3d worldSpaceToScreenSpace(Vec3d pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.getEntityRenderDispatcher().camera;
        int displayHeight = mc.getWindow().getHeight();
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        Vector3f target = new Vector3f();

        double deltaX = pos.x - camera.getPos().x;
        double deltaY = pos.y - camera.getPos().y;
        double deltaZ = pos.z - camera.getPos().z;

        Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.f).mul(lastWorldSpaceMatrix);

        Matrix4f matrixProj = new Matrix4f(lastProjMat);
        Matrix4f matrixModel = new Matrix4f(lastModMat);

        matrixProj.mul(matrixModel)
                .project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport,
                        target);

        return new Vec3d(target.x / mc.getWindow().getScaleFactor(),
                (displayHeight - target.y) / mc.getWindow().getScaleFactor(), target.z);
    }

    public static Identifier getIdentifier(Block block) {
        String retu = "";
        if(block == Blocks.DIAMOND_ORE) {
            retu = "textures/item/diamond.png";
        }
        else if(block == Blocks.IRON_ORE) {
            retu = "textures/item/iron_ingot.png";
        }

        if(retu.isEmpty()) {
            return null;
        }

        return Identifier.of("minecraft", retu);
    }

    public static boolean isOnScreen(Vec3d pos) {
        return pos != null && pos.z > -1 && pos.z < 1;
    }

    static class PingResult {
        public Vec3d pos;
        public Identifier particle;
        public long creationTime;
        @Nullable
        public Entity tagged;

        public PingResult(Vec3d pos, Identifier particle, long creationTime, @Nullable Entity tagged) {
            this.pos = pos;
            this.particle = particle;
            this.creationTime = creationTime;
            this.tagged = tagged;
        }
    }
}
