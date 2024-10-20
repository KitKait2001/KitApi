package net.caitie.kitapi.api.entity.ai.actions;

import com.google.common.collect.Sets;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

import java.util.Set;

public class DefensiveTargetAction extends Action {

    public <T extends Mob & IKitNPC> DefensiveTargetAction(T npc, ServerLevel level, Set<String> disabledIds, int priority) {
        super(npc, level, Sets.newHashSet(ActionTypes.TARGETING_COMBAT.id()), disabledIds, priority);
    }

    @Override
    public boolean canStart() {
        return mob.getLastAttacker() != null && mob.getLastAttacker().isAlive() && mob.canAttack(mob.getLastAttacker());
    }

    @Override
    public boolean canContinue() {
        return mob.getTarget() != null && mob.getTarget().isAlive() && mob.canAttack(mob.getTarget());
    }

    @Override
    public void start() {
        super.start();
        mob.setTarget(mob.getLastAttacker());
    }

    @Override
    public void stop() {
        super.stop();
        mob.setTarget(null);
    }
}
