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
 * @author Pedro Morgado Alarc&oacute;n
 * @param <C> Type of the chromosomes
 */
public final class FitnessComparator<C extends Chromosome<C>> implements Comparator<C> {

    private final Problem<?,C> problem;

    public FitnessComparator (final Problem<?,C> problem) {
        this.problem = problem;
    }

    @Override
    public int compare (final C c1, final C c2) {
        final double f1 = problem.fitness(c1);
        final double f2 = problem.fitness(c2);
        return problem.type().getCompareMultiplier() * (int) Math.signum(f1 - f2);
    }
}
