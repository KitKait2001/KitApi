package net.caitie.kitapi.api.entity.ai.actions;

import net.caitie.kitapi.api.entity.IHungry;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.food.FoodProperties;

import java.util.Set;

public class EatFoodAction extends UseItemAction {
    protected FoodProperties foodProperties;

    public <T extends Mob & IKitNPC> EatFoodAction(T npc, ServerLevel level, Set<String> disabledIds, int priority) {
        super(npc, level, disabledIds, priority, InteractionHand.MAIN_HAND);
        this.addIds(Set.of(ActionTypes.EATING.id()));
    }

    @Override
    public boolean canStart() {
        if (!mob.isUsingItem()) {
            if ((mob instanceof IHungry hungry && hungry.isHungry())) {
                if (mob instanceof InventoryCarrier carrier) {
                    this.item = ActionUtils.findItemOfType(carrier, npc::canEat);
                } else {
                    this.item = mob.getMainHandItem();
                }
                if (item.isEmpty() || !npc.canEat(item)) {
                    item = null;
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void completeUsingItem() {
        if (this.foodProperties != null && !mob.isDeadOrDying()) {
            mob.eat(level, item, foodProperties);
        }
        else super.completeUsingItem();
    }

    @Override
    public void stopUsingItem() {
        super.stopUsingItem();
        this.foodProperties = null;
    }
}
