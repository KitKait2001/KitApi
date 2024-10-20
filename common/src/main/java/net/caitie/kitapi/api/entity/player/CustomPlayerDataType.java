package net.caitie.kitapi.api.entity.player;

public class CustomPlayerDataType<T extends Class<? extends CustomPlayerData>> {
    private final T dataClass;

    public CustomPlayerDataType(final T dataClass) {
        this.dataClass = dataClass;
    }

    public T get() {
        return dataClass;
    }

}
