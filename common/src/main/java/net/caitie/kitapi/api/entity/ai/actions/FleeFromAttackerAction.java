package net.caitie.kitapi.api.entity.ai.actions;

import com.google.common.collect.Sets;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class FleeFromAttackerAction extends Action implements CombatAction {
    protected final double speed;
    protected final int forgetTime;
    protected LivingEntity attacker;
    protected int forgetTimer;

    public <T extends Mob & IKitNPC> FleeFromAttackerAction(T npc, ServerLevel level, Set<String> disabledIds, int priority, double speed, int forgetTime) {
        super(npc, level, Sets.newHashSet(ActionTypes.LOOK_AT_ENTITY.id(), ActionTypes.FLEE_FROM_ENTITY.id(), ActionTypes.NON_AGGRESSIVE_COMBAT.id()), disabledIds, priority);
        this.speed = speed;
        this.forgetTime = forgetTime;
        this.disabledIds.addAll(ActionTypes.randomLookAndWalk());
    }

    @Override
    public boolean canStart() {
        if (!(mob instanceof PathfinderMob)) return false;
        this.attacker = mob.getLastAttacker();
        return attacker != null && attacker.isAlive();
    }

    @Override
    public void start() {
        super.start();
        this.forgetTimer = forgetTime;
    }

    @Override
    public boolean canContinue() {
        return attacker != null && attacker.isAlive() && forgetTimer > 0;
    }

    @Override
    public void tick() {
        super.tick();
        ActionUtils.lookAt(mob, attacker);
        Vec3 vec3 = DefaultRandomPos.getPosAway((PathfinderMob) mob, 16, 7, attacker.position());
        if (vec3 != null) {
            ActionUtils.moveTo(mob, vec3, speed);
        }
        --forgetTimer;
    }

    @Override
    public LivingEntity getTarget() {
        return attacker;
    }

    @Override
    public boolean isAggressive() {
        return false;
    }
}
