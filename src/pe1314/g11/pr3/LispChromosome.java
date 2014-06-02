package pe1314.g11.pr3;

import java.util.Random;

import pe1314.g11.Chromosome;

public class LispChromosome extends Chromosome<LispChromosome> implements Comparable<LispChromosome> {

    private final LispList list;

    public LispChromosome (final LispList l) {
        list = l;
    }

    public static LispChromosome newRandom (final boolean complete, final Random random, final int depth) {
        return new LispChromosome(LispUtils.generateRandom(complete, random, depth));
    }

    public LispList getLispList () {
        return list;
    }

    @Override
    public int getMutationPlaces () {
        return 0;
    }

    @Override
    public int getMutationTypes () {
        return 0;
    }

    @Override
    public LispChromosome getMutated (final int type, final int place, final int length) {
        return this;
    }

    @Override
    public int getCombinationPlaces () {
        return 0;
    }

    @Override
    public int getCombinationTypes () {
        return 0;
    }

    @Override
    public LispChromosome getCombined (final LispChromosome other, final int type, final int place, final int length) {
        return this;
    }

    @Override
    public int hashCode () {
        return list.hashCode();
    }

    @Override
    public boolean equals (final Object obj) {
        if (!(obj instanceof LispChromosome)) {
            return false;
        }
        final LispChromosome lc = (LispChromosome) obj;
        return list.equals(lc.list);
    }

    @Override
    public String toString () {
        return list.toString();
    }

    @Override
    public int compareTo (final LispChromosome o) {
        return list.compareTo(o.list);
    }
}
