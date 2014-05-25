package pe1314.g11.pr2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;
import pe1314.g11.sga.PermutationChromosome;
import pe1314.g11.util.FitnessComparator;
import pe1314.g11.util.PermutationUtils;

public class HeuristicMutationStep<V> implements SolverStep<V,PermutationChromosome> {
    private static final int NMIN = 3;
    private static final int NMAX = 5;

    private final double probability;

    public HeuristicMutationStep (final double probability) {
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
        // For every input chromosome...
        for (PermutationChromosome chromo : input) {
            // For every mutable place...
            for (int i = 0; i < chromo.getMutationPlaces(); i++) {
                // Should we mutate this place?
                if (random.nextDouble() < probability) {
                    chromo = performMutation(chromo, problem, random);
                }
            }

            output.add(chromo);
        }
    }

    private PermutationChromosome performMutation (
        final PermutationChromosome chromo, final Problem<V,PermutationChromosome> problem, final Random random)
    {
        final Comparator<PermutationChromosome> comp = new FitnessComparator<>(problem);

        final List<Integer> operm = chromo.getPermutation();
        final List<Integer> perm = new ArrayList<>(operm);
        final int spsize = Math.min(NMAX, random.nextInt(perm.size() - NMIN) + NMIN);

        PermutationChromosome best = null;

        final List<Integer> elems = selectRandom(perm.size(), spsize, random);
        final Iterable<List<Integer>> permutations = PermutationUtils.permutations(elems);

        for (final List<Integer> elemPerm : permutations) {
            for (int i = 0; i < spsize; i++) {
                perm.set(elems.get(i).intValue(), operm.get(elemPerm.get(i).intValue()));
            }

            final PermutationChromosome curr = new PermutationChromosome(perm);
            if (best == null || comp.compare(curr, best) < 0) {
                best = curr;
            }
        }

        return best;
    }

    private static List<Integer> selectRandom (final int size, final int spsize, final Random random) {
        final List<Integer> nums = PermutationUtils.firstN(size);
        Collections.shuffle(nums, random);
        return nums.subList(0, spsize);
    }

}
