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
import pe1314.g11.util.Permutations;

public class HeuristicMutationStep<V> implements SolverStep<V,PermutationChromosome> {
    private static final int NMIN = 3;
    private static final int NMAX = 5;
    private static final List<Integer> NUMS = new ArrayList<>();

    private final double probability;

    public HeuristicMutationStep (double probability) {
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
        PermutationChromosome chromo, Problem<V,PermutationChromosome> problem, Random random)
    {
        Comparator<PermutationChromosome> comp = new FitnessComparator<>(problem);

        List<Integer> operm = chromo.getPermutation();
        List<Integer> perm = new ArrayList<>(operm);
        final int spsize = Math.min(NMAX, random.nextInt(perm.size() - NMIN) + NMIN);

        PermutationChromosome best = null;

        List<Integer> elems = selectRandom(perm.size(), spsize);
        Iterable<List<Integer>>permutations = Permutations.permutations(elems);

        for (List<Integer> elemPerm : permutations) {
            for (int i = 0; i < spsize; i++) {
                perm.set(elems.get(i).intValue(), operm.get(elemPerm.get(i).intValue()));
            }

            PermutationChromosome curr = new PermutationChromosome(perm);
            if (best == null || comp.compare(curr, best) < 0) {
                best = curr;
            }
        }

        return best;
    }

    private static List<Integer> selectRandom (int size, int spsize) {
        while (NUMS.size() < size) {
            NUMS.add(Integer.valueOf(NUMS.size()));
        }

        List<Integer> nums = new ArrayList<>(NUMS.subList(0, size));
        Collections.shuffle(nums);
        return nums.subList(0, spsize);
    }


}
