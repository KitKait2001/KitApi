package net.caitie.kitapi.api.entity.ai.actions;

import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ShieldItem;

import java.util.Set;

public class UseShieldAction extends UseItemAction implements CombatAction {
    protected LivingEntity target;

    public <T extends Mob & IKitNPC> UseShieldAction(T npc, ServerLevel level, Set<String> disabledIds, int priority) {
        super(npc, level, disabledIds, priority, InteractionHand.OFF_HAND);
        this.addIds(Set.of(ActionTypes.LOOK_AT_ENTITY.id(), ActionTypes.DEFENSIVE_COMBAT.id()));
        this.disabledIds.addAll(Set.of(ActionTypes.FLEE_FROM_ENTITY.id(), ActionTypes.EATING.id()));
    }

    @Override
    public boolean canStart() {
        if (!mob.isUsingItem()) {
            this.item = mob.getOffhandItem();
            if (!(this.item.getItem() instanceof ShieldItem)) {
                if (mob instanceof InventoryCarrier carrier) {
                    this.item = ActionUtils.findItemOfType(carrier, (itm) -> itm.getItem() instanceof ShieldItem);
                    if (this.item.isEmpty()) {
                        this.item = null;
                        return false;
                    }
                }
                else {
                    this.item = null;
                    return false;
                }
            }
            if (mob.getLastAttacker() instanceof LivingEntity entity) {
                this.target = entity;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinue() {
        return item != null && !item.isEmpty() && this.target != null;
    }

    @Override
    public void tick() {
        this.target = mob.getLastAttacker();
        if (this.target != null) {
            ActionUtils.strafeAwayFrom(mob, target, 0.7F);
            if (this.target.getLastHurtMob() == null || !this.target.getLastHurtMob().is(this.mob)) {
                this.target = null;
            }
        }
    }

    @Override
    public void stopUsingItem() {
        mob.stopUsingItem();
        super.stopUsingItem();
        this.target = null;
    }

    @Override
    public void completeUsingItem() {
    }

    @Override
    public boolean canUseIfChild() {
        return false;
    }

    @Override
    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public boolean isAggressive() {
        return false;
    }
}
