package pe1314.g11.pr3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A LISP list value.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class LispList implements LispValue {

    private final List<LispValue> values;

    public LispList (final List<? extends LispValue> values) {
        this.values = Collections.unmodifiableList(new ArrayList<LispValue>(values));
    }

    public int size () {
        return values.size();
    }

    public LispValue get (final int n) {
        return values.get(n);
    }

    @Override
    public int depth () {
        int max = 0;
        for (final LispValue lv : values) {
            max = Math.max(max, lv.depth());
        }
        return 1 + max;
    }

    public List<LispValue> values () {
        return values;
    }

    @Override
    public int hashCode () {
        return super.hashCode();
    }

    @Override
    public boolean equals (final Object obj) {
        if (!(obj instanceof LispList)) {
            return false;
        }
        final LispList lc = (LispList) obj;
        return values.equals(lc.values);
    }

    @Override
    public String toString () {
        final StringBuilder sb = new StringBuilder("(");
        final Iterator<LispValue> it = values.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(" ").append(it.next());
            }
        }
        return sb.append(")").toString();
    }

    @Override
    public int nodes () {
        int num = 0;
        for (final LispValue lv : values) {
            num += lv.nodes();
        }
        return 1 + num;
    }

    @Override
    public int expressions () {
        int num = 0;
        for (int i = 1; i < values.size(); i++) {
            num += values.get(i).expressions();
        }
        return 1 + num;
    }

    @Override
    public int compareTo (final LispValue o) {
        if (o instanceof LispTerminal) {
            return -o.compareTo(this);
        }

        final LispList ll = (LispList) o;

        int cmp = size() - ll.size();
        if (cmp != 0) {
            return cmp;
        }

        for (int i = 0; i < size(); i++) {
            cmp = get(i).compareTo(ll.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }
}
