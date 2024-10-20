package net.caitie.kitapi.api.entity.ai.actions;

import com.google.common.collect.Sets;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;

import java.util.List;
import java.util.Set;

public class PickUpItemAction extends Action {
    protected final double searchRange;
    protected final double pickUpRange;
    protected final double speed;
    protected ItemEntity item;

    public <T extends Mob & IKitNPC> PickUpItemAction(T npc, ServerLevel level, Set<String> disabledIds, int priority, double searchRange, double pickUpRange, double speed) {
        super(npc, level, Sets.newHashSet(ActionTypes.PICK_UP_ITEM.id()), disabledIds, priority);
        this.searchRange = searchRange;
        this.pickUpRange = pickUpRange;
        this.speed = speed;
    }

    @Override
    public boolean canStart() {
        if (!(mob instanceof InventoryCarrier)) return false;
        this.item = findItem();
        if (item == null) return false;
        return ((InventoryCarrier)mob).getInventory().canAddItem(item.getItem());
    }

    @Override
    public boolean canContinue() {
        return item != null && item.isAlive() && !mob.getNavigation().isStuck();
    }

    @Override
    public void start() {
        mob.getNavigation().stop();
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        ActionUtils.lookAndMoveTo(mob, item, speed);
        if (item != null && !item.hasPickUpDelay() && mob.distanceTo(item) <= pickUpRange) {
            InventoryCarrier.pickUpItem(mob, (InventoryCarrier) mob, item);
        }
    }

    @Override
    public void stop() {
        this.item = null;
        super.stop();
    }

    protected ItemEntity findItem() {
        List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(searchRange), (itm) -> mob.wantsToPickUp(itm.getItem()));
        if (list.isEmpty()) return null;
        return list.get(mob.getRandom().nextInt(list.size()));
    }
}
