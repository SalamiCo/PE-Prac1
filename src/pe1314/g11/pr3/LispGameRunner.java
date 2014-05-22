package pe1314.g11.pr3;

import java.util.ArrayDeque;
import java.util.Deque;

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

    /** The game in which to run the program */
    private final GameState game;

    /** The stack for the current state */
    private final Deque<StackFrame> stack = new ArrayDeque<>();

    public LispGameRunner (GameState game, LispList program) {
        this.game = game;
        this.program = checkProgram(program);

        reset();
    }

    /**
     * Resets the runner and all its recorded statistics.
     */
    public void reset () {
        stack.clear();
    }

    /**
     * Runs a single execution step of the program.
     * 
     * @return <tt>true</tt> if the game advanced, <tt>false</tt> otherwise
     */
    public boolean step () {
        /* If the stack is empty, add an initial frame */
        if (stack.isEmpty()) {
            stack.addLast(new StackFrame(program, 0));
        }
        
        /* Remove frames if they are finished */
        StackFrame frame = stack.getLast();
        while (frame != null && frame.position >= frame.scope.size()) {
            stack.removeLast();
            frame = stack.isEmpty() ? null : stack.getLast();
        }
        
        /* If no frame (stack is empty), apply a NOP then return */
        if (frame == null) {
            game.advance(null);
            return true;
        }
        
        /* At this point, frame contains the next instruction to run */

        return false;
    }

    /**
     * Runs the program until the game advances one turn.
     * 
     * @return <tt>true</tt> if the game finished, <tt>false</tt> otherwise
     */
    public boolean runUntilGameAdvances () {
        boolean advanced;
        do {
            advanced = step();
        } while (!advanced);

        return game.finished();
    }

    /**
     * Runs the program until the game finishes.
     * 
     * @return <tt>true</tt> if the game was won, <tt>false</tt> otherwise
     */
    public boolean runUntilGameFinishes () {
        while (game.finished()) {
            step();
        }

        return game.won();
    }

    /* package */static LispList checkProgram (LispList program) {
        /* TODO Write some actual checks */
        return program;
    }

    private static final class StackFrame {
        /* package */LispList scope;
        /* package */int position;

        public StackFrame (LispList scope, int position) {
            this.scope = scope;
            this.position = position;
        }
    }
}
