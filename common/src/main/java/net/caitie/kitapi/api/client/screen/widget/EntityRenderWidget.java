package net.caitie.kitapi.api.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntityRenderWidget extends AbstractWidget {
    protected final int X;
    protected final int Y;
    protected final int entScale;
    protected final Vector3f entTranslate;
    protected final Quaternionf entPose;
    protected final Quaternionf cameraAngle;
    protected final LivingEntity entity;

    public EntityRenderWidget(int pX, int pY, int pWidth, int pHeight, int entScale, Vector3f translate, Quaternionf pose, Quaternionf cameraAngle, LivingEntity entity) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.X = pX;
        this.Y = pY;
        this.entScale = entScale;
        this.entTranslate = translate;
        this.entPose = pose;
        this.cameraAngle = cameraAngle;
        this.entity = entity;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        InventoryScreen.renderEntityInInventory(guiGraphics, X, Y, entScale, entTranslate, entPose, cameraAngle, entity);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
