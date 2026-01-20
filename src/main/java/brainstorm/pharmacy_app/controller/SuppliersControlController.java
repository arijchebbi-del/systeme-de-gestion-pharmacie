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
        filterCombo.getItems().add("Tous");

        /*
           TODO:
           - Créer dans FournisseurService une méthode:
             List<String> getAllCategories();
           - Elle appelle DAO pour récupérer DISTINCT typeProduits depuis la table fournisseur
           - Puis ici:
             filterCombo.getItems().addAll(fournisseurService.getAllCategories());
        */

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

        /*
           TODO:
           - Ajouter dans FournisseurService une méthode:
             List<Fournisseur> getAllFournisseurs();

           - Cette méthode appelle FournisseurDAO.selectAll() (ou autre nom)

           - Exemple:
             fournisseurList.addAll(fournisseurService.getAllFournisseurs());
        */

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

        /*
           TODO:
           - Créer une nouvelle scene / popup add_fournisseur.fxml
           - Créer AddFournisseurController
           - Ouvrir la fenêtre ici avec FXMLLoader

           Après ajout:
           - Appeler loadFournisseurs() pour rafraîchir la table
        */

    }

    // DELETE SUPPLIER
    private void deleteSelectedSupplier() {

        Fournisseur selected = tableFournisseurs.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        /*
           TODO:
           - Appeler:
             fournisseurService.supprimerFournisseur(selected.getId_Fournisseur());

           - Puis:
             loadFournisseurs();
        */
    }

    // UPDATE SUPPLIER (optionnel plus tard) 
    private void updateSelectedSupplier() {

        Fournisseur selected = tableFournisseurs.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        /*
           TODO:
           - Ouvrir popup modification avec selected passé en paramètre
           - Après validation:
             fournisseurService.modifierFournisseur(fournisseurModifie);
           - Puis:
             loadFournisseurs();
        */
    }
}

