package pe1314.g11.pr2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import pe1314.g11.Problem;
import pe1314.g11.sga.PermutationChromosome;
import pe1314.g11.util.IntSqMatrix;

public final class P2Problem extends Problem<List<Integer>,PermutationChromosome> {

    private final IntSqMatrix distance;
    private final IntSqMatrix traffic;

    public P2Problem (final IntSqMatrix distance, final IntSqMatrix traffic) {
        if (distance.size() != traffic.size()) {
            throw new IllegalArgumentException("traffic and distance matrices are not equally large ("
                + distance.size() + " != " + traffic.size() + ")");
        }

        this.distance = new IntSqMatrix(distance);
        this.traffic = new IntSqMatrix(traffic);

    }

    @Override
    public PermutationChromosome random (final Random random) {
        return PermutationChromosome.newRandom(distance.size(), random);
    }

    @Override
    public List<Integer> value (final PermutationChromosome chromosome) {
        return chromosome.getPermutation();
    }

    @Override
    public double fitness (final PermutationChromosome chromosome) {
        long sum = 0;

        final int size = distance.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final int pi = chromosome.getPermutation().get(i).intValue();
                final int pj = chromosome.getPermutation().get(j).intValue();
                sum += traffic.get(pi, pj) * distance.get(i, j);
            }
        }

        return sum;
    }

    @Override
    public Problem.Type type () {
        return Problem.Type.MINIMIZATION;
    }

    public static P2Problem readFromURL (final URL url) throws IOException {
        final Charset UTF8 = Charset.forName("UTF-8");
        try (InputStream in = url.openStream()) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, UTF8));

            /* First line tells us the size of the matrices */
            final int size = Integer.parseInt(reader.readLine().trim());
            reader.readLine();

            /* Read the traffic matrix */
            final IntSqMatrix traffic = readMatrix(reader, size);
            reader.readLine();

            /* Read the distance matrix */
            final IntSqMatrix distance = readMatrix(reader, size);

            /* Return the result */
            return new P2Problem(distance, traffic);
        }
    }

    private static IntSqMatrix readMatrix (final BufferedReader reader, final int size) throws IOException {
        final IntSqMatrix matrix = new IntSqMatrix(size);

        for (int i = 0; i < size; i++) {
            final String[] line = reader.readLine().trim().split("\\s+");

            for (int j = 0; j < size; j++) {
                matrix.set(i, j, Integer.parseInt(line[j]));
            }
        }

        return matrix;
    }
}
