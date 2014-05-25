package pe1314.g11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents the process of finding a solution executed within a solver.
 * <p>
 * This class is internally mutable, but clients do not have access to mutating methods, and it will not be mutated
 * after being returned. This class can be considered immutable from the eyes of clients.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarc&oacute;n
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public final class SolverTrace<V, C extends Chromosome<C>> {

    /** The problem being traced */
    private final Problem<V,C> problem;

    /** The best chromosome seen */
    private C best = null;

    /** List of generation summaries */
    private final List<Summary> summaries = new ArrayList<Summary>();

    public SolverTrace (final Problem<V,C> problem) {
        if (problem == null) {
            throw new NullPointerException("problem");
        }

        this.problem = problem;
    }

    /**
     * 
     * @return <tt>this</tt>
     */
    /* package */SolverTrace<V,C> generation (final List<C> population, final long nanoseconds) {
        final int len = population.size();

        double sum = 0;
        double sqsum = 0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for (final C chromo : population) {
            final double fitness = problem.fitness(chromo);

            // Set this as the best if better than the old best
            if (best == null || fitness > problem.fitness(best)) {
                best = chromo;
            }

            max = Math.max(max, fitness);
            min = Math.min(min, fitness);
            sum += fitness;
            sqsum += fitness * fitness;
        }

        summaries.add(new Summary(summaries.size(), max, min, sum / len, Math.sqrt(Math.abs(sqsum / len - sum / len))));
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

    public List<Summary> getSummaries () {
        return Collections.unmodifiableList(summaries);
    }

    /**
     * A class that summarized what was found on a given generation.
     * 
     * @author Daniel Escoz Solana
     * @author Pedro Morgado Alarcón
     */
    public final static class Summary {

        private final int generation;
        private final double max;
        private final double min;
        private final double average;
        private final double standardDeviation;

        /* protected */Summary (final int gen, final double max, final double min, final double avg, final double stdev)
        {
            this.generation = gen;
            this.max = max;
            this.min = min;
            this.average = avg;
            this.standardDeviation = stdev;
        }

        public int getGeneration () {
            return generation;
        }

        public double getMax () {
            return max;
        }

        public double getMin () {
            return min;
        }

        public double getAverage () {
            return average;
        }

        public double getStandardDeviation () {
            return standardDeviation;
        }

    }

}
