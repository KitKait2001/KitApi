package net.caitie.kitapi.content.entity.ai.actions;

import net.caitie.kitapi.content.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class Action {
    final IKitNPC npc;
    final AgeableMob mob;
    final ServerLevel level;
    public final Set<String> ids;
    final int priority;

    boolean isRunning = false;

    public <T extends AgeableMob & IKitNPC> Action(T npc, ServerLevel level, Set<String> ids, int priority) {
        this.npc = npc;
        this.mob = npc;
        this.level = level;
        this.ids = ids;
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

    @Nullable
    public Set<String> disabledIds() {
        return null;
    }

}
