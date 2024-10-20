package net.caitie.kitapi.event;


import net.caitie.kitapi.Constants;
import net.caitie.kitapi.client.ForgeCustomPlayerRenderData;
import net.caitie.kitapi.client.ForgeCustomPlayerRenderDataInitializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
        ForgeCustomPlayerRenderDataInitializer.registry.sort(Comparator.comparingInt(ForgeCustomPlayerRenderData::priority));
        for (ForgeCustomPlayerRenderData data : ForgeCustomPlayerRenderDataInitializer.registry) {
            if (data.shouldAddLayers(event)) {
                data.addLayers(event);
            }
        }
    }

}
