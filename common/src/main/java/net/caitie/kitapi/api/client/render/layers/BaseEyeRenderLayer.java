package net.caitie.kitapi.api.client.render.layers;

import net.caitie.kitapi.api.entity.genetics.Genetics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;

public abstract class BaseEyeRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends GeneticsRenderLayer<T, M> {
    private final int eyeIndex;

    public BaseEyeRenderLayer(RenderLayerParent<T, M> pRenderer, boolean rightEye) {
        super(pRenderer);
        this.eyeIndex = rightEye ? 1 : 0;
    }

    @Override
    public boolean canRenderLayer(T geneticsMob) {
        return !geneticsMob.isInvisible();
    }

    @Override
    public int getRenderColor(Genetics genetics) {
        return genetics.getEyeColors()[eyeIndex];
    }

}
