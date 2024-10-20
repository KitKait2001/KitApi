package net.caitie.kitapi.api.entity.ai.navigation;

import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class PlayerLikeNpcNavigation<T extends Mob & IKitNPC> extends GroundPathNavigation {

    public PlayerLikeNpcNavigation(T pMob, Level pLevel) {
        super(pMob, pLevel);
    }

    @Override
    protected PathFinder createPathFinder(int pMaxVisitedNodes) {
        this.nodeEvaluator = new PlayerLikeNpcWalkNodeEvaluator();
        this.setCanOpenDoors(true);
        this.setCanPassDoors(true);
        this.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, pMaxVisitedNodes);
    }


}
