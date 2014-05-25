package pe1314.g11.sga;

import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

/**
 * A problem solver step that mutates binary chromosomes with a given probability on a random mutation point.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarc&oacute;n
 * @param <V> Type of the values
 */
public final class MultiMutationStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    private final double probability;

    private final int type;

    public MultiMutationStep (final double probability, final int type) {
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
            // For every mutable place...
            for (int i = 0; i < chromo.getMutationPlaces(); i++) {
                // Should we mutate this place?
                if (random.nextDouble() < probability) {
                    chromo = chromo.getMutated(type, i, 1);
                }
            }

            output.add(chromo);
        }
    }

}
