package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Service.FournisseurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SuppliersControlController {

    @FXML private TableView<Fournisseur> tableFournisseurs;
    @FXML private TableColumn<Fournisseur, Integer> colId;
    @FXML private TableColumn<Fournisseur, String> colNom;
    @FXML private TableColumn<Fournisseur, String> colTel;
    @FXML private TableColumn<Fournisseur, String> colType;
    @FXML private TableColumn<Fournisseur, Void> colView;
    @FXML private TableColumn<Fournisseur, Void> colDelete;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCombo;
    @FXML private Button btnAdd;

    private FournisseurService fournisseurService = new FournisseurService();
    private ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();
    private FilteredList<Fournisseur> filteredData;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id_Fournisseur"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("numTelephone"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeProduits"));

        loadFournisseurs();

        filterCombo.getItems().clear();
        filterCombo.getItems().add("Tous");
        filterCombo.getItems().addAll(fournisseurService.getAllCategories());
        filterCombo.setValue("Tous");

        filteredData = new FilteredList<>(fournisseurList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        SortedList<Fournisseur> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableFournisseurs.comparatorProperty());
        tableFournisseurs.setItems(sortedData);

        addViewButtonToTable();
        addDeleteButtonToTable();

        btnAdd.setOnAction(e -> openAddSupplier());
    }

    private void loadFournisseurs() {
        fournisseurList.clear();
        fournisseurList.addAll(fournisseurService.getAllFournisseurs());
    }

    private void applyFilter() {
        String searchText = searchField.getText().toLowerCase();
        String selectedType = filterCombo.getValue();

        filteredData.setPredicate(f -> {
            boolean matchesSearch =
                    f.getNom().toLowerCase().contains(searchText) ||
                            f.getNumTelephone().contains(searchText) ||
                            f.getTypeProduits().toLowerCase().contains(searchText);

            boolean matchesType =
                    selectedType.equals("Tous") || f.getTypeProduits().equalsIgnoreCase(selectedType);

            return matchesSearch && matchesType;
        });
    }

    // ---------------- Add Supplier ----------------
    private void openAddSupplier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/brainstorm/pharmacy_app/View/add_fournisseur.fxml"));
            Parent root = loader.load();

            brainstorm.pharmacy_app.Controller.AddSupplierController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter Fournisseur");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadFournisseurs();
    }

    // ---------------- View Button ----------------
    private void addViewButtonToTable() {
        colView.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("View");
            {
                btn.setOnAction(event -> {
                    Fournisseur f = getTableView().getItems().get(getIndex());
                    openViewSupplier(f);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openViewSupplier(Fournisseur f) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/brainstorm/pharmacy_app/View/view_fournisseur.fxml"));
            Parent root = loader.load();

            ViewFournisseurController controller = loader.getController();
            controller.setFournisseur(f);

            Stage stage = new Stage();
            stage.setTitle("Fournisseur Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Delete Button ----------------
    private void addDeleteButtonToTable() {
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction(event -> {
                    Fournisseur f = getTableView().getItems().get(getIndex());
                    deleteSupplier(f);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteSupplier(Fournisseur f) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer Fournisseur");
        confirm.setContentText("Voulez-vous vraiment supprimer ce fournisseur ?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            fournisseurService.supprimerFournisseur(f.getId_Fournisseur());
            loadFournisseurs();

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Succès");
            info.setContentText("Fournisseur supprimé avec succès !");
            info.show();
        }
    }
}



