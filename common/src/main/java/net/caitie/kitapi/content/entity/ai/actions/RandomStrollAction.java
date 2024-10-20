package net.caitie.kitapi.content.entity.ai.actions;

import net.caitie.kitapi.content.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class RandomStrollAction extends Action {
    final double speed;
    final double homeDist;
    Vec3 wantedPos;

    public <T extends AgeableMob & IKitNPC> RandomStrollAction(T npc, ServerLevel level, Set<String> ids, int priority, double speed, double homeDist) {
        super(npc, level, ids, priority);
        this.speed = speed;
        this.homeDist = homeDist;
    }

    @Override
    public boolean canStart() {
        if (mob.isPassenger()) return false;
        if (mob.isAggressive()) return false;

        if (mob.getRandom().nextFloat() <= 0.01F) {
            Vec3 vec3 = this.getPosition();
            if (vec3 == null) return false;
            this.wantedPos = vec3;
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinue() {
        return !mob.getNavigation().isDone() && !mob.isPassenger() && !mob.isAggressive();
    }

    @Override
    public void start() {
        super.start();
        ActionUtils.moveTo(mob, this.wantedPos, this.speed);
    }

    public Vec3 getPosition() {
        if (mob.isInWaterOrBubble()) return LandRandomPos.getPos(mob, 15, 7);
        if (npc.hasValidHomePosition() && npc.homePosition().distSqr(mob.blockPosition()) >= homeDist) {
            return DefaultRandomPos.getPosTowards(mob, 10, 7, Vec3.atBottomCenterOf(npc.homePosition()), (float)Math.PI / 2F);
        }
        return DefaultRandomPos.getPos(mob, 10, 7);
    }
}
