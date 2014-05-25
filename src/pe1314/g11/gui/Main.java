package pe1314.g11.gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarc&oacute;n
 */
public final class Main {

    /**
     * Setup the Look and Feel so the app does not look awful.
     */
    /* package */static void setupLookAndFeel () {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Application entry point.
     * 
     * @param args Unused
     */
    public static void main (final String[] args) {
        setupLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                final MainFrame frame = new MainFrame();
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });
    }

    /** Private constructor to avoid instantiation */
    private Main () {
        throw new AssertionError();
    }

}
