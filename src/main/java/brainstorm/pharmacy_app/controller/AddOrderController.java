package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.*;
import brainstorm.pharmacy_app.Model.*;
import brainstorm.pharmacy_app.Utils.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Date;
import java.time.LocalDate;

public class AddOrderController {

    @FXML private TextField txtRef;
    @FXML private TextField txtQty;
    @FXML private TextField txtFournisseurId; // MA yefreghch wakt wakt tenzel aala add
    @FXML private Label lblProductName;
    @FXML private Label lblError;
    @FXML private Label lblTotalPrix;
    @FXML private Button btnSubmit;
    @FXML private Button btnCancel;

    @FXML private TableView<Composer> tableTempItems;
    @FXML private TableColumn<Composer, Integer> colRef;
    @FXML private TableColumn<Composer, Integer> colQty;

    private ObservableList<Composer> tempItems = FXCollections.observableArrayList();
    private float montantTotalCommande = 0.0f;

    private StockIM stockDAO = new StockIM();
    private CommandeIM commandeDAO = new CommandeIM();
    private ComposerIM composerDAO = new ComposerIM();

    @FXML
    public void initialize() {
        colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        tableTempItems.setItems(tempItems);

        lblTotalPrix.setText("Total : 0.00 DT");

        txtRef.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) verifierProduit();
            else lblProductName.setText("");
        });
    }

    @FXML
    private void verifierProduit() {
        try {
            int ref = Integer.parseInt(txtRef.getText());
            String nom = stockDAO.getNomProduit(ref);

            if (nom != null) {
                lblProductName.setText("Produit : " + nom);
                lblProductName.setStyle("-fx-text-fill: #27ae60;");
                lblError.setText("");
            } else {
                lblProductName.setText("Erreur : Produit inexistant");
                lblProductName.setStyle("-fx-text-fill: #e74c3c;");
            }
        } catch (NumberFormatException e) {
            lblProductName.setText("Référence invalide");
            lblProductName.setStyle("-fx-text-fill: #e74c3c;");
        }
    }


    @FXML
    private void handleAddItem() {
        try {
            // nthabet illi kteb fournisseur mi lowel
            if (txtFournisseurId.getText().isEmpty()) {
                lblError.setText("Veuillez saisir l'ID du fournisseur d'abord.");
                return;
            }

            if (txtRef.getText().isEmpty() || txtQty.getText().isEmpty()) {
                lblError.setText("Saisissez une référence et une quantité.");
                return;
            }

            int ref = Integer.parseInt(txtRef.getText());
            int qte = Integer.parseInt(txtQty.getText());

            if (stockDAO.getNomProduit(ref) == null) {
                lblError.setText("Référence produit invalide.");
                return;
            }


            float prixUnitaire = stockDAO.getPrixProduitByRef(ref);
            montantTotalCommande += (prixUnitaire * qte);
            lblTotalPrix.setText(String.format("Total : %.2f DT", montantTotalCommande));

            // yzid fi lista
            tempItems.add(new Composer(ref, 0, qte));

            // ma nfasskhou ken champ mtaa produit
            txtRef.clear();
            txtQty.clear();
            lblProductName.setText("");
            lblError.setText("");


            txtFournisseurId.setDisable(true);

        } catch (NumberFormatException e) {
            lblError.setText("Format invalide.");
        }
    }

    @FXML
    private void handleRemoveItem() {
        Composer selected = tableTempItems.getSelectionModel().getSelectedItem();
        if (selected != null) {
            float prixUnitaire = stockDAO.getPrixProduitByRef(selected.getReference());
            montantTotalCommande -= (prixUnitaire * selected.getQuantiteComposer());
            lblTotalPrix.setText(String.format("Total : %.2f DT", montantTotalCommande));
            tempItems.remove(selected);

            // ki tefregh ll lista nraj3ou najmou nbadlou fll fournisseur
            if (tempItems.isEmpty()) {
                txtFournisseurId.setDisable(false);
            }
        }
    }

    @FXML
    private void handleConfirmOrder() {
        try {
            if (tempItems.isEmpty()) {
                lblError.setText("Erreur : Ajoutez au moins un produit.");
                return;
            }


            int idFournisseur = Integer.parseInt(txtFournisseurId.getText());
            int idEmploye = User.getInstance().getUser().getIdEmploye();

            Date dateCmd = Date.valueOf(LocalDate.now());
            Date dateArr = Date.valueOf(LocalDate.now().plusDays(3));

            Commande cmd = new Commande(0, idFournisseur, idEmploye, dateCmd, dateArr);
            cmd.setPrixTotal(montantTotalCommande);
            cmd.setEtat("CREE");

            commandeDAO.creation_c(cmd);

            for (Composer item : tempItems) {
                composerDAO.ajouterLigneCommande(cmd.getIdCommande(), item.getReference(), item.getQuantiteComposer());
            }

            Stage stage = (Stage) btnSubmit.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            lblError.setText("Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        if (!tempItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Annuler la commande en cours ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    stage.close();
                }
            });
        } else {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }
}