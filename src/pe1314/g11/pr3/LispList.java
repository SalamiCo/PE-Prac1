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
public final class LispList {

    private final List<LispValue> values;

    public LispList (List<? extends LispValue> values) {
        this.values = Collections.unmodifiableList(new ArrayList<LispValue>(values));
    }
    
    public int depth () {
        int max = 0;
        for (LispValue lv : values) {
            max = Math.max(max, lv.depth());
        }
        return 1 + max;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder("(");
        Iterator<LispValue> it = values.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while(it.hasNext()) {
                sb.append(" ").append(it.next());
            }
        }
        return sb.append(")").toString();
    }
}
