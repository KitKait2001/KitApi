package net.caitie.kitapi;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class Kitapi {

    public Kitapi(IEventBus eventBus, Dist dist, ModContainer container) {
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        KitApiMain.init();

        container.registerConfig(ModConfig.Type.COMMON, NeoForgeConfigs.COMMON_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfigs.CLIENT_SPEC);
    }
}
