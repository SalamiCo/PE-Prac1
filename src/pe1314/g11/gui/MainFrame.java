package pe1314.g11.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

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

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.Solver;
import pe1314.g11.Solver.Callbacks;
import pe1314.g11.SolverStep;
import pe1314.g11.SolverTrace;
import pe1314.g11.pr1.P1F1Problem;
import pe1314.g11.pr1.P1F2Problem;
import pe1314.g11.sga.BinaryChromosome;
import pe1314.g11.sga.BinaryCombinationStep;
import pe1314.g11.sga.BinaryMutationStep;
import pe1314.g11.sga.RouletteSelectionStep;
import pe1314.g11.sga.TournamentSelectionStep;
import pe1314.g11.util.ElitismStepPair;
import pe1314.g11.util.FitnessComparator;
import pe1314.g11.util.RandomGenerationStep;
import pe1314.g11.util.XorShiftRandom;

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
    private JSpinner spinnerPrecission;

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

    /* package */ResultsPanel resultsPanel;

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
                    "pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 8dlu, pref");

            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            CellConstraints cc = new CellConstraints();

            /* @formatter:off */
            builder.addSeparator("Problema", cc.xyw(1, 1, 5));
            builder.addLabel("Problema:",    cc.xyw(1, 3, 3));
            builder.add(comboProblem,        cc.xy (5, 3));
            builder.addLabel("Precisión:",   cc.xyw(1, 5, 3));
            builder.add(spinnerPrecission,   cc.xy (5, 5));


            builder.addSeparator("Algoritmo",      cc.xyw(1,  7, 5));
            builder.addLabel("Tamaño Población:",  cc.xyw(1,  9, 3));
            builder.add(spinnerMinPopSize,         cc.xy (5,  9));
            builder.addLabel("Tamaño Élite:",      cc.xyw(1, 11, 3));
            builder.add(spinnerEliteSize,          cc.xy (5, 11));
            builder.addLabel("Selección:",         cc.xyw(1, 13, 3));
            builder.add(comboSelectionType,        cc.xy (5, 13));
            builder.addLabel("Prob. Combinación:", cc.xyw(1, 15, 3));
            builder.add(spinnerCombineProb,        cc.xy (5, 15));
            builder.addLabel("Prob. Mutación:",    cc.xyw(1, 17, 3));
            builder.add(spinnerMutateProb,         cc.xy (5, 17));


            builder.addSeparator("Parada",          cc.xyw(1, 19, 5));
            builder.add(checkboxStopGeneration,     cc.xy (1, 21));
            labelStopGeneration =
                builder.addLabel("Generaciones:",   cc.xy (3, 21));
            builder.add(spinnerStopGenerations,     cc.xy (5, 21));
            builder.add(checkboxStopStall,          cc.xy (1, 23));
            labelStopStall =
                builder.addLabel("Estancamiento:",  cc.xy (3, 23));
            builder.add(spinnerStopStalled,         cc.xy (5, 23));


            builder.addSeparator("Otros",         cc.xyw(1, 25, 5));
            builder.add(checkboxRandomSeed,       cc.xy (1, 27));
            labelRandomSeed =
                builder.addLabel("Semilla RNG:",  cc.xy (3, 27));
            builder.add(textfieldRandomSeed,      cc.xy (5, 27));


            builder.add(createLeftFormButtonPanel(), cc.xyw(1, 29, 5));
            /* @formatter:on */

            JPanel leftPanel = builder.getPanel();
            panel.add(leftPanel, BorderLayout.LINE_START);

            updateLeftForm();
        }

        { /* Center Content Panel */
            resultsPanel = new ResultsPanel();
            panel.add(resultsPanel, BorderLayout.CENTER);
        }

        panel.invalidate();
    }

    private void createLeftFormElements () {
        comboProblem = new JComboBox<String>();
        comboProblem.setModel(new DefaultComboBoxModel<String>(new String[] {
            PRB_P1_F1, PRB_P1_F2, PRB_P1_F3, PRB_P1_F4, PRB_P1_F5 }));

        spinnerPrecission = new JSpinner();
        spinnerPrecission.setModel(new SpinnerNumberModel(0.001, 0.00001, 1, 0.00001));

        spinnerMinPopSize = new JSpinner();
        spinnerMinPopSize.setModel(new SpinnerNumberModel(64, 8, 65536, 8));

        spinnerEliteSize = new JSpinner();
        spinnerEliteSize.setModel(new SpinnerNumberModel(0.01, 0, 0.5, 0.005));

        comboSelectionType = new JComboBox<String>();
        comboSelectionType.setModel(new DefaultComboBoxModel<String>(new String[] { SEL_ROULETTE, SEL_TOURNAMENT }));

        spinnerCombineProb = new JSpinner();
        spinnerCombineProb.setModel(new SpinnerNumberModel(0.6, 0.0, 1.0, 0.05));

        spinnerMutateProb = new JSpinner();
        spinnerMutateProb.setModel(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.001));

        checkboxStopGeneration = new JCheckBox();
        checkboxStopGeneration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent arg0) {
                updateLeftForm();
            }
        });

        spinnerStopGenerations = new JSpinner();
        spinnerStopGenerations.setModel(new SpinnerNumberModel(64, 8, 65536, 8));

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
        buttonPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent arg0) {
                clickedPlay();
            }
        });

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

    /* package */void clickedPlay () {
        String problemName = (String) comboProblem.getSelectedItem();
        double precission = ((Number) spinnerPrecission.getValue()).doubleValue();
        switch (problemName) {
            case PRB_P1_F1:
                solveBinaryProblem(new P1F1Problem(precission));
                break;
            case PRB_P1_F2:
                solveBinaryProblem(new P1F2Problem(precission));
        }
    }

    private <V> void solveBinaryProblem (Problem<V,BinaryChromosome> problem) {
        int populationSize = ((Number) spinnerMinPopSize.getValue()).intValue();
        double eliteSize = ((Number) spinnerEliteSize.getValue()).doubleValue();
        String selection = (String) comboSelectionType.getSelectedItem();
        double combineProb = ((Number) spinnerCombineProb.getValue()).doubleValue();
        double mutateProb = ((Number) spinnerMutateProb.getValue()).doubleValue();
        boolean generationsIsChecked = checkboxStopGeneration.isSelected();
        int generations = generationsIsChecked ? ((Number) spinnerStopGenerations.getValue()).intValue() : 0;
        boolean stallIsChecked = checkboxStopStall.isSelected();
        int stall = stallIsChecked ? ((Number) spinnerStopStalled.getValue()).intValue() : 0;

        ElitismStepPair<V,BinaryChromosome> esp = new ElitismStepPair<>(eliteSize);

        SolverStep<V,BinaryChromosome> selectionStep;
        int tournamentSize = 8;

        switch (selection) {
            case SEL_ROULETTE:
                selectionStep = new RouletteSelectionStep<>();
                break;

            case SEL_TOURNAMENT:
                selectionStep = new TournamentSelectionStep<>(tournamentSize);
                break;

            default:
                return;
        }

        /* @formatter:off */
        Solver<V, BinaryChromosome> solver = Solver.builder(problem)
            .step(new RandomGenerationStep<V,BinaryChromosome>(populationSize, 0))
            .step(esp.getSaveStep())
            .step(selectionStep)
            .step(new BinaryCombinationStep<V>(combineProb))
            .step(new BinaryMutationStep<V>(mutateProb))
            .step(esp.getRestoreStep())
            .build();
        /* @formatter:on */

        SolverWorker<V,BinaryChromosome> worker =
            new SolverWorker<V,BinaryChromosome>(
                solver, SwingSolverCallbackHelper.wrap(new SolverCallbacks<V,BinaryChromosome>(generations, stall)),
                new XorShiftRandom());
        worker.execute();
    }

    private final class SolverCallbacks<V, C extends Chromosome<C>> implements Callbacks<V,C> {

        /** Maximum generations to run */
        private final int generations;

        /** Maximum generations for the best fitness to be stalled */
        private final int stalled;

        /** Solver used on the process */
        private volatile Solver<V,C> solver;

        /** Best chromosome seen */
        private volatile C best;

        /** Currently running generation */
        private volatile int currentGeneration;

        /** Currently running generation */
        private volatile int currentStall;

        /* package */SolverCallbacks (int generations, int stalled) {
            this.generations = generations;
            this.stalled = stalled;
        }

        @Override
        public boolean shouldStop () {
            if (generations > 0 && currentGeneration >= generations) {
                return true;
            }

            if (stalled > 0 && currentStall >= stalled) {
                return true;
            }

            return false;
        }

        @Override
        public void startProcess (Solver<V,C> psolver) {
            solver = psolver;
            best = null;
            currentStall = 0;
            currentGeneration = 0;

            resultsPanel.clearResults();
        }

        @Override
        public void startGeneration (int gen, List<C> population) {
            currentGeneration = gen;
        }

        @Override
        public void startStep (SolverStep<V,C> step, List<C> population) {
            // TODO Auto-generated method stub

        }

        @Override
        public void endStep (List<C> population) {
            // TODO Auto-generated method stub

        }

        @Override
        public void endGeneration (List<C> population) {
            Comparator<C> comp = new FitnessComparator<>(solver.getProblem());

            C nextBest = null;
            for (C chromo : population) {
                if (nextBest == null || comp.compare(chromo, nextBest) < 0) {
                    nextBest = chromo;
                }
            }

            if (best == null || (nextBest != null && comp.compare(nextBest, best) < 0)) {
                currentStall = 0;
                best = nextBest;

            } else {
                currentStall++;
            }

            resultsPanel.addGeneration(solver.getProblem(), currentGeneration, population, best);
        }

        @Override
        public void endProcess (SolverTrace<V,C> trace) {
            solver = null;
            best = null;
        }

    }
}
