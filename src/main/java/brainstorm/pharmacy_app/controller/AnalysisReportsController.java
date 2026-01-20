package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.RapportIM;
import brainstorm.pharmacy_app.Model.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;

public class AnalysisReportsController {

    // li bchtkhtar behom date
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    // 3 taa stock
    @FXML private Label lblTotalProducts;
    @FXML private Label lblLowStockProducts;
    @FXML private Label lblOutOfStockProducts;
    @FXML private Button btnStockReport; // btn taa stock full report

    // labelllllls
    @FXML private Label lblTotalRevenue;
    @FXML private Label lblNbSales;
    @FXML private Label lblAverageBasket;
    @FXML private Button btnFullRevenueReport;

    // labelsss agaain
    @FXML private Label lblTotalSuppliersOrders;
    @FXML private Label lblSuppliersOnTime;
    @FXML private Label lblSuppliersLate;
    @FXML private Button btnFullSuppliersReport;

    private RapportIM rapportIM = new RapportIM();
    private ObservableList<Stock> stockList = FXCollections.observableArrayList();
    // 3 labels taa stock
    private void updateStockSummary() {
        int totalProducts = stockList.size();
        int lowStockProducts = 0;
        int outOfStockProducts = 0;

        for (Stock s : stockList) {
            if (s.getEtat().equals("LOW")) {
                lowStockProducts++;
            }
            if (s.getQuantite() == 0) {
                outOfStockProducts++;
            }
        }

        lblTotalProducts.setText(String.valueOf(totalProducts));
        lblLowStockProducts.setText(String.valueOf(lowStockProducts));
        lblOutOfStockProducts.setText(String.valueOf(outOfStockProducts));
    }

    //resume taa revenue heheheh
    @FXML
    private void showRevenueSummary(ActionEvent event) {
        if (dateDebut.getValue() == null || dateFin.getValue() == null) return;

        Date debut = Date.valueOf(dateDebut.getValue());
        Date fin = Date.valueOf(dateFin.getValue());

        lblTotalRevenue.setText(rapportIM.getTotalRevenue(debut, fin) + " DT");
        lblNbSales.setText(String.valueOf(rapportIM.getNumberOfSales(debut, fin)));
        lblAverageBasket.setText(rapportIM.getAverageBasket(debut, fin) + " DT");
    }

    // resume sghair labels taa fournissuer
    @FXML
    private void showSuppliersSummary(ActionEvent event) {
        lblTotalSuppliersOrders.setText(String.valueOf(rapportIM.getTotalSupplierOrders()));
        lblSuppliersOnTime.setText(String.valueOf(rapportIM.getSuppliersOnTime()));
        lblSuppliersLate.setText(String.valueOf(rapportIM.getSuppliersLate()));
    }

    // hedha bahth lflous
    @FXML
    private void openFullRevenueReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ViewRevenueReport.fxml"));
            Parent root = loader.load();

            ViewRevenueReportController controller = loader.getController();
            Date debut = Date.valueOf(dateDebut.getValue());
            Date fin = Date.valueOf(dateFin.getValue());
            controller.showReport(debut, fin);

            Stage stage = new Stage();
            stage.setTitle("Full Revenue Report");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // hedha bch tunlocki l full potential
    @FXML
    private void openFullSuppliersReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ViewSupplierReport.fxml"));
            Parent root = loader.load();

            ViewSupplierReportController controller = loader.getController();
            controller.showReport();

            Stage stage = new Stage();
            stage.setTitle("Full Suppliers Report");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openStockReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StockDetails.fxml")); // your FXML for stock table
            Parent root = loader.load();

            // get controller if you want to pass data
            StockDetailsController controller = loader.getController();
            controller.refreshTable(); // load/update stock info

            Stage stage = new Stage();
            stage.setTitle("Stock State Report");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
