package brainstorm.pharmacy_app.controller;
/*

import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Service.ProduitService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class AddProductController {

        @FXML private TextField nomField;
        @FXML private TextField categorieField;
        @FXML private TextField typeField;
        @FXML private TextField prixAchatField;
        @FXML private TextField prixVenteField;
        @FXML private CheckBox ordonnanceCheck;

        private ProduitService produitService = new ProduitService();

        @FXML
        private void handleSaveProduct(ActionEvent event) {
            try {
                Produit p = new Produit();
                p.setNomProduit(nomField.getText());
                p.setCategorie(categorieField.getText());
                p.setType(typeField.getText());
                p.setPrixAchat(Float.parseFloat(prixAchatField.getText()));
                p.setPrixVente(Float.parseFloat(prixVenteField.getText()));
                p.setOrdonnance(ordonnanceCheck.isSelected());

                produitService.ajouterProduit(p);

                // Close window after success
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

*/
