package pe1314.g11.pr3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.Timer;

import pe1314.g11.util.XorShiftRandom;

public final class RunnerDialog extends JDialog implements ActionListener, WindowListener {

    /**
     * 
     */
    private static final long serialVersionUID = -8790655197820758454L;

    private final LispList program;

    private LispGameRunner runner;

    private final Timer ticker;

    private GameState game;

    private final GameStateCanvas canvas;

    private final Random random;

    private long won;
    private long total;

    public RunnerDialog (final LispList program) {
        this.program = program;
        ticker = new Timer(80, this);
        canvas = new GameStateCanvas();
        random = new XorShiftRandom();

        total = 0;
        won = 0;

        add(canvas);
        pack();

        addWindowListener(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        updateTitle();
    }

    @Override
    public void actionPerformed (final ActionEvent evt) {
        if (game == null || game.finished()) {
            if (game != null && game.finished()) {
                total++;
                if (game.won()) {
                    won++;
                }
            }

            game = GameState.newRandom(random);
            runner = new LispGameRunner(game, program);
            canvas.setGameState(game);

            updateTitle();
        } else {
            runner.runUntilGameAdvances();
        }

        canvas.repaint();
    }

    private void updateTitle () {
        setTitle(String.format("%d / %d (%.2f%%)", won, total, (total == 0)
            ? 0.0 : 100 * ((double) won / (double) total)));
    }

    @Override
    public void windowOpened (final WindowEvent e) {
        ticker.start();
    }

    @Override
    public void windowClosing (final WindowEvent e) {
        ticker.stop();
    }

    @Override
    public void windowActivated (final WindowEvent e) {
    }

    @Override
    public void windowClosed (final WindowEvent e) {
    }

    @Override
    public void windowDeactivated (final WindowEvent e) {
    }

    @Override
    public void windowDeiconified (final WindowEvent e) {
    }

    @Override
    public void windowIconified (final WindowEvent e) {
    }

}
