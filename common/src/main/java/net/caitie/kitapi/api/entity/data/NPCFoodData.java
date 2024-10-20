package net.caitie.kitapi.api.entity.data;

import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;

public class NPCFoodData<T extends Mob & IKitNPC> {
    private final T npc;
    private int foodLevel = 20;
    private float saturationLevel = 5.0F;
    private float exhaustionLevel;
    private int tickTimer;
    private int lastFoodLevel = 20;

    public NPCFoodData(T npc) {
        this.npc = npc;
    }

    private void add(int pFoodLevel, float pSaturationLevel) {
        this.foodLevel = Mth.clamp(pFoodLevel + this.foodLevel, 0, 20);
        this.saturationLevel = Mth.clamp(pSaturationLevel + this.saturationLevel, 0.0F, (float)this.foodLevel);
    }

    public void eat(int pFoodLevelModifier, float pSaturationLevelModifier) {
        this.add(pFoodLevelModifier, FoodConstants.saturationByModifier(pFoodLevelModifier, pSaturationLevelModifier));
    }

    public void eat(FoodProperties pFoodProperties) {
        this.add(pFoodProperties.nutrition(), pFoodProperties.saturation());
    }

    public void tick(Level level, boolean starve, boolean regenerate) {
        Difficulty difficulty = level.getDifficulty();
        this.lastFoodLevel = this.foodLevel;
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        if (regenerate && this.saturationLevel > 0.0F && npc.isHurt() && this.foodLevel >= 20) {
            ++this.tickTimer;
            if (this.tickTimer >= 10) {
                float f = Math.min(this.saturationLevel, 6.0F);
                npc.heal(f / 6.0F);
                this.addExhaustion(f);
                this.tickTimer = 0;
            }
        } else if (regenerate && this.foodLevel >= 18 && npc.isHurt()) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                npc.heal(1.0F);
                this.addExhaustion(6.0F);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                if (npc.getHealth() > 10.0F || starve || npc.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    npc.hurt(npc.damageSources().starve(), 1.0F);
                }

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompoundTag) {
        if (pCompoundTag.contains("foodLevel", 99)) {
            this.foodLevel = pCompoundTag.getInt("foodLevel");
            this.tickTimer = pCompoundTag.getInt("foodTickTimer");
            this.saturationLevel = pCompoundTag.getFloat("foodSaturationLevel");
            this.exhaustionLevel = pCompoundTag.getFloat("foodExhaustionLevel");
        }

    }

    public void addAdditionalSaveData(CompoundTag pCompoundTag) {
        pCompoundTag.putInt("foodLevel", this.foodLevel);
        pCompoundTag.putInt("foodTickTimer", this.tickTimer);
        pCompoundTag.putFloat("foodSaturationLevel", this.saturationLevel);
        pCompoundTag.putFloat("foodExhaustionLevel", this.exhaustionLevel);
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public int getLastFoodLevel() {
        return this.lastFoodLevel;
    }

    public boolean needsFood() {
        return this.foodLevel < 20;
    }

    public void addExhaustion(float pExhaustion) {
        this.exhaustionLevel = Math.min(this.exhaustionLevel + pExhaustion, 40.0F);
    }

    public float getExhaustionLevel() {
        return this.exhaustionLevel;
    }

    public float getSaturationLevel() {
        return this.saturationLevel;
    }

    public void setFoodLevel(int pFoodLevel) {
        this.foodLevel = pFoodLevel;
    }

    public void setSaturation(float pSaturationLevel) {
        this.saturationLevel = pSaturationLevel;
    }

    public void setExhaustion(float pExhaustionLevel) {
        this.exhaustionLevel = pExhaustionLevel;
    }
}
