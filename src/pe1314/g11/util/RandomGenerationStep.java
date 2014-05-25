package pe1314.g11.util;

import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

/**
 * A problem solver step that generates random chromosomes for a given problem until a minimum population size is
 * reached.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarc&oacute;n
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public final class RandomGenerationStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    /** The minimum size of the generated population */
    private final int populationSize;

    /** The minimum number of generated chromosomes */
    private final int minInserted;

    /**
     * @param minPopulationSize Minimum size of the generated population
     * @param chromosomeLength Length of the generated chromosomes
     */
    public RandomGenerationStep (final int minPopulationSize, final int minInserted) {
        this.populationSize = minPopulationSize;
        this.minInserted = minInserted;
    }

    @Override
    public
        void apply (
            final Problem<V,C> problem, final List<C> input, final Random random, final int generation,
            final List<C> output)
    {
        output.addAll(input);

        final int inserted = 0;

        // Keep generating until population is large enough
        while (output.size() < populationSize || inserted < minInserted) {
            output.add(problem.random(random));
        }
    }
}
