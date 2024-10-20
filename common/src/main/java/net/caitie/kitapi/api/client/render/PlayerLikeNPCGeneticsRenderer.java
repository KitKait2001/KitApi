package net.caitie.kitapi.api.client.render;

import net.caitie.kitapi.api.entity.IKitGenetics;
import net.caitie.kitapi.api.entity.IKitNPC;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;

public abstract class PlayerLikeNPCGeneticsRenderer<T extends LivingEntity & IKitNPC & IKitGenetics> extends PlayerLikeNPCRenderer<T> {

    public PlayerLikeNPCGeneticsRenderer(EntityRendererProvider.Context pContext, boolean slim) {
        super(pContext, slim);
    }

    protected abstract void addExtraRenderLayers(EntityRendererProvider.Context context, boolean slim);

    @Override
    protected void addLayers(EntityRendererProvider.Context context, boolean slim) {
        addExtraRenderLayers(context, slim);
        super.addLayers(context, slim);
    }
}
