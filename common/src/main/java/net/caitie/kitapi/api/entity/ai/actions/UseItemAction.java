package net.caitie.kitapi.api.entity.ai.actions;

import com.google.common.collect.Sets;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public abstract class UseItemAction extends Action {
    protected final InteractionHand hand;
    protected ItemStack item;

    public <T extends Mob & IKitNPC> UseItemAction(T npc, ServerLevel level, Set<String> disabledIds, int priority, InteractionHand hand) {
        super(npc, level, Sets.newHashSet(ActionTypes.USE_ITEM.id()), disabledIds, priority);
        this.hand = hand;
    }

    public int useTicks() {
        return this.item == null ? 0 : this.item.getUseDuration(mob);
    }

    @Override
    public boolean canContinue() {
        return !mob.isDeadOrDying() && !item.isEmpty() && this.item.getUseDuration(mob) - mob.getTicksUsingItem() < useTicks();
    }

    @Override
    public void start() {
        super.start();
        npc.useItem(item, hand);
    }

    @Override
    public void stop() {
        if (this.item != null) {
            if (this.item.getUseDuration(mob) - mob.getTicksUsingItem() < this.useTicks()) {
                completeUsingItem();
            }
            stopUsingItem();
        }
        super.stop();
    }

    public void completeUsingItem() {
        this.item.shrink(1);
    }

    public void stopUsingItem() {
        this.item = null;
    }
}
