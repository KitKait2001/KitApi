package net.caitie.kitapi.api.entity.ai.actions;

import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

import java.util.Set;

public abstract class Action {
    protected final IKitNPC npc;
    protected final Mob mob;
    protected final ServerLevel level;
    public final Set<String> ids;
    protected final Set<String> disabledIds;
    protected final int priority;

    boolean isRunning = false;

    public <T extends Mob & IKitNPC> Action(T npc, ServerLevel level, Set<String> ids, Set<String> disabledIds, int priority) {
        this.npc = npc;
        this.mob = npc;
        this.level = level;
        this.ids = ids;
        this.disabledIds = disabledIds;
        this.priority = priority;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int priority() {
        return priority;
    }

    public abstract boolean canStart();

    public abstract boolean canContinue();

    public void start() {
        this.isRunning = true;
    }

    public void tick() {
    }

    public void stop() {
        this.isRunning = false;
    }

    public boolean stopLowerPriorities() {
        return false;
    }

    public boolean canUseIfChild() {
        return true;
    }

    public boolean canUseIfAdult() {return true;}

    public Set<String> disabledIds() {
        return disabledIds;
    }

    public void addIds(Set<String> ids) {
        this.ids.addAll(ids);
    }

}
