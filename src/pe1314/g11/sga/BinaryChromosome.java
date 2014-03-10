package pe1314.g11.sga;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

import pe1314.g11.Chromosome;
import pe1314.g11.util.XorShiftRandom;

/**
 * A chromosome that uses a bit string.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class BinaryChromosome extends Chromosome<BinaryChromosome> {

    /** Number of bits in this chromosome */
    private final int length;

    /** Bits of this chromosome */
    private final BitSet bits;

    /**
     * @param length Number of bits used in this chromosome
     * @param bits Bits of this chromosome
     */
    public BinaryChromosome (int length, BitSet bits) {
        this.length = length;
        this.bits = (BitSet) bits.clone();

        // Mask the bits of this chromosome
        BitSet mask = new BitSet();
        mask.set(0, length, true);
        this.bits.intersects(mask);
    }

    /**
     * Creates a new random chromosome with the specified length and random generator.
     *
     * @param length Length of the new chromosome
     * @param random The RNG to be used
     * @return A new random chromosome
     */
    public static BinaryChromosome newRandom (int length, Random random) {
        BitSet bits = new BitSet();
        for (int i = 0; i < length; i++) {
            bits.set(i, random.nextBoolean());
        }

        return new BinaryChromosome(length, bits);
    }

    /**
     * Creates a new random chromosome with the specified length.
     *
     * @param length Length of the new chromosome
     * @return A new random chromosome
     */
    public static BinaryChromosome newRandom (int length) {
        return newRandom(length, new XorShiftRandom());
    }

    public int getLength () {
        return length;
    }

    @Override
    public int getMutationPlaces () {
        return length;
    }

    @Override
    public BinaryChromosome getMutated (int place) {
        if (place < 0 || place >= getMutationPlaces()) {
            throw new IllegalArgumentException("invalid mutation place (" + place + ")");
        }

        BitSet newBits = (BitSet) bits.clone();
        newBits.flip(place);

        return new BinaryChromosome(length, newBits);
    }

    @Override
    public int getCombinationPlaces () {
        return length - 1;
    }

    @Override
    public BinaryChromosome getCombined (BinaryChromosome other, int place) {
        if (this.length != other.length) {
            throw new IllegalArgumentException("unmatching lengths (" + length + " != " + other.length + ")");
        }

        if (place < 0 || place >= getCombinationPlaces()) {
            throw new IllegalArgumentException("invalid combination place (" + place + ")");
        }

        BitSet newBits = (BitSet) bits.clone();
        for (int i = 0; i < place; i++) {
            newBits.set(i, other.bits.get(i));
        }

        return new BinaryChromosome(length, newBits);
    }

    /**
     * Returns an unsigned integer representation of this chromosome.
     *
     * @return Integer representation of this chromosome
     */
    public BigInteger toBigInteger () {
        if (bits.cardinality() == 0) {
            return BigInteger.ZERO;
        }

        BigInteger bi = new BigInteger(bits.toByteArray());

        // If the number turns out to be negative, make it positive again
        if (bi.compareTo(BigInteger.ZERO) < 0) {
            bi = bi.add(BigInteger.ONE.shiftLeft(length));
        }

        return bi;
    }

    @Override
    public int hashCode () {
        return bits.hashCode() * 17 + length;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BinaryChromosome)) {
            return false;
        }

        BinaryChromosome bc = (BinaryChromosome) obj;

        return length == bc.length && bits.equals(bc.bits);
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder("(");
        for (int i = length - 1; i >= 0; i--) {
            sb.append(bits.get(i) ? '1' : '0');
        }
        return sb.append(")").toString();
    }

}
