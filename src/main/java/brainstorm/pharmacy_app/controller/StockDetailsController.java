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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StockDetailsController {

    @FXML
    private TableView<Stock> tableStock;

    @FXML
    private TableColumn<Stock, Integer> colNumLot;
    @FXML
    private TableColumn<Stock, String> colProduit;
    @FXML
    private TableColumn<Stock, Integer> colQuantite;
    @FXML
    private TableColumn<Stock, Integer> colSeuil;
    @FXML
    private TableColumn<Stock, String> colEtat;
    @FXML
    private TableColumn<Stock, Integer> colDecalage;
    @FXML
    private TableColumn<Stock, String> colDerniereMAJ;

    @FXML
    private MFXTextField searchField;
    @FXML
    private MFXComboBox<String> filterCombo;
    @FXML
    private MFXButton btnRefresh;

    private RapportIM rapportIM = new RapportIM();
    private ObservableList<Stock> stockList = FXCollections.observableArrayList();
    private FilteredList<Stock> filteredData;

    private ProduitIM produitIM = new ProduitIM(); // to fetch product names

    @FXML
    public void initialize() {

        // map col
        colNumLot.setCellValueFactory(new PropertyValueFactory<>("numLot"));
        colProduit.setCellValueFactory(cellData -> {
            // Display product name using ProduitIM
            String nomProduit = produitIM.getNomProduitByRef(cellData.getValue().getReference());
            return new javafx.beans.property.SimpleStringProperty(nomProduit);
        });
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colSeuil.setCellValueFactory(new PropertyValueFactory<>("seuilMinimal"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDecalage.setCellValueFactory(new PropertyValueFactory<>("decalage"));
        colDerniereMAJ.setCellValueFactory(new PropertyValueFactory<>("derniereMAJ"));

        // ycolori l etat hasb low wala ok
        colEtat.setCellFactory(column -> new TableCell<Stock, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("OK")) {
                        setStyle("-fx-background-color: lightgreen; -fx-alignment: CENTER;");
                    } else if (item.equals("LOW")) {
                        setStyle("-fx-background-color: lightcoral; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        // charg donnes
        loadStock();

        // combo mfalter
        filterCombo.getItems().clear();
        filterCombo.getItems().addAll("Tous", "OK", "LOW");
        filterCombo.setValue("Tous");

        // list mfaltra
        filteredData = new FilteredList<>(stockList, p -> true);

        // Listeners for search and filter
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // Sorted list for column sorting
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
        tableStock.setItems(sortedData);

        // Refresh button
        btnRefresh.setOnAction(e -> loadStock());
    }

    // load stock ml rapportetat stokc
    private void loadStock() {
        stockList.clear();
        stockList.addAll(rapportIM.rapportEtatStock()); // returns List<Stock>
    }

    // recherche et filre
    private void applyFilter() {
        String searchText = searchField.getText().toLowerCase();
        String selectedEtat = filterCombo.getValue();

        filteredData.setPredicate(stock -> {
            // Search matches reference or product name
            boolean matchesReference = String.valueOf(stock.getReference()).contains(searchText);
            boolean matchesNom = produitIM.getNomProduitByRef(stock.getReference()).toLowerCase().contains(searchText);
            boolean matchesSearch = matchesReference || matchesNom;

            // Etat filter
            boolean matchesEtat = selectedEtat.equals("Tous") || stock.getEtat().equals(selectedEtat);

            return matchesSearch && matchesEtat;
        });
    }

    // refresh
    public void refreshTable() {
        loadStock();
    }

    // nav
    @FXML
    private void chargerDashboard(ActionEvent event) {
        Navigation.navTo("/FXML/Dashboard.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerPointOfSale(ActionEvent event) {
        Navigation.navTo("/FXML/PointOfSale.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerProductControl(ActionEvent event) {
        Navigation.navTo("/FXML/ProductControl.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerOrderControl(ActionEvent event) {
        Navigation.navTo("/FXML/OrderControl.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerSuppliersControl(ActionEvent event) {
        Navigation.navTo("/FXML/SuppliersControl.fxml", ((Node) event.getSource()));
    }

    @FXML
    private void chargerHistory(ActionEvent event) {
        Navigation.navTo("/FXML/History.fxml", ((Node) event.getSource()));
    }
}



