package net.caitie.kitapi;

import net.caitie.kitapi.api.config.Config;
import net.caitie.kitapi.api.config.IKitConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NeoForgeConfigs {

    public static class Common implements IKitConfig {
        protected ModConfigSpec.ConfigValue<Integer> heterochromiaChance;
        protected ModConfigSpec.ConfigValue<Integer> albinoChance;
        protected ModConfigSpec.ConfigValue<Boolean> allowCustomPlayers;

        public Common(ModConfigSpec.Builder builder) {
            builder.push("kitapi_neoforge_common");
            this.heterochromiaChance = builder.comment(Config.Values.HETEROCHROMIA_CHANCE.description).define(Config.Values.HETEROCHROMIA_CHANCE.name, (int) Config.Values.HETEROCHROMIA_CHANCE.value);
            this.albinoChance = builder.comment(Config.Values.ALBINO_CHANCE.description).define(Config.Values.ALBINO_CHANCE.name, (int) Config.Values.ALBINO_CHANCE.value);
            this.allowCustomPlayers = builder.comment(Config.Values.ALLOW_CUSTOM_PLAYERS.description).define(Config.Values.ALLOW_CUSTOM_PLAYERS.name, (boolean) Config.Values.ALLOW_CUSTOM_PLAYERS.value);
            builder.pop();
        }

        @Override
        public void update() {
            KitApiMain.CONFIG.heterochromiaChance = this.heterochromiaChance.get();
            KitApiMain.CONFIG.albinoChance = this.albinoChance.get();
            KitApiMain.CONFIG.allowCustomPlayers = this.allowCustomPlayers.get();
        }
    }
    public static class Client implements IKitConfig {
        protected ModConfigSpec.ConfigValue<Boolean> renderCustomPlayers;

        public Client(ModConfigSpec.Builder builder) {
            builder.push("kitapi_forge_common");
            renderCustomPlayers = builder.comment(Config.Values.RENDER_CUSTOM_PLAYERS.description).define(Config.Values.RENDER_CUSTOM_PLAYERS.name, (boolean) Config.Values.RENDER_CUSTOM_PLAYERS.value);
            builder.pop();
        }

        @Override
        public void update() {
            KitApiMain.CONFIG.renderCustomPlayers = renderCustomPlayers.get();
        }
    }

    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;

    public static final Common COMMON;
    public static final Client CLIENT;

    static {
        Pair<Common, ModConfigSpec> common = new ModConfigSpec.Builder().configure(Common::new);
        Pair<Client, ModConfigSpec> client = new ModConfigSpec.Builder().configure(Client::new);

        COMMON_SPEC = common.getRight();
        COMMON = common.getLeft();

        CLIENT_SPEC = client.getRight();
        CLIENT = client.getLeft();
    }

    @SubscribeEvent
    public static void onConfigLoaded(final ModConfigEvent.Loading event) {
        if (Objects.equals(event.getConfig().getModId(), Constants.MOD_ID)) {
            ModConfig.Type type = event.getConfig().getType();
            switch (type) {
                case COMMON -> COMMON.update();
                case CLIENT -> CLIENT.update();
            }
        }
    }

    @SubscribeEvent
    public static void onConfigReloaded(final ModConfigEvent.Reloading event) {
        if (Objects.equals(event.getConfig().getModId(), Constants.MOD_ID)) {
            ModConfig.Type type = event.getConfig().getType();
            switch (type) {
                case COMMON -> COMMON.update();
                case CLIENT -> CLIENT.update();
            }
        }
    }
}
