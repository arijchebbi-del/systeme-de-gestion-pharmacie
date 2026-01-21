package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.StockProduitIM;
import brainstorm.pharmacy_app.DAO.VenteIM;
import brainstorm.pharmacy_app.DAO.ConstituerIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Model.*;
import brainstorm.pharmacy_app.Service.ProduitService;
import brainstorm.pharmacy_app.nav.Navigation;
import brainstorm.pharmacy_app.Utils.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.sql.Date;
import java.time.LocalDate;

public class PointOfSaleController {
    @FXML
    private void chargerDashboard(ActionEvent event) {
        Navigation.navTo("/FXML/Dashboard.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerPointOfSale(ActionEvent event) {
        Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerProductControl(ActionEvent event) {
        Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerStockDetails(ActionEvent event) {
        Navigation.navTo("/FXML/StockDetails.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerOrderControl(ActionEvent event) {
        Navigation.navTo("/FXML/OrderControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerSuppliersControl(ActionEvent event) {
        Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerHistory(ActionEvent event) {
        Navigation.navTo("/FXML/History.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerEmployeesControl(ActionEvent event) {
        Navigation.navTo("/FXML/EmployeesControl.fxml",((Node) event.getSource())); //charger dashboard
    }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        Navigation.navTo("/FXML/AnalysisReports.fxml",((Node) event.getSource())); //charger dashboard
    }


    @FXML private MFXTextField txtSearch;
    @FXML private MFXComboBox<String> comboCategory;
    @FXML private TableView<StockProduit> tableStock;
    @FXML private TableColumn<StockProduit, Integer> colIdLot;
    @FXML private TableColumn<StockProduit, Integer> colReference;
    @FXML private TableColumn<StockProduit, String> colName;
    @FXML private TableColumn<StockProduit, Integer> colQuantiteStock;
    @FXML private TableColumn<StockProduit, Float> colPrice;
    @FXML private TableColumn<StockProduit, Void> colActions;

    @FXML private Label lblTotal;

    private VenteIM venteDAO = new VenteIM();
    private ConstituerIM constituerDAO = new ConstituerIM();
    private StockIM stockDAO = new StockIM();
    private ProduitService produitService = new ProduitService();
    private StockProduitIM stockProduitDAO = new StockProduitIM();

    private Vente venteActuelle = null;
    private ObservableList<StockProduit> masterStockProduitData = FXCollections.observableArrayList();

    private float montantTotal = 0.0f;

    @FXML
    public void initialize() {
        // hedhi mtaa erreur mtaa attribut null erreur ligne 46 ya taz
        if (colIdLot != null && colReference != null && colQuantiteStock != null) {
            colIdLot.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue().getStock().getNumLot()).asObject()
            );

            colReference.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue().getStock().getReference()).asObject()
            );

            colQuantiteStock.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue().getStock().getQuantite()).asObject()
            );

            colName.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue().getProduit().getNomProduit())
            );

            colPrice.setCellValueFactory(cd ->
                    new SimpleFloatProperty(cd.getValue().getProduit().getPrixVente()).asObject()
            );


            setupActionsColumn();
            loadStockData();
            setupSearchFilter();
        } else {
            System.err.println("ERREUR : Une ou plusieurs colonnes TableColumn sont nulles. Vérifiez les fx:id dans Scene Builder.");
        }
    }

    private void loadStockData() {
        masterStockProduitData.setAll(stockProduitDAO.getAll());

        // tazz hedhi categorie ken thebni nbdadel chnowa yodhher badel wahdek hhahahhaahaha
        if (comboCategory != null) {
            comboCategory.setItems(FXCollections.observableArrayList("Toutes", "Médicament", "Parapharmacie", "Hygiène"));
            comboCategory.getSelectionModel().selectFirst();
        }
    }

    private void setupSearchFilter() {
        if (txtSearch == null) return;

        FilteredList<StockProduit> filteredData = new FilteredList<>(masterStockProduitData, p -> true);

        // hedhi bch tkhalik des que tenzel aal categorie yetbadel ll affichage
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePredicate(filteredData);
        });

        if (comboCategory != null) {
            comboCategory.valueProperty().addListener((obs, old, newValue) -> {
                updatePredicate(filteredData);
            });
        }
        //bch yodhhrou mnadhmin
        SortedList<StockProduit> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
        tableStock.setItems(sortedData);
    }

    //  filtrage mtaa barre de recherche w categorie
    private void updatePredicate(FilteredList<StockProduit> filteredData) {
        filteredData.setPredicate(stockProduit -> {
            String filter = txtSearch.getText() == null ? "" : txtSearch.getText().toLowerCase();
            String cat = comboCategory.getValue();

            boolean matchesSearch = String.valueOf(stockProduit.getStock().getReference()).contains(filter) ||
                    String.valueOf(stockProduit.getStock()).contains(filter);

            boolean matchesCategory = cat == null || cat.equals("Toutes");

            return matchesSearch && matchesCategory;
        });
    }
    // ll fonction illi watretnii
    private void setupActionsColumn() {
        if (colActions == null) return;

        colActions.setCellFactory(param -> new TableCell<>() {
            private final MFXTextField txtQty = new MFXTextField();
            private final MFXButton btnAdd = new MFXButton("+");
            private final MFXButton btnDelete = new MFXButton("🗑");
            private final HBox container = new HBox(8, txtQty, btnAdd, btnDelete);

            {
                container.setAlignment(Pos.CENTER);
                txtQty.setPrefWidth(50);
                txtQty.setText("1");
                btnAdd.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    StockProduit sp = getTableView().getItems().get(getIndex());
                    btnAdd.setOnAction(event -> {
                        try {
                            int qte = Integer.parseInt(txtQty.getText());

                            // hedhi mtaa ll quantité
                            if (qte > sp.getStock().getQuantite()) {
                                afficherAlerte("Stock Insuffisant",
                                        "Il ne reste que " + sp.getStock().getQuantite() + " articles.", Alert.AlertType.ERROR);
                            } else {
                                handleVente(sp.getStock(), qte);
                                // hedhi des que yetaada ligne de commande tonkess ll quantite toul
                                sp.getStock().setQuantite(sp.getStock().getQuantite() - qte);
                                getTableView().refresh();
                            }
                        } catch (NumberFormatException e) {
                            afficherAlerte("Erreur", "Veuillez saisir un nombre entier.", Alert.AlertType.ERROR);
                        }
                    });
                    btnDelete.setOnAction(event -> handleSuppression(sp.getStock()));
                    setGraphic(container);
                }
            }
        });
    }

    private void handleVente(Stock s, int qte) {
        if (qte <= 0) {
            afficherAlerte("Quantité", "La quantité doit être supérieure à 0.", Alert.AlertType.WARNING);
            return;
        }

        if (venteActuelle == null) {
            venteActuelle = new Vente();
            venteActuelle.setDateVente(Date.valueOf(LocalDate.now()));

            // billehi ken khatfet farahni
            if (User.getInstance() != null) {
                venteActuelle.setIdEmploye(User.getInstance().getUser().getIdEmploye());
            }

            venteActuelle.setPrixTotal(0.0);
            venteDAO.creation_v(venteActuelle);
        }

        Constituer ligne = new Constituer();
        ligne.setNumFacture(venteActuelle.getNumFacture());
        ligne.setReference(s.getReference());
        ligne.setQuantiteVendu(qte);
        constituerDAO.ajouterLigneVente(ligne);

        float prix = stockDAO.getPrixProduitByRef(s.getReference());
        montantTotal += (prix * qte);
        if (lblTotal != null) lblTotal.setText(String.format("%.2f DT", montantTotal));
    }

    private void handleSuppression(Stock s) {
        if (venteActuelle == null) return;

        if (constituerDAO.verifierPresenceProduit(venteActuelle.getNumFacture(), s.getReference())) {
            constituerDAO.supprimerLigneVente(venteActuelle.getNumFacture(), s.getReference());
            afficherAlerte("Succès", "Produit retiré de la facture.", Alert.AlertType.INFORMATION);
        } else {
            afficherAlerte("Erreur", "Ce produit n'est pas dans la vente en cours.", Alert.AlertType.ERROR);
        }
    }

    private void afficherAlerte(String t, String m, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }
}