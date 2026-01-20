package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Stock; // your stock model
import brainstorm.pharmacy_app.DAO.RapportIM; // your existing rapport class
import brainstorm.pharmacy_app.Model.Produit;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
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

    @FXML
    public void initialize() {

        // Map columns to Stock properties
        colNumLot.setCellValueFactory(new PropertyValueFactory<>("numLot"));
        colProduit.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colSeuil.setCellValueFactory(new PropertyValueFactory<>("seuilMinimal"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDecalage.setCellValueFactory(new PropertyValueFactory<>("decalage"));
        colDerniereMAJ.setCellValueFactory(new PropertyValueFactory<>("derniereMAJ"));

        // Load initial data
        loadStock();

        // Filter combo setup
        filterCombo.getItems().clear();
        filterCombo.getItems().addAll("Tous", "OK", "LOW");
        filterCombo.setValue("Tous");

        // Filtered list for search + filter
        filteredData = new FilteredList<>(stockList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // Sorted list to allow column sorting
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
        tableStock.setItems(sortedData);

        // Refresh button
        btnRefresh.setOnAction(e -> loadStock());
    }

    private void loadStock() {
        stockList.clear();
        stockList.addAll(rapportIM.rapportEtatStock()); // your method returns List<Stock>
    }

    private void applyFilter() {
        String searchText = searchField.getText().toLowerCase();
        String selectedEtat = filterCombo.getValue();

        filteredData.setPredicate(stock -> {
            boolean matchesSearch = String.valueOf(stock.getReference()).contains(searchText);
            boolean matchesEtat = selectedEtat.equals("Tous") || stock.getEtat().equals(selectedEtat);
            return matchesSearch && matchesEtat;
        });
    }

    public void refreshTable() {
        loadStock();
    }
}


