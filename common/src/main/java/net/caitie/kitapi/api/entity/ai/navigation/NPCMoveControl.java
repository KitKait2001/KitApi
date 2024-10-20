package net.caitie.kitapi.api.entity.ai.navigation;

import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

public class NPCMoveControl extends MoveControl {
    public <T extends Mob & IKitNPC> NPCMoveControl(T pMob) {
        super(pMob);
    }

    @Override
    public void tick() {
        this.speedModifier = getSpeedModifier();
        super.tick();
    }

    @Override
    public double getSpeedModifier() {
        return mob.isUsingItem() || mob.isCrouching() ? (double) ((float)(super.getSpeedModifier() * 0.4F)) : super.getSpeedModifier();
    }
}
