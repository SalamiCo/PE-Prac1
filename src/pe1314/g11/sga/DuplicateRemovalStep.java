package pe1314.g11.sga;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

public final class DuplicateRemovalStep<V, C extends Chromosome<C> & Comparable<C>> implements SolverStep<V,C> {

    @Override
    public
        void apply (
            final Problem<V,C> problem, final List<C> input, final Random random, final int generation,
            final List<C> output)
    {
        output.addAll(input);

        /* Put together chromosomes with the same hashCode */
        Collections.sort(output);

        /* After sorting, linearly remove duplicates */
        C last = null;
        for (final Iterator<C> it = output.iterator(); it.hasNext();) {
            final C curr = it.next();

            if (curr.equals(last)) {
                it.remove();
            }

            last = curr;
        }
    }
}
