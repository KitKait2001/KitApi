package net.caitie.kitapi.api.entity.ai.actions;

import net.caitie.kitapi.api.entity.IKitNPC;
import net.caitie.kitapi.api.entity.ai.navigation.PlayerLikeNpcNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.Set;

public class UseDoorsAndGatesAction extends Action {
    protected boolean hasDoor;
    protected boolean hasFenceGate;
    protected BlockPos doorPos;
    protected boolean passed;
    protected int forgetTime;
    protected float doorOpenDirX;
    protected float doorOpenDirZ;

    public <T extends AgeableMob & IKitNPC> UseDoorsAndGatesAction(T npc, ServerLevel level, Set<String> disabledIds, int priority) {
        super(npc, level, ActionTypes.interactWithDoorAndFence(), disabledIds, priority);
    }

    @Override
    public boolean canStart() {
        if (mob.getNavigation() instanceof PlayerLikeNpcNavigation<?> nav) {
            if (!nav.canOpenDoors()) return false;
            Path path = nav.getPath();
            if (path != null && !path.isDone()) {
                for (int i=0; i<path.getNodeCount(); i++) {
                    Node node = path.getNode(i);
                    this.doorPos = node.asBlockPos();
                    BlockState state = level.getBlockState(doorPos);
                    if (this.mob.distanceToSqr(this.doorPos.getX(), this.mob.getY(), this.doorPos.getZ()) <= 3) {
                        this.hasDoor = DoorBlock.isWoodenDoor(state);
                        if (this.hasDoor) {
                            this.doorPos = doorPos.above();
                            return true;
                        }
                        this.hasFenceGate = state.getBlock() instanceof FenceGateBlock;
                        if (hasFenceGate) return true;
                    }
                }

                this.doorPos = this.mob.blockPosition().above();
                this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level(), this.doorPos);
                BlockState state = level.getBlockState(doorPos.below());
                this.hasFenceGate = state.getBlock() instanceof FenceGateBlock;
                return this.hasDoor || this.hasFenceGate;
            }
        }
        return false;
    }

    @Override
    public boolean canContinue() {
        return (this.hasDoor || this.hasFenceGate) && !passed && this.forgetTime > 0;
    }

    @Override
    public void start() {
        this.passed = false;
        this.doorOpenDirX = (float)((double)this.doorPos.getX() + 0.5D - mob.getX());
        this.doorOpenDirZ = (float)((double)this.doorPos.getZ() + 0.5D - mob.getZ());
        this.forgetTime = 20;
        super.start();
        setOpen(true);
    }

    @Override
    public void tick() {
        --this.forgetTime;
        float f = (float)((double)this.doorPos.getX() + 0.5D - mob.getX());
        float f1 = (float)((double)this.doorPos.getZ() + 0.5D - mob.getZ());
        float f2 = this.doorOpenDirX * f + this.doorOpenDirZ * f1;
        if (f2 < 0.0F) {
            this.passed = true;
        }
    }

    @Override
    public void stop() {
        if (this.isOpen()) {
            this.setOpen(false);
        }
        super.stop();
    }


    protected void setOpen(boolean open) {
        if (this.hasFenceGate) {
            BlockState blockState = level.getBlockState(doorPos);
            if (blockState.getBlock() instanceof FenceGateBlock && blockState.getValue(FenceGateBlock.OPEN) != open) {
                mob.swing(InteractionHand.MAIN_HAND);
                ActionUtils.toggleFenceGate(mob, level, doorPos, open);
            }
        } else {
            if (this.hasDoor) {
                BlockState blockState = level.getBlockState(doorPos);
                if (blockState.getBlock() instanceof DoorBlock) {
                    mob.swing(InteractionHand.MAIN_HAND);
                    ((DoorBlock) blockState.getBlock()).setOpen(mob, level, blockState, doorPos, open);
                }
            }
        }
    }

    protected boolean isOpen() {
        if (this.hasFenceGate) {
            BlockState blockState = level.getBlockState(doorPos);
            if (blockState.getBlock() instanceof FenceGateBlock) {
                return blockState.getValue(FenceGateBlock.OPEN);
            }
            else {
                this.hasFenceGate = false;
                return false;
            }
        }
        if (!this.hasDoor) return false;
        BlockState blockState = level.getBlockState(doorPos);
        if (!(blockState.getBlock() instanceof DoorBlock)) {
            this.hasDoor = false;
            return false;
        }
        return blockState.getValue(DoorBlock.OPEN);
    }
}
