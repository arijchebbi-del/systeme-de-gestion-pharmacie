package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Service.FournisseurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SuppliersControlController {

    //tableau taa fournisseur
    @FXML
    private TableView<Fournisseur> tableFournisseurs;

    @FXML private TableColumn<Fournisseur, Integer> colId;
    @FXML private TableColumn<Fournisseur, String> colNom;
    @FXML private TableColumn<Fournisseur, String> colTel;
    @FXML private TableColumn<Fournisseur, String> colType;

    // recherche wl filtre
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterCombo;

    //l boutonettt
    @FXML
    private Button btnAdd;//addsupllier

    private FournisseurService fournisseurService = new FournisseurService();
    private ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();
    private FilteredList<Fournisseur> filteredData;

    @FXML
    public void initialize() {

        // l colonne liasion maa les attributs
        colId.setCellValueFactory(new PropertyValueFactory<>("id_Fournisseur"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("numTelephone"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeProduits"));

        //nchargiw ml base teena les fournisseurs
        loadFournisseurs();

        //Initialiser filtre catégorie (à adapter selon ta base)
        filterCombo.getItems().clear();
        filterCombo.getItems().add("Tous");

        filterCombo.getItems().addAll(fournisseurService.getAllCategories());

        filterCombo.setValue("Tous");

        // Recherche wl filtre dynamique
        filteredData = new FilteredList<>(fournisseurList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        SortedList<Fournisseur> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableFournisseurs.comparatorProperty());

        tableFournisseurs.setItems(sortedData);

        // Bouton Add Supplier
        btnAdd.setOnAction(e -> openAddSupplier());
    }

    // charger ml base de donne
    private void loadFournisseurs() {

        fournisseurList.clear();
        fournisseurList.addAll(fournisseurService.getAllFournisseurs());
    }

    //FILTER (SEARCH + CATEGORY)
    private void applyFilter() {

        String searchText = searchField.getText().toLowerCase();
        String selectedType = filterCombo.getValue();

        filteredData.setPredicate(f -> {

            boolean matchesSearch =
                    f.getNom().toLowerCase().contains(searchText) ||
                            f.getEmail().toLowerCase().contains(searchText) ||
                            f.getAdresse().toLowerCase().contains(searchText) ||
                            f.getNumTelephone().contains(searchText);

            boolean matchesType =
                    selectedType.equals("Tous") ||
                            f.getTypeProduits().equalsIgnoreCase(selectedType);

            return matchesSearch && matchesType;
        });
    }

    // add supplier
    private void openAddSupplier() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/brainstorm/pharmacy_app/View/add_fournisseur.fxml"));
            Parent root = loader.load();

            // نجيب controller متاع popup
            AddFournisseurController controller = loader.getController();

            // نبعثلو reference متاع هذا controller باش يعمل refresh بعد ajout
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

    // DELETE SUPPLIER
    private void deleteSelectedSupplier() {

        Fournisseur selected = tableFournisseurs.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setContentText("Veuillez sélectionner un fournisseur à supprimer.");
            alert.show();
            return;
        }

        // Confirmation avant suppression
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer Fournisseur");
        confirm.setContentText("Voulez-vous vraiment supprimer ce fournisseur ?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            fournisseurService.supprimerFournisseur(selected.getId_Fournisseur());

            // mise a jour l table  la table
            loadFournisseurs();

            // Message succès
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Succès");
            info.setContentText("Fournisseur supprimé avec succès !");
            info.show();
        }
    }


    // modifier SUPPLIER
    private void updateSelectedSupplier() {
        Fournisseur selected = tableFournisseurs.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setContentText("Veuillez sélectionner un fournisseur à modifier.");
            alert.show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/brainstorm/pharmacy_app/View/edit_fournisseur.fxml"));
            Parent root = loader.load();

            // نجيب controller متاع popup
            EditFournisseurController controller = loader.getController();

            // نبعثلو fournisseur المختار + reference باش يعمل refresh بعد update
            controller.setFournisseur(selected);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier Fournisseur");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

