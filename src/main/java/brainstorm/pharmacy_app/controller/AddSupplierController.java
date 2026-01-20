package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Service.FournisseurService;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;


public class AddSupplierController {
    @FXML private MFXTextField nomField;
    @FXML private MFXTextField telephoneField;
    @FXML private MFXTextField emailField;
    @FXML private MFXTextField adresseField;
    @FXML private MFXTextField productTypeField;

    private FournisseurService fournisseurService = new FournisseurService();
    private SuppliersControlController parentController;

    public void setParentController(SuppliersControlController parent) {
        this.parentController = parent;
    }

    @FXML
    private void handleSaveSupplier(ActionEvent event) {
        try {
            Fournisseur f = new Fournisseur();
            f.setNom(nomField.getText());
            f.setNumTelephone(telephoneField.getText());
            f.setEmail(emailField.getText());
            f.setAdresse(adresseField.getText());
            f.setTypeProduits(productTypeField.getText());
            fournisseurService.ajouterFournisseur(f);
            parentController.refreshTable();

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (Exception e) {
            showError("Erreur", e.getMessage());
        }
    }
    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }


    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}

