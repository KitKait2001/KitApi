package net.caitie.kitapi.content.entity.ai;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.caitie.kitapi.content.entity.IKitNPC;
import net.caitie.kitapi.content.entity.ai.actions.Action;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class NPCBrain<T extends AgeableMob & IKitNPC> {
    private final T npc;
    private final ServerLevel level;
    protected final Map<Integer, ObjectArrayList<Action>> actionMap = new Object2ObjectOpenHashMap<>();
    protected final ObjectArrayList<Action> coreActions = new ObjectArrayList<>();
    protected ObjectArrayList<Action> runningActions = new ObjectArrayList<>();
    int currentState;
    int lastState;
    boolean initialized;
    boolean frozen;
    @Nullable NPCBrain<?> mindController;
    boolean child;
    boolean nightTime;
    boolean sleeping;
    int tickDelay;
    int refreshCheck;

    public NPCBrain(T npc, ServerLevel level) {
        this.npc = npc;
        this.level = level;
    }

    public void init() {
        updateVars();
        this.refreshCheck = refreshTime();
        initGoals();
        initialized = true;
    }

    public void updateVars() {
        this.tickDelay = tickDelay();
        this.child = npc.isBaby();
        this.nightTime = level.isNight();
        this.sleeping = npc.isSleeping();
    }

    public abstract void initGoals();

    public void tick() {
        if (--tickDelay <= 0) {
            updateVars();
            tickGoals();
        }
    }

    protected void tickGoals() {
        if (this.hasBrainFreeze()) {
            return;
        }

        if (lastState != currentState) {
            ObjectArrayList<Action> actions = this.actionMap.get(lastState);
            for (Action action : actions) {
                if (action.isRunning()) {
                    action.stop();
                }
            }
        }

        int priority = -1;
        Set<String> disabled = new HashSet<>();

        actionUpdate(coreActions, priority, disabled);

        if (isMindControlled()) return;

        if (runningActions.isEmpty()) {
            runningActions = actionMap.get(currentState);
            runningActions.sort(Comparator.comparingInt(Action::priority));
        }

        actionUpdate(runningActions, priority, disabled);
    }

    protected void actionUpdate(ObjectArrayList<Action> actions, int priority, Set<String> disabled) {
        if (actions != null && !actions.isEmpty()) {
            for (Action action : actions) {
                if (!action.isRunning() && action.canStart() && !isActionDisabled(action, priority, disabled)) {
                    boolean check = priority < action.priority();
                    action.start();
                    if (action.stopLowerPriorities() && check) {
                        priority = action.priority();
                    }
                    if (action.disabledIds() instanceof Set<String> set && !set.isEmpty()) {
                        disabled.addAll(set);
                    }
                    continue;
                }

                if (action.isRunning()) {
                    if (!isActionDisabled(action, priority, disabled) && action.canContinue()) {
                        action.tick();
                    }
                    else action.stop();
                }
            }
        }
    }

    protected boolean isActionDisabled(Action action, int priority, Set<String> disabled) {
        if (child && !action.canUseIfChild()) return true;
        if (action.ids.stream().anyMatch(disabled::contains)) {
            return true;
        }
        if (priority < 0) return false;
        return action.priority() <= priority;
    }

    public boolean hasBrainFreeze() {
        return frozen;
    }

    public boolean isMindControlled() {
        return mindController != null;
    }

    public boolean initialized() {
        return initialized;
    }

    public int tickDelay() {
        return 20;
    }

    public int refreshTime() {
        return 200;
    }

    public boolean addCoreAction(Action action) {
        return coreActions.add(action);
    }

    public boolean addToBrain(Integer key, Action value) {
        return actionMap.get(key).add(value);
    }

    public void removeFromBrain(Action action) {
        actionMap.forEach((key, actions) -> {
            if (actions.contains(action)) {
                actions.remove(action);
                if (action.isRunning()) action.stop();
            }
        });
    }

}
