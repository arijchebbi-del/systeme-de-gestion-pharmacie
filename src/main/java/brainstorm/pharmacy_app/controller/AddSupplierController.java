package brainstorm.pharmacy_app.Controller;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Service.FournisseurService;
import brainstorm.pharmacy_app.controller.SuppliersControlController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddSupplierController {

    @FXML private TextField txtNom;
    @FXML private TextField txtTel;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtType;

    private FournisseurService fournisseurService = new FournisseurService();
    private SuppliersControlController parentController;

    public void setParentController(SuppliersControlController parent) {
        this.parentController = parent;
    }

    @FXML
    private void handleSave() {
        Fournisseur f = new Fournisseur();
        f.setNom(txtNom.getText());
        f.setNumTelephone(txtTel.getText());
        f.setEmail(txtEmail.getText());
        f.setAdresse(txtAdresse.getText());
        f.setTypeProduits(txtType.getText());

        fournisseurService.ajouterFournisseur(f);

        if (parentController != null) {
            parentController.refreshTable();
        }

        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
}

