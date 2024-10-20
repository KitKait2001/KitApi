package net.caitie.kitapi;

import net.caitie.kitapi.api.config.Config;
import net.caitie.kitapi.api.config.IKitConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeConfigs {

    public static class Common implements IKitConfig {
        protected ForgeConfigSpec.ConfigValue<Integer> heterochromiaChance;
        protected ForgeConfigSpec.ConfigValue<Integer> albinoChance;
        protected ForgeConfigSpec.ConfigValue<Boolean> allowCustomPlayers;
        protected ForgeConfigSpec.ConfigValue<Boolean> renderCustomPlayers;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("kitapi_forge_common");
            this.heterochromiaChance = builder.comment(Config.Values.HETEROCHROMIA_CHANCE.description).define(Config.Values.HETEROCHROMIA_CHANCE.name, (int) Config.Values.HETEROCHROMIA_CHANCE.value);
            this.albinoChance = builder.comment(Config.Values.ALBINO_CHANCE.description).define(Config.Values.ALBINO_CHANCE.name, (int) Config.Values.ALBINO_CHANCE.value);
            this.allowCustomPlayers = builder.comment(Config.Values.ALLOW_CUSTOM_PLAYERS.description).define(Config.Values.ALLOW_CUSTOM_PLAYERS.name, (boolean) Config.Values.ALLOW_CUSTOM_PLAYERS.value);
            this.renderCustomPlayers = builder.comment(Config.Values.RENDER_CUSTOM_PLAYERS.description).define(Config.Values.RENDER_CUSTOM_PLAYERS.name, (boolean) Config.Values.RENDER_CUSTOM_PLAYERS.value);
            builder.pop();
        }

        @Override
        public void update() {
            KitApiMain.CONFIG.heterochromiaChance = this.heterochromiaChance.get();
            KitApiMain.CONFIG.albinoChance = this.albinoChance.get();
            KitApiMain.CONFIG.allowCustomPlayers = this.allowCustomPlayers.get();
            KitApiMain.CONFIG.renderCustomPlayers = renderCustomPlayers.get();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
        
        COMMON_SPEC = common.getRight();
        COMMON = common.getLeft();
    }

    @SubscribeEvent
    public static void onConfigLoaded(final ModConfigEvent.Loading event) {
        if (Objects.equals(event.getConfig().getModId(), Constants.MOD_ID)) {
            ModConfig.Type type = event.getConfig().getType();
            if (Objects.requireNonNull(type) == ModConfig.Type.COMMON) {
                COMMON.update();
            }
        }
    }

    @SubscribeEvent
    public static void onConfigReloaded(final ModConfigEvent.Reloading event) {
        if (Objects.equals(event.getConfig().getModId(), Constants.MOD_ID)) {
            ModConfig.Type type = event.getConfig().getType();
            if (Objects.requireNonNull(type) == ModConfig.Type.COMMON) {
                COMMON.update();
            }
        }
    }

}
