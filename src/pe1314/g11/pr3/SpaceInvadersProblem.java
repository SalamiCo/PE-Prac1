package pe1314.g11.pr3;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.util.XorShiftRandom;

public class SpaceInvadersProblem extends Problem<LispList,LispChromosome> {

    private static final int REPETITIONS = 100;
    private static final long SEED = 890643579046317841L;

    private final int fitnessType;
    private final int bloatingPenaltyType;
    private final Map<LispChromosome,Double> cache = new HashMap<>();

    private final boolean complete;
    private final int depth;
    
    public SpaceInvadersProblem (int f, final boolean complete, final int depth, int b) {
        fitnessType = f;
        bloatingPenaltyType = b;
        this.complete = complete;
        this.depth = depth;
    }

    @Override
    public LispChromosome random (final Random random) {
        return LispChromosome.newRandom(complete, random, depth);
    }

    @Override
    public LispList value (final LispChromosome chromosome) {
        return chromosome.getLispList();
    }

    @Override
    public double fitness (final LispChromosome chromosome) {
        if (isInCache(chromosome)) {
            return getFromCache(chromosome);
        }

        double fitness = bloatingPenalty(chromosome, fitnessType == 0 ? calcFitnessA(chromosome) : calcFitnessB(chromosome));

        addToCache(chromosome, fitness);

        return fitness;
    }

    private double bloatingPenalty (LispChromosome chromo, double f) {
        switch (bloatingPenaltyType) {
            case 1:
                // Extra depth penalty
                double dp1 = 1.1;
                return f * Math.pow(dp1, Math.max(0, chromo.getLispList().depth() - depth)) * (2.5 / dp1);
                
            case 2:
                // Extra depth + size penalty
                double dp2 = 1.0 + 0.05 * chromo.getLispList().nodes();
                return f * Math.pow(dp2, Math.max(1, chromo.getLispList().depth() - depth));
        }
        return f;
    }

    private static double calcFitnessA (LispChromosome chromosome) {
        double fitness = 0;

        try {
            final Random r = new XorShiftRandom(SEED);
            for (int i = 0; i < REPETITIONS; i++) {
                final GameState gs = GameState.newRandom(r);
                final LispGameRunner lgr = new LispGameRunner(gs, chromosome.getLispList());
                int min = GameState.SIZE;
                while (!gs.finished()) {
                    lgr.runUntilGameAdvances();
                    if (gs.getShotCoord() != null) {
                        if (gs.getShotCoord().y == gs.getAlienCoord().y) {
                            final int distance = Math.abs(gs.getShotCoord().x - gs.getAlienCoord().x);
                            min = Math.min(min, distance);
                        }
                    }
                }
                fitness += min;
                if (gs.lose()) {
                    fitness += REPETITIONS;
                }
            }
        } catch (final Exception exc) {
            System.err.println("Unrunnable chromosome: " + chromosome);
            fitness = REPETITIONS * GameState.SIZE * 10;
        }

        return fitness;
    }

    private static double calcFitnessB (LispChromosome chromosome) {
        double fitness = 0;

        try {
            final Random r = new XorShiftRandom(SEED);
            for (int i = 0; i < REPETITIONS; i++) {
                final GameState gs = GameState.newRandom(r);
                final LispGameRunner lgr = new LispGameRunner(gs, chromosome.getLispList());

                double sum = 0;
                int shoots = 0;
                while (!gs.finished()) {
                    lgr.runUntilGameAdvances();
                    if (gs.getShotCoord() != null) {
                        if (gs.getShotCoord().y == gs.getAlienCoord().y) {
                            sum += Math.abs(gs.getShotCoord().x - gs.getAlienCoord().x);
                            shoots++;
                        }
                    }
                }

                fitness += (shoots == 0) ? GameState.SIZE : sum / shoots;
                fitness += shoots;
                if (gs.lose()) {
                    fitness += REPETITIONS;
                }
            }
        } catch (final Exception exc) {
            System.err.println("Unrunnable chromosome: " + chromosome);
            fitness = REPETITIONS * GameState.SIZE * 10;
        }

        return fitness;
    }

    private void addToCache (final LispChromosome chromosome, final double fitness) {
        if (cache.size() >= 4096) {
            cache.clear();
        }

        cache.put(chromosome, Double.valueOf(fitness));
    }

    private double getFromCache (final LispChromosome chromosome) {
        return cache.get(chromosome).doubleValue();
    }

    private boolean isInCache (final LispChromosome chromosome) {
        return cache.containsKey(chromosome);
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

}
