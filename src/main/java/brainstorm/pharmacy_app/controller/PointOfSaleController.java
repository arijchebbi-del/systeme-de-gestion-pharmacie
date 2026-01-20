package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.VenteIM;
import brainstorm.pharmacy_app.DAO.ConstituerIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Model.Constituer;
import brainstorm.pharmacy_app.Service.ProduitService;
import brainstorm.pharmacy_app.nav.Navigation;
import brainstorm.pharmacy_app.Utils.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource()));
    }
    @FXML
    private void chargerProductControl(ActionEvent event) {
        Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource()));
    }
    @FXML
    private void chargerOrderControl(ActionEvent event) {
        Navigation.navTo("/FXML/OrderControl.fxml",((Node) event.getSource()));
    }
    @FXML
    private void chargerSuppliersControl(ActionEvent event) {
        Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource()));
    }
    @FXML
    private void chargerHistory(ActionEvent event) {
        Navigation.navTo("/FXML/History.fxml",((Node) event.getSource()));
    }

    @FXML private MFXTextField txtSearch;
    @FXML private MFXComboBox<String> comboCategory;
    @FXML private TableView<Stock> tableStock;
    @FXML private TableColumn<Stock, Integer> colIdLot;
    @FXML private TableColumn<Stock, Integer> colReference;
    @FXML private TableColumn<Produit, String> colName;

    @FXML private TableColumn<Stock, Integer> colQuantiteStock;
    @FXML private TableColumn<Produit, Float> colPrice;

    @FXML private TableColumn<Stock, Void> colActions;
    @FXML private Label lblTotal;

    private VenteIM venteDAO = new VenteIM();
    private ConstituerIM constituerDAO = new ConstituerIM();
    private StockIM stockDAO = new StockIM();
    private ProduitService produitService = new ProduitService();

    private Vente venteActuelle = null;
    private ObservableList<Stock> masterStockData = FXCollections.observableArrayList();
    private ObservableList<Produit> masterProduitData = FXCollections.observableArrayList();

    private float montantTotal = 0.0f;

    @FXML
    public void initialize() {
        // hedhi mtaa erreur mtaa attribut null erreur ligne 46 ya taz
        if (colIdLot != null && colReference != null && colQuantiteStock != null) {
            colIdLot.setCellValueFactory(new PropertyValueFactory<>("numLot"));
            colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
            colQuantiteStock.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("prixVente"));// hawka zedet ll prix ma todhrebnich
            colName.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));

            setupActionsColumn();
            loadStockData();
            setupSearchFilter();
        } else {
            System.err.println("ERREUR : Une ou plusieurs colonnes TableColumn sont nulles. Vérifiez les fx:id dans Scene Builder.");
        }
    }

    private void loadStockData() {
        masterStockData.setAll(stockDAO.getToutLeStock());
        masterProduitData.setAll(produitService.getAllProduits());

        // tazz hedhi categorie ken thebni nbdadel chnowa yodhher badel wahdek hhahahhaahaha
        if (comboCategory != null) {
            comboCategory.setItems(FXCollections.observableArrayList("Toutes", "Médicament", "Parapharmacie", "Hygiène"));
            comboCategory.getSelectionModel().selectFirst();
        }
    }

    private void setupSearchFilter() {
        if (txtSearch == null) return;

        FilteredList<Stock> filteredData = new FilteredList<>(masterStockData, p -> true);

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
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
        tableStock.setItems(sortedData);
    }

    //  filtrage mtaa barre de recherche w categorie
    private void updatePredicate(FilteredList<Stock> filteredData) {
        filteredData.setPredicate(stock -> {
            String filter = txtSearch.getText() == null ? "" : txtSearch.getText().toLowerCase();
            String cat = comboCategory.getValue();

            boolean matchesSearch = String.valueOf(stock.getReference()).contains(filter) ||
                    String.valueOf(stock.getNumLot()).contains(filter);

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
                    Stock s = getTableView().getItems().get(getIndex());
                    btnAdd.setOnAction(event -> {
                        try {
                            int qte = Integer.parseInt(txtQty.getText());

                            // hedhi mtaa ll quantité
                            if (qte > s.getQuantite()) {
                                afficherAlerte("Stock Insuffisant",
                                        "Il ne reste que " + s.getQuantite() + " articles.", Alert.AlertType.ERROR);
                            } else {
                                handleVente(s, qte);
                                // hedhi des que yetaada ligne de commande tonkess ll quantite toul
                                s.setQuantite(s.getQuantite() - qte);
                                getTableView().refresh();
                            }
                        } catch (NumberFormatException e) {
                            afficherAlerte("Erreur", "Veuillez saisir un nombre entier.", Alert.AlertType.ERROR);
                        }
                    });
                    btnDelete.setOnAction(event -> handleSuppression(s));
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