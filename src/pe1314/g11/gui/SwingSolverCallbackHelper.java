package pe1314.g11.gui;

import java.util.List;

import javax.swing.SwingUtilities;

import pe1314.g11.Chromosome;
import pe1314.g11.Solver;
import pe1314.g11.Solver.Callbacks;
import pe1314.g11.SolverStep;
import pe1314.g11.SolverTrace;

public final class SwingSolverCallbackHelper<V, C extends Chromosome<C>> implements Callbacks<V,C> {

    /** The wrapped callbacks */
    private Callbacks<V,C> callbacks;

    private SwingSolverCallbackHelper (Callbacks<V,C> callbacks) {
        if (callbacks == null) {
            throw new NullPointerException("callbacks");
        }

        this.callbacks = callbacks;
    }

    /**
     * Wraps a specific {@link Callbacks callbacks} implementation so that its methods, with the exception of
     * {@link #shouldStop()}, are always executed in the Swing Event Dispatch Thread.
     *
     * @param callbacks The callbacks object to wrap
     * @return A new callbacks object that is Swing-friendly
     */
    public static <V, C extends Chromosome<C>> SwingSolverCallbackHelper<V,C> wrap (Callbacks<V,C> callbacks) {
        return new SwingSolverCallbackHelper<V,C>(callbacks);
    }

    // ======================
    // === IMPLEMENTATION ===

    @Override
    public synchronized boolean shouldStop () {
        return callbacks.shouldStop();
    }

    @Override
    public void startProcess (final Solver<V,C> solver) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                callbacks.startProcess(solver);
            }
        });
    }

    @Override
    public void startGeneration (final int gen, final List<C> population) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                callbacks.startGeneration(gen, population);
            }
        });
    }

    @Override
    public void startStep (final SolverStep<V,C> step, final List<C> population) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                callbacks.startStep(step, population);
            }
        });
    }

    @Override
    public void endStep (final List<C> population) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                callbacks.endStep(population);
            }
        });
    }

    @Override
    public void endGeneration (final List<C> population) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                callbacks.endGeneration(population);
            }
        });
    }

    @Override
    public void endProcess (final SolverTrace<V,C> trace) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                callbacks.endProcess(trace);
            }
        });
    }

}
