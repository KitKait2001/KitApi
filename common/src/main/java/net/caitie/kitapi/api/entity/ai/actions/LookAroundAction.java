package net.caitie.kitapi.api.entity.ai.actions;

import com.google.common.collect.Sets;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.Set;

public class LookAroundAction extends Action {
    protected LivingEntity lookAt;
    private double relX;
    private double relZ;
    private int lookTime;
    protected final TargetingConditions lookAtContext;

    public <T extends Mob & IKitNPC> LookAroundAction(T npc, ServerLevel level, Set<String> disabledIds, int priority) {
        super(npc, level, Sets.newHashSet(ActionTypes.RANDOM_LOOK.id()), disabledIds, priority);
        this.lookAtContext = TargetingConditions.forNonCombat().range(8.0D);
    }

    @Override
    public boolean canStart() {
        if (this.mob.isAggressive()) return false;
        if (this.mob.getRandom().nextFloat() <= 0.05F) {
            if (this.mob.getRandom().nextFloat() <= 0.8F) {
                this.lookAt = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(LivingEntity.class,
                        this.mob.getBoundingBox().inflate(8.0D, 4.0D, 8.0D), (entity) -> entity != mob),
                        this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            }
            this.lookTime = 40 + this.mob.getRandom().nextInt(20);
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinue() {
        return this.lookTime > 0 && !mob.isAggressive();
    }

    @Override
    public void start() {
        super.start();
        double d0 = (Math.PI * 2D) * this.mob.getRandom().nextDouble();
        this.relX = Math.cos(d0);
        this.relZ = Math.sin(d0);
    }

    @Override
    public void stop() {
        super.stop();
        this.lookAt = null;
    }

    @Override
    public void tick() {
        --lookTime;
        if (lookAt != null && lookAt.isAlive()) {
            ActionUtils.lookAt(this.mob, lookAt);
        }
        else ActionUtils.lookAt(this.mob, this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
    }
}
