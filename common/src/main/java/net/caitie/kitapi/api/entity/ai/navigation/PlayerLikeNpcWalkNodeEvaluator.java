package net.caitie.kitapi.api.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;

public class PlayerLikeNpcWalkNodeEvaluator extends WalkNodeEvaluator {

    @Override
    public PathType getPathType(PathfindingContext pContext, int pX, int pY, int pZ) {
        BlockState blockState = pContext.getBlockState(new BlockPos.MutableBlockPos(pX, pY, pZ));
        if (blockState.getBlock() instanceof FenceGateBlock) {
            return PathType.WALKABLE_DOOR;
        }
        return super.getPathType(pContext, pX, pY, pZ);
    }

    @Override
    public Node getStart() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockstate = mob.level().getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
        if (!this.mob.canStandOnFluid(blockstate.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!blockstate.is(Blocks.WATER) && blockstate.getFluidState() != Fluids.WATER.getSource(false)) {
                        --i;
                        break;
                    }

                    ++i;
                    blockstate = mob.level().getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                i = Mth.floor(this.mob.getY() + 0.5D);
            } else {
                BlockPos blockpos;
                for(blockpos = this.mob.blockPosition(); (mob.level().getBlockState(blockpos).isAir() || this.isPathfindableOrFenceGate(mob.level().getBlockState(blockpos)) && blockpos.getY() > this.mob.level().getMinBuildHeight()); blockpos = blockpos.below()) {
                }

                i = blockpos.above().getY();
            }
        } else {
            while(this.mob.canStandOnFluid(blockstate.getFluidState())) {
                ++i;
                blockstate = mob.level().getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
            }

            --i;
        }

        BlockPos blockpos1 = this.mob.blockPosition();
        if (!this.canStartAt(blockpos$mutableblockpos.set(blockpos1.getX(), i, blockpos1.getZ()))) {
            AABB aabb = this.mob.getBoundingBox();
            if (this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, i, aabb.maxZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, i, aabb.maxZ))) {
                return this.getStartNode(blockpos$mutableblockpos);
            }
        }

        return this.getStartNode(new BlockPos(blockpos1.getX(), i, blockpos1.getZ()));
    }



    protected boolean isPathfindableOrFenceGate(BlockState blockState) {
        return blockState.getBlock() instanceof FenceGateBlock || blockState.isPathfindable(PathComputationType.LAND);
    }
}
