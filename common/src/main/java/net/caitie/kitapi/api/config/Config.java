package net.caitie.kitapi.api.config;

public class Config {
    public enum Values {
        HETEROCHROMIA_CHANCE("heterochromiaChance", "Chance for an NPC to have different colored eyes from 0 to this number. (set to 0 to disable)", 100),
        ALBINO_CHANCE("albinoChance", "Chance for an NPC to be albino from 0 to this number. (set to 0 to disable)", 500),
        ALLOW_CUSTOM_PLAYERS("allowCustomPlayers", "Enables the ability to have customizable players with genetics and other attributes.", true),
        RENDER_CUSTOM_PLAYERS("renderCustomPlayers", "Determines if custom player features should be rendered on the client.", false);

        public final String name;
        public final String description;
        public final Object value;

        Values(String name, String desc, Object value) {
            this.name = name;
            this.description = desc;
            this.value = value;
        }
    }

    public int heterochromiaChance = 100;
    public int albinoChance = 500;
    public boolean allowCustomPlayers = true;

    // ONLY ON CLIENT
    public boolean renderCustomPlayers = false;
}
