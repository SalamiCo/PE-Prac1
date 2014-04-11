package pe1314.g11.util;

import java.util.Arrays;

/**
 * A class that represetns a square matrix of <tt>int</tt>s.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class IntSqMatrix {

    /** Side length of this matrix */
    public final int size;

    /** Actual values on the matrix */
    public final int[] values;

    /**
     * Create a new matrix with a side length of <tt>size</tt>.
     * 
     * @param size Length of both matrix dimensions
     */
    public IntSqMatrix (int size) {
        if (size < 0) {
            throw new IllegalArgumentException("invalid size: " + size);
        }

        this.size = size;
        values = new int[size * size];
    }

    /**
     * Create a new matrix that is a copy of <tt>other</tt> matrix.
     * 
     * @param other The matrix to copy into a new one
     */
    public IntSqMatrix (IntSqMatrix other) {
        size = other.size;
        values = Arrays.copyOf(other.values, other.values.length);
    }

    /** @return Side length of this matrix */
    public int size () {
        return size;
    }

    /**
     * @param row Row to get
     * @param col Column to get
     * @return The value stored at the given position
     * @throws IndexOutOfBoundsException if <tt>row</tt> or <tt>col</tt> are negative or greater than or equal to
     *         <tt>size</tt>
     */
    public int get (int row, int col) {
        checkRange(row, col);
        return values[index(row, col)];
    }

    /**
     * @param row Row to set
     * @param col Column to set
     * @param val Value to set
     * @throws IndexOutOfBoundsException if <tt>row</tt> or <tt>col</tt> are negative or greater than or equal to
     *         <tt>size</tt>
     */
    public void set (int row, int col, int val) {
        checkRange(row, col);
        values[index(row, col)] = val;
    }

    /**
     * Checks that the <tt>row</tt> and <tt>col</tt> arguments represent a valid position of the matrix
     * 
     * @param row Row to check
     * @param col Column to check
     * @throws IndexOutOfBoundsException if <tt>row</tt> or <tt>col</tt> are negative or greater than or equal to
     *         <tt>size</tt>
     */
    private void checkRange (int row, int col) {
        if (row < 0 || row >= size) {
            throw new IndexOutOfBoundsException("row: " + row);
        }
        if (col < 0 || col >= size) {
            throw new IndexOutOfBoundsException("col: " + row);
        }
    }

    /**
     * Calculates the position on the internal array for a given <tt>row</tt> and <tt>col</tt>.
     * <p>
     * This method does not perform any bounds check.
     * 
     * @param row Matrix row
     * @param col Matrix column
     * @return The index of the internal array for the given position
     */
    private int index (int row, int col) {
        return row * size + col;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append("\n |");
            } else {
                sb.append(">|");
            }

            for (int j = 0; j < size; j++) {
                if (j > 0) {
                    sb.append(", ");
                }
                sb.append(get(i, j));
            }
            
            sb.append('|');
        }

        return sb.toString();
    }
}
