package pe1314.g11.pr1;

import java.math.BigInteger;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.BinaryChromosome;
import pe1314.g11.util.DoubleDouble;

public class P1F5Problem extends Problem<DoubleDouble,BinaryChromosome> {
    
    private static final double DOMAIN_MIN = -10.0;
    private static final double DOMAIN_MAX = 10.0;
    
    private final int length;
    
    public P1F5Problem (double precission) {
        length = (int) Math.ceil(Math.log((DOMAIN_MAX - DOMAIN_MIN) / precission) / Math.log(2));
    }

    @Override
    public BinaryChromosome random (Random random) {
        return BinaryChromosome.newRandom(length * 2, random);
    }

    @Override
    public DoubleDouble value (BinaryChromosome chromosome) {
        double x = chromosome.toPartialBigInteger(0, length).doubleValue();
        double y = chromosome.toPartialBigInteger(length, length).doubleValue();
        double max = BigInteger.ONE.shiftLeft(length).doubleValue();
        return new DoubleDouble(//
            Double.valueOf(DOMAIN_MIN + (x / max * (DOMAIN_MAX - DOMAIN_MIN))), //
            Double.valueOf(DOMAIN_MIN + (y / max * (DOMAIN_MAX - DOMAIN_MIN))));
    }

    @Override
    public double fitness (BinaryChromosome chromosome) {
        DoubleDouble xy = value(chromosome);
        double left = 0.0;
        double right = 0.0;
        for (int i = 0; i < 5; i++){
            left += i * Math.cos((i+1) * xy.getX().doubleValue() + i);
            right += i * Math.cos((i+1) * xy.getY().doubleValue() + i);
        }
        return left * right;
    }

    @Override
    public pe1314.g11.Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

}
