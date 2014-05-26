package pe1314.g11.pr3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    private final JSlider slider;

    private final Random random;

    private long won;
    private long total;
    
    private int[] speeds = {100, 80, 40, 10, 1};

    public RunnerDialog (final LispList program) {
        this.program = program;
        ticker = new Timer(100, this);
        random = new XorShiftRandom();

        total = 0;
        won = 0;

        canvas = new GameStateCanvas();
        
        slider = new JSlider();
        slider.setMinimum(0);
        slider.setMaximum(speeds.length - 1);
        slider.setValue(0);
        slider.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged (ChangeEvent arg0) {
                ticker.setDelay(speeds[slider.getValue()]);
            }
        });

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(canvas, BorderLayout.CENTER);
        container.add(slider, BorderLayout.PAGE_END);
       
        setContentPane(container);
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
