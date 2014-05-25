package pe1314.g11.pr3;

import java.util.Objects;

/**
 * A LISP terminal value.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class LispTerminal implements LispValue {

    private final String string;

    public LispTerminal (final String string) {
        this.string = Objects.requireNonNull(string, "string");
    }

    @Override
    public int depth () {
        return 0;
    }

    @Override
    public int hashCode () {
        return string.hashCode();
    }

    @Override
    public boolean equals (final Object obj) {
        if (!(obj instanceof LispTerminal)) {
            return false;
        }
        final LispTerminal lt = (LispTerminal) obj;
        return string.equals(lt.string);
    }

    @Override
    public String toString () {
        return string;
    }

    @Override
    public int nodes () {
        return 1;
    }

    @Override
    public int expressions () {
        return 1;
    }

    @Override
    public int compareTo (final LispValue o) {
        if (o instanceof LispList) {
            return -1;
        }

        final LispTerminal lt = (LispTerminal) o;
        return string.compareTo(lt.string);
    }
}
