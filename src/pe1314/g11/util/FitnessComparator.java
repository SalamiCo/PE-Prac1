package pe1314.g11.util;

import java.util.Comparator;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;

/**
 * A comparator that sorts chromosomes based on their fitness.
 * <p>
 * Objects of this class need a {@link Problem} instance to know with fitness function to use.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <C> Type of the chromosomes
 */
public final class FitnessComparator<C extends Chromosome<C>> implements Comparator<C> {

    private final Problem<?,C> problem;

    public FitnessComparator (Problem<?,C> problem) {
        this.problem = problem;
    }

    @Override
    public int compare (C c1, C c2) {
        double f1 = problem.fitness(c1);
        double f2 = problem.fitness(c2);
        return problem.type().getCompareMultiplier() * (int) Math.signum(f1 - f2);
    }
}
