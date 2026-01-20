package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.nav.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
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
    private void handleAddProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddProductPopUp.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Nouveau Produit");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());

            stage.showAndWait();

            // OPTIONAL: refresh dashboard stats here

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void handleAddSupplier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddSupplierPopUp.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Nouveau Produit");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());

            stage.showAndWait();

            // OPTIONAL: refresh dashboard stats here

        } catch (IOException e) {
            e.printStackTrace();
        }



}}
