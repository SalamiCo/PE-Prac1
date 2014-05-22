package pe1314.g11.pr3;

import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.sga.PermutationChromosome;

public class LispChromosome extends Chromosome<LispChromosome> {

    private final LispList list;

    public LispChromosome (LispList l) {
        list = l;
    }

    public static LispChromosome newRandom (Random random) {
        return new LispChromosome(LispUtils.generateRandom(random, 0));
    }
    
    public LispList getLispList(){
        return list;
    }

    @Override
    public int getMutationPlaces () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMutationTypes () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public LispChromosome getMutated (int type, int place, int length) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCombinationPlaces () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getCombinationTypes () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public LispChromosome getCombined (LispChromosome other, int type, int place, int length) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public int hashCode () {
        return list.hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        return list.equals(obj);
    }
    
    @Override
    public String toString () {
        return list.toString();
    }
}
