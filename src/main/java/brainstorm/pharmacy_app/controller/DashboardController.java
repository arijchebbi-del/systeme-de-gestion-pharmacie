package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;
import  io.github.palexdev.materialfx.controls.MFXPasswordField;
import  io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import brainstorm.pharmacy_app.DAO.EmployeIM;
import brainstorm.pharmacy_app.Model.Employe;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import brainstorm.pharmacy_app.nav.Navigation;

public class DashboardController {
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
        // تجيب المستخدم اللي متسجل
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;

        if(current != null && "admin".equalsIgnoreCase(current.getRole())) {
            // يسمح بالوصول
            Navigation.navTo("/FXML/EmployeeControl.fxml", ((Node) event.getSource()));
        } else {
            // ممنوع الوصول
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }
    }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        Navigation.navTo("/FXML/AnalysisReports.fxml",((Node) event.getSource())); //charger dashboard
    }

    @FXML
    private void chargerPointOfSaleim(MouseEvent event) {
        Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerProductControlim(MouseEvent event) {
        Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerStockDetailsim(MouseEvent event) {
        Navigation.navTo("/FXML/StockDetails.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerHistoryim(MouseEvent event) {
        Navigation.navTo("/FXML/History.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerAddProduct(ActionEvent event){
        ProductControlController pcc = new ProductControlController();
        Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); //charger dashboard
        pcc.handleOpenAddPopup(event);
    }
    @FXML
    private void chargerAddSupplier(ActionEvent event){
        SuppliersControlController scc = new SuppliersControlController();
        Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource())); //charger dashboard
        scc.openAddSupplier();
    }

}
