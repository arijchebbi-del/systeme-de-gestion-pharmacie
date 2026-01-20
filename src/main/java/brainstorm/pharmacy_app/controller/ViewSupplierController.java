package brainstorm.pharmacy_app.controller;
import brainstorm.pharmacy_app.Model.Fournisseur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ViewSupplierController {

    @FXML private Label lblId;
    @FXML private Label lblNom;
    @FXML private Label lblTel;
    @FXML private Label lblType;
    @FXML private Label lblEmail;
    @FXML private Label lblAdresse;

    public void setSupplier(Fournisseur f) {
        lblId.setText(String.valueOf(f.getId_Fournisseur()));
        lblNom.setText(f.getNom());
        lblTel.setText(f.getNumTelephone());
        lblType.setText(f.getTypeProduits());
        lblEmail.setText(f.getEmail());
        lblAdresse.setText(f.getAdresse());
    }
    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

}

