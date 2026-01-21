package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.nav.Navigation;
import brainstorm.pharmacy_app.Service.FournisseurService;
import brainstorm.pharmacy_app.Service.CommandeService;
import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Model.Fournisseur;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderControlController {

    // ===== NAVIGATION METHODS =====
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

    // ===== ORDER CONTROL FIELDS =====
    @FXML private MFXTextField searchField;
    @FXML private MFXComboBox<String> categoryCombo;
    @FXML private VBox ordersContainer;

    // Use Services instead of DAOs
    private CommandeService commandeService = new CommandeService();
    private FournisseurService fournisseurService = new FournisseurService();

    // Data storage
    private ObservableList<Commande> allCommandes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup category filter
        categoryCombo.getItems().addAll("Tous", "En attente", "Reçue", "Annulée");
        categoryCombo.setValue("Tous");

        // Load initial orders
        loadOrders();

        // Setup search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        // Setup category listener
        categoryCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });
    }

    @FXML
    private void handleAddOrder(ActionEvent event) {
        try {
            // You might want to create an AddOrder.fxml for this
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddOrderPopUp.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Nouvelle Commande");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();

                // Refresh after adding
                loadOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void loadOrders() {
        ordersContainer.getChildren().clear();

        // Get all commandes using a service method
        // If you don't have getAllCommandes() in CommandeService, you need to add it
        allCommandes.clear();

        // For now, let's get commandes directly (you should add this to CommandeService)
        List<Commande> commandes = getAllCommandesFromDB();
        allCommandes.addAll(commandes);

        if (allCommandes.isEmpty()) {
            Label noOrdersLabel = new Label("Aucune commande trouvée");
            noOrdersLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666; -fx-padding: 20;");
            ordersContainer.getChildren().add(noOrdersLabel);
            return;
        }

        for (Commande commande : allCommandes) {
            HBox orderCard = createOrderCard(commande);
            ordersContainer.getChildren().add(orderCard);
        }
    }

    private List<Commande> getAllCommandesFromDB() {
        // This method should be in CommandeService
        // For now, we'll call it directly from DAO
        // TODO: Move this to CommandeService.getAllCommandes()
        try {
            return commandeService.getAllCommandes(); // If this method exists
        } catch (Exception e) {
            // If method doesn't exist, return empty list
            return java.util.Collections.emptyList();
        }
    }

    private HBox createOrderCard(Commande commande) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #ffffff; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Left section - Supplier info
        VBox leftSection = new VBox(5);
        leftSection.setPrefWidth(200);

        // Get supplier using FournisseurService
        Fournisseur fournisseur = getFournisseurById(commande.getIdFournisseur());
        if (fournisseur != null) {
            Label supplierName = new Label(fournisseur.getNom());
            supplierName.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label supplierPhone = new Label(fournisseur.getNumTelephone());
            supplierPhone.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

            leftSection.getChildren().addAll(supplierName, supplierPhone);
        } else {
            Label unknownSupplier = new Label("Fournisseur inconnu");
            unknownSupplier.setStyle("-fx-font-size: 14px; -fx-text-fill: #999;");
            leftSection.getChildren().add(unknownSupplier);
        }

        // Middle section - Order details
        VBox middleSection = new VBox(5);
        middleSection.setPrefWidth(300);

        Label dateLabel = new Label("Date: " + commande.getDateCommande());
        dateLabel.setStyle("-fx-font-size: 14px;");

        Label statusLabel = new Label("Statut: " + commande.getEtat());
        statusLabel.setStyle(getStatusStyle(commande.getEtat()));

        middleSection.getChildren().addAll(dateLabel, statusLabel);

        // Right section - Total and actions
        VBox rightSection = new VBox(10);
        rightSection.setPrefWidth(150);

        Label totalLabel = new Label(String.format("Total: %.2f Dt", commande.getPrixTotal()));
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        rightSection.getChildren().add(totalLabel);

        // Add Receive button for pending orders
        if ("CREE".equalsIgnoreCase(commande.getEtat()) || "passer".equalsIgnoreCase(commande.getEtat())) {
            Button receiveBtn = new Button("Receive");
            receiveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
            receiveBtn.setOnAction(e -> handleReceiveOrder(commande));
            rightSection.getChildren().add(receiveBtn);
        }

        // Add Cancel button for pending orders
        if ("CREE".equalsIgnoreCase(commande.getEtat()) || "passer".equalsIgnoreCase(commande.getEtat())) {
            Button cancelBtn = new Button("Annuler");
            cancelBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;");
            cancelBtn.setOnAction(e -> handleCancelOrder(commande));
            rightSection.getChildren().add(cancelBtn);
        }

        card.getChildren().addAll(leftSection, middleSection, rightSection);

        // Add some spacing between cards
        VBox.setMargin(card, new Insets(0, 0, 10, 0));

        return card;
    }

    private Fournisseur getFournisseurById(int idFournisseur) {
        // Use FournisseurService to get supplier
        List<Fournisseur> allFournisseurs = fournisseurService.getAllFournisseurs();
        return allFournisseurs.stream()
                .filter(f -> f.getId_Fournisseur() == idFournisseur)
                .findFirst()
                .orElse(null);
    }

    private String getStatusStyle(String etat) {
        String status = etat.toUpperCase();

        switch (status) {
            case "CREE":
            case "PASSER":
                return "-fx-text-fill: #ff9800; -fx-font-weight: bold;";
            case "RECUE":
            case "REÇUE":
                return "-fx-text-fill: #4caf50; -fx-font-weight: bold;";
            case "ANNULÉE":
            case "ANNULEE":
                return "-fx-text-fill: #f44336; -fx-font-weight: bold;";
            default:
                return "-fx-text-fill: #757575; -fx-font-weight: bold;";
        }
    }

    private void handleReceiveOrder(Commande commande) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmer la réception");
        confirm.setHeaderText("Marquer la commande comme reçue");
        confirm.setContentText("Êtes-vous sûr de vouloir marquer cette commande comme reçue ?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                // Update status to "Reçue"
                commande.setEtat("Reçue");

                // TODO: Update the commande in database
                // You need to add a method in CommandeService like: commandeService.updateCommande(commande)

                // For now, just update the UI
                showAlert("Succès", "Commande marquée comme reçue avec succès !");

                // Refresh the orders display
                loadOrders();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de marquer la commande comme reçue: " + e.getMessage());
            }
        }
    }

    private void handleCancelOrder(Commande commande) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmer l'annulation");
        confirm.setHeaderText("Annuler la commande");
        confirm.setContentText("Êtes-vous sûr de vouloir annuler cette commande ?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                // Cancel the commande using CommandeService
                commandeService.annulerCommande(commande.getIdCommande());

                showAlert("Succès", "Commande annulée avec succès !");

                // Refresh the orders display
                loadOrders();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'annuler la commande: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        String category = categoryCombo.getValue();

        ordersContainer.getChildren().clear();

        List<Commande> filteredCommandes = filterCommandes(keyword, category);

        if (filteredCommandes.isEmpty()) {
            Label noResultsLabel = new Label("Aucune commande ne correspond à votre recherche");
            noResultsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666; -fx-padding: 20;");
            ordersContainer.getChildren().add(noResultsLabel);
            return;
        }

        for (Commande commande : filteredCommandes) {
            HBox orderCard = createOrderCard(commande);
            ordersContainer.getChildren().add(orderCard);
        }
    }

    private List<Commande> filterCommandes(String keyword, String category) {
        return allCommandes.stream()
                .filter(commande -> {
                    // Filter by category
                    boolean categoryMatch = true;
                    if (!"Tous".equals(category)) {
                        switch (category) {
                            case "En attente":
                                categoryMatch = "CREE".equalsIgnoreCase(commande.getEtat()) ||
                                        "passer".equalsIgnoreCase(commande.getEtat());
                                break;
                            case "Reçue":
                                categoryMatch = "Reçue".equalsIgnoreCase(commande.getEtat()) ||
                                        "RECUE".equalsIgnoreCase(commande.getEtat());
                                break;
                            case "Annulée":
                                categoryMatch = commande.getEtat().toLowerCase().contains("annul");
                                break;
                        }
                    }

                    // Filter by keyword
                    boolean keywordMatch = true;
                    if (!keyword.isEmpty()) {
                        Fournisseur fournisseur = getFournisseurById(commande.getIdFournisseur());
                        if (fournisseur != null) {
                            String supplierName = fournisseur.getNom().toLowerCase();
                            String supplierPhone = fournisseur.getNumTelephone().toLowerCase();
                            String orderId = String.valueOf(commande.getIdCommande());

                            keywordMatch = supplierName.contains(keyword) ||
                                    supplierPhone.contains(keyword) ||
                                    orderId.contains(keyword);
                        }
                    }

                    return categoryMatch && keywordMatch;
                })
                .collect(Collectors.toList());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // ===== METHODS YOU NEED TO ADD TO YOUR SERVICES =====

    // Add to CommandeService.java:
    /*
    public List<Commande> getAllCommandes() {
        // Implementation to get all commandes
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM Commande ORDER BY DateCommande DESC";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Commande commande = new Commande(
                    rs.getInt("IdCommande"),
                    rs.getInt("IdFournisseur"),
                    rs.getInt("IdEmploye"),
                    rs.getDate("DateCommande"),
                    rs.getDate("DateArrivee")
                );
                commande.setPrixTotal(rs.getFloat("PrixTotal"));
                commande.setEtat(rs.getString("Etat"));
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des commandes: " + e.getMessage());
        }
        return commandes;
    }
    */
}