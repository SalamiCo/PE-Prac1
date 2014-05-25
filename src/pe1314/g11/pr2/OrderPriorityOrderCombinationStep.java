package pe1314.g11.pr2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;
import pe1314.g11.sga.PermutationChromosome;
import pe1314.g11.util.PermutationUtils;

public final class OrderPriorityOrderCombinationStep<V> implements SolverStep<V,PermutationChromosome> {

    private final double probability;

    public OrderPriorityOrderCombinationStep (final double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public void apply (
        final Problem<V,PermutationChromosome> problem, final List<PermutationChromosome> input, final Random random,
        final int generation, final List<PermutationChromosome> output)
    {
        final Iterator<PermutationChromosome> it = input.iterator();
        while (it.hasNext()) {
            final PermutationChromosome a = it.next();

            if (it.hasNext()) {
                final PermutationChromosome b = it.next();

                if (random.nextDouble() < probability) {
                    final int place = random.nextInt(a.getCombinationPlaces());
                    int p2 = place;
                    while (place == p2) {
                        p2 = random.nextInt(a.getCombinationPlaces());
                    }

                    final List<Integer> selected =
                        selectRandom(a.getPermutation().size(), a.getPermutation().size() / 3, random);
                    output.add(performCombination(a, b, random, selected));
                    output.add(performCombination(b, a, random, selected));

                } else {
                    output.add(a);
                    output.add(b);
                }

            } else {
                output.add(a);
            }
        }
    }

    private static
        PermutationChromosome performCombination (
            final PermutationChromosome a, final PermutationChromosome b, final Random random,
            final List<Integer> selected)
    {
        final List<Integer> newPerm = new ArrayList<>(a.getPermutation());
        Collections.sort(selected);

        // Null out the numbers
        for (final Integer idx : selected) {
            newPerm.set(a.getPermutation().indexOf(b.getPermutation().get(idx.intValue())), null);
        }

        // Fill them again
        for (final Integer idx : selected) {
            newPerm.set(newPerm.indexOf(null), b.getPermutation().get(idx.intValue()));
        }

        return new PermutationChromosome(newPerm);
    }

    private static List<Integer> selectRandom (final int size, final int spsize, final Random random) {
        final List<Integer> nums = PermutationUtils.firstN(size);
        Collections.shuffle(nums, random);
        return nums.subList(0, spsize);
    }
}
