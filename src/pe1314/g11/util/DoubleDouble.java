package pe1314.g11.util;

import java.util.Objects;

public final class DoubleDouble {

    private final Double x;
    private final Double y;

    public DoubleDouble (final Double x, final Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX () {
        return x;
    }

    public Double getY () {
        return y;
    }

    @Override
    public int hashCode () {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals (final Object obj) {
        if (!(obj instanceof DoubleDouble)) {
            return false;
        }
        final DoubleDouble dd = (DoubleDouble) obj;
        return Objects.equals(x, dd.x) && Objects.equals(y, dd.y);
    }

    @Override
    public String toString () {
        return "(" + x + "; " + y + ")";
    }
}
