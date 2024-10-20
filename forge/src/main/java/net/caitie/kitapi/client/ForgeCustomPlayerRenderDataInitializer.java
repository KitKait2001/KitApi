package net.caitie.kitapi.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ForgeCustomPlayerRenderDataInitializer {

    public static final List<ForgeCustomPlayerRenderData> registry = Lists.newArrayList();

    public static void addData(ForgeCustomPlayerRenderData data) {
        registry.add(data);
    }

}
