package net.caitie.kitapi.api.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public abstract class TextureRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public TextureRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T mob, float v, float v1, float v2, float v3, float v4, float v5) {
        if (canRender(mob)) {
            this.getParentModel().renderToBuffer(poseStack, multiBufferSource.getBuffer(getRenderType(mob)), i, LivingEntityRenderer.getOverlayCoords(mob, 0.0F));
        }
    }

    public boolean canRender(T mob) {
        return !mob.isInvisible();
    }

    public RenderType getRenderType(T mob) {
        return RenderType.entityCutoutNoCull(getTextureLocation(mob));
    }

    public abstract ResourceLocation getTexture(T mob);

    @Override
    protected ResourceLocation getTextureLocation(T pEntity) {
        return getTexture(pEntity);
    }
}
