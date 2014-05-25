package pe1314.g11.pr3;

import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.sga.PermutationChromosome;

public class LispChromosome extends Chromosome<LispChromosome> implements Comparable<LispChromosome> {

    private final LispList list;

    public LispChromosome (LispList l) {
        list = l;
    }

    public static LispChromosome newRandom (Random random) {
        return new LispChromosome(LispUtils.generateRandom(random, 4));
    }
    
    public LispList getLispList(){
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
    public LispChromosome getMutated (int type, int place, int length) {
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
    public LispChromosome getCombined (LispChromosome other, int type, int place, int length) {
        return this;
    }
    
    @Override
    public int hashCode () {
        return list.hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof LispChromosome)) {
            return false;
        }
        LispChromosome lc = (LispChromosome) obj;
        return list.equals(lc.list);
    }
    
    @Override
    public String toString () {
        return list.toString();
    }
    
    @Override
    public int compareTo (LispChromosome o) {
        return list.compareTo(o.list);
    }
}
