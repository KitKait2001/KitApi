package net.caitie.kitapi.content.entity;

import net.caitie.kitapi.content.entity.inventory.NPCInventory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractVillagerNPC extends AbstractVillager implements IKitNPC {
    protected final NPCInventory<AbstractVillagerNPC> inventory = new NPCInventory<>(this, 33);
    protected int experience = 0;
    protected Player lastTradedPlayer;

    public AbstractVillagerNPC(EntityType<? extends AbstractVillagerNPC> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.inventory.tick();
    }

    @Override
    protected void customServerAiStep() {
        this.tickAi(this, serverLevel());
        super.customServerAiStep();
    }

    @Nullable
    @Override
    public ServerLevel serverLevel() {
        if (this.level().isClientSide()) return null;
        return (ServerLevel) this.level();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    protected void rewardTradeXp(MerchantOffer merchantOffer) {
        int i = 3 + this.random.nextInt(4);
        this.experience += merchantOffer.getXp();
        this.lastTradedPlayer = this.getTradingPlayer();
        if (merchantOffer.shouldRewardExp()) {
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), i));
        }
    }

    @Override
    public NPCInventory<AbstractVillagerNPC> getInventory() {
        return this.inventory;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean traded) {
        return null;
    }

    @Override
    public void playCelebrateSound() {
    }

}
