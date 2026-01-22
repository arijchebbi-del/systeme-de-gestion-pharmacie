package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.*;
import brainstorm.pharmacy_app.Model.*;
import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class OrderControlController {
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

    @FXML private TableView<Commande> tableOrders;
    @FXML private TableColumn<Commande, Integer> colId;
    @FXML private TableColumn<Commande, Float> colPrix;
    @FXML private TableColumn<Commande, String> colDate;
    @FXML private TableColumn<Commande, String> colFournisseur;
    @FXML private TableColumn<Commande, String> colEmploye;
    @FXML private TableColumn<Commande, String> colEtat;
    @FXML private TableColumn<Commande, Void> colActions;

    private ObservableList<Commande> masterOrderData = FXCollections.observableArrayList();

    private CommandeIM commandeDAO = new CommandeIM();
    private ComposerIM composerDAO = new ComposerIM();
    private StockIM stockDAO = new StockIM();

    @FXML
    public void initialize() {
        setupColumns();
        loadOrders();
        setupSearchFilter();


        tableOrders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCommande"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));
        colDate.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getDateCommande().toString()));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));


        TableColumn<Commande, Integer> colIdFourn = new TableColumn<>("ID Fourn.");
        colIdFourn.setCellValueFactory(new PropertyValueFactory<>("idFournisseur"));
        colIdFourn.setStyle("-fx-alignment: CENTER;");
        if (!tableOrders.getColumns().contains(colIdFourn)) {
            tableOrders.getColumns().add(2, colIdFourn);
        }


        colFournisseur.setCellValueFactory(cd -> {
            String nomComplet = commandeDAO.getNomFournisseur(cd.getValue().getIdFournisseur());
            return new SimpleStringProperty(nomComplet);
        });
        colFournisseur.setStyle("-fx-alignment: CENTER;");


        colEmploye.setCellValueFactory(cd -> {
            String nom = commandeDAO.getNomEmploye(cd.getValue().getIdEmploye());
            return new SimpleStringProperty(nom);
        });
        colEmploye.setStyle("-fx-alignment: CENTER;");


        colId.setStyle("-fx-alignment: CENTER;");
        colEtat.setStyle("-fx-alignment: CENTER;");
        colPrix.setStyle("-fx-alignment: CENTER;");

        setupActionsColumn();
    }

    private void setupSearchFilter() {
        if (txtSearch == null) return;
        FilteredList<Commande> filteredData = new FilteredList<>(masterOrderData, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> updatePredicate(filteredData));
        if (comboCategory != null) {
            comboCategory.valueProperty().addListener((obs, old, newValue) -> updatePredicate(filteredData));
        }
        SortedList<Commande> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableOrders.comparatorProperty());
        tableOrders.setItems(sortedData);
    }

    private void updatePredicate(FilteredList<Commande> filteredData) {
        filteredData.setPredicate(commande -> {
            String filter = txtSearch.getText() == null ? "" : txtSearch.getText().toLowerCase();
            String cat = comboCategory.getValue();
            boolean matchesSearch = String.valueOf(commande.getIdCommande()).contains(filter) ||
                    commande.getEtat().toLowerCase().contains(filter);
            boolean matchesCategory = cat == null || cat.equals("Toutes") || commande.getEtat().equals(cat);
            return matchesSearch && matchesCategory;
        });
    }

    private void setupActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnReceive = new Button("Receive");
            private final Button btnView = new Button("👁");
            private final Button btnDelete = new Button("🗑");
            private final HBox container = new HBox(8, btnReceive, btnView, btnDelete);

            {
                btnReceive.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                btnView.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                container.setAlignment(javafx.geometry.Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Commande cmd = getTableView().getItems().get(getIndex());
                    boolean isReceived = "RECUE".equalsIgnoreCase(cmd.getEtat()) || "Received".equalsIgnoreCase(cmd.getEtat());
                    btnReceive.setDisable(isReceived);
                    btnReceive.setOnAction(e -> handleReceive(cmd));
                    btnDelete.setOnAction(e -> handleDelete(cmd));
                    btnView.setOnAction(e -> handleViewDetails(cmd));
                    setGraphic(container);
                }
            }
        });
    }

    private void loadOrders() {
        masterOrderData.setAll(commandeDAO.getAllCommandes());
        if (comboCategory != null) {
            comboCategory.setItems(FXCollections.observableArrayList("Toutes", "CREE", "MODIFIE", "ANNULEE", "RECUE"));
            comboCategory.getSelectionModel().selectFirst();
        }
    }

    private void handleReceive(Commande cmd) {
        List<Composer> produitsCommandes = composerDAO.getProduitsParCommande(cmd.getIdCommande());
        for (Composer ligne : produitsCommandes) {
            int numLotExistant = stockDAO.getNumLotParReference(ligne.getReference());
            if (numLotExistant != -1) {
                int qteActuelle = stockDAO.getQuantiteByProduit(ligne.getReference());
                stockDAO.updateQuantiteStock(numLotExistant, qteActuelle + ligne.getQuantite());
            } else {
                Stock nouveauStock = new Stock(0, ligne.getReference(), ligne.getQuantite());
                nouveauStock.setDerniereMiseAJour(Timestamp.valueOf(LocalDateTime.now()));
                stockDAO.creation_s(nouveauStock);
            }
        }
        commandeDAO.updateEtat(cmd.getIdCommande(), "RECUE");
        loadOrders();
        afficherAlerte("Réception validée", "Le stock a été mis à jour avec succès.");
    }

    private void handleDelete(Commande cmd) {
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;
        if(current != null && "admin".equalsIgnoreCase(current.getRole())) {
            if (!(cmd.getEtat().equalsIgnoreCase("RECUE"))){
                commandeDAO.annulation_c(cmd.getIdCommande());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Demande refusée");
                alert.setHeaderText("Action impossible");
                alert.setContentText("You can't delete received orders");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }
        loadOrders();
    }

    private void handleViewDetails(Commande cmd) {
        Stage stage = new Stage();
        VBox box = new VBox(10);
        box.setStyle("-fx-padding: 15; -fx-background-color: white;");

        TableView<Composer> detailTable = new TableView<>();
        detailTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Anti-tronquage

        TableColumn<Composer, String> colNomProd = new TableColumn<>("Nom Produit");
        colNomProd.setCellValueFactory(cd -> {
            String nom = stockDAO.getNomProduit(cd.getValue().getReference());
            return new SimpleStringProperty(nom != null ? nom : "N/A");
        });

        TableColumn<Composer, Integer> colRef = new TableColumn<>("Référence");
        colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));

        TableColumn<Composer, Integer> colQte = new TableColumn<>("Qté Commandée");
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        detailTable.getColumns().addAll(colNomProd, colRef, colQte);
        detailTable.setItems(FXCollections.observableArrayList(composerDAO.getProduitsParCommande(cmd.getIdCommande())));

        Label title = new Label("Détails de la Commande #" + cmd.getIdCommande());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        box.getChildren().addAll(title, detailTable);
        stage.setScene(new Scene(box, 500, 400));
        stage.setTitle("Contenu de la commande");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void afficherAlerte(String titre, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titre); a.setHeaderText(null); a.setContentText(message);
        a.showAndWait();
    }

    @FXML
    private void chargerAddOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddOrderPopUp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Passer une nouvelle commande");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadOrders();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}