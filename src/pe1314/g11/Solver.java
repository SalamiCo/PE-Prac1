package pe1314.g11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    /** List of steps to take for a population */
    private final List<SolverStep<V,C>> steps;

    /**
     * Creates a problem solver that will use the given steps.
     *
     * @param steps Steps to be applied to each generation
     */
    Solver (List<SolverStep<V,C>> steps) {
        this.steps = steps;
    }

    /**
     * Solves a specific problem using this solver.
     *
     * @param problem Problem to solve
     * @param random The random generator to use
     * @return The best value seen
     */
    public V solve (Problem<V,C> problem, Random random) {
        return problem.value(doTrace(problem, random, false).getBestSeen());
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
    public V solve (Problem<V,C> problem) {
        return solve(problem, new Random());
    }

    private SolverTrace<V,C> doTrace (Problem<V,C> problem, Random random, boolean traceOptions) {
        SolverTrace<V,C> trace = new SolverTrace<V,C>();
        List<C> population = Collections.emptyList();

        int gen = 0;
        while (gen < 1024) {
            trace.generation(gen);

            // Apply every step
            for (SolverStep<V,C> step : steps) {
                trace.step(step);

                population = step.apply(problem, population, random, gen);

                trace.population(population);
            }

            // Update generation
            gen++;
        }

        return trace;
    }

    /**
     * Creates and returns a new {@link Solver.Builder}
     *
     * @return A new <tt>Builder</tt>
     */
    public static <V, C extends Chromosome<C>> Builder<V,C> builder () {
        return new Builder<V,C>();
    }

    /**
     * Fluent interface for creating {@link Solver ProblemSolvers}.
     *
     * @author Daniel Escoz
     * @param <C> Type of the chromosomes to be processed
     */
    public static final class Builder<V, C extends Chromosome<C>> {

        private List<SolverStep<V,C>> steps = new ArrayList<SolverStep<V,C>>(4);

        /** Creates a new problem solver builder */
        /* package */Builder () {

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
            return new Solver<V,C>(Collections.unmodifiableList(steps));

        }
    }

}
