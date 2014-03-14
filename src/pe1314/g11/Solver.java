package pe1314.g11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pe1314.g11.util.XorShiftRandom;

/**
 * A class representing the general algorithm for solving genetic problems, with a configurable "pipeline"for different
 * algorithm variations.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values of the solved problem
 * @param <C> Type of the chromosomes to be processed
 */
public final class Solver<V, C extends Chromosome<C>> {

    /** The problem being solved */
    private final Problem<V,C> problem;

    /** List of steps to take for a population */
    private final List<SolverStep<V,C>> steps;

    /**
     * Creates a problem solver that will use the given steps.
     *
     * @param steps Steps to be applied to each generation
     */
    Solver (Problem<V,C> problem, List<SolverStep<V,C>> steps) {
        this.problem = problem;
        this.steps = steps;
    }

    // ==============================
    // === PUBLIC SOLVING METHODS ===

    /**
     * Solves a specific problem using this solver.
     *
     * @param problem Problem to solve
     * @param random The random generator to use
     * @return The best value seen
     */
    public V solve (Random random) {
        return problem.value(doTrace(random, false, null).getBestSeen());
    }

    /**
     * Solves a specific problem using this solver.
     * <p>
     * This method creates a new {@link Random} object using its default constructor. Use
     * {@link #solve(Problem, Random)} to pick a specific random generator.
     *
     * @param problem Problem to solve
     * @return The best value seen
     */
    public V solve () {
        return solve(new XorShiftRandom());
    }

    /**
     * Solves a specific problem using this solver and returns a trace of the progress.
     *
     * @param problem Problem to solve
     * @param random The random generator to use
     * @return The problem solution trace
     */
    public SolverTrace<V,C> trace (Random random) {
        return doTrace(random, true, null);
    }

    /**
     * Solves a specific problem using this solver and returns a trace of the progress.
     * <p>
     * This method creates a new {@link Random} object using its default constructor. Use
     * {@link #trace(Problem, Random)} to pick a specific random generator.
     *
     * @param problem Problem to solve
     * @return The problem solution trace
     */
    public SolverTrace<V,C> trace () {
        return trace(new Random());
    }

    // ===============================
    // === INTERNAL IMPLEMENTATION ===

    private SolverTrace<V,C> doTrace (Random random, boolean traceOptions, Solver.Callbacks<V,C> callbacks)
    {
        SolverTrace<V,C> trace = new SolverTrace<V,C>(problem);

        List<C> population = new ArrayList<C>();
        List<C> buffer = new ArrayList<C>();

        int gen = 0;
        while (gen < 1024) {

            // Apply every step
            long time = System.nanoTime();
            for (SolverStep<V,C> step : steps) {
                List<C> input = population;
                List<C> output = buffer;
                output.clear();

                step.apply(problem, Collections.unmodifiableList(input), random, gen, output);

                // Swap both
                population = output;
                buffer = input;
            }
            time = System.nanoTime() - time;

            // Update generation
            gen++;
            trace.generation(population, time);
        }

        return trace;
    }

    // ===============
    // === BUILDER ===

    /**
     * Creates and returns a new {@link Solver.Builder}
     *
     * @return A new <tt>Builder</tt>
     */
    public static <V, C extends Chromosome<C>> Builder<V,C> builder (Problem<V,C> problem) {
        return new Builder<V,C>(problem);
    }

    /**
     * Fluent interface for creating {@link Solver ProblemSolvers}.
     *
     * @author Daniel Escoz Solana
     * @author Pedro Morgado Alarcón
     * @param <V> Type of the values of the solved problem
     * @param <C> Type of the chromosomes to be processed
     */
    public static final class Builder<V, C extends Chromosome<C>> {

        /** Problem to solve */
        private final Problem<V,C> problem;

        /** List of steps */
        private List<SolverStep<V,C>> steps = new ArrayList<SolverStep<V,C>>(4);

        /** Creates a new problem solver builder */
        /* package */Builder (Problem<V,C> problem) {
            this.problem = problem;
        }

        /**
         * Adds a step to this builder.
         * <p>
         * Steps are added in order, so they are applied in the order added here.
         *
         * @param step Step to be added to this builder
         * @return <tt>this</tt>
         */
        public Builder<V,C> step (SolverStep<V,C> step) {
            if (steps == null) {
                throw new IllegalStateException("already used");
            }

            steps.add(step);
            return this;
        }

        /**
         * Constructs a {@link Solver} using the steps specified in this builder.
         *
         * @return A new <tt>ProblemSolver</tt> that uses the specified steps.
         */
        public Solver<V,C> build () {
            if (steps == null) {
                throw new IllegalStateException("already used");
            }

            List<SolverStep<V,C>> steps = this.steps;
            this.steps = null;
            return new Solver<V,C>(problem, Collections.unmodifiableList(steps));
        }
    }

    // =======================
    // === TRACE CALLBACKS ===

    /**
     * Callbacks interface with methods called by the tracer when certains events occur.
     *
     * @author Daniel Escoz Solana
     * @author Pedro Morgado Alarcón
     * @param <V> Type of the values of the solved problem
     * @param <C> Type of the chromosomes to be processed
     */
    public interface Callbacks<V, C extends Chromosome<C>> {

    }
}
