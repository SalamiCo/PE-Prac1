package pe1314.g11.pr3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

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
    private final JEditorPane area;
    private final JScrollPane areaScroll;

    private final Random random;

    private long won;
    private long total;

    private int[] speeds = { 1000, 500, 100, 80, 40, 10, 1 };

    public RunnerDialog (final LispList program) {
        int cspd = 2;

        this.program = program;
        ticker = new Timer(speeds[cspd], this);
        random = new XorShiftRandom();

        total = 0;
        won = 0;

        canvas = new GameStateCanvas();

        slider = new JSlider();
        slider.setMinimum(0);
        slider.setMaximum(speeds.length - 1);
        slider.setValue(cspd);
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged (ChangeEvent arg0) {
                ticker.setDelay(speeds[slider.getValue()]);
            }
        });

        String bodyRule = "body { font-family: monospace; font-size: 8px; }";
        String italicRule = "i { color: #999999; }";

        area = new JEditorPane(new HTMLEditorKit().getContentType(), "");
        ((HTMLDocument) area.getDocument()).getStyleSheet().addRule(bodyRule);
        ((HTMLDocument) area.getDocument()).getStyleSheet().addRule(italicRule);
        area.setEditable(false);

        DefaultCaret caret = (DefaultCaret) area.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        areaScroll = new JScrollPane(area);
        areaScroll.getViewport().setPreferredSize(new Dimension(240, canvas.getHeight() + slider.getHeight()));

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(canvas, BorderLayout.CENTER);
        container.add(slider, BorderLayout.PAGE_START);
        container.add(areaScroll, BorderLayout.LINE_END);

        setContentPane(container);
        pack();

        addWindowListener(this);

        setResizable(false);
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
            runner.step();
        }

        String text = runner.getCurrentStatusAsString();
        int idx = text.indexOf('@');

        area.setContentType("text/html");
        area.setText(text//
            .replace("@", "")//
            .replace("[", "<b>").replace("]", "</b>")//
            .replace("{", "<i>").replace("}", "</i>")//
            .replace("\n", "<br/>").replace(" ", "&nbsp;"));

        if (idx > 0) {
            String upToAt = text.substring(0, idx);
            int lines = text.length() - text.replace("\n", "").length();
            int lineno = upToAt.length() - upToAt.replace("\n", "").length();

            JScrollBar sbar = areaScroll.getVerticalScrollBar();
            sbar.setValue(sbar.getMaximum());
            int sbarmax = sbar.getValue();
            int sbarmin = sbar.getMinimum();
            int sbardif = sbarmax - sbarmin;

            int pos = sbarmin + (sbardif * lineno) / lines;
            sbar.setValue(Math.max(0, pos));
        }

        canvas.repaint();
        pack();
        setLocationRelativeTo(null);
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
