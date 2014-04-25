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
    public static final int MUTATION_INVERSION = 0;
    public static final int MUTATION_EXCHANGE = 1;
    public static final int MUTATION_INSERTION = 2;

    public static final int COMINATION_PMX = 0;
    public static final int COMINATION_OX = 1;
    public static final int COMINATION_CYCLES = 2;

    private final List<Integer> permutation;

    public PermutationChromosome (List<Integer> permutation) {
        this.permutation = Collections.unmodifiableList(new ArrayList<>(permutation));

        BitSet seen = new BitSet();
        for (Integer i : permutation) {
            if (i.intValue() < 0 || i.intValue() >= permutation.size() || seen.get(i.intValue())) {
                throw new IllegalArgumentException("not a permutation: " + permutation);
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
        return 3;
    }

    @Override
    public PermutationChromosome getMutated (int type, int place, int length) {
        switch (type) {
            case MUTATION_INVERSION:
                return getInversionMutated(place, length);
            case MUTATION_EXCHANGE:
                return getExchangeMutated(place, length);
            case MUTATION_INSERTION:
                return getInsertionMutated(place, length);
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

    private PermutationChromosome getInsertionMutated (int place, int length) {
        final List<Integer> newPerm = new ArrayList<>(permutation);

        newPerm.add(place + length, newPerm.remove(place));

        return new PermutationChromosome(newPerm);
    }

    @Override
    public int getCombinationPlaces () {
        return permutation.size() - 1;
    }

    @Override
    public int getCombinationTypes () {
        return 1;
    }

    @Override
    public PermutationChromosome getCombined (PermutationChromosome other, int type, int place, int length) {
        switch (type) {
            case COMINATION_PMX:
                return getCombinedPmx(other, place, length);
            case COMINATION_OX:
                return getCombinedOx(other, place, length);
            case COMINATION_CYCLES:
                return getCombinedCycles(other, place, length);
        }

        throw new IllegalArgumentException("Invalid muration type " + type);
    }

    private PermutationChromosome getCombinedPmx (PermutationChromosome other, int place, int length) {
        List<Integer> newPerm = new ArrayList<>(permutation);

        final int li = Math.min(place, place + length);
        final int ri = Math.max(place, place + length);

        // Get the other range into the new perm
        for (int i = li; i <= ri; i++) {
            newPerm.set(i, other.permutation.get(i));
        }

        // For every non-range number: if in the range, swap for the old in that pos; else, skip
        for (int i = 0; i < newPerm.size(); i++) {
            // Skip the range
            if (i == li) {
                i = ri;
                continue;
            }

            // Check if the range
            int curr = newPerm.get(i).intValue();
            for (int j = li; j <= ri; j++) {
                int r = newPerm.get(j).intValue();

                if (curr == r) {
                    // Gotcha! Swap for the old
                    newPerm.set(i, permutation.get(j));
                    break;
                }
            }
        }

        return new PermutationChromosome(newPerm);
    }

    private PermutationChromosome getCombinedOx (PermutationChromosome other, int place, int length) {
        List<Integer> newPerm = new ArrayList<>(permutation);

        final int li = Math.min(place, place + length);
        final int ri = Math.max(place, place + length);

        for (int i = li; i <= ri; i++) {
            newPerm.set(i, other.permutation.get(i));
        }

        int pos = place + length + 1;
        int i = pos;
        List<Integer> subPerm = newPerm.subList(place, place + length + 1);
        while (pos != place) {
            if (!(subPerm.contains(permutation.get(i)))) {
                newPerm.set(pos, permutation.get(i));
                pos++;
            }

            i++;

            if (pos >= permutation.size()) {
                pos = 0;
            }

            if (i >= permutation.size()) {
                i = 0;
            }
        }

        return new PermutationChromosome(newPerm);
    }

    private PermutationChromosome getCombinedCycles (PermutationChromosome other, int place, int length) {
        List<Integer> newPerm = new ArrayList<Integer>(Collections.<Integer>nCopies(permutation.size(), null));

        // First, make a full cycle
        int idx = 0;
        do {
            Integer n = other.permutation.get(idx);
            idx = permutation.indexOf(n);

            newPerm.set(idx, n);
        } while (idx != 0);

        // Then, fill the blanks
        for (int i = 0; i < permutation.size(); i++) {
            if (newPerm.get(i) == null) {
                newPerm.set(i, other.permutation.get(i));
            }
        }

        return new PermutationChromosome(newPerm);
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
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < permutation.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(permutation.get(i).intValue() + 1);
        }

        return sb.append("]").toString();
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
