package net.caitie.kitapi.api.entity;

import net.caitie.kitapi.KitApiMain;
import net.caitie.kitapi.api.entity.genetics.Genetics;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;

public interface IKitGenetics {

    Genetics getGenes();

    void setGenes(Genetics genes);

    default <T extends Mob & IKitGenetics> void createFromBreeding(Genetics mom, Genetics dad, T offspring) {
        RandomSource randomSource = offspring.getRandom();
        boolean heterochromia = Genetics.canHaveHeterochromia() && randomSource.nextInt(KitApiMain.CONFIG.heterochromiaChance) == 0;
        boolean albino = Genetics.canBeAlbino() && (mom.isAlbino() || dad.isAlbino() ? randomSource.nextInt(10) == 0 : randomSource.nextInt(KitApiMain.CONFIG.albinoChance) == 0);
        int[] eyes = heterochromia ? (randomSource.nextBoolean() ?
                new int[]{mom.getEyeColors()[0], dad.getEyeColors()[1]} :
                new int[]{dad.getEyeColors()[0], mom.getEyeColors()[1]}) :
                (randomSource.nextBoolean() ? new int[]{mom.getEyeColors()[0], mom.getEyeColors()[0]} : new int[]{dad.getEyeColors()[0], dad.getEyeColors()[0]});
        int hair = randomSource.nextBoolean() ? (randomSource.nextBoolean() ? mom.getHairColor() : dad.getHairColor()) :
                FastColor.ARGB32.lerp(randomSource.nextFloat(), mom.getHairColor(), dad.getHairColor());
        int skin = FastColor.ARGB32.lerp(randomSource.nextFloat(), mom.getSkinColor(), dad.getSkinColor());
        int style = randomSource.nextBoolean() ? mom.getHairStyle() : dad.getHairStyle();
        offspring.setGenes(new Genetics(albino, eyes, hair, skin, style));
    }
}
