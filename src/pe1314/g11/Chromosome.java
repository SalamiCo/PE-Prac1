package pe1314.g11;

/**
 * An individual on a population.
 * <p>
 * The <tt>&lt,C&gt;</tt> parameter of this class should be specified on every subclass as the subclass itself. For
 * example:
 * 
 * <pre>
 * class MyChromosome extends Chromosome<MyChromosome> {
 * ...
 * }
 * </pre>
 * 
 * This is the intended way of fulfilling the generic constraints of this class.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 * @param <C> Self-type, for better method signature
 */
public abstract class Chromosome<C extends Chromosome<C>> {

    /**
     * Obtain the number of positions that can be mutated in this chromosome.
     * <p>
     * The number returned by this method must be non-negative.
     * 
     * @return Number of mutable places of this chromosome
     */
    public abstract int getMutationPlaces ();

    /**
     * Return a new chromosome with the same information as this but with the specified <tt>place</tt> mutated.
     * <p>
     * The <tt>place</tt> argument must be non-negative and strictly less than the result of
     * {@link #getMutationPlaces()}.
     * 
     * @param place The place to mutate
     * @return A mutated chromosome
     */
    public abstract C getMutated (int place);

    /**
     * Obtain the number of positions that can be used to combine this chromosome with another.
     * <p>
     * The number returned by this method must be non-negative.
     * 
     * @return Number of positions available for combination
     */
    public abstract int getCombinationPlaces ();

    /**
     * Return a new chromosome combined with another one using the specified combination <tt>place</tt>.
     * <p>
     * The <tt>place</tt> argument must be non-negative and strictly less than the result of
     * {@link #getCombinationPlaces()}.
     * 
     * @param other Another chromosome for combination
     * @param place The position to use for combination
     * @return A combined chromosome
     */
    public abstract C getCombined (C other, int place);

}
