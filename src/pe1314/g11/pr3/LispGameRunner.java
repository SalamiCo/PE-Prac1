package pe1314.g11.pr3;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import pe1314.g11.pr3.GameState.Move;

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
     * Runs a single execution step izquierdas o de derechasof the program.
     * 
     * @return <tt>true</tt> if the game advanced, <tt>false</tt> otherwise
     */
    public boolean step () {
        /* If the stack is empty, add an initial frame */
        if (stack.isEmpty()) {
            stack.addLast(new StackFrame(program, 1));
        }

        /* Remove frames if they are finished */
        StackFrame frame = stack.getLast();
        while (frame != null && frame.position < 0) {
            StackFrame oldFrame = stack.removeLast();
            frame = stack.isEmpty() ? null : stack.getLast();
            if (frame != null && frame.position > 0) {
                frame.returns[frame.position - 1] = oldFrame.returns[0];
            }
        }

        /* If no frame (stack is empty), apply a NOP then return */
        if (frame == null) {
            game.advance(null);
            return true;
        }

        /* At this point, frame contains the next instruction to run, so do it */
        boolean adv = stepFunc(frame);
        if (frame.position == 0) {
            frame.position--;
        } else {
            frame.position++;
        }

        /* Back to position 0 */
        if (frame.position == frame.scope.size()) {
            frame.position = 0;
        }

        return adv;
    }

    private boolean stepFunc (StackFrame frame) {
        String fun = ((LispTerminal) frame.scope.get(0)).toString();

        switch (fun) {
            case IF:
                return stepIf(frame);
            case EQ:
                return stepEq(frame);
            case PROG2:
                return stepProg2(frame);
            case PROG3:
                return stepProg3(frame);
            default:
                throw new IllegalArgumentException("'" + fun + "' is not a function");
        }
    }

    private boolean stepIf (StackFrame frame) {
        if (frame.position == 1) {
            return stepCall(frame);
        }

        if (frame.position == 2 && frame.returns[1] != 0) {
            return stepCall(frame);
        }

        if (frame.position == 3 && frame.returns[1] == 0) {
            return stepCall(frame);
        }

        return false;
    }

    private boolean stepEq (StackFrame frame) {
        if (frame.position > 0) {
            return stepCall(frame);
        } else {
            frame.returns[0] = (frame.returns[1] == frame.returns[2]) ? 1 : 0;
            return false;
        }
    }

    private boolean stepProg2 (StackFrame frame) {
        if (frame.position > 0) {
            return stepCall(frame);
        }

        return false;
    }

    private boolean stepProg3 (StackFrame frame) {
        if (frame.position > 0) {
            return stepCall(frame);
        }

        return false;
    }

    private boolean stepCall (StackFrame frame) {
        LispValue what = frame.scope.get(frame.position);

        if (what instanceof LispList) {
            stack.addLast(new StackFrame((LispList) what, 1));
            return false;
        }

        String fun = ((LispTerminal) what).toString();
        switch (fun) {
            case LEFT:
                game.advance(Move.LEFT);
                return true;
            case RIGHT:
                game.advance(Move.RIGHT);
                return true;
            case SHOOT:
                game.advance(Move.SHOOT);
                return true;

            case DST_X:
                frame.returns[frame.position] = game.getDistX();
                return false;

            case DST_Y:
                frame.returns[frame.position] = game.getDistY();
                return false;

            default:
                throw new IllegalArgumentException("'" + fun + "' is not a function");
        }
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
        /* package */final LispList scope;
        /* package */int position;
        /* package */final int[] returns;

        public StackFrame (LispList scope, int position) {
            this.scope = scope;
            this.position = position;
            returns = new int[scope.size()];
        }

        @Override
        public String toString () {
            return position + " @ " + scope + " = " + Arrays.toString(returns);
        }
    }
}
