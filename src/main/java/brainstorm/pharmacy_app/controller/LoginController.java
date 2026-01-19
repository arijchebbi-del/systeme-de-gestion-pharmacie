package brainstorm.pharmacy_app.controller;

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

public class LoginController {
    @FXML private MFXTextField txtUsername;
    @FXML private MFXPasswordField txtPassword;
    @FXML private Label lblError;

    private EmployeIM employeDAO = new EmployeIM();

    @FXML
    private void handleLogin(ActionEvent event) {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("Veuillez remplir tous les champs");
            return;
        }


        Employe emp = employeDAO.authentifier(user, pass);

        if (emp != null) {
            System.out.println("Connexion réussie : " + emp.getNom());
            chargerDashboard(event);
        } else {
            lblError.setText("Identifiants incorrects.");
        }
    }

    private void chargerDashboard(ActionEvent event) {
        try {
            // Chargement de la scène Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
