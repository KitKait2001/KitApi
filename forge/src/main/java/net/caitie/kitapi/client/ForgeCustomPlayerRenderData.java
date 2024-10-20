package net.caitie.kitapi.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
public abstract class ForgeCustomPlayerRenderData {

    public abstract int priority();

    public abstract boolean shouldAddLayers(EntityRenderersEvent.AddLayers event);

    public abstract void addLayers(EntityRenderersEvent.AddLayers event);

}
