package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Service.EmployeService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

    public class ViewEmployeeController {

        @FXML private MFXTextField txtId;
        @FXML private MFXTextField txtNom;
        @FXML private MFXTextField txtPrenom;
        @FXML private MFXTextField txtEmail;
        @FXML private MFXTextField txtTel;
        @FXML private MFXTextField txtHoraire;

        @FXML private MFXTextField txtMotDePasse;
        @FXML private MFXComboBox<String> comboRole;

        @FXML private MFXButton btnConfirm;
        @FXML private MFXButton btnCancel;

        private EmployeService employeService = new EmployeService();
        private EmployeeControlController parentController;
        private Employe current;

        @FXML
        public void initialize() {
            comboRole.getItems().addAll("admin", "employe");
        }

        public void setParentController(EmployeeControlController p) {
            this.parentController = p;
        }

        public void setEmploye(Employe e) {
            this.current = e;

            txtId.setText(String.valueOf(e.getIdEmploye()));
            txtNom.setText(e.getNom());
            txtPrenom.setText(e.getPrenom());
            txtEmail.setText(e.getEmail());
            txtTel.setText(String.valueOf(e.getNumTelephoneEmploye()));
            txtHoraire.setText(e.getHoraire());

            comboRole.setValue(e.getRole());
            txtId.setDisable(true);
        }

        @FXML
        private void handleConfirm(ActionEvent e) {
            try {
                current.setNom(txtNom.getText());
                current.setPrenom(txtPrenom.getText());
                current.setEmail(txtEmail.getText());
                current.setHoraire(txtHoraire.getText());
                current.setRole(comboRole.getValue());
                current.setNumTelephone(Integer.parseInt(txtTel.getText()));

                employeService.modifierEmploye(current);

                if (txtMotDePasse.getText() != null && !txtMotDePasse.getText().isEmpty()) {
                    employeService.changerMotDePasse(
                            current.getIdEmploye(),
                            txtMotDePasse.getText()
                    );
                }

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


