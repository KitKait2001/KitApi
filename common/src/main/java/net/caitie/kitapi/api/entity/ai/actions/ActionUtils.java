package net.caitie.kitapi.api.entity.ai.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class ActionUtils {
    public static final RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> COOKING_CHECK = RecipeManager.createCheck(RecipeType.SMELTING);

    public static void moveTo(Mob mob, Vec3 pos, double speed) {
        if (pos != null) {
            mob.getNavigation().moveTo(pos.x, pos.y, pos.z, speed);
        }
    }

    public static void moveTo(Mob mob, Entity entity, double speed) {
        if (entity != null) {
            mob.getNavigation().moveTo(entity, speed);
        }
    }

    public static void lookAt(Mob mob, Entity lookAt) {
        if (lookAt != null) {
            mob.getLookControl().setLookAt(lookAt);
        }
    }

    public static void lookAt(Mob mob, double x, double y, double z) {
        mob.getLookControl().setLookAt(x, y, z);
    }

    public static void lookAndMoveTo(Mob mob, Entity entity, double speed) {
        lookAt(mob, entity);
        moveTo(mob, entity, speed);
    }

    public static void lookAndTurnTo(Mob mob, Entity lookAt) {
        mob.getLookControl().setLookAt(lookAt, 30.0F, 30.0F);
        mob.setYRot(Mth.rotateIfNecessary(mob.getYRot(), mob.getYHeadRot(), 0.0F));
    }

    public static void strafeAwayFrom(Mob mob, Entity target, float dist) {
        mob.getNavigation().stop();
        lookAndTurnTo(mob, target);
        mob.getMoveControl().strafe(-dist, 0);
    }

    public static void toggleFenceGate(Mob mob, Level level, BlockPos fenceGatePos, boolean open) {
        mob.swing(InteractionHand.MAIN_HAND);
        level.setBlockAndUpdate(fenceGatePos, level.getBlockState(fenceGatePos).setValue(FenceGateBlock.OPEN, open));
        level.playSound(null, fenceGatePos, open ? SoundEvents.FENCE_GATE_OPEN : SoundEvents.FENCE_GATE_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public static ItemStack findItemOfType(InventoryCarrier carrier, Predicate<ItemStack> predicate) {
        return findItemOfType(carrier, predicate, ItemStack.EMPTY);
    }

    public static ItemStack findItemOfType(InventoryCarrier carrier, Predicate<ItemStack> predicate, ItemStack fallback) {
        for (ItemStack itemStack : carrier.getInventory().getItems()) {
            if (predicate.test(itemStack)) return itemStack;
        }
        return fallback;
    }

    public static boolean meleeAttack(Mob mob, LivingEntity target) {
        if (target == null || !target.isAlive()) return false;
        if (mob.isWithinMeleeAttackRange(target)) {
            return mob.doHurtTarget(target);
        }
        return false;
    }
}
