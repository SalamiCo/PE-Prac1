package pe1314.g11.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The main frame for the application graphical interface.
 *
 * @author Daniel Escoz Solana
 * @author Pedro Morgado Alarcón
 */
public final class MainFrame extends JFrame {

    private static final String PRB_P1_F1 = "P1 Función 1";
    private static final String PRB_P1_F2 = "P1 Función 2";
    private static final String PRB_P1_F3 = "P1 Función 3";
    private static final String PRB_P1_F4 = "P1 Función 4";
    private static final String PRB_P1_F5 = "P1 Función 5";

    /** Generated SVUID */
    private static final long serialVersionUID = -8605437477715617439L;

    private JComboBox<String> comboProblem;

    private JSpinner spinnerMinPopSize;
    private JSpinner spinnerEliteSize;
    private JComboBox<String> comboSelectionType;
    private JSpinner spinnerCombineProb;
    private JSpinner spinnerMutateProb;

    private JCheckBox checkboxStopGeneration;
    private JSpinner spinnerStopGenerations;
    private JCheckBox checkboxStopStall;
    private JSpinner spinnerStopStalled;
    private JCheckBox checkboxRandomSeed;
    private JTextField textfieldRandomSeed;

    /**
     * Creates an empty frame and fills it with the necessary components to work.
     */
    public MainFrame () {
        setTitle("Grupo 11 - Programación Evolutiva");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        /* Schedule the GUI creation for later for greater responsivity */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                setupGui();
            }
        });
    }

    /* package */void setupGui () {
        setupMenuBar();
        setupContent();

        pack();
    }

    private void setupMenuBar () {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        menubar.setVisible(true);

        { /* File Menu */
            JMenu menu = new JMenu("File");
            menubar.add(menu);

            { /* Exit Option */
                JMenuItem item = new JMenuItem("Exit");
                menu.add(item);

                item.setMnemonic('x');
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed (ActionEvent evt) {
                        actionExit();
                    }
                });
            }

            menu.setMnemonic('f');
            getContentPane().setLayout(new BorderLayout(0, 0));
            menu.setVisible(true);
        }

        menubar.invalidate();
    }

    private void setupContent () {
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BorderLayout());

        { /* Left Form */
            createLeftFormElements();

            FormLayout layout =
                new FormLayout(
                    "pref, 3dlu, right:pref, 3dlu, pref",
                    "pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref");

            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            CellConstraints cc = new CellConstraints();

            /* @formatter:off */
            builder.addSeparator("Problem", cc.xyw(1, 1, 5));
            builder.addLabel("Problem:",    cc.xyw(1, 3, 3));
            builder.add(comboProblem,       cc.xy (5, 3));

            builder.addSeparator("Algorithm",      cc.xyw(1,  5, 5));
            builder.addLabel("Population Size:",   cc.xyw(1,  7, 3));
            builder.add(spinnerMinPopSize,         cc.xy (5,  7));
            builder.addLabel("Elite Size:",        cc.xyw(1,  9, 3));
            builder.add(spinnerEliteSize,          cc.xy (5,  9));
            builder.addLabel("Selection:",         cc.xyw(1, 11, 3));
            builder.add(comboSelectionType,        cc.xy (5, 11));
            builder.addLabel("Combination Prob.:", cc.xyw(1, 13, 3));
            builder.add(spinnerCombineProb,        cc.xy (5, 13));
            builder.addLabel("Mutation Prob.:",    cc.xyw(1, 15, 3));
            builder.add(spinnerMutateProb,         cc.xy (5, 15));

            builder.addSeparator("Stopping",    cc.xyw(1, 17, 5));
            builder.add(checkboxStopGeneration, cc.xy (1, 19));
            builder.addLabel("Generations: ",   cc.xy (3, 19));
            builder.add(spinnerStopGenerations, cc.xy (5, 19));
            builder.add(checkboxStopStall,      cc.xy (1, 21));
            builder.addLabel("Fitness Stall: ", cc.xy (3, 21));
            builder.add(spinnerStopStalled,     cc.xy (5, 21));

            builder.addSeparator("Other",     cc.xyw(1, 23, 5));
            builder.add(checkboxRandomSeed,   cc.xy (1, 25));
            builder.addLabel("Random Seed: ", cc.xy (3, 25));
            builder.add(textfieldRandomSeed,  cc.xy (5, 25));
            /* @formatter:on */

            JPanel leftPanel = builder.getPanel();
            panel.add(leftPanel, BorderLayout.LINE_START);
        }

        { /* Left Form */
            JPanel centerPanel = new JPanel();
            panel.add(centerPanel, BorderLayout.CENTER);
        }

        panel.invalidate();
    }

    private void createLeftFormElements () {
        comboProblem = new JComboBox<String>();
        comboProblem.setModel(new DefaultComboBoxModel<String>(new String[] {
            PRB_P1_F1, PRB_P1_F2, PRB_P1_F3, PRB_P1_F4, PRB_P1_F5 }));

        spinnerMinPopSize = new JSpinner();
        spinnerEliteSize = new JSpinner();
        comboSelectionType = new JComboBox<String>();
        spinnerCombineProb = new JSpinner();
        spinnerMutateProb = new JSpinner();

        checkboxStopGeneration = new JCheckBox();
        spinnerStopGenerations = new JSpinner();
        checkboxStopStall = new JCheckBox();
        spinnerStopStalled = new JSpinner();
        checkboxRandomSeed = new JCheckBox();
        textfieldRandomSeed = new JTextField();
    }

    /** The user pressed an exit button */
    /* package */void actionExit () {
        dispose();
    }
}
