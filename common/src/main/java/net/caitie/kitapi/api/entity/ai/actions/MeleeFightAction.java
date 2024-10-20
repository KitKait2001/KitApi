package net.caitie.kitapi.api.entity.ai.actions;

import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.Set;

public class MeleeFightAction extends Action implements CombatAction {
    protected final double speed;
    protected LivingEntity target;
    protected int cooldown = 0;
    protected int cooldownTick;

    public <T extends Mob & IKitNPC> MeleeFightAction(T npc, ServerLevel level, Set<String> disabledIds, int priority, double speed) {
        super(npc, level, ActionTypes.combatAggressive(), disabledIds, priority);
        this.speed = speed;
    }

    @Override
    public boolean canStart() {
        LivingEntity target = mob.getTarget();
        if (target != null && target.isAlive()) {
            this.target = target;
            return mob.canAttack(target);
        }
        return false;
    }

    @Override
    public boolean canContinue() {
        return target instanceof LivingEntity && target.isAlive() && mob.canAttack(target);
    }

    @Override
    public void start() {
        super.start();
        if (mob instanceof InventoryCarrier carrier) {
            mob.setItemInHand(InteractionHand.MAIN_HAND, ActionUtils.findItemOfType(carrier, npc::isMeleeWeapon, mob.getMainHandItem()));
            mob.setItemInHand(InteractionHand.OFF_HAND, ActionUtils.findItemOfType(carrier, npc::isShield, mob.getOffhandItem()));
            if (npc.isMeleeWeapon(mob.getMainHandItem()) && mob.getMainHandItem().get(DataComponents.ATTRIBUTE_MODIFIERS) instanceof ItemAttributeModifiers modifiers) {
                modifiers.modifiers().stream().filter(m -> m.attribute().is(Attributes.ATTACK_SPEED)).findFirst().ifPresent(((e) -> cooldown = (int) e.modifier().amount()));
            }
        }
        if (cooldown == 0) {
            cooldown = 20;
        }
        mob.setSprinting(true);
    }

    @Override
    public void tick() {
        if (cooldownTick <= 0) {
            if (ActionUtils.meleeAttack(mob, target)) {
                mob.swing(InteractionHand.MAIN_HAND);
                cooldownTick = cooldown;
            }
        }
        else --cooldownTick;
        ActionUtils.lookAndMoveTo(mob, target, speed);
    }

    @Override
    public void stop() {
        super.stop();
        mob.setSprinting(false);
    }

    @Override
    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public boolean isAggressive() {
        return true;
    }
}
