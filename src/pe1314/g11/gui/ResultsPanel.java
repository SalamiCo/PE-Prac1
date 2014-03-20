package pe1314.g11.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
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
    private JPanel tablePanel;

    private JFreeChart chart;

    private JSlider slider;
    private JTable table;
    private DefaultTableModel tableModel;

    private XYSeriesCollection dataset;
    private XYSeries seriesAverage;
    private XYSeries seriesMax;
    private XYSeries seriesMin;
    private XYSeries seriesBest;

    public ResultsPanel () {
        super(JSplitPane.VERTICAL_SPLIT);
        setupGui();
    }

    private void setupGui () {
        chartPanel = new ChartPanel(null);

        table = new JTable();
        slider = new JSlider();

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(table, BorderLayout.CENTER);

        setLeftComponent(chartPanel);
        setRightComponent(tablePanel);

        clearResults();
    }

    public void clearResults () {
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
        dataset.addSeries(seriesMax);
        dataset.addSeries(seriesMin);
        dataset.addSeries(seriesBest);

        chart = ChartFactory.createXYLineChart("Results", "Generación", "Fitness", dataset);

        chartPanel.setChart(chart);
    }

    private void clearTable () {
        tableModel = new DefaultTableModel(new String[] { "Chromosome", "Value", "Fitness" }, 1);

        table.setModel(tableModel);
    }

    public <V, C extends Chromosome<C>> void addGeneration (Problem<V,C> problem, int gen, List<C> population, C best) {
        final int len = population.size();

        double sum = 0;
        double sqsum = 0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for (C chromo : population) {
            double fitness = problem.fitness(chromo);

            max = Math.max(max, fitness);
            min = Math.min(min, fitness);
            sum += fitness;
            sqsum += fitness * fitness;
        }

        seriesMax.add(gen, max);
        seriesMin.add(gen, max);
        seriesAverage.add(gen, sum / len);
        seriesBest.add(gen, problem.fitness(best));

        for (C chromo : population) {
            tableModel.addRow(new String[] {
                chromo.toString(), problem.value(chromo).toString(), String.valueOf(problem.fitness(chromo)) });
        }
    }
}
