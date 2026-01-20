package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.DAO.RapportIM;
import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.nav.Navigation;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StockDetailsController {

    // --- Table and columns ---
    @FXML private TableView<Stock> tableStock;
    @FXML private TableColumn<Stock, Integer> colNumLot;
    @FXML private TableColumn<Stock, String> colProduit;
    @FXML private TableColumn<Stock, Integer> colQuantite;
    @FXML private TableColumn<Stock, Integer> colSeuil;
    @FXML private TableColumn<Stock, String> colEtat;
    @FXML private TableColumn<Stock, Integer> colDecalage;
    @FXML private TableColumn<Stock, String> colDerniereMAJ;

    // --- Search / Filter ---
    @FXML private MFXTextField searchField;
    @FXML private MFXComboBox<String> filterCombo;
    @FXML private MFXButton btnRefresh;

    // --- Summary Labels ---
    @FXML private Label lblTotalProducts;
    @FXML private Label lblLowStockProducts;
    @FXML private Label lblOutOfStockProducts;

    private RapportIM rapportIM = new RapportIM();
    private ProduitIM produitIM = new ProduitIM(); // To fetch product names
    private ObservableList<Stock> stockList = FXCollections.observableArrayList();
    private FilteredList<Stock> filteredData;

    // ===================== INITIALIZE =====================
    @FXML
    public void initialize() {

        // --- Table Columns ---
        colNumLot.setCellValueFactory(new PropertyValueFactory<>("numLot"));
        colProduit.setCellValueFactory(cellData -> {
            String nomProduit = produitIM.getNomProduitByRef(cellData.getValue().getReference());
            return new javafx.beans.property.SimpleStringProperty(nomProduit);
        });
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colSeuil.setCellValueFactory(new PropertyValueFactory<>("seuilMinimal"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDecalage.setCellValueFactory(new PropertyValueFactory<>("decalage"));
        colDerniereMAJ.setCellValueFactory(new PropertyValueFactory<>("derniereMAJ"));

        // --- Color coding for Etat column ---
        colEtat.setCellFactory(column -> new TableCell<Stock, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("LOW")) {
                        setStyle("-fx-background-color: lightcoral; -fx-alignment: CENTER;");
                    } else if (item.equals("OK")) {
                        setStyle("-fx-background-color: lightgreen; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        // --- Load Stock Data ---
        loadStock();

        // --- Filter ComboBox ---
        filterCombo.getItems().clear();
        filterCombo.getItems().addAll("Tous", "OK", "LOW");
        filterCombo.setValue("Tous");

        // --- Filtered List for search & filter ---
        filteredData = new FilteredList<>(stockList, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // --- Sorted List for TableView ---
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
        tableStock.setItems(sortedData);

        // --- Refresh Button ---
        btnRefresh.setOnAction(e -> loadStock());
    }

    // ===================== LOAD STOCK =====================
    private void loadStock() {
        stockList.clear();
        stockList.addAll(rapportIM.rapportEtatStock()); // returns List<Stock>
        updateStockSummary(); // Update summary labels
    }

    // ===================== UPDATE SUMMARY LABELS =====================
    private void updateStockSummary() {
        int totalProducts = stockList.size();
        int lowStockProducts = 0;
        int outOfStockProducts = 0;

        for (Stock s : stockList) {
            if (s.getEtat().equals("LOW")) {
                lowStockProducts++;
            }
            if (s.getQuantite() == 0) {
                outOfStockProducts++;
            }
        }

        lblTotalProducts.setText(String.valueOf(totalProducts));
        lblLowStockProducts.setText(String.valueOf(lowStockProducts));
        lblOutOfStockProducts.setText(String.valueOf(outOfStockProducts));
    }

    // ===================== SEARCH & FILTER =====================
    private void applyFilter() {
        String searchText = searchField.getText().toLowerCase();
        String selectedEtat = filterCombo.getValue();

        filteredData.setPredicate(stock -> {
            boolean matchesReference = String.valueOf(stock.getReference()).contains(searchText);
            boolean matchesNom = produitIM.getNomProduitByRef(stock.getReference()).toLowerCase().contains(searchText);
            boolean matchesSearch = matchesReference || matchesNom;

            boolean matchesEtat = selectedEtat.equals("Tous") || stock.getEtat().equals(selectedEtat);

            return matchesSearch && matchesEtat;
        });
    }

    public void refreshTable() {
        loadStock();
    }

    // ===================== NAVIGATION =====================
    @FXML private void chargerDashboard(ActionEvent event) {
        Navigation.navTo("/FXML/Dashboard.fxml", ((Node) event.getSource()));
    }

    @FXML private void chargerPointOfSale(ActionEvent event) {
        Navigation.navTo("/FXML/PointOfSale.fxml", ((Node) event.getSource()));
    }

    @FXML private void chargerProductControl(ActionEvent event) {
        Navigation.navTo("/FXML/ProductControl.fxml", ((Node) event.getSource()));
    }

    @FXML private void chargerOrderControl(ActionEvent event) {
        Navigation.navTo("/FXML/OrderControl.fxml", ((Node) event.getSource()));
    }

    @FXML private void chargerSuppliersControl(ActionEvent event) {
        Navigation.navTo("/FXML/SuppliersControl.fxml", ((Node) event.getSource()));
    }

    @FXML private void chargerHistory(ActionEvent event) {
        Navigation.navTo("/FXML/History.fxml", ((Node) event.getSource()));
    }
}



