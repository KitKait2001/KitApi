package net.caitie.kitapi.api.entity;

import net.caitie.kitapi.api.entity.ai.NPCBrain;
import net.caitie.kitapi.api.entity.ai.actions.ActionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.phys.Vec3;

public interface IKitNPC {

    Mob entity();

    boolean isMale();

    NPCBrain<?> getAI();

    void createAI(ServerLevel level);

    ServerLevel serverLevel();

    BlockPos homePosition();

    default Vec3 lastDeltaMovement() {
        return Vec3.ZERO;
    }

    default void tickAi(ServerLevel level) {
        if (getAI() instanceof NPCBrain brain) {
            if (!brain.initialized()) {
                brain.init();
                return;
            }
            brain.tick();
        }
        else createAI(level);
    }

    default boolean hasValidHomePosition() {
        return homePosition() != null;
    }

    default boolean isEdibleItem(ItemStack stack) {
        FoodProperties foodProperties = stack.get(DataComponents.FOOD);
        return foodProperties != null && (foodProperties.effects().stream().noneMatch((pair) -> pair.effect().getEffect().value().getCategory() == MobEffectCategory.HARMFUL) || ActionUtils.COOKING_CHECK.getRecipeFor(new SingleRecipeInput(stack), entity().level()).isPresent());
    }

    default boolean canEat(ItemStack stack) {
        FoodProperties foodProperties = stack.get(DataComponents.FOOD);
        return foodProperties != null && foodProperties.effects().stream().noneMatch((pair) -> pair.effect().getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
                && ActionUtils.COOKING_CHECK.getRecipeFor(new SingleRecipeInput(stack), entity().level()).isEmpty();
    }

    default void useItem(ItemStack item, InteractionHand hand) {
        entity().stopUsingItem();
        entity().setItemInHand(hand, item);
        entity().startUsingItem(hand);
    }

    default boolean isHurt() {
        return entity().getHealth() < entity().getMaxHealth();
    }

    default boolean isCombatItem(ItemStack stack) {
        return isMeleeWeapon(stack) || isRangedWeapon(stack) || isShield(stack);
    }

    default boolean isShield(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    default boolean isMeleeWeapon(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem;
    }

    default boolean isRangedWeapon(ItemStack stack) {
        return stack.getItem() instanceof BowItem ||
                stack.getItem() instanceof CrossbowItem;
    }
}
