package net.caitie.kitapi.api.registry;

import com.google.common.collect.Maps;
import net.caitie.kitapi.Constants;
import net.caitie.kitapi.api.entity.player.CustomPlayerData;
import net.caitie.kitapi.api.entity.player.CustomPlayerDataType;

import java.util.Collection;
import java.util.Map;

public class CustomPlayerDataRegistry {

    private static final Map<String, CustomPlayerDataType<?>> registryMap = Maps.newHashMap();

    public static void registerCustomPlayerData(String id, CustomPlayerDataType<?> data) {
        registryMap.put(id, data);
    }

    public static CustomPlayerDataType<?> getTypeFromRegistry(String id) {
        return registryMap.get(id);
    }

    public static CustomPlayerData getDataSafe(String id, boolean logError) {
        try {
            return getDataOrThrow(id);
        }
        catch (Exception e) {
            if (logError) Constants.LOG.debug("CustomPlayerDataRegistry: Could not get DataType: {} from registry. Ignoring!", id);
            return null;
        }
    }

    public static CustomPlayerData getDataUnsafe(String id) {
        try {
            return getDataOrThrow(id);
        }
        catch (Exception e) {
            Constants.LOG.debug("CustomPlayerDataRegistry: Could not get DataType: {} from registry", id, e);
            return null;
        }
    }

    private static CustomPlayerData getDataOrThrow(String id) throws Exception {
        return getTypeFromRegistry(id).get().getDeclaredConstructor().newInstance();
    }

    public static Collection<String> keys() {
        return registryMap.keySet();
    }

    public static Collection<CustomPlayerDataType<?>> values() {
        return registryMap.values();
    }
}
