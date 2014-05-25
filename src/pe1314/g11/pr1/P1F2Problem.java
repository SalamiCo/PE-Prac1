package pe1314.g11.pr1;

import java.math.BigInteger;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.BinaryChromosome;
import pe1314.g11.util.DoubleDouble;

public final class P1F2Problem extends Problem<DoubleDouble,BinaryChromosome> {

    private static final double DOMAIN_MIN = -6.0;
    private static final double DOMAIN_MAX = 6.0;

    private final int length;

    public P1F2Problem (final double precission) {
        length = (int) Math.ceil(Math.log((DOMAIN_MAX - DOMAIN_MIN) / precission) / Math.log(2));
    }

    @Override
    public BinaryChromosome random (final Random random) {
        return BinaryChromosome.newRandom(length * 2, random);
    }

    @Override
    public DoubleDouble value (final BinaryChromosome chromosome) {
        final double x = chromosome.toPartialBigInteger(0, length).doubleValue();
        final double y = chromosome.toPartialBigInteger(length, length).doubleValue();
        final double max = BigInteger.ONE.shiftLeft(length).doubleValue();
        return new DoubleDouble(//
            Double.valueOf(DOMAIN_MIN + (x / max * (DOMAIN_MAX - DOMAIN_MIN))), //
            Double.valueOf(DOMAIN_MIN + (y / max * (DOMAIN_MAX - DOMAIN_MIN))));
    }

    @Override
    public double fitness (final BinaryChromosome chromosome) {
        final DoubleDouble xy = value(chromosome);
        final double x = xy.getX().doubleValue();
        final double y = xy.getY().doubleValue();
        final double v1 = x * x + y - 11;
        final double v2 = x + y * y - 7;
        return (2186.0 - v1 * v1 - v2 * v2) / 2186.0;
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MAXIMIZATION;
    }

}
