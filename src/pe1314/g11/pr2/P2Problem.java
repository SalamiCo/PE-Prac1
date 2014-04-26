package pe1314.g11.pr2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.PermutationChromosome;
import pe1314.g11.util.IntSqMatrix;

public final class P2Problem extends Problem<List<Integer>,PermutationChromosome> {
    
    private final IntSqMatrix distance;
    private final IntSqMatrix traffic;

    public P2Problem (IntSqMatrix distance, IntSqMatrix traffic) {
        if (distance.size() != traffic.size()) {
            throw new IllegalArgumentException("traffic and distance matrices are not equally large ("
                + distance.size() + " != " + traffic.size() + ")");
        }

        this.distance = new IntSqMatrix(distance);
        this.traffic = new IntSqMatrix(traffic);

    }

    @Override
    public PermutationChromosome random (Random random) {
        return PermutationChromosome.newRandom(distance.size(), random);
    }

    @Override
    public List<Integer> value (PermutationChromosome chromosome) {
        return chromosome.getPermutation();
    }

    @Override
    public double fitness (PermutationChromosome chromosome) {
        long sum = 0;

        final int size = distance.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int pi = chromosome.getPermutation().get(i).intValue();
                int pj = chromosome.getPermutation().get(j).intValue();
                sum += traffic.get(i, j) * distance.get(pi, pj);
            }
        }

        return sum;
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

    public static P2Problem readFromURL (URL url) throws IOException {
        Charset UTF8 = Charset.forName("UTF-8");
        try (InputStream in = url.openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, UTF8));

            /* First line tells us the size of the matrices */
            int size = Integer.parseInt(reader.readLine().trim());
            reader.readLine();

            /* Read the traffic matrix */
            IntSqMatrix traffic = readMatrix(reader, size);
            reader.readLine();
            
            /* Read the distance matrix */
            IntSqMatrix distance = readMatrix(reader, size);
            
            /* Return the result */
            return new P2Problem(distance, traffic);
        }
    }
    
    private static IntSqMatrix readMatrix (BufferedReader reader, int size) throws IOException {
        IntSqMatrix matrix = new IntSqMatrix(size);
        
        for (int i = 0; i < size; i++) {
            String[] line = reader.readLine().trim().split("\\s+");
            
            for (int j = 0; j < size; j++) {
                matrix.set(i, j, Integer.parseInt(line[j]));
            }
        }
        
        return matrix;
    }
}
