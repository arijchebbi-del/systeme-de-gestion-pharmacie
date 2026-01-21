package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Service.ProduitService;
import brainstorm.pharmacy_app.nav.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ProductControlController {
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
    @FXML private TableView<Produit> tableProducts;
    @FXML private TableColumn<Produit, Integer> colRef;
    @FXML private TableColumn<Produit, String> colName;
    @FXML private TableColumn<Produit, String> colCategory;
    @FXML private TableColumn<Produit, Float> colPrice;
    @FXML private TableColumn<Produit, Void> colActions;
    private ProduitService produitService = new ProduitService();
    private ProduitIM produitDAO = new ProduitIM();
    private ObservableList<Produit> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTable();
        loadData();
        setupFilters();
    }

    private void configureTable() {
        colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("prixVente"));

        setupActionsColumn();
    }

    private void loadData() {
        // Liste fictive de catégories pour le filtre, à adapter selon vos besoins
        comboCategory.setItems(FXCollections.observableArrayList("Toutes", "Médicament", "Cosmétique", "Hygiène"));
        comboCategory.getSelectionModel().selectFirst();

        refreshTable();
    }

    public void refreshTable() {
        // Ici, vous devriez avoir une méthode getAll() dans ProduitIM
        masterData.setAll(produitService.getAllProduits());
        tableProducts.refresh();
    }

    private void setupFilters() {
        FilteredList<Produit> filteredData = new FilteredList<>(masterData, p -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyFilter(filteredData));
        comboCategory.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter(filteredData));

        tableProducts.setItems(filteredData);
    }

    private void applyFilter(FilteredList<Produit> filteredData) {
        filteredData.setPredicate(p -> {
            String search = txtSearch.getText() == null ? "" : txtSearch.getText().toLowerCase();
            String cat = comboCategory.getValue();

            boolean matchSearch = p.getNomProduit().toLowerCase().contains(search) ||
                    String.valueOf(p.getReference()).contains(search);

            boolean matchCat = cat == null || cat.equals("Toutes") || p.getCategorie().equals(cat);

            return matchSearch && matchCat;
        });
    }

    @FXML
    void handleOpenAddPopup(ActionEvent event) {
        openProductDialog(null);
    }

    private void openProductDialog(Produit p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddProductPopUp.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle(p == null ? "Ajouter Produit" : "Modifier Produit");

            stage.setScene(new Scene(loader.load()));

            ProductFormController controller = loader.getController();
            controller.initData(p, this); // On passe le produit et ce contrôleur pour refresh

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final MFXButton btnEdit = new MFXButton("✎");
            private final MFXButton btnDelete = new MFXButton("🗑");
            private final HBox container = new HBox(10, btnEdit, btnDelete);

            {
                container.setAlignment(Pos.CENTER);
                btnEdit.setStyle("-fx-text-fill: #2980b9; -fx-cursor: hand; -fx-font-size: 14px;");
                btnDelete.setStyle("-fx-text-fill: #c0392b; -fx-cursor: hand; -fx-font-size: 14px;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Produit current = getTableView().getItems().get(getIndex());

                    btnEdit.setOnAction(e -> openProductDialog(current));

                    btnDelete.setOnAction(e -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + current.getNomProduit() + " ?", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.YES) {
                                produitDAO.suppression_p(current.getReference());
                                masterData.remove(current);
                            }
                        });
                    });

                    setGraphic(container);
                }
            }
        });
    }
}
