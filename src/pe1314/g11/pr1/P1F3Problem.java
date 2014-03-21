package pe1314.g11.pr1;

import java.math.BigInteger;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.BinaryChromosome;
import pe1314.g11.util.DoubleDouble;

public final class P1F3Problem extends Problem<DoubleDouble,BinaryChromosome> {

    private static final double DOMAIN_MIN_X = -3.0;
    private static final double DOMAIN_MAX_X = 12.1;
    private static final double DOMAIN_MIN_Y = 4.1;
    private static final double DOMAIN_MAX_Y = 5.8;

    private final int lengthX;
    private final int lengthY;

    public P1F3Problem (double precission) {
        lengthX = (int) Math.ceil(Math.log((DOMAIN_MAX_X - DOMAIN_MIN_X) / precission) / Math.log(2));
        lengthY = (int) Math.ceil(Math.log((DOMAIN_MAX_Y - DOMAIN_MIN_Y) / precission) / Math.log(2));
    }

    @Override
    public BinaryChromosome random (Random random) {
        return BinaryChromosome.newRandom(lengthX + lengthY, random);
    }

    @Override
    public DoubleDouble value (BinaryChromosome chromosome) {
        double x = chromosome.toPartialBigInteger(0, lengthX).doubleValue();
        double y = chromosome.toPartialBigInteger(lengthX, lengthY).doubleValue();
        double mx = BigInteger.ONE.shiftLeft(lengthX).doubleValue();
        double my = BigInteger.ONE.shiftLeft(lengthY).doubleValue();
        return new DoubleDouble(//
            Double.valueOf(DOMAIN_MIN_X + (x / mx * (DOMAIN_MAX_X - DOMAIN_MIN_X))), //
            Double.valueOf(DOMAIN_MIN_Y + (y / my * (DOMAIN_MAX_Y - DOMAIN_MIN_Y))));
    }

    @Override
    public double fitness (BinaryChromosome chromosome) {
        DoubleDouble xy = value(chromosome);
        double x = xy.getX().doubleValue();
        double y = xy.getY().doubleValue();
        return 21.5 + x * Math.sin(2 * Math.PI * x) + y * Math.sin(20 * Math.PI * y);
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MAXIMIZATION;
    }
}
