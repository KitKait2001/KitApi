package net.caitie.kitapi.api.entity.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public abstract class CustomPlayerData {

    public abstract String id();

    public abstract void init(Player player);

    public abstract CustomPlayerData load(CompoundTag tag);

    public abstract void saveAdditional(CompoundTag tag);

    public final CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id());
        saveAdditional(tag);
        return tag;
    }
}
