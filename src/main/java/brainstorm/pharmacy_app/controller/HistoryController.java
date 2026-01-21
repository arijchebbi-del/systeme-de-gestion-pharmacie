package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.nav.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class HistoryController {
    @FXML
    private void chargerDashboard(ActionEvent event) {
        Navigation.navTo("/FXML/Dashboard.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerPointOfSale(ActionEvent event) {
        Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerProductControl(ActionEvent event) {
        Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerStockDetails(ActionEvent event) {
        Navigation.navTo("/FXML/StockDetails.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerOrderControl(ActionEvent event) {
        Navigation.navTo("/FXML/OrderControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerSuppliersControl(ActionEvent event) {
        Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerHistory(ActionEvent event) {
        Navigation.navTo("/FXML/History.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerEmployeesControl(ActionEvent event) {
        Navigation.navTo("/FXML/EmployeesControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        Navigation.navTo("/FXML/AnalysisReports.fxml",((Node) event.getSource())); //charger dashboard
    }

}
