package net.caitie.kitapi.content.entity.inventory;

import net.caitie.kitapi.content.entity.IKitNPC;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AgeableMob;

public class NPCInventory<T extends AgeableMob & IKitNPC> extends SimpleContainer {
    protected final T owner;

    public NPCInventory(T npc, int size) {
        super(size);
        this.owner = npc;
    }

    public void tick() {}

    public T getOwner() {
        return owner;
    }

}
