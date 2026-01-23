package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.*;
import brainstorm.pharmacy_app.Model.*;
import brainstorm.pharmacy_app.Utils.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Date;
import java.time.LocalDate;

public class AddOrderController {

    @FXML private MFXTextField txtRef;
    @FXML private MFXTextField txtQty;
    @FXML private MFXTextField txtFournisseurId; // MA yefreghch wakt wakt tenzel aala add
    @FXML private Label lblProductName;
    @FXML private Label lblFournisseurName; // ahawa ya taz zetou houni
    @FXML private Label lblError;
    @FXML private Label lblTotalPrix;
    @FXML private MFXButton btnSubmit;
    @FXML private MFXButton btnCancel;

    @FXML private TableView<Composer> tableTempItems;
    @FXML private TableColumn<Composer, Integer> colRef;
    @FXML private TableColumn<Composer, Integer> colQty;

    private ObservableList<Composer> tempItems = FXCollections.observableArrayList();
    private float montantTotalCommande = 0.0f;

    private StockIM stockDAO = new StockIM();
    private CommandeIM commandeDAO = new CommandeIM();
    private ComposerIM composerDAO = new ComposerIM();
    private FournisseurIM fournisseurDAO = new FournisseurIM();


    @FXML
    public void initialize() {
        colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        tableTempItems.setItems(tempItems);

        lblTotalPrix.setText("0.00 DT");

        txtRef.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) verifierProduit();
            else lblProductName.setText("");
        });
        txtFournisseurId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) verifierFournisseur();
            else lblFournisseurName.setText("");
        });
    }
    @FXML
    private void verifierFournisseur() {
        try {
            int idF = Integer.parseInt(txtFournisseurId.getText());
            String nomF = fournisseurDAO.getNomFournisseur(idF);

            if (nomF != null) {
                lblFournisseurName.setText(nomF);
                lblFournisseurName.setStyle("-fx-text-fill: #27ae60;");
                lblError.setText("");
            } else {
                lblFournisseurName.setText("Fournisseur inconnu");
                lblFournisseurName.setStyle("-fx-text-fill: #e74c3c;");
            }
        } catch (NumberFormatException e) {
            lblFournisseurName.setText("ID Invalide");
        }
    }
    @FXML
    private void verifierProduit() {
        try {
            int ref = Integer.parseInt(txtRef.getText());
            String nom = stockDAO.getNomProduit(ref);

            if (nom != null) {
                lblProductName.setText(nom);
                lblProductName.setStyle("-fx-text-fill: #27ae60;");
                lblError.setText("");
            } else {
                lblProductName.setText("Produit inexistant");
                lblProductName.setStyle("-fx-text-fill: #e74c3c;");
            }
        } catch (NumberFormatException e) {
            lblProductName.setText("Référence invalide");
            lblProductName.setStyle("-fx-text-fill: #e74c3c;");
        }
    }


    @FXML
    private void handleAddItem(ActionEvent event) {
        try {
            // AJOUT : On verifie aussi que le fournisseur existe bien
            if (txtFournisseurId.getText().isEmpty() || lblFournisseurName.getText().equals("Fournisseur inconnu")) {
                lblError.setText("Veuillez saisir un fournisseur valide.");
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
            lblTotalPrix.setText(String.format("%.2f DT", montantTotalCommande));

            // ajout dans la liste
            tempItems.add(new Composer(commandeDAO.getLastId()+1, ref, qte));

            // on efface que le champ du produit
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
            montantTotalCommande -= (prixUnitaire * selected.getQuantite());
            lblTotalPrix.setText(String.format("%.2f DT", montantTotalCommande));
            tempItems.remove(selected);


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
                composerDAO.ajouterLigneCommande(cmd.getIdCommande(), item.getReference(), item.getQuantite());
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