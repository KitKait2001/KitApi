package net.caitie.kitapi.api.entity.ai.actions;

import net.minecraft.world.entity.LivingEntity;

public interface CombatAction {

    LivingEntity getTarget();

    boolean isAggressive();
}
