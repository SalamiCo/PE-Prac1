package pe1314.g11.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

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
public final class ResultsPanel extends JPanel {

    private ChartPanel chartPanel;
    private JFreeChart chart;

    private XYSeriesCollection dataset;
    private XYSeries seriesAverage;
    private XYSeries seriesMax;
    private XYSeries seriesMin;
    private XYSeries seriesBest;

    public ResultsPanel () {
        setupGui();
    }

    private void setupGui () {
        setLayout(new BorderLayout());

        chartPanel = new ChartPanel(null);
        add(chartPanel, BorderLayout.CENTER);

        clearChart();
    }

    public void clearChart () {
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
    }
}
