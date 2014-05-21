package pe1314.g11.pr3;

/**
 * An object that, given a <tt>GameState</tt> and a <tt>LispList</tt> in the form of a program, will run the game. This
 * class also keeps track of certain statistics about the game.
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class LispGameRunner {

    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String SHOOT = "shoot";
    public static final String DST_X = "dist-x";
    public static final String DST_Y = "dist-y";

    public static final String IF = "if";
    public static final String EQ = "eq";
    public static final String PROG2 = "prog-2";
    public static final String PROG3 = "prog-3";

    /** The program to be run */
    private final LispList program;

    /** The game in which run the program */
    private final GameState game;

    public LispGameRunner (GameState game, LispList program) {
        this.game = game;
        this.program = checkProgram(program);
    }

    /* package */static LispList checkProgram (LispList program) {
        /* TODO Write some actual checks */
        return program;
    }
}
