package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Service.EmployeService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AddEmployeeController {

    @FXML private MFXTextField txtNom;
    @FXML private MFXTextField txtPrenom;
    @FXML private MFXTextField txtEmail;
    @FXML private MFXTextField txtTel;
    @FXML private MFXTextField txtHoraire;

    @FXML private MFXButton btnConfirm;
    @FXML private MFXButton btnCancel;

    private EmployeService employeService = new EmployeService();
    private EmployeeControlController parentController;

    public void setParentController(EmployeeControlController p) {
        this.parentController = p;
    }

    @FXML
    private void handleConfirm(ActionEvent e) {
        try {
            Employe emp = new Employe();
            emp.setNom(txtNom.getText());
            emp.setPrenom(txtPrenom.getText());
            emp.setEmail(txtEmail.getText());
            emp.setHoraire(txtHoraire.getText());
            emp.setNumTelephone(Integer.parseInt(txtTel.getText()));

            employeService.ajouterEmploye(emp);

            if (parentController != null)
                parentController.refreshTable();

            close();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent e) {
        close();
    }

    private void close() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private void showError(String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(m);
        a.show();
    }
}
