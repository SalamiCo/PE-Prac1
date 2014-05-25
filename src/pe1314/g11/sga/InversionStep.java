package pe1314.g11.sga;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;
import pe1314.g11.util.FitnessComparator;

public class InversionStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    private final double probability;

    public InversionStep (final double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public
        void apply (
            final Problem<V,C> problem, final List<C> input, final Random random, final int generation,
            final List<C> output)
    {
        final Comparator<C> comp = new FitnessComparator<>(problem);

        // For every input chromosome...
        for (C chromo : input) {
            if (random.nextDouble() < probability) {
                final int p1 = random.nextInt(chromo.getMutationPlaces());

                int p2 = p1;
                while (p1 == p2) {
                    p2 = random.nextInt(chromo.getMutationPlaces());
                }

                final C newChromo = chromo.getMutated(PermutationChromosome.MUTATION_INVERSION, p1, p2 - p1);

                if (comp.compare(newChromo, chromo) < 0) {
                    chromo = newChromo;
                }
            }

            output.add(chromo);
        }

    }

}
