package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.Model.Produit;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ProductControlController {

    //  Barre de recherche rapide
    @FXML private MFXTextField txtQuickSearch;

    // Bouton pour ajouter un produit
    @FXML private MFXButton btnAddProduct;

    //  Filtres avancés
    @FXML private MFXComboBox<String> comboCategory;
    @FXML private MFXTextField txtFilterValue;


    @FXML private TableView<Produit> tableProducts;
    @FXML private TableColumn<Produit, Integer> colRef;
    @FXML private TableColumn<Produit, String> colNom;
    @FXML private TableColumn<Produit, String> colCategorie;
    @FXML private TableColumn<Produit, String> colType;
    @FXML private TableColumn<Produit, Float> colPrixVente;

    private ProduitIM produitDAO = new ProduitIM();
    private ObservableList<Produit> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes selon tes attributs SQL
        colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrixVente.setCellValueFactory(new PropertyValueFactory<>("prixVente"));

        loadProducts();

        // Listener pour la recherche rapide (Zone Bleue)
        txtQuickSearch.textProperty().addListener((obs, old, newVal) -> applyFilter(newVal));
    }

    private void loadProducts() {
        masterData.clear();
        // Récupération de tous les produits via le DAO
        masterData.addAll(produitDAO.getToutsLesProduits());
        tableProducts.setItems(masterData);
    }

    // Action déclenchée par le bouton "Add Product" (Zone Blanche)
    @FXML
    private void handleOpenAddForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddProductDialog.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un Produit");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadProducts(); // Rafraîchir le tableau après l'ajout
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyFilter(String query) {
        if (query.isEmpty()) {
            tableProducts.setItems(masterData);
        } else {
            tableProducts.setItems(masterData.filtered(p ->
                    p.getNomProduit().toLowerCase().contains(query.toLowerCase())));
        }
    }
}