package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Service.CommandeService;
import brainstorm.pharmacy_app.Service.FournisseurService;
import brainstorm.pharmacy_app.Model.Fournisseur;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class AddOrderController {
    @FXML private MFXComboBox<String> fournisseurCombo;
    @FXML private MFXTextField prixTotalField;
    @FXML private MFXTextField dateArriveeField;

    private CommandeService commandeService = new CommandeService();
    private FournisseurService fournisseurService = new FournisseurService();
    private OrderControlController parentController;

    @FXML
    public void initialize() {
        loadFournisseurs();
    }

    public void setParentController(OrderControlController parent) {
        this.parentController = parent;
    }

    private void loadFournisseurs() {
        List<Fournisseur> fournisseurs = fournisseurService.getAllFournisseurs();
        ObservableList<String> fournisseurNames = FXCollections.observableArrayList();

        for (Fournisseur f : fournisseurs) {
            fournisseurNames.add(f.getNom() + " (ID: " + f.getId_Fournisseur() + ")");
        }

        fournisseurCombo.setItems(fournisseurNames);
    }

    @FXML
    private void handleSaveOrder(ActionEvent event) {
        try {
            if (fournisseurCombo.getValue() == null || fournisseurCombo.getValue().isEmpty()) {
                showError("Erreur", "Veuillez sélectionner un fournisseur");
                return;
            }

            if (prixTotalField.getText() == null || prixTotalField.getText().isEmpty()) {
                showError("Erreur", "Veuillez entrer un prix total");
                return;
            }

            // Parse the supplier ID from the combo box value
            String selectedValue = fournisseurCombo.getValue();
            int idFournisseur = extractFournisseurId(selectedValue);

            // Get current date as order date
            java.sql.Date dateCommande = new java.sql.Date(System.currentTimeMillis());

            // Parse arrival date if provided, otherwise use null
            java.sql.Date dateArrivee = null;
            if (dateArriveeField.getText() != null && !dateArriveeField.getText().isEmpty()) {
                try {
                    LocalDate arrivalDate = LocalDate.parse(dateArriveeField.getText());
                    dateArrivee = java.sql.Date.valueOf(arrivalDate);
                } catch (Exception e) {
                    showError("Format de date incorrect", "Veuillez utiliser le format YYYY-MM-DD");
                    return;
                }
            }

            float prixTotal = Float.parseFloat(prixTotalField.getText());
            int idEmploye = 1; // TODO: Get the actual logged-in employee ID
            int tempId = 0; // Temporary ID, will be set by database

            // Create the commande using the correct constructor
            Commande commande = new Commande(tempId, idFournisseur, idEmploye, dateCommande, dateArrivee);
            commande.setPrixTotal(prixTotal);
            commande.setEtat("CREE");

            // Save the commande using the service (you need to add createCommande method)
            // For now, let's use the DAO directly
            commandeService.createCommande(commande);

            if (parentController != null) {
                parentController.loadOrders(); // Refresh the order list
            }

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showError("Erreur de format", "Veuillez entrer un prix total valide (ex: 150.50)");
        } catch (Exception e) {
            showError("Erreur", e.getMessage());
        }
    }

    private int extractFournisseurId(String selectedValue) {
        try {
            // Extract ID from string like "Supplier Name (ID: 123)"
            int startIndex = selectedValue.indexOf("ID: ") + 4;
            int endIndex = selectedValue.indexOf(")", startIndex);
            String idStr = selectedValue.substring(startIndex, endIndex);
            return Integer.parseInt(idStr.trim());
        } catch (Exception e) {
            throw new RuntimeException("Impossible de lire l'ID du fournisseur: " + selectedValue);
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