package pe1314.g11.gui;

import java.util.Random;

import javax.swing.SwingWorker;

import pe1314.g11.Chromosome;
import pe1314.g11.Solver;

/**
 * A worker that runs a solver.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values of the solved problem
 * @param <C> Type of the chromosomes to be processed
 */
public final class SolverWorker<V, C extends Chromosome<C>> extends SwingWorker<Void,Void> {

    /** The solver to run */
    private final Solver<V,C> solver;

    /** Callbacks object to pass to the solver */
    private final Solver.Callbacks<V,C> callbacks;

    /** A random number generator to be used */
    private final Random random;

    public SolverWorker (Solver<V,C> solver, Solver.Callbacks<V,C> callbacks, Random random) {
        this.solver = solver;
        this.callbacks = callbacks;
        this.random = random;
    }

    @Override
    protected Void doInBackground () throws Exception {
        try {
            solver.solve(random, callbacks);
        } catch (Throwable exc) {
            exc.printStackTrace();

            // Retrhow if not an exception
            if (!(exc instanceof Exception)) {
                throw exc;
            }
        }

        return null;
    }

}
