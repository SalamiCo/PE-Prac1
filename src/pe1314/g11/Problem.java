package pe1314.g11;

import java.util.Random;

/**
 * Representation of a genetic problem.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <C> Type of the chromosomes used in this problem
 */
public abstract class Problem<V, C extends Chromosome<C>> {

    /**
     * Generate a chromosome that can be used for this problem
     *
     * @param random The random number generator that should be used
     * @return A new random chromosome for this problem
     */
    public abstract C random (Random random);

    /**
     * Retrieve the value represented by a given chromosome.
     *
     * @return The value represented by a chromosome
     */
    public abstract V value (C chromosome);

    /**
     * Obtains the fitness value from a given chromosome.
     *
     * @param chromosome Chromosome to get the fitness from
     * @return The fitness of the given chromosome
     */
    public abstract double fitness (C chromosome);

    /**
     * Obtains the kind of problem we are trying to solve.
     *
     * @return Kind of problem being solved.
     */
    public abstract Type type ();

    /**
     * The kind of problem we are trying to solve.
     *
     * @author Daniel Escoz Solana
     * @author Pedro Morgado Alarcón
     */
    public static enum Type {
        /** This problem tries to maximize fitness */
        MAXIMIZATION(-1),

        /** This problem tries to minimize fitness */
        MINIMIZATION(1);

        private final int mult;

        private Type (int mult) {
            this.mult = mult;
        }

        /** @return A number to multiply a <tt>compare</tt> method result for correct comparison */
        public int getCompareMultiplier () {
            return mult;
        }
    }
}
