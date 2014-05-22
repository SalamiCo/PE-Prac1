package pe1314.g11.pr3;

import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.PermutationChromosome;

public class SpaceInvadersProblem extends Problem<LispList,LispChromosome> {

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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

}
