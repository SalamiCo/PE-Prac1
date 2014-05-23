package pe1314.g11.pr3;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.SolverStep;
import pe1314.g11.util.PermutationUtils;

public class LispCombination implements SolverStep<LispList,LispChromosome> {

    private final double probability;

    public LispCombination (double probability) {
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
        List<LispValue> listA = a.getLispList().values();
        List<LispValue> listB = b.getLispList().values();
        
        //TODO
        
        return null;
    }
}
