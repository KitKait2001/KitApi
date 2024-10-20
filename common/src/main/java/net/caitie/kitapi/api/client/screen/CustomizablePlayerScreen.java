package net.caitie.kitapi.api.client.screen;

import com.google.common.collect.Maps;
import net.caitie.kitapi.api.client.screen.widget.EntityRenderWidget;
import net.caitie.kitapi.api.entity.IKitGenetics;
import net.caitie.kitapi.api.entity.genetics.Genetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public class CustomizablePlayerScreen extends Screen {
    public static final String GENETICS_ID = "kitapi_genes";
    private static final Vector3f PLAYER_TRANSLATION = new Vector3f();
    private static final Quaternionf PLAYER_ANGLE = new Quaternionf().rotationXYZ(0.0F, 0.0F, (float) Math.PI);
    private static final int PLAYER_SCALE = 40;
    private static final int PLAYER_OFFSET_Y = 75;
    private static final int PLAYER_OFFSET_X = 141;
    protected final boolean closeOnEsc;
    protected final Player player;
    protected Button OK_BUTTON;
    protected Button CANCEL_BUTTON;
    protected EntityRenderWidget playerRenderWidget;
    protected boolean ableToChangePlayer;
    protected int skinColor;
    protected int hairColor;
    protected int[] eyeColors;
    protected int hairStyle;
    protected boolean albino;
    protected Map<String, PlayerData> originalData = Maps.newHashMap();
    protected Map<String, PlayerData> newData = Maps.newHashMap();

    protected GeneticsData geneticsData;

    public CustomizablePlayerScreen(Component pTitle, boolean closeOnEsc) {
        super(pTitle);
        this.player = Minecraft.getInstance().player;
        this.addPlayerData(GENETICS_ID, new GeneticsData(player), true);
        this.closeOnEsc = closeOnEsc;
    }

    @Override
    protected void init() {
        this.ableToChangePlayer = false;
        super.init();
        initComponents();
        initData();
    }

    @Override
    public void onClose() {
        if (isAbleToChangePlayer()) {
            this.createCustomPlayer();
        }
        else revertToOriginalPlayer();
        super.onClose();
    }

    public void initData() {
        geneticsData = new GeneticsData(new Genetics(albino, eyeColors, hairColor, skinColor, hairStyle));
        this.updatePlayerData(GENETICS_ID, geneticsData);
    }

    public void addPlayerData(String id, PlayerData playerData, boolean original) {
        if (original) {
            this.originalData.put(id, playerData);
            return;
        }
        this.newData.put(id, playerData);
        playerData.sync(player);
    }

    public void updatePlayerData(String id, PlayerData data) {
        this.newData.put(id, data);
        data.sync(player);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        if (this.closeOnEsc) {
            this.ableToChangePlayer = false;
            return true;
        }
        return false;
    }

    public boolean isAbleToChangePlayer() {
        return this.ableToChangePlayer;
    }

    public void onOKButtonPressed() {
        this.ableToChangePlayer = true;
        this.onClose();
    }

    public void onCancelButtonPressed() {
        this.ableToChangePlayer = false;
        this.onClose();
    }

    protected void createCustomPlayer() {
        for (PlayerData playerData : newData.values()) {
            playerData.sync(player);
        }
    }

    protected void revertToOriginalPlayer() {
        for (PlayerData playerData : originalData.values()) {
            playerData.sync(player);
        }
    }

    protected void initComponents() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);
        this.OK_BUTTON = rowHelper.addChild(Button.builder(Component.translatable("gui.ok"), (press) -> this.onOKButtonPressed())
                .size(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT)
                .build());
        this.CANCEL_BUTTON = rowHelper.addChild(Button.builder(Component.translatable("gui.cancel"), (press) -> this.onCancelButtonPressed())
                .size(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT)
                .build());
        this.playerRenderWidget = rowHelper.addChild(new EntityRenderWidget(PLAYER_OFFSET_X, PLAYER_OFFSET_Y, PLAYER_SCALE, PLAYER_SCALE, PLAYER_SCALE, PLAYER_TRANSLATION, PLAYER_ANGLE, null, player));
        gridLayout.arrangeElements();
        FrameLayout.alignInRectangle(gridLayout, 0, 20, this.width, this.height, 0.5F, 0.25F);
        gridLayout.visitWidgets(this::addRenderableWidget);
    }

    public static class GeneticsData extends PlayerData {
        public Genetics genetics;

        public GeneticsData(Player player) {
            this.genetics = ((IKitGenetics)player).getGenes();
        }

        public GeneticsData(Genetics genetics) {
            this.genetics = genetics;
        }

        public void sync(Player player) {
            ((IKitGenetics)player).setGenes(genetics);
        }
    }

    public abstract static class PlayerData {
        public abstract void sync(Player player);
    }

}
