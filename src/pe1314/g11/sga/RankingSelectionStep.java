package pe1314.g11.sga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;
import pe1314.g11.util.FitnessComparator;

/**
 * A step that implements the selection of chromosomes using the ranking mechanism.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarc&oacute;n
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public class RankingSelectionStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    private final double beta = 2.0;

    @Override
    public void apply (Problem<V,C> problem, List<C> input, Random random, int generation, List<C> output) {
        List<C> sorted = new ArrayList<>(input);
        Collections.sort(sorted, new FitnessComparator<>(problem));

        // An array of accumulated fitness
        double[] accs = new double[input.size()];

        // Obtain the sum of the fitnesses and the accumulated
        double acc = 0;
        for (int i = 0; i < input.size(); i++) {
            double prob = (double) i / input.size();
            prob = prob * 2.0 * (beta - 1);
            prob = beta - prob;
            prob = prob * (1.0 / input.size());
            
            acc += prob;
            accs[i] = acc;
        }

        // Make the selection
        for (int i = 0; i < input.size(); i++) {
            double rnd = random.nextDouble() * acc;

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
