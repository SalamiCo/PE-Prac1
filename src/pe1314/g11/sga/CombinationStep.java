package pe1314.g11.sga;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

/**
 * A problem solver step that combines binary chromosomes with a given probability on a random combination point.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 */
public final class CombinationStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    private final double probability;

    private final int type;

    public CombinationStep (double probability, int type) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
        this.type = type;
    }

    @Override
    public void apply (Problem<V,C> problem, List<C> input, Random random, int generation, List<C> output) {

        Iterator<C> it = input.iterator();
        while (it.hasNext()) {
            C a = it.next();

            if (it.hasNext()) {
                C b = it.next();

                if (random.nextDouble() < probability) {
                    int place = random.nextInt(a.getCombinationPlaces());
                    int p2 = place;
                    while (place == p2) {
                        p2 = random.nextInt(a.getCombinationPlaces());
                    }

                    output.add(a.getCombined(b, type, place, p2 - place));
                    output.add(b.getCombined(a, type, place, p2 - place));

                } else {
                    output.add(a);
                    output.add(b);
                }

            } else {
                output.add(a);
            }
        }
    }

}
