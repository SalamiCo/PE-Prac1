package pe1314.g11.pr3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

public class LispMutationStep implements SolverStep<LispList,LispChromosome> {
    private static final int NMIN = 3;
    private static final int NMAX = 5;

    private final double probability;

    public LispMutationStep (double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public void apply (
        Problem<LispList,LispChromosome> problem, List<LispChromosome> input, Random random, int generation,
        List<LispChromosome> output)
    {
        // For every input chromosome...
        for (LispChromosome chromo : input) {
            // For every mutable place...
            for (int i = 0; i < chromo.getMutationPlaces(); i++) {
                // Should we mutate this place?
                if (random.nextDouble() < probability) {
                    chromo = performMutation(chromo, random);
                }
            }

            output.add(chromo);
        }
    }

    private LispChromosome performMutation (LispChromosome chromo, Random random) {
        return new LispChromosome(mutate(chromo.getLispList(), random, 4, 0.0));
    }

    private LispList mutate (LispValue lv, Random random, int depth, double p) {
        if (lv instanceof LispTerminal || random.nextDouble() < p) {
            return LispUtils.generateRandom(random, depth);
        }

        LispList list = (LispList) lv;
        int pos = random.nextInt(list.size());

        List<LispValue> values = new ArrayList<>(list.values());
        values.set(pos, mutate(list.get(pos), random, depth - 1, p + (1.0 - p) * 0.3));

        return new LispList(values);
    }

}
