package us.pandamc.npc.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NPCUtils {
    public int getCompressedAngle(float value) {
        return (int)(value * 256 / 360);
    }
}