package pe1314.g11.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;
import pe1314.g11.Solver;
import pe1314.g11.Solver.Callbacks;
import pe1314.g11.SolverStep;
import pe1314.g11.SolverTrace;
import pe1314.g11.pr1.P1F1Problem;
import pe1314.g11.pr1.P1F2Problem;
import pe1314.g11.pr1.P1F3Problem;
import pe1314.g11.pr1.P1F4Problem;
import pe1314.g11.pr1.P1F5Problem;
import pe1314.g11.pr2.HeuristicMutationStep;
import pe1314.g11.pr2.OrderPriorityOrderCombinationStep;
import pe1314.g11.pr2.P2Problem;
import pe1314.g11.pr2.PositionPriorityOrderCombinationStep;
import pe1314.g11.pr3.LispChromosome;
import pe1314.g11.pr3.LispCombinationStep;
import pe1314.g11.pr3.LispList;
import pe1314.g11.pr3.LispMutationStep;
import pe1314.g11.pr3.SpaceInvadersProblem;
import pe1314.g11.sga.BinaryChromosome;
import pe1314.g11.sga.CombinationStep;
import pe1314.g11.sga.DuplicateRemovalStep;
import pe1314.g11.sga.InversionStep;
import pe1314.g11.sga.LengthedMutationStep;
import pe1314.g11.sga.MultiMutationStep;
import pe1314.g11.sga.PermutationChromosome;
import pe1314.g11.sga.RankingSelectionStep;
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
 * @author Pedro Morgado Alarc&oacute;n
 */
public final class MainFrame extends JFrame {

    private static final String PRB_P2_A = "(P2) Hospital (Ajuste)";
    private static final String PRB_P2_12 = "(P2) Hospital (12x12)";
    private static final String PRB_P2_15 = "(P2) Hospital (15x15)";
    private static final String PRB_P2_30 = "(P2) Hospital (30x30)";

    private static final String PRB_P1_F1 = "(P1) Funci\u00F3n 1";
    private static final String PRB_P1_F2 = "(P1) Funci\u00F3n 2";
    private static final String PRB_P1_F3 = "(P1) Funci\u00F3n 3";
    private static final String PRB_P1_F4 = "(P1) Funci\u00F3n 4";
    private static final String PRB_P1_F5 = "(P1) Funci\u00F3n 5";

    private static final String PRB_P3 = "(P3) Space Invaders";

    private static final String SEL_ROULETTE = "Ruleta";
    private static final String SEL_TOURNAMENT = "Torneo";
    private static final String SEL_RANKING = "Ranking";

    private static final String COMB_PMX = "Emp. Parcial";
    private static final String COMB_OX = "Orden";
    private static final String COMB_OX_POS = "Orden (Pos. Prior.)";
    private static final String COMB_OX_ORD = "Orden (Ord. Prior.)";
    private static final String COMB_CX = "Ciclos";
    private static final String COMB_ORDCOD = "Cod. Ordinal";
    private static final String COMB_RECOMB = "Recomb. Rutas";

    private static final String MUT_INVERSION = "Inversi\u00F3n";
    private static final String MUT_EXCHANGE = "Intercambio";
    private static final String MUT_INSERTION = "Inserci\u00F3n";
    private static final String MUT_HEURISTIC = "Heur\u00EDstica";
    private static final String MUT_ROTATION = "Rotaci\u00F3n";

    /** Generated SVUID */
    private static final long serialVersionUID = -8605437477715617439L;

    private JComboBox<String> comboProblem;
    private JLabel labelPrecission;
    private JSpinner spinnerPrecission;

    private JLabel labelExtra1;
    private JSpinner spinnerExtra1;

    private JSpinner spinnerMinPopSize;
    private JSpinner spinnerEliteSize;
    private JComboBox<String> comboSelectionType;
    private JLabel labelCombinationType;
    private JComboBox<String> comboCombinationType;
    private JLabel labelMutationType;
    private JComboBox<String> comboMutationType;
    private JSpinner spinnerCombineProb;
    private JSpinner spinnerMutateProb;
    private JSpinner spinnerInversionProb;

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

    /* package */SwingWorker<?,?> geneticWorker;
    /* package */volatile boolean stopWorker;

    /* package */ResultsPanel resultsPanel;

    /**
     * Creates an empty frame and fills it with the necessary components to work.
     */
    public MainFrame () {
        setTitle("Grupo 11 - Programaci\u00F3n Evolutiva");
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
        final JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        menubar.setVisible(true);

        { /* File Menu */
            final JMenu menu = new JMenu("File");
            menubar.add(menu);

            { /* Exit Option */
                final JMenuItem item = new JMenuItem("Exit");
                menu.add(item);

                item.setMnemonic('x');
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed (final ActionEvent evt) {
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
        final JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BorderLayout());

        { /* Left Form */
            createLeftFormElements();

            final FormLayout layout =
                new FormLayout(
                    "right:pref, 3dlu, right:pref, 3dlu, pref", //
                    "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, "
                        + "pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 8dlu, "
                        + "pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 2dlu, pref, 8dlu, pref");

            final PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();

            final CellConstraints cc = new CellConstraints();

            /* @formatter:off */
            builder.addSeparator("Problema", cc.xyw(1, 1, 5));
            builder.addLabel("Problema:",    cc.xyw(1, 3, 3));
            builder.add(comboProblem,        cc.xy (5, 3));
            labelPrecission =
                builder.addLabel("Precisi\u00F3n:",   cc.xyw(1, 5, 3));
            builder.add(spinnerPrecission,   cc.xy (5, 5));
            
            labelExtra1 =
                builder.addLabel("Valor de N:", cc.xyw(1, 7, 3));
            builder.add(spinnerExtra1,          cc.xy (5, 7));


            builder.addSeparator("Algoritmo",      cc.xyw(1,  9, 5));
            builder.addLabel("Tama\u00F1o Poblaci\u00F3n:",  cc.xyw(1, 11, 3));
            builder.add(spinnerMinPopSize,         cc.xy (5, 11));
            builder.addLabel("Tama\u00F1o \u00C9lite:",      cc.xyw(1, 13, 3));
            builder.add(spinnerEliteSize,          cc.xy (5, 13));
            builder.addLabel("Selecci\u00F3n:",         cc.xyw(1, 15, 3));
            builder.add(comboSelectionType,        cc.xy (5, 15));
            labelCombinationType = 
                builder.addLabel("Combinaci\u00F3n:",       cc.xyw(1, 17, 3));
            builder.add(comboCombinationType,      cc.xy (5, 17));
            builder.addLabel("Prob. Combinaci\u00F3n:", cc.xyw(1, 19, 3));
            builder.add(spinnerCombineProb,        cc.xy (5, 19));
            labelMutationType =
                builder.addLabel("Mutaci\u00F3n:",          cc.xyw(1, 21, 3));
            builder.add(comboMutationType,         cc.xy (5, 21));
            builder.addLabel("Prob. Mutaci\u00F3n:",    cc.xyw(1, 23, 3));
            builder.add(spinnerMutateProb,         cc.xy (5, 23));
            builder.addLabel("Prob. Inversi\u00F3n:",   cc.xyw(1, 25, 3));
            builder.add(spinnerInversionProb,      cc.xy (5, 25));


            builder.addSeparator("Parada",          cc.xyw(1, 27, 5));
            builder.add(checkboxStopGeneration,     cc.xy (1, 29));
            labelStopGeneration =
                builder.addLabel("Generaciones:",   cc.xy (3, 29));
            builder.add(spinnerStopGenerations,     cc.xy (5, 29));
            builder.add(checkboxStopStall,          cc.xy (1, 31));
            labelStopStall =
                builder.addLabel("Estancamiento:",  cc.xy (3, 31));
            builder.add(spinnerStopStalled,         cc.xy (5, 31));


            builder.addSeparator("Otros",         cc.xyw(1, 33, 5));
            builder.add(checkboxRandomSeed,       cc.xy (1, 35));
            labelRandomSeed =
                builder.addLabel("Semilla RNG:",  cc.xy (3, 35));
            builder.add(textfieldRandomSeed,      cc.xy (5, 35));


            builder.add(createLeftFormButtonPanel(), cc.xyw(1, 37, 5));
            /* @formatter:on */

            final JPanel leftPanel = builder.getPanel();
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
        comboProblem
            .setModel(new DefaultComboBoxModel<String>(new String[] {
                PRB_P3, PRB_P2_A, PRB_P2_12, PRB_P2_15, PRB_P2_30, PRB_P1_F1, PRB_P1_F2, PRB_P1_F3, PRB_P1_F4,
                PRB_P1_F5 }));
        comboProblem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (final ActionEvent arg0) {
                updateLeftForm();
            }
        });

        spinnerExtra1 = new JSpinner();
        spinnerExtra1.setModel(new SpinnerNumberModel(3, 1, 7, 1));

        spinnerPrecission = new JSpinner();
        spinnerPrecission.setModel(new SpinnerNumberModel(0.001, 0.000000001, 1, 0.0001));
        spinnerPrecission.setEditor(new JSpinner.NumberEditor(spinnerPrecission, "0.#########"));

        spinnerMinPopSize = new JSpinner();
        spinnerMinPopSize.setModel(new SpinnerNumberModel(100, 10, 100000, 10));

        spinnerEliteSize = new JSpinner();
        spinnerEliteSize.setModel(new SpinnerNumberModel(0.03, 0, 0.5, 0.005));

        comboSelectionType = new JComboBox<String>();
        comboSelectionType.setModel(new DefaultComboBoxModel<String>(new String[] {
            SEL_ROULETTE, SEL_TOURNAMENT, SEL_RANKING }));

        comboCombinationType = new JComboBox<String>();
        comboCombinationType.setModel(new DefaultComboBoxModel<String>(new String[] {
            COMB_PMX, COMB_OX, COMB_OX_POS, COMB_OX_ORD, COMB_CX, COMB_ORDCOD, COMB_RECOMB }));

        comboMutationType = new JComboBox<String>();
        comboMutationType.setModel(new DefaultComboBoxModel<String>(new String[] {
            MUT_INVERSION, MUT_EXCHANGE, MUT_INSERTION, MUT_HEURISTIC, MUT_ROTATION }));

        spinnerCombineProb = new JSpinner();
        spinnerCombineProb.setModel(new SpinnerNumberModel(0.6, 0.0, 1.0, 0.05));

        spinnerMutateProb = new JSpinner();
        spinnerMutateProb.setModel(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.01));

        spinnerInversionProb = new JSpinner();
        spinnerInversionProb.setModel(new SpinnerNumberModel(0.1, 0.0, 1.0, 0.05));

        checkboxStopGeneration = new JCheckBox();
        checkboxStopGeneration.setSelected(true);
        checkboxStopGeneration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (final ActionEvent arg0) {
                updateLeftForm();
            }
        });

        spinnerStopGenerations = new JSpinner();
        spinnerStopGenerations.setModel(new SpinnerNumberModel(100, 10, 100000, 10));

        checkboxStopStall = new JCheckBox();
        checkboxStopStall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (final ActionEvent arg0) {
                updateLeftForm();
            }
        });

        spinnerStopStalled = new JSpinner();
        spinnerStopStalled.setModel(new SpinnerNumberModel(50, 10, 1000, 10));

        checkboxRandomSeed = new JCheckBox();
        checkboxRandomSeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (final ActionEvent arg0) {
                updateLeftForm();
            }
        });

        textfieldRandomSeed = new JTextField();

        buttonPlay = new JButton("\u25B6");
        buttonPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (final ActionEvent arg0) {
                clickedPlay();
            }
        });

        buttonPause = new JButton("\u275A\u275A");

        buttonStop = new JButton("\u25FE");
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (final ActionEvent arg0) {
                clickedStop();
            }
        });
    }

    private JPanel createLeftFormButtonPanel () {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(buttonPlay);
        // panel.add(buttonPause);
        panel.add(buttonStop);

        return panel;
    }

    private int obtainPNum () {
        if (Arrays.asList(PRB_P1_F1, PRB_P1_F2, PRB_P1_F3, PRB_P1_F4, PRB_P1_F5).contains(
            comboProblem.getSelectedItem()))
        {
            return 1;
        }

        if (Arrays.asList(PRB_P2_A, PRB_P2_12, PRB_P2_15, PRB_P2_30).contains(comboProblem.getSelectedItem())) {
            return 2;
        }

        if (comboProblem.getSelectedItem().equals(PRB_P3)) {
            return 3;
        }

        return 0;
    }

    /* package */void updateLeftForm () {
        if (geneticWorker == null) {
            final boolean p1 = obtainPNum() == 1;
            final boolean p2 = obtainPNum() == 2;

            buttonPlay.setEnabled(true);
            buttonPause.setEnabled(false);
            buttonStop.setEnabled(false);

            comboProblem.setEnabled(true);
            labelPrecission.setEnabled(p1);
            spinnerPrecission.setEnabled(p1);

            spinnerMinPopSize.setEnabled(true);
            spinnerEliteSize.setEnabled(true);
            comboSelectionType.setEnabled(true);
            comboCombinationType.setEnabled(p2);
            labelCombinationType.setEnabled(p2);
            comboMutationType.setEnabled(p2);
            labelMutationType.setEnabled(p2);
            spinnerMutateProb.setEnabled(true);
            spinnerCombineProb.setEnabled(true);
            spinnerInversionProb.setEnabled(p2);

            checkboxStopGeneration.setEnabled(true);
            checkboxStopStall.setEnabled(true);
            checkboxRandomSeed.setEnabled(true);

            final boolean extra1 = comboProblem.getSelectedItem().equals(PRB_P1_F4);
            labelExtra1.setEnabled(extra1);
            spinnerExtra1.setEnabled(extra1);

            labelStopGeneration.setEnabled(checkboxStopGeneration.isSelected());
            spinnerStopGenerations.setEnabled(checkboxStopGeneration.isSelected());

            labelStopStall.setEnabled(checkboxStopStall.isSelected());
            spinnerStopStalled.setEnabled(checkboxStopStall.isSelected());

            labelRandomSeed.setEnabled(checkboxRandomSeed.isSelected());
            textfieldRandomSeed.setEnabled(checkboxRandomSeed.isSelected());

        } else {
            buttonPlay.setEnabled(false);
            buttonPause.setEnabled(true);
            buttonStop.setEnabled(true);

            comboProblem.setEnabled(false);
            spinnerPrecission.setEnabled(false);
            spinnerExtra1.setEnabled(false);

            spinnerMinPopSize.setEnabled(false);
            spinnerEliteSize.setEnabled(false);
            comboSelectionType.setEnabled(false);
            comboCombinationType.setEnabled(false);
            comboMutationType.setEnabled(false);
            spinnerMutateProb.setEnabled(false);
            spinnerCombineProb.setEnabled(false);
            spinnerInversionProb.setEnabled(false);

            checkboxStopGeneration.setEnabled(false);
            spinnerStopGenerations.setEnabled(false);
            checkboxStopStall.setEnabled(false);
            spinnerStopStalled.setEnabled(false);

            checkboxRandomSeed.setEnabled(false);
            textfieldRandomSeed.setEnabled(false);
        }
    }

    /** The user pressed an exit button */
    /* package */void actionExit () {
        stopWorker = true;
        dispose();
    }

    /* package */void clickedPlay () {
        final String problemName = (String) comboProblem.getSelectedItem();
        final double precission = ((Number) spinnerPrecission.getValue()).doubleValue();
        switch (problemName) {
            case PRB_P2_A:
                solveHospitalProblem("ajuste");
                break;
            case PRB_P2_12:
                solveHospitalProblem("tai12");
                break;
            case PRB_P2_15:
                solveHospitalProblem("tai15");
                break;
            case PRB_P2_30:
                solveHospitalProblem("tai30");
                break;

            case PRB_P1_F1:
                solveBinaryProblem(new P1F1Problem(precission));
                break;
            case PRB_P1_F2:
                solveBinaryProblem(new P1F2Problem(precission));
                break;
            case PRB_P1_F3:
                solveBinaryProblem(new P1F3Problem(precission));
                break;
            case PRB_P1_F4:
                final int n = ((Number) spinnerExtra1.getValue()).intValue();
                solveBinaryProblem(new P1F4Problem(precission, n));
                break;
            case PRB_P1_F5:
                solveBinaryProblem(new P1F5Problem(precission));

            case PRB_P3:
                solveSpaceInvadersProblem();
        }
    }

    /* package */void clickedStop () {
        stopWorker = true;
    }

    // =========================
    // === GENERIC OBTAINERS ===

    private Random obtainRandomGenerator () {
        final boolean rngIsSelected = checkboxRandomSeed.isSelected();
        final String seed = rngIsSelected ? textfieldRandomSeed.getText() : String.valueOf(System.nanoTime());
        textfieldRandomSeed.setText(seed);
        return new XorShiftRandom(seed.hashCode());
    }

    private <V, C extends Chromosome<C>> SolverStep<V,C> obtainGenerationStep () {
        final int populationSize = ((Number) spinnerMinPopSize.getValue()).intValue();
        return new RandomGenerationStep<>(populationSize, 0);
    }

    private <V, C extends Chromosome<C>> SolverStep<V,C> obtainSelectionStep () {
        final String selection = (String) comboSelectionType.getSelectedItem();
        final int tournamentSize = 8;

        switch (selection) {
            case SEL_ROULETTE:
                return new RouletteSelectionStep<>();

            case SEL_TOURNAMENT:
                return new TournamentSelectionStep<>(tournamentSize);

            case SEL_RANKING:
                return new RankingSelectionStep<>();
        }

        return null;
    }

    private int obtainCombinationType () {
        switch (comboCombinationType.getSelectedItem().toString()) {
            case COMB_PMX:
                return PermutationChromosome.COMBINATION_PMX;
            case COMB_OX:
                return PermutationChromosome.COMBINATION_OX;
            case COMB_CX:
                return PermutationChromosome.COMBINATION_CX;
            case COMB_ORDCOD:
                return PermutationChromosome.COMBINATION_ORDCOD;
            case COMB_RECOMB:
                return PermutationChromosome.COMBINATION_RECOMB;
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    private <V, C extends Chromosome<C>> SolverStep<V,C> obtainCombinationStep () {
        final double combineProb = ((Number) spinnerCombineProb.getValue()).doubleValue();

        if (comboCombinationType.getSelectedItem().toString().equals(COMB_OX_POS)) {
            return (SolverStep<V,C>) new PositionPriorityOrderCombinationStep<V>(combineProb);
        }
        if (comboCombinationType.getSelectedItem().toString().equals(COMB_OX_ORD)) {
            return (SolverStep<V,C>) new OrderPriorityOrderCombinationStep<V>(combineProb);
        }

        switch (obtainPNum()) {
            case 1:
                return new CombinationStep<>(combineProb, 0);
            case 2:
                return new CombinationStep<>(combineProb, obtainCombinationType());
            case 3:
                return (SolverStep<V,C>) new LispCombinationStep(combineProb);
        }

        return null;
    }

    private int obtainMutationType () {
        switch (comboMutationType.getSelectedItem().toString()) {
            case MUT_EXCHANGE:
                return PermutationChromosome.MUTATION_EXCHANGE;
            case MUT_INSERTION:
                return PermutationChromosome.MUTATION_INSERTION;
            case MUT_INVERSION:
                return PermutationChromosome.MUTATION_INVERSION;
            case MUT_ROTATION:
                return PermutationChromosome.MUTATION_ROTATION;
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    private <V, C extends Chromosome<C>> SolverStep<V,C> obtainMutationStep () {
        final double mutateProb = ((Number) spinnerMutateProb.getValue()).doubleValue();

        if (comboMutationType.getSelectedItem().toString().equals(MUT_HEURISTIC)) {
            return (SolverStep<V,C>) new HeuristicMutationStep<V>(mutateProb);
        }

        switch (obtainPNum()) {
            case 1:
                return new MultiMutationStep<>(mutateProb, 0);
            case 2:
                return new LengthedMutationStep<>(mutateProb, obtainMutationType());
            case 3:
                return (SolverStep<V,C>) new LispMutationStep(mutateProb);
        }

        return null;
    }

    private <V, C extends Chromosome<C>> SolverStep<V,C> obtainInversionStep () {
        final double inversionProb = ((Number) spinnerInversionProb.getValue()).doubleValue();

        return new InversionStep<>(inversionProb);
    }

    private <V, C extends Chromosome<C>> ElitismStepPair<V,C> obtainEletismPair () {
        final double eliteSize = ((Number) spinnerEliteSize.getValue()).doubleValue();
        return new ElitismStepPair<>(eliteSize);
    }

    private <V, C extends Chromosome<C>> SolverWorker<V,C> obtainWorker (final Solver<V,C> solver, final Random random)
    {
        final boolean generationsIsChecked = checkboxStopGeneration.isSelected();
        final int generations = generationsIsChecked ? ((Number) spinnerStopGenerations.getValue()).intValue() : 0;
        final boolean stallIsChecked = checkboxStopStall.isSelected();
        final int stall = stallIsChecked ? ((Number) spinnerStopStalled.getValue()).intValue() : 0;

        if (!generationsIsChecked && !stallIsChecked) {
            JOptionPane.showMessageDialog(
                this, "Elige al menos una condici\u00F3n de parada", "Formulario incompleto",
                JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new SolverWorker<V,C>(
            solver, SwingSolverCallbackHelper.wrap(new SolverCallbacks<V,C>(generations, stall)), random);
    }

    private <V> void solveBinaryProblem (final Problem<V,BinaryChromosome> problem) {
        final Random random = obtainRandomGenerator();

        final ElitismStepPair<V,BinaryChromosome> esp = obtainEletismPair();
        final SolverStep<V,BinaryChromosome> generationStep = obtainGenerationStep();
        final SolverStep<V,BinaryChromosome> selectionStep = obtainSelectionStep();
        final SolverStep<V,BinaryChromosome> combinationStep = obtainCombinationStep();
        final SolverStep<V,BinaryChromosome> mutationStep = obtainMutationStep();

        /* @formatter:off */
        final Solver<V, BinaryChromosome> solver = Solver.builder(problem)
            .step(generationStep)
            .step(esp.getSaveStep())
            .step(selectionStep)
            .step(combinationStep)
            .step(mutationStep)
            .step(esp.getRestoreStep())
            .build();
        /* @formatter:on */

        geneticWorker = obtainWorker(solver, random);
        if (geneticWorker != null) {
            geneticWorker.execute();
        }
    }

    private void solveHospitalProblem (final String fname) {
        try {
            final P2Problem problem =
                P2Problem.readFromURL(MainFrame.class.getResource("/pe1314/g11/pr2/" + fname + ".dat"));

            final Random random = obtainRandomGenerator();

            final ElitismStepPair<List<Integer>,PermutationChromosome> esp = obtainEletismPair();
            final SolverStep<List<Integer>,PermutationChromosome> generationStep = obtainGenerationStep();
            final SolverStep<List<Integer>,PermutationChromosome> selectionStep = obtainSelectionStep();
            final SolverStep<List<Integer>,PermutationChromosome> combinationStep = obtainCombinationStep();
            final SolverStep<List<Integer>,PermutationChromosome> mutationStep = obtainMutationStep();
            final SolverStep<List<Integer>,PermutationChromosome> inversionStep = obtainInversionStep();
            final SolverStep<List<Integer>,PermutationChromosome> dedupStep = new DuplicateRemovalStep<>();

            /* @formatter:off */
            final Solver<List<Integer>, PermutationChromosome> solver = Solver.builder(problem)
                .step(generationStep)
                .step(esp.getSaveStep())
                .step(selectionStep)
                .step(combinationStep)
                .step(mutationStep)
                .step(inversionStep)
                .step(esp.getRestoreStep())
                .step(dedupStep)
                .build();
            /* @formatter:on */

            geneticWorker = obtainWorker(solver, random);
            if (geneticWorker != null) {
                stopWorker = false;
                geneticWorker.execute();
            }
        } catch (final IOException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de entrada/salida:\n" + exc, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void solveSpaceInvadersProblem () {
        final SpaceInvadersProblem problem = new SpaceInvadersProblem();

        final Random random = obtainRandomGenerator();

        final ElitismStepPair<LispList,LispChromosome> esp = obtainEletismPair();
        final SolverStep<LispList,LispChromosome> generationStep = obtainGenerationStep();
        final SolverStep<LispList,LispChromosome> selectionStep = obtainSelectionStep();
        final SolverStep<LispList,LispChromosome> combinationStep = obtainCombinationStep();
        final SolverStep<LispList,LispChromosome> mutationStep = obtainMutationStep();
        final SolverStep<LispList,LispChromosome> dedupStep = new DuplicateRemovalStep<>();

        /* @formatter:off */
        final Solver<LispList,LispChromosome> solver = Solver.builder(problem)
            .step(generationStep)
            .step(esp.getSaveStep())
            .step(selectionStep)
            .step(combinationStep)
            .step(mutationStep)
            .step(esp.getRestoreStep())
            .step(dedupStep)
            .build();
        /* @formatter:on */

        geneticWorker = obtainWorker(solver, random);
        if (geneticWorker != null) {
            stopWorker = false;
            geneticWorker.execute();
        }
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

        /* package */SolverCallbacks (final int generations, final int stalled) {
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

            return stopWorker;
        }

        @Override
        public void startProcess (final Solver<V,C> psolver) {
            solver = psolver;
            best = null;
            currentStall = 0;
            currentGeneration = 0;

            resultsPanel.clearResults();
            updateLeftForm();
        }

        @Override
        public void startGeneration (final int gen, final List<C> population) {
            currentGeneration = gen;
        }

        @Override
        public void startStep (final SolverStep<V,C> step, final List<C> population) {
        }

        @Override
        public void endStep (final List<C> population) {
        }

        @Override
        public void endGeneration (final List<C> population) {
            final Comparator<C> comp = new FitnessComparator<>(solver.getProblem());

            C nextBest = null;
            for (final C chromo : population) {
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
        public void endProcess (final SolverTrace<V,C> trace) {
            solver = null;
            best = null;

            geneticWorker = null;
            updateLeftForm();
        }

    }
}
