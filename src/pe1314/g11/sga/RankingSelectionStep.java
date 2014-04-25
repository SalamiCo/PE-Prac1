package pe1314.g11.sga;

import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

/**
 * A step that implements the selection of chromosomes using the ranking mechanism.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public class RankingSelectionStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    @Override
    public void apply (Problem<V,C> problem, List<C> input, Random random, int generation, List<C> output) {
        output.addAll(input);
    }
}
