package pe1314.g11.sga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.SolverStep;
import pe1314.g11.util.FitnessComparator;

/**
 * A step that implements the selection of chromosomes using the tournament mechanism.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <V> Type of the values
 * @param <C> Type of the chromosomes
 */
public class TournamentSelectionStep<V, C extends Chromosome<C>> implements SolverStep<V,C> {

    /** The size of the tournament */
    private final int tournamentSize;

    /**
     * @param tournamentSize Size of the tournament
     */
    public TournamentSelectionStep (int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public void apply (Problem<V,C> problem, List<C> input, Random random, int generation, List<C> output) {
        List<C> tournament = new ArrayList<C>(tournamentSize);
        Comparator<C> sorter = new FitnessComparator<C>(problem);

        for (int i = 0; i < input.size(); i++) {
            tournament.clear();

            // Fill in the tournament
            while (tournament.size() < tournamentSize) {
                tournament.add(input.get(random.nextInt(input.size())));
            }

            // Sort the tournament
            Collections.sort(tournament, sorter);

            // Add the best (first)
            output.add(tournament.get(0));
        }
    }
}
