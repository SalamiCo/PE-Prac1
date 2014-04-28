package pe1314.g11.sga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import pe1314.g11.Chromosome;
import pe1314.g11.util.PermutationUtils;
import pe1314.g11.util.XorShiftRandom;

public final class PermutationChromosome extends Chromosome<PermutationChromosome>
    implements Comparable<PermutationChromosome>
{
    public static final int MUTATION_INVERSION = 0;
    public static final int MUTATION_EXCHANGE = 1;
    public static final int MUTATION_INSERTION = 2;
    public static final int MUTATION_ROTATION = 3;

    public static final int COMBINATION_PMX = 0;
    public static final int COMBINATION_OX = 1;
    public static final int COMBINATION_CX = 2;
    public static final int COMBINATION_ORDCOD = 3;
    public static final int COMBINATION_RECOMB = 4;

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
        List<Integer> nums = PermutationUtils.firstN(length);

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
        return 4;
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
            case MUTATION_ROTATION:
                return getRotationMutated(place, length);
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

    private PermutationChromosome getRotationMutated (int place, int length) {
        final List<Integer> newPerm = new ArrayList<>(permutation);

        int alen = (length < 0) ? length + permutation.size() : length;

        for (int i = 0; i < alen; i++) {
            newPerm.add(newPerm.remove(0));
        }

        return new PermutationChromosome(newPerm);
    }

    @Override
    public int getCombinationPlaces () {
        return permutation.size() - 1;
    }

    @Override
    public int getCombinationTypes () {
        return 5;
    }

    @Override
    public PermutationChromosome getCombined (PermutationChromosome other, int type, int place, int length) {
        switch (type) {
            case COMBINATION_PMX:
                return getPmxCombined(other, place, length);
            case COMBINATION_OX:
                return getOxCombined(other, place, length);
            case COMBINATION_CX:
                return getCxCombined(other, place, length);
            case COMBINATION_ORDCOD:
                return getOrdCodCombined(other, place, length);
            case COMBINATION_RECOMB:
                return getRecombCombined(other, place, length);
        }

        throw new IllegalArgumentException("Invalid muration type " + type);
    }

    private PermutationChromosome getOrdCodCombined (PermutationChromosome other, int place, int length) {
        int[] codThis = ordinalEncode(this);
        int[] codOther = ordinalEncode(other);

        for (int i = place; i < permutation.size(); i++) {
            codThis[i] = codOther[i];
        }

        return ordinalDecode(codThis);
    }

    private static int[] ordinalEncode (PermutationChromosome chromo) {
        int[] cod = new int[chromo.permutation.size()];
        List<Integer> nums = PermutationUtils.firstN(cod.length);

        for (int i = 0; i < cod.length; i++) {
            cod[i] = nums.indexOf(chromo.permutation.get(i));
            nums.remove(cod[i]);
        }

        return cod;
    }

    private static PermutationChromosome ordinalDecode (int[] cod) {
        List<Integer> nums = PermutationUtils.firstN(cod.length);
        List<Integer> perm = new ArrayList<>();

        for (int i = 0; i < cod.length; i++) {
            perm.add(nums.remove(cod[i]));
        }

        return new PermutationChromosome(perm);
    }

    private PermutationChromosome getPmxCombined (PermutationChromosome other, int place, int length) {
        List<Integer> newPerm = new ArrayList<>(permutation);

        final int li = Math.min(place, place + length);
        final int ri = Math.max(place, place + length);

        // Get the other range into the new perm
        for (int i = li; i <= ri; i++) {
            newPerm.set(i, other.permutation.get(i));
        }

        // For every non-range number: if in the range, swap for the other
        List<Integer> subPerm = newPerm.subList(li, ri + 1);
        for (int i = 0; i < newPerm.size(); i++) {
            // Skip the range
            if (i == li) {
                i = ri;
                continue;
            }

            // Check if in the range
            while (subPerm.contains(newPerm.get(i))) {
                newPerm.set(i, permutation.get(li + subPerm.indexOf(newPerm.get(i))));
            }
        }

        return new PermutationChromosome(newPerm);

    }

    private PermutationChromosome getOxCombined (PermutationChromosome other, int place, int length) {
        List<Integer> newPerm = new ArrayList<>(permutation);

        final int li = Math.min(place, place + length);
        final int ri = Math.max(place, place + length);

        for (int i = li; i <= ri; i++) {
            newPerm.set(i, other.permutation.get(i));
        }

        int pos = ri + 1;
        int i = pos;
        List<Integer> subPerm = newPerm.subList(li, ri + 1);
        while (pos != li) {
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

        try {
            return new PermutationChromosome(newPerm);
        } catch (Exception e) {
            throw e;
        }
    }

    private PermutationChromosome getCxCombined (PermutationChromosome other, int place, int length) {
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

    private PermutationChromosome getRecombCombined (PermutationChromosome other, int place, int length) {
        Random random = new XorShiftRandom(other.hashCode());

        // Create an empty table
        List<Set<Integer>> table = new ArrayList<>();
        for (int i = 0; i < other.permutation.size(); i++) {
            Set<Integer> neighs = new HashSet<>();
            table.add(neighs);
        }

        // Fill the table
        for (int i = 0; i < permutation.size(); i++) {
            Integer ii = PermutationUtils.wrapInt(i);

            int idxt = permutation.indexOf(ii);
            int idxtr = (idxt + 1) % permutation.size();
            int idxtl = (idxt + permutation.size() - 1) % permutation.size();

            int idxo = other.permutation.indexOf(ii);
            int idxor = (idxo + 1) % other.permutation.size();
            int idxol = (idxo + other.permutation.size() - 1) % other.permutation.size();

            table.get(i).addAll(Arrays.asList(//
                permutation.get(idxtl), permutation.get(idxtr), //
                other.permutation.get(idxol), other.permutation.get(idxor) //
                ));
        }

        // Start the process
        int tries = 0;
        while (tries < 100) {
            tries++;

            List<Integer> perm = new ArrayList<>();
            perm.add(other.permutation.get(0));

            while (perm.size() < permutation.size()) {
                Integer last = perm.get(perm.size() - 1);

                // Get the neighbours (unused)
                Set<Integer> neighs = new HashSet<>(table.get(last.intValue()));
                neighs.removeAll(perm);

                // Stall?
                if (neighs.isEmpty()) {
                    break;
                }

                // Select next
                List<Integer> mins = new ArrayList<>();
                for (Integer neigh : neighs) {
                    if (mins.isEmpty() || table.get(neigh.intValue()).size() < table.get(mins.get(0).intValue()).size())
                    {
                        mins.clear();
                        mins.add(neigh);
                    } else if (table.get(neigh.intValue()).size() == table.get(mins.get(0).intValue()).size()) {
                        mins.add(neigh);
                    }
                }

                // Select a min
                perm.add(mins.get(random.nextInt(mins.size())));
            }

            if (perm.size() == permutation.size()) {
                return new PermutationChromosome(perm);
            }
        }

        System.err.println("Recombination Stalled! Skipping...");
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
