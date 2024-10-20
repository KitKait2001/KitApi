package net.caitie.kitapi.mixin;

import net.caitie.kitapi.api.entity.genetics.Genetics;
import net.caitie.kitapi.api.entity.player.CustomPlayerData;
import net.caitie.kitapi.api.entity.player.IKitPlayer;
import net.caitie.kitapi.api.registry.CustomPlayerDataRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IKitPlayer {
    @Unique private static final EntityDataAccessor<CompoundTag> KITAPI_DATA_GENES = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    @Unique private static final EntityDataAccessor<Boolean> KITAPI_DATA_MALE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<CompoundTag> KITAPI_DATA_CUSTOM = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    @Unique private Genetics kitApi$genetics = Genetics.EMPTY;
    @Unique private List<CustomPlayerData> kitApi$customData = Lists.newArrayList();

    private PlayerMixin() {
        super(null, null);
        throw new IllegalStateException();
    }

    @Inject(method = "defineSynchedData", at=@At("TAIL"))
    protected void kitApi_PlayerMixin_defineSynchedData(SynchedEntityData.Builder pBuilder, CallbackInfo ci) {
        pBuilder.define(KITAPI_DATA_GENES, Genetics.EMPTY.save());
        pBuilder.define(KITAPI_DATA_MALE, true);
        pBuilder.define(KITAPI_DATA_CUSTOM, new CompoundTag());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    protected void kitApi_PlayerMixin_addAdditionalSaveData(CompoundTag pTag, CallbackInfo ci) {
        pTag.put("kitapi_Genetics", getGenes().save());
        pTag.putBoolean("kitapi_Male", isMale());
        pTag.put("kitapi_CustomData", ((Player)(Object)this).getEntityData().get(KITAPI_DATA_CUSTOM));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    protected void kitApi_PlayerMixin_readAdditionalSaveData(CompoundTag pTag, CallbackInfo ci) {
        this.setGenes(Genetics.loadFromNBT(pTag));
        this.setMale(pTag.getBoolean("kitapi_Male"));
        CompoundTag dataTag = pTag.getCompound("kitapi_CustomData");
        for (int i=0; i<dataTag.size(); i++) {
            String id = dataTag.getCompound(Integer.toString(i)).getString("id");
            CustomPlayerData data = CustomPlayerDataRegistry.getDataSafe(id, false);
            if (data != null) {
                this.setCustomData(i, data.load(dataTag));
            }
        }
    }

    @Override
    public Genetics getGenes() {
        if (((Player)(Object)this).level().isClientSide()) {
            return Genetics.loadFromTag(((Player)(Object)this).getEntityData().get(KITAPI_DATA_GENES));
        }
        else if (kitApi$genetics == null) {
            kitApi$genetics = Genetics.loadFromTag(((Player)(Object)this).getEntityData().get(KITAPI_DATA_GENES));
        }
        return kitApi$genetics;
    }

    @Override
    public void setGenes(Genetics genes) {
        ((Player)(Object)this).getEntityData().set(KITAPI_DATA_GENES, genes.save());
        kitApi$genetics = genes;
    }

    @Override
    public boolean isMale() {
        return ((Player)(Object)this).getEntityData().get(KITAPI_DATA_MALE);
    }

    @Override
    public void setMale(boolean male) {
        ((Player)(Object)this).getEntityData().set(KITAPI_DATA_MALE, male);
    }

    @Override
    public CustomPlayerData getCustomData(String id) {
        CustomPlayerData data = kitApi$customData.stream().filter((d) -> d.id().equals(id)).findFirst().orElse(null);
        if (data == null) {
            CustomPlayerData data1 = CustomPlayerDataRegistry.getDataSafe(id, true);
            if (data1 != null) {
                data1.init((Player) (Object) this);
                kitApi$customData.add(data1);
                return data1;
            }
        }
        return data;
    }

    @Override
    public <T extends CustomPlayerData> void setCustomData(T newData) {
        if (newData == null) return;
        int i = this.kitApi$customData.indexOf(getCustomData(newData.id()));
        setCustomData(i, newData);
    }

    @Override
    public <T extends CustomPlayerData> void setCustomData(int i, T newData) {
        if (newData == null) return;
        this.kitApi$customData.add(newData);
        String id = Integer.toString(i);
        CompoundTag tag = ((Player)(Object)this).getEntityData().get(KITAPI_DATA_CUSTOM);
        if (tag.contains(id)) {
            tag.remove(id);
        }
        tag.put(id, newData.save());
        ((Player)(Object)this).getEntityData().set(KITAPI_DATA_CUSTOM, tag);
    }
}
