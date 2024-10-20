package net.caitie.kitapi.api.entity.player;

import net.caitie.kitapi.api.entity.IKitGenetics;

public interface IKitPlayer extends IKitGenetics {

    boolean isMale();

    void setMale(boolean male);

    CustomPlayerData getCustomData(String id);

    <T extends CustomPlayerData> void setCustomData(T newData);

    <T extends CustomPlayerData> void setCustomData(int i, T newData);
}
