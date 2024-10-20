package net.caitie.kitapi.api.entity.ai;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.caitie.kitapi.api.entity.ai.actions.Action;
import net.caitie.kitapi.api.entity.ai.actions.CombatAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class NPCBrain<T extends Mob & IKitNPC> {
    protected final T npc;
    protected final ServerLevel level;
    protected final Map<Integer, ObjectArrayList<Action>> actionMap = new Object2ObjectOpenHashMap<>();
    protected final ObjectArrayList<Action> coreActions = new ObjectArrayList<>();
    protected ObjectArrayList<Action> runningActions = new ObjectArrayList<>();
    protected int currentState;
    protected int lastState;
    protected boolean initialized;
    protected boolean frozen;
    protected NPCBrain<?> mindController;
    protected boolean child;
    protected boolean nightTime;
    protected boolean sleeping;
    protected int refreshCheck;

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
        this.child = npc.isBaby();
        this.nightTime = level.isNight();
        this.sleeping = npc.isSleeping();
    }

    public abstract void initGoals();

    public void tick() {
        updateVars();
        tickGoals();
    }

    public void switchState(int state) {
        if (currentState == state) return;
        this.lastState = currentState;
        this.currentState = state;
        this.runningActions.forEach(action -> {
            if (action.isRunning()) action.stop();
        });
        this.runningActions.clear();
    }

    public int getCurrentState() {
        return this.currentState;
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
            this.lastState = currentState;
        }

        int priority = -1;
        Set<String> disabled = new HashSet<>();

        actionUpdate(coreActions, priority, disabled);

        if (isMindControlled()) return;

        if (runningActions.isEmpty()) {
            runningActions = actionMap.get(currentState).clone();
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
                    if (action instanceof CombatAction combatAction && combatAction.isAggressive()) {
                        this.npc.setAggressive(true);
                    }
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
                    else {
                        action.stop();
                        if (action instanceof CombatAction combatAction && combatAction.isAggressive()) {
                            npc.setAggressive(false);
                        }
                    }
                }
            }
        }
    }

    protected boolean isActionDisabled(Action action, int priority, Set<String> disabled) {
        if (child && !action.canUseIfChild()) return true;
        if (!child && !action.canUseIfAdult()) return true;
        for (String s : disabled) {
            for (String s1 : action.ids) {
                if (s1.equals(s)) {
                    return true;
                }
            }
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

    public int refreshTime() {
        return 200;
    }

    public boolean addCoreAction(Action action) {
        return coreActions.add(action);
    }

    public boolean addToBrain(Integer key, Action value) {
        actionMap.computeIfAbsent(key, k -> new ObjectArrayList<>());
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
