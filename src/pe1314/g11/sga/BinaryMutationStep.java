package pe1314.g11.sga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

/**
 * A problem solver step that mutates binary chromosomes with a given probability on a random mutation point.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 */
public final class BinaryMutationStep<V> implements SolverStep<V,BinaryChromosome> {

    private final double probability;

    public BinaryMutationStep (double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public List<BinaryChromosome> apply (
        Problem<V,BinaryChromosome> problem, List<BinaryChromosome> input, Random random, int generation)
    {
        List<BinaryChromosome> output = new ArrayList<BinaryChromosome>(input.size());

        // For every input chromosome...
        for (BinaryChromosome chromo : input) {
            // For every mutable place...
            for (int i = 0; i < chromo.getLength(); i++) {
                // Should we mutate this place?
                if (random.nextDouble() < probability) {
                    chromo = chromo.getMutated(i);
                }
            }

            output.add(chromo);
        }

        return Collections.unmodifiableList(output);
    }

}
