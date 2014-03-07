package pe1314.g11.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class Main {

    /**
     * Setup the Look and Feel so the app does not look awful.
     */
    /* package */static void setupLookAndFeel () {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Application entry point.
     * 
     * @param args Unused
     */
    public static void main (String[] args) {
        setupLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                MainFrame frame = new MainFrame();
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });
    }

    /** Private constructor to avoid instantiation */
    private Main () {
        new AssertionError();
    }

}
