package pe1314.g11.sga;

import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

public class LengthedMutationStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    private final double probability;

    private final int type;

    public LengthedMutationStep (final double probability, final int type) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
        this.type = type;
    }

    @Override
    public
        void apply (
            final Problem<V,C> problem, final List<C> input, final Random random, final int generation,
            final List<C> output)
    {
        // For every input chromosome...
        for (C chromo : input) {
            if (random.nextDouble() < probability) {
                final int p1 = random.nextInt(chromo.getMutationPlaces());

                int p2 = p1;
                while (p1 == p2) {
                    p2 = random.nextInt(chromo.getMutationPlaces());
                }

                chromo = chromo.getMutated(type, p1, p2 - p1);
            }

            output.add(chromo);
        }
    }

}
