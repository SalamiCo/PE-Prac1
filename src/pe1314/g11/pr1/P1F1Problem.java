package pe1314.g11.pr1;

import java.math.BigInteger;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.BinaryChromosome;

/**
 * A problem representing the first function given on our first assignment.
 * <p>
 * This problem attempts to find the minimum value of the following function of the [0, 25) domain:
 * 
 * <pre>
 *           sin x
 * f = ——————————————————
 *           _    cos x
 *      1 + √x + ———————
 *                1 + x
 * </pre>
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarc&oacute;n
 */
public final class P1F1Problem extends Problem<Double,BinaryChromosome> {

    private static final double DOMAIN_MIN = 0.0;
    private static final double DOMAIN_MAX = 25.0;

    private final int length;

    public P1F1Problem (final double precission) {
        length = (int) Math.ceil(Math.log((DOMAIN_MAX - DOMAIN_MIN) / precission) / Math.log(2));
    }

    @Override
    public BinaryChromosome random (final Random random) {
        return BinaryChromosome.newRandom(length, random);
    }

    @Override
    public Double value (final BinaryChromosome chromosome) {
        final double binVal = chromosome.toBigInteger().doubleValue();
        final double binMax = BigInteger.ONE.shiftLeft(length).doubleValue();
        return Double.valueOf(DOMAIN_MIN + (binVal / binMax * (DOMAIN_MAX - DOMAIN_MIN)));
    }

    @Override
    public double fitness (final BinaryChromosome chromosome) {
        final double x = value(chromosome).doubleValue();
        return (Math.sin(x)) / (1.0 + Math.sqrt(x) + (Math.cos(x) / (1.0 + x)));
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

}
