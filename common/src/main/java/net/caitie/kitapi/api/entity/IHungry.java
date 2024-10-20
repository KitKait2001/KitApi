package net.caitie.kitapi.api.entity;

import net.caitie.kitapi.api.entity.data.NPCFoodData;

public interface IHungry {

    NPCFoodData<?> getFoodData();

    default boolean isHungry() {
        return getFoodData().needsFood();
    }
}
