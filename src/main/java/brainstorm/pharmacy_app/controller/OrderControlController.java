package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Service.CommandeService;
import brainstorm.pharmacy_app.nav.Navigation;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class OrderControlController {
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private FlowPane ordersContainer;
    private void loadOrders() {
        ordersContainer.getChildren().clear();

        List<Commande> Commandes = CommandeService.();

        for (MysqlxCrud.Order order : orders) {
            ordersContainer.getChildren().add(createOrderCard(order));
        }
    }
    private VBox createOrderCard(Commande order) {
        VBox card = new VBox(8);
        card.getStyleClass().add("order-card");
        card.setPrefWidth(280);

        Label supplier = new Label(order.getFournisseur().getNom());
        Label date = new Label("Date : " + order.getDateCommande());
        Label total = new Label("Total : " + order.getPrixTotal() + " Dt");

        Button receiveBtn = new Button("Receive");
        receiveBtn.setOnAction(e -> handleReceiveOrder(order));

        card.getChildren().addAll(supplier, date, total, receiveBtn);
        return card;
    }
    @FXML
    private void handleAddOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddOrder.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Nouvelle Commande");
            stage.initModality(Modality.APPLICATION_MODAL);

            Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.initOwner(owner);

            stage.showAndWait();

            // VERY IMPORTANT
            loadOrders(); // refresh after add

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        String category = categoryCombo.getValue();

        ordersContainer.getChildren().clear();

        List<Commande> orders = CommandeService.searchOrders(keyword, category);

        for (Commande o : orders) {
            ordersContainer.getChildren().add(createOrderCard(o));
        }
    }




    private CommandeService orderService = new CommandeService();
    @FXML
    public void initialize() {
        categoryCombo.getItems().addAll("All", "Pending", "Received");
        categoryCombo.setValue("All");

        loadOrders();
    }
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
}
