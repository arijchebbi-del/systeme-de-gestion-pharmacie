package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.VenteIM;
import brainstorm.pharmacy_app.DAO.ConstituerIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Model.Constituer;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.sql.Date;
import java.time.LocalDate;

public class PointOfSaleController {

    @FXML private TableView<Stock> tableStock;
    @FXML private TableColumn<Stock, Integer> colIdStock;
    @FXML private TableColumn<Stock, Integer> colReference;
    @FXML private TableColumn<Stock, Integer> colQuantiteStock;
    @FXML private TableColumn<Stock, Void> colActions;

    @FXML private Label lblTotal;


    private VenteIM venteDAO = new VenteIM();
    private ConstituerIM constituerDAO = new ConstituerIM();
    private StockIM stockDAO = new StockIM();

    private Vente venteActuelle = null;
    private ObservableList<Stock> masterStockData = FXCollections.observableArrayList();
    private float montantTotal = 0.0f;

    @FXML
    public void initialize() {
        //  Liaison des colonnes avec les attributs de la classe Stock
        colIdStock.setCellValueFactory(new PropertyValueFactory<>("idStock"));
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colQuantiteStock.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // 2. Configuration de la colonne interactive (+ / poubelle / saisie)
        setupActionsColumn();

        // Chargement de tout le stock disponible
        loadAllStock();
    }

    /**
     * Charge toutes les lignes de stock depuis la base de données.
     */
    private void loadAllStock() {
        // Utilise une méthode simple "getAll" puisque tout est déjà reçu
        masterStockData.setAll(stockDAO.getToutLeStock());
        tableStock.setItems(masterStockData);
    }


    private void setupActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final MFXTextField txtQtyVente = new MFXTextField();
            private final MFXButton btnAdd = new MFXButton("+");
            private final MFXButton btnDelete = new MFXButton("🗑");
            private final HBox container = new HBox(8, txtQtyVente, btnAdd, btnDelete);

            {
                container.setAlignment(Pos.CENTER);
                txtQtyVente.setPrefWidth(50);
                txtQtyVente.setText("1");
                btnAdd.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Stock s = getTableView().getItems().get(getIndex());

                    // --- ACTION AJOUTER (+) ---
                    btnAdd.setOnAction(event -> {
                        try {
                            int qteAVendre = Integer.parseInt(txtQtyVente.getText());
                            handleLogiqueVente(s, qteAVendre);
                        } catch (NumberFormatException e) {
                            afficherMessage("Erreur", "La quantité doit être un nombre entier.", Alert.AlertType.ERROR);
                        }
                    });

                    // --- ACTION SUPPRIMER (🗑) ---
                    btnDelete.setOnAction(event -> handleLogiqueSuppression(s));

                    setGraphic(container);
                }
            }
        });
    }

    private void handleLogiqueVente(Stock s, int qte) {
        if (qte <= 0) {
            afficherMessage("Attention", "Veuillez saisir une quantité supérieure à zéro.", Alert.AlertType.WARNING);
            return;
        }


        if (venteActuelle == null) {
            venteActuelle = new Vente();
            venteActuelle.setDateVente(Date.valueOf(LocalDate.now()));
            venteActuelle.setPrixTotal(0.0);
            venteDAO.creation_v(venteActuelle);
        }


        Constituer ligne = new Constituer();
        ligne.setNumFacture(venteActuelle.getNumFacture());
        ligne.setReference(s.getReference());
        ligne.setQuantiteVendu(qte);
        constituerDAO.ajouterLigneVente(ligne);


        float prixUnitaire = stockDAO.getPrixProduitByRef(s.getReference());
        montantTotal += (prixUnitaire * qte);
        lblTotal.setText(String.format("%.2f DT", montantTotal));
    }


    private void handleLogiqueSuppression(Stock s) {
        if (venteActuelle == null) return;

        boolean existe = constituerDAO.verifierPresenceProduit(venteActuelle.getNumFacture(), s.getReference());

        if (existe) {
            constituerDAO.supprimerLigneVente(venteActuelle.getNumFacture(), s.getReference());
            afficherMessage("Info", "Produit retiré de la vente.", Alert.AlertType.INFORMATION);
            // Ici, vous devriez idéalement soustraire le montant du total
        } else {
            afficherMessage("Erreur", "Ce produit ne figure pas dans la vente en cours.", Alert.AlertType.ERROR);
        }
    }

    private void afficherMessage(String titre, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}