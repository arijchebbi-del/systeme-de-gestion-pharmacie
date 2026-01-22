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
import java.util.Optional;

public class PointOfSaleController {
    @FXML private void chargerDashboard(ActionEvent event) { Navigation.navTo("/FXML/Dashboard.fxml",((Node) event.getSource())); }
    @FXML private void chargerPointOfSale(ActionEvent event) { Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource())); }
    @FXML private void chargerProductControl(ActionEvent event) { Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerStockDetails(ActionEvent event) { Navigation.navTo("/FXML/StockDetails.fxml",((Node) event.getSource())); }
    @FXML private void chargerOrderControl(ActionEvent event) { Navigation.navTo("/FXML/OrderControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerSuppliersControl(ActionEvent event) { Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerHistory(ActionEvent event) { Navigation.navTo("/FXML/History.fxml",((Node) event.getSource())); }
    @FXML private void chargerEmployeesControl(ActionEvent event) {
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;
        if(current != null && "admin".equalsIgnoreCase(current.getRole())) {
            Navigation.navTo("/FXML/EmployeesControl.fxml", ((Node) event.getSource()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }
    }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;
        if (current != null && "admin".equalsIgnoreCase(current.getRole())) {
            Navigation.navTo("/FXML/AnalysisReports.fxml", ((Node) event.getSource()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }
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

    // tazzzz ahawma mtaa cart
    @FXML private TableView<CartItem> tableCart;
    @FXML private TableColumn<CartItem, String> colCartName;
    @FXML private TableColumn<CartItem, Integer> colCartQty;
    @FXML private TableColumn<CartItem, Float> colCartUnitPrice;
    @FXML private TableColumn<CartItem, Float> colCartTotalLine;
    @FXML private TableColumn<CartItem, Void> colCartActions;

    @FXML private Label lblTotal;
    @FXML private MFXButton btnPayment;

    private VenteIM venteDAO = new VenteIM();
    private ConstituerIM constituerDAO = new ConstituerIM();
    private StockIM stockDAO = new StockIM();
    private StockProduitIM stockProduitDAO = new StockProduitIM();

    private Vente venteActuelle = null;
    private ObservableList<StockProduit> masterStockProduitData = FXCollections.observableArrayList();


    private ObservableList<CartItem> cartData = FXCollections.observableArrayList();
    private FilteredList<StockProduit> filteredData;
    private float montantTotal = 0.0f;


    public static class CartItem {
        private final StockProduit stockProduit;
        private int quantite;
        public CartItem(StockProduit sp, int qty) { this.stockProduit = sp; this.quantite = qty; }
        public String getNom() { return stockProduit.getProduit().getNomProduit(); }
        public int getQty() { return quantite; }
        public float getUnitPrice() { return stockProduit.getProduit().getPrixVente(); }
        public float getTotal() { return quantite * getUnitPrice(); }
        public Stock getStock() { return stockProduit.getStock(); }
    }

    @FXML
    public void initialize() {
        if (colIdLot != null && colReference != null && colQuantiteStock != null) {
            colIdLot.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getStock().getNumLot()).asObject());
            colReference.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getStock().getReference()).asObject());
            colQuantiteStock.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getStock().getQuantite()).asObject());
            colName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getProduit().getNomProduit()));
            colPrice.setCellValueFactory(cd -> new SimpleFloatProperty(cd.getValue().getProduit().getPrixVente()).asObject());


            setupCartTable();
            setupActionsColumn();
            loadStockData();
            setupSearchFilter();

            if(btnPayment != null) {
                btnPayment.setOnAction(event -> handlePayment());
            }
        }
    }


    private void setupCartTable() {
        if (tableCart == null) return;
        colCartName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getNom()));
        colCartQty.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getQty()).asObject());
        colCartUnitPrice.setCellValueFactory(cd -> new SimpleFloatProperty(cd.getValue().getUnitPrice()).asObject());
        colCartTotalLine.setCellValueFactory(cd -> new SimpleFloatProperty(cd.getValue().getTotal()).asObject());
        tableCart.setItems(cartData);
        setupCartActionsColumn();
    }

    private void loadStockData() {
        masterStockProduitData.setAll(stockProduitDAO.getAll());
        if (comboCategory != null) {
            comboCategory.setItems(FXCollections.observableArrayList("Toutes", "Médicament", "Parapharmacie", "Hygiène"));
            comboCategory.getSelectionModel().selectFirst();
        }
    }

    private void setupSearchFilter() {
        if (txtSearch == null) return;
        filteredData = new FilteredList<>(masterStockProduitData, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        if (comboCategory != null) {
            comboCategory.valueProperty().addListener((obs, old, newValue) -> applyFilters());
        }
        SortedList<StockProduit> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
        tableStock.setItems(sortedData);
    }

    private void applyFilters() {
        String filter = (txtSearch.getText() == null) ? "" : txtSearch.getText().toLowerCase().trim();
        String cat = (comboCategory != null) ? comboCategory.getValue() : "Toutes";
        filteredData.setPredicate(sp -> {
            if (sp == null || sp.getProduit() == null || sp.getStock() == null) return false;
            boolean matchesSearch = filter.isEmpty() || sp.getProduit().getNomProduit().toLowerCase().contains(filter) || String.valueOf(sp.getStock().getReference()).contains(filter);
            boolean matchesCategory = cat == null || cat.equals("Toutes") || (sp.getProduit().getCategorie() != null && sp.getProduit().getCategorie().equals(cat));
            return matchesSearch && matchesCategory;
        });
    }

    private void setupActionsColumn() {
        if (colActions == null) return;
        colActions.setCellFactory(param -> new TableCell<>() {
            private final MFXTextField txtQty = new MFXTextField();
            private final MFXButton btnAdd = new MFXButton("+");
            // CHANGEMENT: Suppression du bouton Delete ici
            private final HBox container = new HBox(8, txtQty, btnAdd);

            {
                container.setAlignment(Pos.CENTER);
                txtQty.setPrefWidth(50);
                txtQty.setText("1");
                btnAdd.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    StockProduit sp = getTableView().getItems().get(getIndex());
                    btnAdd.setOnAction(event -> {
                        try {
                            int qte = Integer.parseInt(txtQty.getText());
                            if (qte > sp.getStock().getQuantite()) {
                                afficherAlerte("Stock Insuffisant", "Il ne reste que " + sp.getStock().getQuantite(), Alert.AlertType.ERROR);
                            } else {
                                handleVente(sp, qte);
                                sp.getStock().setQuantite(sp.getStock().getQuantite() - qte);
                                tableStock.refresh();
                            }
                        } catch (NumberFormatException e) {
                            afficherAlerte("Erreur", "Saisissez un nombre.", Alert.AlertType.ERROR);
                        }
                    });
                    setGraphic(container);
                }
            }
        });
    }


    private void setupCartActionsColumn() {
        colCartActions.setCellFactory(param -> new TableCell<>() {
            private final MFXButton btnDel = new MFXButton("🗑");
            {
                btnDel.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                setAlignment(Pos.CENTER);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    CartItem ci = getTableView().getItems().get(getIndex());
                    btnDel.setOnAction(e -> handleSuppression(ci));
                    setGraphic(btnDel);
                }
            }
        });
    }

    private void handleVente(StockProduit sp, int qte) {
        if (qte <= 0) return;
        if (venteActuelle == null) {
            venteActuelle = new Vente();
            venteActuelle.setDateVente(Date.valueOf(LocalDate.now()));
            if (User.getInstance() != null) venteActuelle.setIdEmploye(User.getInstance().getUser().getIdEmploye());
            venteActuelle.setPrixTotal(0.0);
            venteDAO.creation_v(venteActuelle);
        }
        Constituer ligne = new Constituer();
        ligne.setNumFacture(venteActuelle.getNumFacture());
        ligne.setReference(sp.getStock().getReference());
        ligne.setQuantiteVendu(qte);
        constituerDAO.ajouterLigneVente(ligne);


        cartData.add(new CartItem(sp, qte));

        montantTotal += (sp.getProduit().getPrixVente() * qte);
        updateTotalLabel();
    }


    private void handleSuppression(CartItem ci) {
        constituerDAO.supprimerLigneVente(venteActuelle.getNumFacture(), ci.getStock().getReference());
        montantTotal -= ci.getTotal();
        ci.getStock().setQuantite(ci.getStock().getQuantite() + ci.getQty());
        cartData.remove(ci);
        tableStock.refresh();
        updateTotalLabel();
    }

    private void handlePayment() {
        if (montantTotal <= 0 || venteActuelle == null) return;
        venteActuelle.setPrixTotal((double) montantTotal);
        venteDAO.updatePrixTotal(venteActuelle.getNumFacture(), montantTotal);
        afficherAlerte("Succès", "Vente terminée.", Alert.AlertType.INFORMATION);
        venteActuelle = null;
        cartData.clear();
        montantTotal = 0.0f;
        updateTotalLabel();
    }

    private void updateTotalLabel() {
        if (lblTotal != null) lblTotal.setText(String.format("%.2f DT", montantTotal));
    }

    private void afficherAlerte(String t, String m, Alert.AlertType type) {
        Alert a = new Alert(type); a.setTitle(t); a.setContentText(m); a.showAndWait();
    }
}