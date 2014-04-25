package pe1314.g11.sga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pe1314.g11.Chromosome;

public final class PermutationChromosome extends Chromosome<PermutationChromosome> {

    private final List<Integer> permutation;

    public PermutationChromosome (List<Integer> permutation) {
        this.permutation = Collections.unmodifiableList(new ArrayList<>(permutation));
    }

    @Override
    public int getMutationPlaces () {
        return permutation.size();
    }

    @Override
    public int getMutationTypes () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public PermutationChromosome getMutated (int type, int place) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCombinationPlaces () {
        return permutation.size() - 1;
    }

    @Override
    public int getCombinationTypes () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public PermutationChromosome getCombined (PermutationChromosome other, int type, int place) {
        // TODO Auto-generated method stub
        return null;
    }

}
