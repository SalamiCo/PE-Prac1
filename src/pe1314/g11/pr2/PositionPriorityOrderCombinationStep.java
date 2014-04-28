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

public final class PositionPriorityOrderCombinationStep<V> implements SolverStep<V,PermutationChromosome> {

    private final double probability;

    public PositionPriorityOrderCombinationStep (double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public void apply (
        Problem<V,PermutationChromosome> problem, List<PermutationChromosome> input, Random random, int generation,
        List<PermutationChromosome> output)
    {
        Iterator<PermutationChromosome> it = input.iterator();
        while (it.hasNext()) {
            PermutationChromosome a = it.next();

            if (it.hasNext()) {
                PermutationChromosome b = it.next();

                if (random.nextDouble() < probability) {
                    int place = random.nextInt(a.getCombinationPlaces());
                    int p2 = place;
                    while (place == p2) {
                        p2 = random.nextInt(a.getCombinationPlaces());
                    }

                    List<Integer> selected =
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

    private static PermutationChromosome performCombination (
        PermutationChromosome a, PermutationChromosome b, Random random, List<Integer> selected)
    {
        List<Integer> newPerm = new ArrayList<>(a.getPermutation());
        Collections.sort(selected);

        // Remove all numbers from a
        for (Integer idx : selected) {
            newPerm.remove(b.getPermutation().get(idx.intValue()));
        }

        // Add them again
        for (Integer idx : selected) {
            newPerm.add(idx.intValue(), b.getPermutation().get(idx.intValue()));
        }

        return new PermutationChromosome(newPerm);
    }

    private static List<Integer> selectRandom (int size, int spsize, Random random) {
        List<Integer> nums = PermutationUtils.firstN(size);
        Collections.shuffle(nums, random);
        return nums.subList(0, spsize);
    }
}
