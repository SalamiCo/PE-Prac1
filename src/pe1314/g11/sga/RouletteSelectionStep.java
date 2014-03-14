package pe1314.g11.sga;

import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

/**
 * A step that implements the selection of chromosomes using the roulette mechanism.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public final class RouletteSelectionStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    @Override
    public void apply (Problem<V,C> problem, List<C> input, Random random, int generation, List<C> output) {
        // An array of accumulated fitness
        double[] accs = new double[input.size()];

        // Obtain the sum of the finesses and the accumulated
        double fitnessSum = 0;
        for (int i = 0; i < input.size(); i++) {
            fitnessSum += problem.fitness(input.get(i));
            accs[i] = fitnessSum;
        }

        // Make the selection
        for (int i = 0; i < input.size(); i++) {
            double rnd = random.nextDouble() * fitnessSum;

            C selected = null;
            for (int j = 0; j < accs.length && selected == null; j++) {
                if (rnd < accs[j]) {
                    selected = input.get(j);
                }
            }

            if (selected == null) {
                // Double imprecision, none was selected
                selected = input.get(random.nextInt(input.size()));
            }

            output.add(selected);
        }
    }

}
