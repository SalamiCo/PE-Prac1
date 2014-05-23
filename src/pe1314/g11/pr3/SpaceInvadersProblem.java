package pe1314.g11.pr3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.util.XorShiftRandom;

public class SpaceInvadersProblem extends Problem<LispList,LispChromosome> {

    private static final int REPETITIONS = 50;
    private static final long SEED = 890643579046317843L;

    private static final Map<LispChromosome,Double> CACHE = new HashMap<>();

    @Override
    public LispChromosome random (Random random) {
        return LispChromosome.newRandom(random);
    }

    @Override
    public LispList value (LispChromosome chromosome) {
        return chromosome.getLispList();
    }

    @Override
    public double fitness (LispChromosome chromosome) {
        if (isInCache(chromosome)) {
            return getFromCache(chromosome);
        }

        Random r = new XorShiftRandom(SEED);
        int fitness = 0;
        for (int i = 0; i < REPETITIONS; i++) {
            GameState gs = GameState.newRandom(r);
            LispGameRunner lgr = new LispGameRunner(gs, chromosome.getLispList());
            int min = GameState.SIZE;
            while (!gs.finished()) {
                lgr.runUntilGameAdvances();
                if (gs.getShotCoord() != null) {
                    if (gs.getShotCoord().y == gs.getAlienCoord().y) {
                        int distance = Math.abs(gs.getShotCoord().x - gs.getAlienCoord().x);
                        min = Math.min(min, distance);
                    }
                }
            }
            fitness += min;
        }

        addToCache(chromosome, fitness);

        return fitness;
    }

    private void addToCache (LispChromosome chromosome, int fitness) {
        Iterator<?> it = CACHE.entrySet().iterator();
        while (CACHE.size() >= 4096) {
            it.next();
            it.remove();
        }
        
        CACHE.put(chromosome, Double.valueOf(fitness));
    }

    private double getFromCache (LispChromosome chromosome) {
        return CACHE.get(chromosome).doubleValue();
    }

    private boolean isInCache (LispChromosome chromosome) {
        return CACHE.containsKey(chromosome);
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

}
