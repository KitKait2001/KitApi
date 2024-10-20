package net.caitie.kitapi.api.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.caitie.kitapi.api.entity.IKitGenetics;
import net.caitie.kitapi.api.entity.genetics.Genetics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public abstract class GeneticsRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public GeneticsRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T mob, float v, float v1, float v2, float v3, float v4, float v5) {
        if (mob instanceof IKitGenetics && canRenderLayer(mob)) {
            poseStack.pushPose();
            this.getParentModel().renderToBuffer(poseStack, multiBufferSource.getBuffer(getRenderType(mob)), i, LivingEntityRenderer.getOverlayCoords(mob, 0.0F), getRenderColor(((IKitGenetics)mob).getGenes()));
            poseStack.popPose();
        }
    }

    public RenderType getRenderType(T geneticsMob) {
        return RenderType.entityCutoutNoCull(getTexture(geneticsMob));
    }

    public abstract boolean canRenderLayer(T geneticsMob);

    public abstract int getRenderColor(Genetics genetics);

    public abstract ResourceLocation getTexture(T geneticsMob);
}
