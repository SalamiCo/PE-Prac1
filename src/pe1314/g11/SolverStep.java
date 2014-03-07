package pe1314.g11;

import java.util.List;
import java.util.Random;

/**
 * Represents a step of a genetic algorithm.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <C> Type of the chromosomes
 */
public interface SolverStep<V, C extends Chromosome<C>> {

    /**
     * Processes the <tt>input</tt> chromosome population and returns a new chromosome population.
     *
     * @param problem Problem being solved
     * @param input Original population to be processed
     * @param random A random object for random processing
     * @param generation The generation of the
     * @return Population after processing
     */
    public abstract List<C> apply (Problem<V,C> problem, List<C> input, Random random, int generation);
}
