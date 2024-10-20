package net.caitie.kitapi.api.entity.ai.actions;

import com.google.common.collect.Sets;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

import java.util.Set;

public class FloatInWaterAction extends Action {

    public <T extends Mob & IKitNPC> FloatInWaterAction(T npc, ServerLevel level, Set<String> disabledIds, int priority) {
        super(npc, level, Sets.newHashSet(ActionTypes.FLOAT.id()), disabledIds, priority);
    }

    @Override
    public boolean canStart() {
        return mob.isInWater();
    }

    @Override
    public boolean canContinue() {
        return mob.isInWater();
    }

    @Override
    public void tick() {
        if (mob.isInWater() && mob.getRandom().nextFloat() < 0.85F) {
            mob.getJumpControl().jump();
        }
    }
}
