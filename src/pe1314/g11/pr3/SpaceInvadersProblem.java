package pe1314.g11.pr3;

import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.PermutationChromosome;
import pe1314.g11.util.XorShiftRandom;

public class SpaceInvadersProblem extends Problem<LispList,LispChromosome> {

    private static final int REPETITIONS = 50;
    private static final long SEED = 890643579046317843L;
    
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
        Random r = new XorShiftRandom(SEED);
        int fitness = 0;
        for(int i = 0; i < REPETITIONS; i++){
            GameState gs = GameState.newRandom(r);
            LispGameRunner lgr = new LispGameRunner(gs, chromosome.getLispList());
            int min = GameState.SIZE;
            while(!gs.finished()){
                lgr.runUntilGameAdvances();
                if(gs.getShotCoord() != null){
                    if(gs.getShotCoord().y == gs.getAlienCoord().y){
                        int distance = Math.abs(gs.getShotCoord().x - gs.getAlienCoord().x);
                        min = Math.min(min, distance);
                    }
                }
            }
            fitness+=min;
        }
        return fitness;
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

}
