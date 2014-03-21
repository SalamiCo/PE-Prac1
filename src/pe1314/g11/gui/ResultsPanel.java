package pe1314.g11.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pe1314.g11.Chromosome;
import pe1314.g11.Problem;

/**
 * A Swing Panel that shows the results of a genetic algorithm run.
 * 
 * @author Daniel Escoz solana
 * @author Pedro Morgado Alarcón
 */
public final class ResultsPanel extends JSplitPane {

    private ChartPanel chartPanel;
    private JComponent tablePanel;

    private JFreeChart chart;

    private JSlider slider;
    private JTable table;
    private DefaultTableModel tableModel;

    private XYSeriesCollection dataset;
    private XYSeries seriesAverage;
    private XYSeries seriesMax;
    private XYSeries seriesMin;
    private XYSeries seriesBest;

    private final List<List<String[]>> tables = new ArrayList<>();

    public ResultsPanel () {
        super(JSplitPane.VERTICAL_SPLIT);
        setupGui();
    }

    private void setupGui () {
        chartPanel = new ChartPanel(null);

        table = new JTable();

        slider = new JSlider();
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged (ChangeEvent evt) {
                sliderChange();
            }
        });

        tablePanel = Box.createVerticalBox();
        tablePanel.add(slider);
        tablePanel.add(table);

        setLeftComponent(chartPanel);
        setRightComponent(new JScrollPane(tablePanel));

        clearResults();
    }

    public void clearResults () {
        slider.setMinimum(0);
        slider.setMaximum(0);
        slider.setEnabled(false);

        tables.clear();

        clearChart();
        clearTable();
    }

    private void clearChart () {
        seriesAverage = new XYSeries("Media");
        seriesMax = new XYSeries("Máximo");
        seriesMin = new XYSeries("Minimo");
        seriesBest = new XYSeries("Mejor");

        dataset = new XYSeriesCollection();
        dataset.addSeries(seriesAverage);
        dataset.addSeries(seriesBest);
        dataset.addSeries(seriesMax);
        dataset.addSeries(seriesMin);

        chart = ChartFactory.createXYLineChart("Results", "Generación", "Fitness", dataset);

        chartPanel.setChart(chart);
    }

    private void clearTable () {
        tableModel = new DefaultTableModel(new String[][] { { "Chromosome", "Value", "Fitness" } }, //
            new String[] { "Chromosome", "Value", "Fitness" });

        table.setModel(tableModel);
    }

    private void updateTable (List<String[]> rows) {
        clearTable();
        for (String[] row : rows) {
            tableModel.addRow(row);
        }
    }

    private void updateSlider (int num) {
        slider.setMinimum(0);
        slider.setMaximum(num);
        int tickspace = (int) Math.log(num);
        slider.setMajorTickSpacing(Math.max(10, tickspace * 10));
        slider.setMinorTickSpacing(Math.max(1, tickspace));
        slider.setValue(num);
        slider.setEnabled(true);
    }

    /* package */void sliderChange () {
        if (!tables.isEmpty()) {
            updateTable(tables.get(slider.getValue()));
        }
    }

    public <V, C extends Chromosome<C>> void addGeneration (Problem<V,C> problem, int gen, List<C> population, C best) {
        final int len = population.size();

        double sum = 0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for (C chromo : population) {
            double fitness = problem.fitness(chromo);

            max = Math.max(max, fitness);
            min = Math.min(min, fitness);
            sum += fitness;
        }

        seriesMax.add(gen, max);
        seriesMin.add(gen, min);
        seriesAverage.add(gen, sum / len);
        seriesBest.add(gen, problem.fitness(best));

        List<String[]> rows = new ArrayList<>();
        clearTable();
        for (C chromo : population) {
            rows.add(new String[] { //
                chromo.toString(), problem.value(chromo).toString(), String.valueOf(problem.fitness(chromo)) });
        }

        tables.add(rows);
        updateTable(rows);
        updateSlider(gen);
    }
}
