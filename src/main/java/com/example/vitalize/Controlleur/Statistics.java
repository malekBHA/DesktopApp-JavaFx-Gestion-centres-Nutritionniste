package com.example.vitalize.Controlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Service.ReclamationService;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics {

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Long> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private final ReclamationService rs = new ReclamationService();

    @FXML
    public void initialize() {
        // Retrieve type counts
        Map<String, Long> typeCounts = getReclamationTypeCounts();
        Map<String, Long> etatCounts = getReclamationEtatCounts();

        // Calculate total count for etat
        long totalEtatCount = etatCounts.values().stream().mapToLong(Long::longValue).sum();

        // Create PieChart.Data for each type with percentage
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
        typeCounts.forEach((type, count) -> {
            double percentage = (double) count / totalEtatCount;
            String formattedPercentage = decimalFormat.format(percentage);
            PieChart.Data data = new PieChart.Data(type + " (" + formattedPercentage + ")", count);
            pieChartData.add(data);
        });

        // Set data to PieChart
        pieChart.setData(pieChartData);
        pieChart.setTitle("Reclamation Types Distribution");

        // Add tooltips showing count and percentage to PieChart
        pieChartData.forEach(data -> {
            Tooltip tooltip = new Tooltip(String.format("%d (%s)", (int) data.getPieValue(), data.getName().split(" \\(")[1]));
            Tooltip.install(data.getNode(), tooltip);
        });

        // Create BarChart series for etat counts
        XYChart.Series<String, Long> series = new XYChart.Series<>();
        etatCounts.forEach((etat, count) -> series.getData().add(new XYChart.Data<>(etat, count)));
        barChart.getData().add(series);
        barChart.setTitle("Reclamation Etat Distribution");
        xAxis.setLabel("Etat");
        yAxis.setLabel("Count");

        // Add tooltips showing count to BarChart
        series.getData().forEach(data -> {
            Tooltip tooltip = new Tooltip(String.valueOf(data.getYValue()));
            Tooltip.install(data.getNode(), tooltip);
        });
    }

    private Map<String, Long> getReclamationTypeCounts() {
        // Implement this method to retrieve and count reclamations by type
        List<Reclamation> reclamations = rs.getAll();
        Map<String, Long> typeCounts = reclamations.stream()
                .collect(Collectors.groupingBy(Reclamation::getType, Collectors.counting()));
        return typeCounts;
    }

    private Map<String, Long> getReclamationEtatCounts() {
        // Implement this method to retrieve and count reclamations by etat
        List<Reclamation> reclamations = rs.getAll();
        Map<String, Long> etatCounts = reclamations.stream()
                .collect(Collectors.groupingBy(Reclamation::getEtat, Collectors.counting()));
        return etatCounts;
    }
}
