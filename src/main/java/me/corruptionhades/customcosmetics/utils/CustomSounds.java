package me.corruptionhades.customcosmetics.utils;

import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CustomSounds {
    public static final Identifier PING = Identifier.of("customcosmetics:ping");
    public static SoundEvent PING_EVENT = SoundEvent.of(PING);

    public static final ShaderProgramKey SPK_POSITION_TEX_COLOR_NORMAL =
            new ShaderProgramKey(Identifier.of("customcosmetics", "core/position_tex_color_normal"),
                    VertexFormats.POSITION_TEXTURE_COLOR_NORMAL,
                    Defines.EMPTY);
}
