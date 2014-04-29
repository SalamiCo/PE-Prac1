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
 * @author Pedro Morgado Alarc&oacute;n
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
     * Obtain the number of mutation types available for this chromosome.
     * <p>
     * The number returned by this method must be non-negative.
     * 
     * @return Number of different mutation types of this chromosome
     */
    public abstract int getMutationTypes ();

    /**
     * Return a new chromosome with the same information as this but with the specified <tt>place</tt> mutated using the
     * specific <tt>type</tt> of mutation.
     * <p>
     * The <tt>place</tt> argument must be non-negative and strictly less than the result of
     * {@link #getMutationPlaces()}.
     * 
     * @param type Mutation type to use
     * @param place The place to mutate
     * @param length Length of the mutation, if applicable
     * @return A mutated chromosome
     */
    public abstract C getMutated (int type, int place, int length);

    /**
     * Obtain the number of positions that can be used to combine this chromosome with another.
     * <p>
     * The number returned by this method must be non-negative.
     * 
     * @return Number of positions available for combination
     */
    public abstract int getCombinationPlaces ();

    /**
     * Obtain the number of combination types available for this chromosome.
     * <p>
     * The number returned by this method must be non-negative.
     * 
     * @return Number of different combination types of this chromosome
     */
    public abstract int getCombinationTypes ();

    /**
     * Return a new chromosome combined with another one using the specified combination <tt>place</tt> and
     * <tt>type</tt>.
     * <p>
     * The <tt>place</tt> argument must be non-negative and strictly less than the result of
     * {@link #getCombinationPlaces()}.
     * 
     * @param other Another chromosome for combination
     * @param type Combination type to use
     * @param place The position to use for combination
     * @param length Length of the combination, if applicable
     * @return A combined chromosome
     */
    public abstract C getCombined (C other, int type, int place, int length);

}
