package pe1314.g11.pr3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A LISP utils.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public class LispUtils {

    private static final List<String> FUNCTIONS = Arrays.asList(
        LispGameRunner.IF, LispGameRunner.EQ, LispGameRunner.PROG2, LispGameRunner.PROG3);
    private static final List<String> TERMINALS = Arrays.asList(
        LispGameRunner.LEFT, LispGameRunner.RIGHT, LispGameRunner.SHOOT, LispGameRunner.DST_X, LispGameRunner.DST_Y);

    @SuppressWarnings("serial")
    private static final Map<String,Integer> ARITY = new HashMap<String,Integer>() {
        {
            put(LispGameRunner.IF, 3);
            put(LispGameRunner.EQ, 2);
            put(LispGameRunner.PROG2, 2);
            put(LispGameRunner.PROG3, 3);
        }
    };

    private LispUtils () {
    }

    public static LispList generateRandom (Random random, int depth) {
        String function = FUNCTIONS.get(random.nextInt(FUNCTIONS.size()));
        int arity = ARITY.get(function);
        List<LispValue> list = new ArrayList<>();
        list.add(new LispTerminal(function));

        for (int i = 1; i <= arity; i++) {
            if (depth < 4 && random.nextBoolean()) {
                list.add(generateRandom(random, depth + 1));
            } else {
                list.add(new LispTerminal(TERMINALS.get(random.nextInt(TERMINALS.size()))));
            }
        }

        return new LispList(list);
    }
}
