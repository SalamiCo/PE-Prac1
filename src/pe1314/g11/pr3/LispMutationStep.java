package pe1314.g11.pr3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

public class LispMutationStep implements SolverStep<LispList,LispChromosome> {
    private final double probability;

    public LispMutationStep (final double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public void apply (
        final Problem<LispList,LispChromosome> problem, final List<LispChromosome> input, final Random random,
        final int generation, final List<LispChromosome> output)
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

    private LispChromosome performMutation (final LispChromosome chromo, final Random random) {
        return new LispChromosome(mutate(chromo.getLispList(), random, 4, 0.0));
    }

    private LispList mutate (final LispValue lv, final Random random, final int depth, final double p) {
        if (lv instanceof LispTerminal || random.nextDouble() < p) {
            return LispUtils.generateRandom(random, depth);
        }

        final LispList list = (LispList) lv;
        final int pos = random.nextInt(list.size());

        final List<LispValue> values = new ArrayList<>(list.values());
        values.set(pos, mutate(list.get(pos), random, depth - 1, p + (1.0 - p) * 0.3));

        return new LispList(values);
    }

}
