package pe1314.g11.util;

import java.util.ArrayList;
import java.util.List;

import pe1314.g11.Chromosome;
import pe1314.g11.SolverStep;

public final class ElitismStepPair<V, C extends Chromosome<C>> {

    /** The elite saved by the save step */
    /* protected */final List<C> elite = new ArrayList<C>();

    /** Percent of chromosomes to be saved */
    /* protected */final double percent;

    public ElitismStepPair (double percent) {
        this.percent = percent;
    }

    /**
     * Returns a {@link ProblemStep} that saves the best chromosomes of the input population and does nothing to the
     * output.
     *
     * @return The saving step of the elitism implementation
     */
    public SolverStep<V,C> getSaveStep () {
        return new SaveStep();
    }

    /**
     * Returns a {@ProbemStep} that resets the previously saved chromosomes to the output population,
     * removing the worst of them.
     *
     * @return The restoring step of the elitism implementation
     */
    public SolverStep<V,C> getRestoreStep () {
        return new RestoreStep();
    }

    private class SaveStep implements SolverStep<V,C> {

    }

    private class RestoreStep implements SolverStep<V,C> {

    }
}
