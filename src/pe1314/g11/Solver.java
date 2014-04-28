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

    public static Chromosome<?> CHROMOSOME;
    public static double MAX_FITNESS;
    public static double MIN_FITNESS;
    public static double SUM_FITNESS;
    public static int N_SUM_FITNESS;
    public static long NUM_COMBS;
    public static long NUM_MUTS;
    public static long NUM_INVS;

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
     * @param random The random generator to use
     * @param callbacks The callbacks to use
     * @return The best value seen
     */
    public SolverTrace<V,C> solve (Random random, Callbacks<V,C> callbacks) {
        return doTrace(random, true, callbacks);
    }

    // ===============================
    // === INTERNAL IMPLEMENTATION ===

    private SolverTrace<V,C> doTrace (Random random, boolean traceOptions, Solver.Callbacks<V,C> callbacks) {
        SolverTrace<V,C> trace = new SolverTrace<V,C>(problem);

        CHROMOSOME = null;
        MAX_FITNESS = Double.NEGATIVE_INFINITY;
        MIN_FITNESS = Double.POSITIVE_INFINITY;
        SUM_FITNESS = 0.0;
        N_SUM_FITNESS = 0;
        NUM_COMBS = 0;
        NUM_MUTS = 0;
        NUM_INVS = 0;

        List<C> population = new ArrayList<C>();
        List<C> buffer = new ArrayList<C>();

        // Notify of the start of the process
        callbacks.startProcess(this);

        int gen = 0;
        while (!callbacks.shouldStop()) {

            // Notify the start of the generation
            callbacks.startGeneration(gen, Collections.unmodifiableList(population));

            // Apply every step
            long gtime = System.nanoTime();
            for (SolverStep<V,C> step : steps) {
                // Notify the start of the step
                callbacks.startStep(step, Collections.unmodifiableList(population));

                List<C> input = population;
                List<C> output = buffer;
                output.clear();

                step.apply(problem, Collections.unmodifiableList(input), random, gen, output);

                // Swap both
                population = output;
                buffer = input;

                // Notify the end of the step
                callbacks.endStep(Collections.unmodifiableList(population));
            }
            gtime = System.nanoTime() - gtime;

            // Update generation
            gen++;
            trace.generation(population, gtime);

            // Notify the end of the generation
            callbacks.endGeneration(Collections.unmodifiableList(population));

            for (C c : population) {
                double f = problem.fitness(c);
                MAX_FITNESS = Math.max(MAX_FITNESS, f);
                MIN_FITNESS = Math.min(MIN_FITNESS, f);
                SUM_FITNESS += f;
                N_SUM_FITNESS++;
            }
        }

        // Notify of the end of the process
        callbacks.endProcess(trace);

        System.out.printf(
            "%.0f, %.0f, %.0f | %d, %d, %d :: %s%n", MIN_FITNESS, SUM_FITNESS / N_SUM_FITNESS, MAX_FITNESS, NUM_COMBS,
            NUM_MUTS, NUM_INVS, trace.getBestSeen());

        return trace;
    }

    public Problem<V,C> getProblem () {
        return problem;
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

            List<SolverStep<V,C>> unmsteps = this.steps;
            steps = null;
            return new Solver<V,C>(problem, Collections.unmodifiableList(unmsteps));
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

        /**
         * Tells the solver whether the solving process should stop.
         * 
         * @return <tt>true</tt> if the process should stop, <tt>false</tt> otherwise
         */
        public abstract boolean shouldStop ();

        /**
         * Notifies this callbacks that the solving process has started.
         * 
         * @param solver The solver used
         */
        public abstract void startProcess (Solver<V,C> solver);

        /**
         * Notifies this callbacks that a new generation is to be processed.
         * 
         * @param gen Number of the new generation
         * @param population The population before the processing
         */
        public abstract void startGeneration (int gen, List<C> population);

        /**
         * Notifies this callbacks that a {@link SolverStep processing step} is starting.
         * 
         * @param step The step in use
         * @param population The population before the processing
         */
        public abstract void startStep (SolverStep<V,C> step, List<C> population);

        /**
         * Notifies this callbacks that the last {@link SolverStep processing step} has ended.
         * 
         * @param population The population after the processing
         */
        public abstract void endStep (List<C> population);

        /**
         * Notifies this callbacks that the last generation processing has ended.
         * 
         * @param population The population after the processing
         */
        public abstract void endGeneration (List<C> population);

        /**
         * Notifies this callbacks that the processing has fully ended.
         * 
         * @param trace
         */
        public abstract void endProcess (SolverTrace<V,C> trace);

    }

}
