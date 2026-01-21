package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.RapportIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.nav.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;

public class AnalysisReportsController {

    // nav
    @FXML
    private void chargerDashboard(ActionEvent event) {
        Navigation.navTo("/FXML/Dashboard.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerPointOfSale(ActionEvent event) {
        Navigation.navTo("/FXML/PointOfSale.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerProductControl(ActionEvent event) {
        Navigation.navTo("/FXML/ProductControl.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerStockDetails(ActionEvent event) {
        Navigation.navTo("/FXML/StockDetails.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerOrderControl(ActionEvent event) {
        Navigation.navTo("/FXML/OrderControl.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerSuppliersControl(ActionEvent event) {
        Navigation.navTo("/FXML/SuppliersControl.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerHistory(ActionEvent event) {
        Navigation.navTo("/FXML/History.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerEmployeesControl(ActionEvent event) {
        Navigation.navTo("/FXML/EmployeesControl.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        Navigation.navTo("/FXML/AnalysisReports.fxml", ((Node) event.getSource()));
    }

    // date
    @FXML private MFXDatePicker dateDebut;
    @FXML private MFXDatePicker dateFin;

    // diel e stock
    @FXML private Label lblTotalProducts;
    @FXML private Label lblLowStockProducts;
    @FXML private Label lblOutOfStockProducts;
    @FXML private Button btnStockReport;

    // diel e revenue
    @FXML private Label lblTotalRevenue;
    @FXML private Label lblNbSales;
    @FXML private Label lblAverageBasket;
    @FXML private MFXButton btnFullRevenueReport;

    // diel l fournisseur
    @FXML private Label lblTotalSuppliersOrders;
    @FXML private Label lblSuppliersOnTime;
    @FXML private Label lblSuppliersLate;
    @FXML private MFXButton btnFullSuppliersReport;

    private RapportIM rapportIM = new RapportIM();
    private ObservableList<Stock> stockList = FXCollections.observableArrayList();

    // labels taa stock w yaamllhom uapdate
    private void updateStockSummary() {
        int totalProducts = stockList.size();
        int lowStockProducts = 0;
        int outOfStockProducts = 0;

        for (Stock s : stockList) {
            if ("LOW".equals(s.getEtat())) {
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
    @FXML
    private void showStockSummary(ActionEvent event) {
        if (dateDebut.getValue() == null || dateFin.getValue() == null) return;

        Date debut = Date.valueOf(dateDebut.getValue());
        Date fin   = Date.valueOf(dateFin.getValue());

        stockList.setAll(StockIM.getStockByPeriod(debut, fin));
        updateStockSummary();
    }

    @FXML
    private void showRevenueSummary(ActionEvent event) {
        if (dateDebut.getValue() == null || dateFin.getValue() == null) return;

        Date debut = Date.valueOf(dateDebut.getValue());
        Date fin   = Date.valueOf(dateFin.getValue());

        lblTotalRevenue.setText(rapportIM.getTotalRevenue(debut, fin) + " DT");
        lblNbSales.setText(String.valueOf(rapportIM.getNumberOfSales(debut, fin)));
        lblAverageBasket.setText(rapportIM.getAverageBasket(debut, fin) + " DT");
    }

    // SUPPLIERS
    @FXML
    private void showSuppliersSummary(ActionEvent event) {
        if (dateDebut.getValue() == null || dateFin.getValue() == null) return;

        Date debut = Date.valueOf(dateDebut.getValue());
        Date fin   = Date.valueOf(dateFin.getValue());

        lblTotalSuppliersOrders.setText(
                String.valueOf(rapportIM.getTotalSupplierOrders(debut, fin))
        );
        lblSuppliersOnTime.setText(
                String.valueOf(rapportIM.getSuppliersOnTime(debut, fin))
        );
        lblSuppliersLate.setText(
                String.valueOf(rapportIM.getSuppliersLate(debut, fin))
        );
    }

    // FULL REVENUE REPORT
    @FXML
    private void openFullRevenueReport(ActionEvent event) {
        try {
            if (dateDebut.getValue() == null || dateFin.getValue() == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ViewRevenueReport.fxml"));
            Parent root = loader.load();

            ViewRevenueReportController controller = loader.getController();
            Date debut = Date.valueOf(dateDebut.getValue());
            Date fin   = Date.valueOf(dateFin.getValue());
            controller.showReport(debut, fin);

            Stage stage = new Stage();
            stage.setTitle("Full Revenue Report");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //FULL STOCK REPORT
    @FXML
    private void openStockReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StockDetails.fxml"));
            Parent root = loader.load();

            StockDetailsController controller = loader.getController();
            controller.refreshTable();

            Stage stage = new Stage();
            stage.setTitle("Stock State Report");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

