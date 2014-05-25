package pe1314.g11.pr3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

public class LispCombinationStep implements SolverStep<LispList,LispChromosome> {

    private final double probability;

    public LispCombinationStep (final double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public void apply (
        final Problem<LispList,LispChromosome> problem, final List<LispChromosome> input, final Random random,
        final int generation, final List<LispChromosome> output)
    {
        final Iterator<LispChromosome> it = input.iterator();
        while (it.hasNext()) {
            final LispChromosome a = it.next();

            if (it.hasNext()) {
                final LispChromosome b = it.next();

                if (random.nextDouble() < probability) {
                    final int posA = random.nextInt(a.getLispList().expressions() - 1);
                    final int posB = random.nextInt(b.getLispList().expressions() - 1);

                    output.add(performCombination(a, b, posA, posB));
                    output.add(performCombination(b, a, posB, posA));

                } else {
                    output.add(a);
                    output.add(b);
                }

            } else {
                output.add(a);
            }
        }
    }

    private static LispChromosome performCombination (
        final LispChromosome a, final LispChromosome b, final int posA, final int posB)
    {
        final LispList listA = a.getLispList();
        final LispList listB = b.getLispList();

        final LispValue subtree = obtainSubtree(listB, posB);
        return new LispChromosome(replaceSubtree(listA, posA, subtree));
    }

    private static LispValue obtainSubtree (final LispList list, final int pos) {
        int skipped = 0;

        for (int i = 1; i < list.size(); i++) {
            final LispValue lv = list.get(i);

            if (skipped == pos) {
                return lv;
            }

            final int toSkip = lv.expressions();
            if (skipped < pos && pos < skipped + toSkip) {
                return obtainSubtree((LispList) lv, pos - skipped - 1);
            }
            skipped += toSkip;
        }

        throw new IllegalArgumentException("pos " + pos);
    }

    private static LispList replaceSubtree (final LispList list, final int pos, final LispValue subtree) {
        int skipped = 0;
        final List<LispValue> values = new ArrayList<>(list.values());

        for (int i = 1; i < values.size(); i++) {
            final LispValue lv = values.get(i);

            if (skipped == pos) {
                values.set(i, subtree);
                return new LispList(values);
            }

            final int toSkip = lv.expressions();
            if (skipped < pos && pos < skipped + toSkip) {
                values.set(i, replaceSubtree((LispList) lv, pos - skipped - 1, subtree));
                return new LispList(values);
            }
            skipped += toSkip;
        }

        throw new IllegalArgumentException("pos " + pos);
    }
}
