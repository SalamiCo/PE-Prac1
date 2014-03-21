package pe1314.g11.pr1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.BinaryChromosome;

public class P1F4Problem extends Problem<List<Double>,BinaryChromosome>{
    
    private static final double DOMAIN_MIN = 0.0;
    private static final double DOMAIN_MAX = Math.PI;

    private final int length;
    private final int n;
    
    public P1F4Problem (double precission, int n) {
        length = (int) Math.ceil(Math.log((DOMAIN_MAX - DOMAIN_MIN) / precission) / Math.log(2));
        this.n = n; 
    }
    
    @Override
    public BinaryChromosome random (Random random) {
        return BinaryChromosome.newRandom(length * n, random);
    }

    @Override
    public List<Double> value (BinaryChromosome chromosome) {
        double max = BigInteger.ONE.shiftLeft(length).doubleValue();
        List<Double> list = new ArrayList<Double>();
        for(int i = 0; i < n; i++){
            double x = chromosome.toPartialBigInteger(length * i, length).doubleValue();
            list.add(Double.valueOf(DOMAIN_MIN + (x / max * (DOMAIN_MAX - DOMAIN_MIN))));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public double fitness (BinaryChromosome chromosome) {
        List<Double> list = value(chromosome);
        double result = 0;
        for (int i = 1; i <= n; i++){
            double x = list.get(i-1).doubleValue();
            result += Math.sin(x) * Math.pow((Math.sin(((i+2) * (x * x)/Math.PI))), 20);
        }
        return -result;
    }

    @Override
    public pe1314.g11.Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

}
