package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.*;
import brainstorm.pharmacy_app.Model.*;
import brainstorm.pharmacy_app.Utils.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Date;
import java.time.LocalDate;

public class AddOrderController {


    @FXML private TextField txtRef;
    @FXML private TextField txtQty;
    @FXML private TextField txtFournisseurId;
    @FXML private Label lblProductName;
    @FXML private Label lblError;
    @FXML private Button btnSubmit;
    @FXML private Button btnCancel;


    private StockIM stockDAO = new StockIM();
    private CommandeIM commandeDAO = new CommandeIM();
    private ComposerIM composerDAO = new ComposerIM();

    @FXML
    public void initialize() {
        // Optionnel : Ajouter un listener pour vérifier le produit dès qu'on change la référence
        txtRef.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                verifierProduit();
            } else {
                lblProductName.setText("");
            }
        });
    }


    @FXML
    private void verifierProduit() {
        try {
            int ref = Integer.parseInt(txtRef.getText());
            String nom = stockDAO.getNomProduit(ref);

            if (nom != null) {
                lblProductName.setText("" + nom);
                lblProductName.setStyle("-fx-text-fill: #27ae60;"); // Vert si trouvé
                lblError.setText("");
            } else {
                lblProductName.setText("Produit inexistant");
                lblProductName.setStyle("-fx-text-fill: #e74c3c;"); // Rouge si non trouvé
            }
        } catch (NumberFormatException e) {
            lblProductName.setText("Référence invalide");
            lblProductName.setStyle("-fx-text-fill: #e74c3c;");
        }
    }


    @FXML
    private void handleConfirmOrder(ActionEvent event) {
        try {

            if (txtRef.getText().isEmpty() || txtQty.getText().isEmpty() || txtFournisseurId.getText().isEmpty()) {
                lblError.setText("Erreur : Tous les champs sont obligatoires.");
                return;
            }

            int ref = Integer.parseInt(txtRef.getText());
            int qte = Integer.parseInt(txtQty.getText());
            int idFournisseur = Integer.parseInt(txtFournisseurId.getText());


            int idEmploye = User.getInstance().getUser().getIdEmploye();

            // ken mawjoud
            if (stockDAO.getNomProduit(ref) == null) {
                lblError.setText("Erreur : Référence produit invalide.");
                return;
            }

            // yhadher fi commande yebda fih id w date
            // behi hatit date prevu baad 3 ayem dima ken theb nabadalha koli
            Date dateCmd = Date.valueOf(LocalDate.now());
            Date dateArr = Date.valueOf(LocalDate.now().plusDays(3));

            Commande cmd = new Commande(0, idFournisseur, idEmploye, dateCmd, dateArr);// lehne kali chat mahi heya auto increment hotha 0 sql yefhm illi howa bch ybadel feha actuel +1


            float total = stockDAO.getPrixProduitByRef(ref) * qte;
            cmd.setPrixTotal(total);
            cmd.setEtat("CREE");


            commandeDAO.creation_c(cmd);

            // yzid ligne de detaille

            composerDAO.ajouterLigneCommande(cmd.getIdCommande(), ref, qte);


            System.out.println("Commande enregistrée avec succès !");

            // fermer bll bouton submit
            Stage stage = (Stage) btnSubmit.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            lblError.setText("Erreur : Saisissez des nombres valides.");
        } catch (Exception e) {
            lblError.setText("Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}