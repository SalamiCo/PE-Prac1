package pe1314.g11;

/**
 * Class that represents the process of finding a solution executed within a solver.
 * <p>
 * This class is internally mutable, but clients do not have access to mutating methods, and it will not be mutated
 * after being returned. This class can be considered immutable from the eyes of clients.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public final class SolverTrace<V, C extends Chromosome<C>> {

    /** The problem being traced */
    private Problem<V,C> problem;

    /** The best chromosome seen */
    private C best = null;

    /** The current generation */
    private int generation = -1;

    public SolverTrace (Problem<V,C> problem) {
        if (problem == null) {
            throw new NullPointerException("problem");
        }

        this.problem = problem;
    }

    /**
     * Logs the beginning of a generation.
     *
     * @param num Generation number
     * @return <tt>this</tt>
     */
    /* package */SolverTrace<V,C> generation (int num) {
        if (num != generation + 1) {
            throw new IllegalStateException("Generations must progress linearly (was "
                + generation + ", given " + num + ")");
        }

        this.generation = num;
        return this;
    }

    /**
     * Gets the best chromosome seen by this trace.
     *
     * @return The best chromosome seen by this trace
     */
    public C getBestSeen () {
        return best;
    }
}
