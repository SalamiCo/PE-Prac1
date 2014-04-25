package pe1314.g11.sga;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;

public final class PermutationChromosome extends Chromosome<PermutationChromosome>
    implements Comparable<PermutationChromosome>
{

    private final List<Integer> permutation;

    public PermutationChromosome (List<Integer> permutation) {
        this.permutation = Collections.unmodifiableList(new ArrayList<>(permutation));

        BitSet seen = new BitSet();
        for (Integer i : permutation) {
            if (i.intValue() < 0 || i.intValue() >= permutation.size() || seen.get(i.intValue())) {
                throw new IllegalArgumentException("not a parmutation: " + permutation);
            }

            seen.set(i.intValue());
        }
    }

    public static PermutationChromosome newRandom (int length, Random random) {
        List<Integer> nums = new ArrayList<Integer>();
        for (int i = 0; i < length; i++) {
            nums.add(Integer.valueOf(i));
        }

        Collections.shuffle(nums, random);
        return new PermutationChromosome(nums);
    }

    public List<Integer> getPermutation () {
        return permutation;
    }

    @Override
    public int getMutationPlaces () {
        return permutation.size();
    }

    @Override
    public int getMutationTypes () {
        return 2;
    }

    @Override
    public PermutationChromosome getMutated (int type, int place, int length) {
        switch (type) {
            case 0:
                return getInversionMutated(place, length);
            case 1:
                return getExchangeMutated(place, length);
        }

        throw new IllegalArgumentException("Invalid muration type " + type);
    }

    private PermutationChromosome getInversionMutated (int place, int length) {
        final List<Integer> newPerm = new ArrayList<>(permutation);

        int i = Math.min(place, place + length);
        int j = Math.max(place, place + length);

        while (i < j) {
            newPerm.set(i, permutation.get(j));
            newPerm.set(j, permutation.get(i));

            i++;
            j--;
        }

        return new PermutationChromosome(newPerm);
    }

    private PermutationChromosome getExchangeMutated (int place, int length) {
        List<Integer> newPerm = new ArrayList<>(permutation);
        
        newPerm.set(place, permutation.get(place + length));
        newPerm.set(place + length, permutation.get(place));
        
        return new PermutationChromosome(newPerm);
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
        return this;
    }

    @Override
    public int hashCode () {
        return permutation.hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof PermutationChromosome)) {
            return false;
        }

        PermutationChromosome pc = (PermutationChromosome) obj;

        return permutation.equals(pc.permutation);
    }

    @Override
    public String toString () {
        return permutation.toString();
    }

    @Override
    public int compareTo (PermutationChromosome other) {
        final int length = Math.min(permutation.size(), other.permutation.size());

        for (int i = 0; i < length; i++) {
            int i1 = permutation.get(i).intValue();
            int i2 = other.permutation.get(i).intValue();

            if (i1 != i2) {
                return i1 - i2;
            }
        }

        return permutation.size() - other.permutation.size();
    }

}
