package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Utils.User;
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
            User.getInstace(emp);
            Navigation.navTo("/FXML/Dashboard.fxml",((Node) event.getSource())); //charger dashboard
        } else {
            lblError.setText("Identifiants incorrects.");
        }
    }


}
