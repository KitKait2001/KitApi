package net.caitie.kitapi.api.entity.genetics;

import net.caitie.kitapi.KitApiMain;
import net.minecraft.nbt.CompoundTag;

public class Genetics {
    public static final Genetics EMPTY = new Genetics(false, new int[]{0, 0}, 0, 0, 0);

    public static final String GENES = "Genes";
    public static final String ALBINO = "Albino";
    public static final String EYE_COLORS = "EyeColors";
    public static final String HAIR_COLOR = "HairColor";
    public static final String SKIN_COLOR = "SkinColor";
    public static final String HAIR_STYLE = "HairStyle";

    protected int[] eyeColors;
    protected int hairColor;
    protected int skinColor;
    protected int hairStyle;
    protected boolean albino;
    protected boolean heterochromia;

    public Genetics(boolean albino, int[] eyeColors, int hairColor, int skinColor, int hairStyle) {
        this.albino = albino;
        this.eyeColors = eyeColors;
        this.hairColor = hairColor;
        this.skinColor = skinColor;
        this.hairStyle = hairStyle;
        this.heterochromia = eyeColors[0] != eyeColors[1];
    }

    public static boolean canBeAlbino() {
        return KitApiMain.CONFIG.albinoChance > 0;
    }

    public static boolean canHaveHeterochromia() {
        return KitApiMain.CONFIG.heterochromiaChance > 0;
    }

    public boolean isAlbino() {
        return albino;
    }

    public int getHairColor() {
        return hairColor;
    }

    public int[] getEyeColors() {
        return eyeColors;
    }

    public int getSkinColor() {
        return skinColor;
    }

    public int getHairStyle() {
        return hairStyle;
    }

    public void setAlbino(boolean albino) {
        this.albino = albino;
    }

    public void saveToNBT(CompoundTag tag) {
        tag.put(GENES, save());
    }

    public CompoundTag save() {
        CompoundTag genes = new CompoundTag();
        genes.putBoolean(ALBINO, this.albino);
        genes.putIntArray(EYE_COLORS, this.eyeColors);
        genes.putInt(HAIR_COLOR, this.hairColor);
        genes.putInt(HAIR_STYLE, this.hairStyle);
        genes.putInt(SKIN_COLOR, this.skinColor);
        return genes;
    }

    public static Genetics loadFromTag(CompoundTag genes) {
        Genetics genetics = new Genetics(
                genes.getBoolean(Genetics.ALBINO),
                genes.getIntArray(Genetics.EYE_COLORS),
                genes.getInt(Genetics.HAIR_COLOR),
                genes.getInt(Genetics.SKIN_COLOR),
                genes.getInt(Genetics.HAIR_STYLE));
        return genetics;
    }

    public static Genetics loadFromNBT(CompoundTag tag) {
        Genetics genetics = null;
        if (tag.contains(GENES)) {
            CompoundTag genes = tag.getCompound(GENES);
            genetics = loadFromTag(genes);
        }
        return genetics;
    }

    public Genetics copyFrom(Genetics original) {
        this.albino = original.albino;
        this.eyeColors = original.eyeColors;
        this.skinColor = original.skinColor;
        this.hairColor = original.hairColor;
        this.hairStyle = original.hairStyle;
        return this;
    }

}
