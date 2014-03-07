package pe1314.g11;

import java.util.Comparator;

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
        return (int) Math.signum(problem.fitness(c1) - problem.fitness(c2));
    }

}
