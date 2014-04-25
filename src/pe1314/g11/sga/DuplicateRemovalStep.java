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
    public void apply (Problem<V,C> problem, List<C> input, Random random, int generation, List<C> output) {
        output.addAll(input);

        /* Put together chromosomes with the same hashCode */
        Collections.sort(output);

        /* After sorting, linearly remove duplicates */
        C last = null;
        for (Iterator<C> it = output.iterator(); it.hasNext();) {
            C curr = it.next();

            if (curr.equals(last)) {
                it.remove();
            }

            last = curr;
        }
    }
}
