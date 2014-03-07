package pe1314.g11.sga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

/**
 * A problem solver step that combines binary chromosomes with a given probability on a random combination point.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 */
public final class BinaryCombinationStep<V> implements SolverStep<V,BinaryChromosome> {

    private final double probability;

    public BinaryCombinationStep (double probability) {
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

        Iterator<BinaryChromosome> it = input.iterator();
        while (it.hasNext()) {
            BinaryChromosome a = it.next();

            if (it.hasNext()) {
                BinaryChromosome b = it.next();

                if (random.nextDouble() < probability) {
                    int place = random.nextInt(a.getCombinationPlaces());

                    output.add(a.getCombined(b, place));
                    output.add(b.getCombined(a, place));

                } else {
                    output.add(a);
                    output.add(b);
                }

            } else {
                output.add(a);
            }
        }

        return Collections.unmodifiableList(output);
    }

}
