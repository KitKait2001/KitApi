package net.caitie.kitapi.content.entity;

import net.caitie.kitapi.content.entity.ai.NPCBrain;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;

import javax.annotation.Nullable;

public interface IKitNPC {

    NPCBrain<?> getAI();

    void createAI(IKitNPC npc, AgeableMob mob, ServerLevel level);

    @Nullable
    ServerLevel serverLevel();

    @Nullable
    BlockPos homePosition();

    default void tickAi(AgeableMob npc, ServerLevel level) {
        if (getAI() instanceof NPCBrain brain) {
            if (!brain.initialized()) {
                brain.init();
                return;
            }
            brain.tick();
        }
        else createAI(this, npc, level);
    }

    default boolean hasValidHomePosition() {
        return homePosition() != null;
    }
}
