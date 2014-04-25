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
    public BinaryChromosome getMutated (int type, int place, int len) {
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
    public BinaryChromosome getCombined (BinaryChromosome other, int type, int place, int len) {
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
        return toPartialBigInteger(0, length);
    }

    /**
     * Returns an unsigned integer representation of part of this chromosome.
     * 
     * @param first First bit to use
     * @param num Number of bits to use
     * @return Integer representation of part of this chromosome
     */
    public BigInteger toPartialBigInteger (int first, int num) {
        BitSet fbits = bits;
        if (num < length || first != 0) {
            fbits = new BitSet();
            for (int i = 0; i < num; i++) {
                fbits.set(i, bits.get(i + first));
            }
        }

        if (fbits.cardinality() == 0) {
            return BigInteger.ZERO;
        }

        byte[] bitsBytes = fbits.toByteArray();
        byte[] bigintBytes = new byte[bitsBytes.length + 1];
        for (int i = 0; i < bitsBytes.length; i++) {
            bigintBytes[bigintBytes.length - 1 - i] = bitsBytes[i];
        }

        BigInteger bi = new BigInteger(bigintBytes);

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
        StringBuilder sb = new StringBuilder(length + 2).append("(");
        for (int i = length - 1; i >= 0; i--) {
            sb.append(bits.get(i) ? '1' : '0');
        }
        return sb.append(")").toString();
    }

    @Override
    public int getMutationTypes () {
        return 1;
    }

    @Override
    public int getCombinationTypes () {
        return 1;
    }

}
