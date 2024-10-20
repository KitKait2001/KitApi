package net.caitie.kitapi.content.entity.ai.actions;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class ActionUtils {

    public static void moveTo(PathfinderMob mob, Vec3 pos, double speed) {
        if (pos != null) {
            mob.getNavigation().moveTo(pos.x, pos.y, pos.z, speed);
        }
    }

}
