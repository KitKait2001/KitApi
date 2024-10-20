package net.caitie.kitapi.api.entity.ai.actions;

import com.google.common.collect.Sets;

import java.util.Locale;
import java.util.Set;

public enum ActionTypes {

    LOOK_AT_ENTITY,
    WALK_TO_ENTITY,
    FLEE_FROM_ENTITY,
    NON_AGGRESSIVE_COMBAT,
    AGGRESSIVE_COMBAT,
    DEFENSIVE_COMBAT,
    TARGETING_COMBAT,
    TARGETING_NON_COMBAT,
    PICK_UP_ITEM,
    USE_ITEM,
    EATING,
    INTERACT_DOOR,
    INTERACT_FENCE_GATE,
    FLOAT,
    RANDOM_LOOK,
    RANDOM_WALK;

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static Set<String> randomLookAndWalk() {
        return Sets.newHashSet(RANDOM_LOOK.id(), RANDOM_WALK.id());
    }

    public static Set<String> entityLookAndWalk() {
        return Sets.newHashSet(LOOK_AT_ENTITY.id(), WALK_TO_ENTITY.id());
    }

    public static Set<String> combatAggressive() {
        return Sets.newHashSet(LOOK_AT_ENTITY.id(), WALK_TO_ENTITY.id(), AGGRESSIVE_COMBAT.id());
    }

    public static Set<String> interactWithDoorAndFence() {
        return Sets.newHashSet(INTERACT_DOOR.id(), INTERACT_FENCE_GATE.id());
    }
}
