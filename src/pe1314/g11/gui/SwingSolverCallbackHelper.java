package pe1314.g11.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import pe1314.g11.Chromosome;
import pe1314.g11.Solver;
import pe1314.g11.Solver.Callbacks;
import pe1314.g11.SolverStep;
import pe1314.g11.SolverTrace;

/**
 * A Swing-friendly version of the {@link Callbacks Solver.Callbacks} interface.
 * <p>
 * Objects of this class are created by wrapping another <tt>Callbacks</tt> object with the static
 * {@link #wrap(Callbacks)} method.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public final class SwingSolverCallbackHelper<V, C extends Chromosome<C>> implements Callbacks<V,C> {

    /** The wrapped callbacks */
    /* callkbacks */Callbacks<V,C> callbacks;

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
    public boolean shouldStop () {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run () {
                    /* Nothing, just wait */
                }
            });
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        synchronized (this) {
            return callbacks.shouldStop();
        }
    }

    @Override
    public void startProcess (final Solver<V,C> solver) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                synchronized (SwingSolverCallbackHelper.this) {
                    callbacks.startProcess(solver);
                }
            }
        });
    }

    @Override
    public void startGeneration (final int gen, final List<C> population) {
        final List<C> clonedPopulation = Collections.unmodifiableList(new ArrayList<C>(population));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                synchronized (SwingSolverCallbackHelper.this) {
                    callbacks.startGeneration(gen, clonedPopulation);
                }
            }
        });
    }

    @Override
    public void startStep (final SolverStep<V,C> step, final List<C> population) {
        final List<C> clonedPopulation = Collections.unmodifiableList(new ArrayList<C>(population));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                synchronized (SwingSolverCallbackHelper.this) {
                    callbacks.startStep(step, clonedPopulation);
                }
            }
        });
    }

    @Override
    public void endStep (final List<C> population) {
        final List<C> clonedPopulation = Collections.unmodifiableList(new ArrayList<C>(population));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                synchronized (SwingSolverCallbackHelper.this) {
                    callbacks.endStep(clonedPopulation);
                }
            }
        });
    }

    @Override
    public void endGeneration (final List<C> population) {
        final List<C> clonedPopulation = Collections.unmodifiableList(new ArrayList<C>(population));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                synchronized (SwingSolverCallbackHelper.this) {
                    callbacks.endGeneration(clonedPopulation);
                }
            }
        });
    }

    @Override
    public void endProcess (final SolverTrace<V,C> trace) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                synchronized (SwingSolverCallbackHelper.this) {
                    callbacks.endProcess(trace);
                }
            }
        });
    }

}
