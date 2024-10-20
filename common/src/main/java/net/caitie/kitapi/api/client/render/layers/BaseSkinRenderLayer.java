package net.caitie.kitapi.api.client.render.layers;

import net.caitie.kitapi.api.entity.genetics.Genetics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;

public abstract class BaseSkinRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends GeneticsRenderLayer<T, M> {

    public BaseSkinRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public boolean canRenderLayer(T geneticsMob) {
        return !geneticsMob.isInvisible();
    }

    @Override
    public int getRenderColor(Genetics genetics) {
        return genetics.getSkinColor();
    }

}
