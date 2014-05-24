package pe1314.g11.pr3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;

public class LispCombinationStep implements SolverStep<LispList,LispChromosome> {

    private final double probability;

    public LispCombinationStep (double probability) {
        if (probability < 0.0 || probability > 1.0 || Double.isInfinite(probability) || Double.isNaN(probability)) {
            throw new IllegalArgumentException("invalid probability: " + probability);
        }

        this.probability = probability;
    }

    @Override
    public void apply (
        Problem<LispList,LispChromosome> problem, List<LispChromosome> input, Random random, int generation,
        List<LispChromosome> output)
    {
        Iterator<LispChromosome> it = input.iterator();
        while (it.hasNext()) {
            LispChromosome a = it.next();

            if (it.hasNext()) {
                LispChromosome b = it.next();

                if (random.nextDouble() < probability) {
                    int place = random.nextInt(a.getCombinationPlaces());
                    int p2 = place;
                    while (place == p2) {
                        p2 = random.nextInt(a.getCombinationPlaces());
                    }

                    int posA = random.nextInt(a.getLispList().nodes());
                    int posB = random.nextInt(b.getLispList().nodes());

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

    private static LispChromosome performCombination (LispChromosome a, LispChromosome b, int posA, int posB) {
        LispList listA = a.getLispList();
        LispList listB = b.getLispList();

        LispValue subtree = obtainSubtree(listB, posB);
        return new LispChromosome(replaceSubtree(listA, posA, subtree));
    }

    private static LispValue obtainSubtree (LispList list, int pos) {
        int skipped = 0;
        for (LispValue lv : list.values()) {
            if (skipped == pos) {
                return lv;
            }
            int toSkip = lv.nodes();
            if (skipped < pos && pos < skipped + toSkip) {
                return obtainSubtree((LispList) lv, pos - skipped - 1);
            }
            skipped += toSkip;
        }

        throw new IllegalArgumentException("pos " + pos);
    }

    private static LispList replaceSubtree (LispList list, int pos, LispValue subtree) {
        int skipped = 0;
        List<LispValue> values = new ArrayList<>(list.values());
        
        for (int i = 0; i < values.size(); i++) {
            LispValue lv = values.get(i);
            
            if (skipped == pos) {
                values.set(i, subtree);
                return new LispList(values);
            }
            
            int toSkip = lv.nodes();
            if (skipped < pos && pos < skipped + toSkip) {
                values.set(i, replaceSubtree((LispList) lv, pos - skipped - 1, subtree));
                return new LispList(values);
            }
            skipped += toSkip;
        }

        throw new IllegalArgumentException("pos " + pos);
    }

    public static void main (String[] args) {
        LispList l = LispUtils.generateRandom(new Random(), 2);
        LispValue t = new LispTerminal("@");
        System.out.println(l);
        for (int n = 0; n < l.nodes() - 1; n++) {
            //System.out.println(n + ": " + obtainSubtree(l, n));
            System.out.println(n + ": " + replaceSubtree(l, n, t));
        }
    }
}
