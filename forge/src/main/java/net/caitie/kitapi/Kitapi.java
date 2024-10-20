package net.caitie.kitapi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class Kitapi {

    public Kitapi(FMLJavaModLoadingContext context) {
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        KitApiMain.init();

        context.registerConfig(ModConfig.Type.COMMON, ForgeConfigs.COMMON_SPEC);
    }
}
