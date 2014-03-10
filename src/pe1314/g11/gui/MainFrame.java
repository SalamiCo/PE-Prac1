package pe1314.g11.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
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

    private static final String SEL_ROULETTE = "Ruleta";
    private static final String SEL_TOURNAMENT = "Torneo";

    /** Generated SVUID */
    private static final long serialVersionUID = -8605437477715617439L;

    private JComboBox<String> comboProblem;

    private JSpinner spinnerMinPopSize;
    private JSpinner spinnerEliteSize;
    private JComboBox<String> comboSelectionType;
    private JSpinner spinnerCombineProb;
    private JSpinner spinnerMutateProb;

    private JLabel labelStopGeneration;
    private JCheckBox checkboxStopGeneration;
    private JSpinner spinnerStopGenerations;

    private JLabel labelStopStall;
    private JCheckBox checkboxStopStall;
    private JSpinner spinnerStopStalled;

    private JLabel labelRandomSeed;
    private JCheckBox checkboxRandomSeed;
    private JTextField textfieldRandomSeed;

    private JButton buttonPlay;
    private JButton buttonPause;
    private JButton buttonStop;

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
                    "right:pref, 3dlu, right:pref, 3dlu, pref",
                    "pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 8dlu, pref");

            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            CellConstraints cc = new CellConstraints();

            /* @formatter:off */
            builder.addSeparator("Problema", cc.xyw(1, 1, 5));
            builder.addLabel("Problema:",    cc.xyw(1, 3, 3));
            builder.add(comboProblem,       cc.xy (5, 3));


            builder.addSeparator("Algoritmo",      cc.xyw(1,  5, 5));
            builder.addLabel("Tamaño Población:",  cc.xyw(1,  7, 3));
            builder.add(spinnerMinPopSize,         cc.xy (5,  7));
            builder.addLabel("Tamaño Élite:",      cc.xyw(1,  9, 3));
            builder.add(spinnerEliteSize,          cc.xy (5,  9));
            builder.addLabel("Selección:",         cc.xyw(1, 11, 3));
            builder.add(comboSelectionType,        cc.xy (5, 11));
            builder.addLabel("Prob. Combinación:", cc.xyw(1, 13, 3));
            builder.add(spinnerCombineProb,        cc.xy (5, 13));
            builder.addLabel("Prob. Mutación:",    cc.xyw(1, 15, 3));
            builder.add(spinnerMutateProb,         cc.xy (5, 15));


            builder.addSeparator("Parada",          cc.xyw(1, 17, 5));
            builder.add(checkboxStopGeneration,     cc.xy (1, 19));
            labelStopGeneration =
                builder.addLabel("Generaciones:",   cc.xy (3, 19));
            builder.add(spinnerStopGenerations,     cc.xy (5, 19));
            builder.add(checkboxStopStall,          cc.xy (1, 21));
            labelStopStall =
                builder.addLabel("Estancamiento:",  cc.xy (3, 21));
            builder.add(spinnerStopStalled,         cc.xy (5, 21));


            builder.addSeparator("Otros",         cc.xyw(1, 23, 5));
            builder.add(checkboxRandomSeed,       cc.xy (1, 25));
            labelRandomSeed =
                builder.addLabel("Semilla RNG:",  cc.xy (3, 25));
            builder.add(textfieldRandomSeed,      cc.xy (5, 25));


            builder.add(createLeftFormButtonPanel(), cc.xyw(1, 27, 5));
            /* @formatter:on */

            JPanel leftPanel = builder.getPanel();
            panel.add(leftPanel, BorderLayout.LINE_START);

            updateLeftForm();
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
        spinnerMinPopSize.setModel(new SpinnerNumberModel(1024, 32, 65536, 32));

        spinnerEliteSize = new JSpinner();
        spinnerEliteSize.setModel(new SpinnerNumberModel(8, 0, 65536, 1));

        comboSelectionType = new JComboBox<String>();
        comboSelectionType.setModel(new DefaultComboBoxModel<String>(new String[] { SEL_ROULETTE, SEL_TOURNAMENT }));

        spinnerCombineProb = new JSpinner();
        spinnerCombineProb.setModel(new SpinnerNumberModel(0.7, 0.0, 1.0, 0.05));

        spinnerMutateProb = new JSpinner();
        spinnerMutateProb.setModel(new SpinnerNumberModel(0.1, 0.0, 1.0, 0.05));

        checkboxStopGeneration = new JCheckBox();
        checkboxStopGeneration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent arg0) {
                updateLeftForm();
            }
        });

        spinnerStopGenerations = new JSpinner();
        spinnerStopGenerations.setModel(new SpinnerNumberModel(1024, 128, 65536, 8));

        checkboxStopStall = new JCheckBox();
        checkboxStopStall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent arg0) {
                updateLeftForm();
            }
        });

        spinnerStopStalled = new JSpinner();
        spinnerStopStalled.setModel(new SpinnerNumberModel(32, 4, 128, 1));

        checkboxRandomSeed = new JCheckBox();
        checkboxRandomSeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent arg0) {
                updateLeftForm();
            }
        });

        textfieldRandomSeed = new JTextField();

        buttonPlay = new JButton("\u25B6");

        buttonPause = new JButton("\u275A\u275A");

        buttonStop = new JButton("\u25FE");
    }

    private JPanel createLeftFormButtonPanel () {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(buttonPlay);
        panel.add(buttonPause);
        panel.add(buttonStop);

        return panel;
    }

    /* package */void updateLeftForm () {
        labelStopGeneration.setEnabled(checkboxStopGeneration.isSelected());
        spinnerStopGenerations.setEnabled(checkboxStopGeneration.isSelected());

        labelStopStall.setEnabled(checkboxStopStall.isSelected());
        spinnerStopStalled.setEnabled(checkboxStopStall.isSelected());

        labelRandomSeed.setEnabled(checkboxRandomSeed.isSelected());
        textfieldRandomSeed.setEnabled(checkboxRandomSeed.isSelected());
    }

    /** The user pressed an exit button */
    /* package */void actionExit () {
        dispose();
    }
}
