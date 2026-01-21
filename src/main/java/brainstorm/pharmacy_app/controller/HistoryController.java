package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.*;
import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import brainstorm.pharmacy_app.DAO.VenteIM;
import brainstorm.pharmacy_app.DAO.ConstituerIM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.util.List;

public class HistoryController {


    @FXML private void chargerDashboard(ActionEvent event) { Navigation.navTo("/FXML/Dashboard.fxml",((Node) event.getSource())); }
    @FXML private void chargerPointOfSale(ActionEvent event) { Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource())); }
    @FXML private void chargerProductControl(ActionEvent event) { Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerStockDetails(ActionEvent event) { Navigation.navTo("/FXML/StockDetails.fxml",((Node) event.getSource())); }
    @FXML private void chargerOrderControl(ActionEvent event) { Navigation.navTo("/FXML/OrderControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerSuppliersControl(ActionEvent event) { Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerHistory(ActionEvent event) { Navigation.navTo("/FXML/History.fxml",((Node) event.getSource())); }
    @FXML private void chargerEmployeesControl(ActionEvent event) {
        // تجيب المستخدم اللي متسجل
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;

        if(current != null && "admin".equalsIgnoreCase(current.getRole())) {
            // يسمح بالوصول
            Navigation.navTo("/FXML/EmployeesControl.fxml", ((Node) event.getSource()));
        } else {
            // ممنوع الوصول
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        } }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        // تجيب المستخدم اللي متسجل
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;
        if (current != null && "admin".equalsIgnoreCase(current.getRole())) {
            // يسمح بالوصول
            Navigation.navTo("/FXML/AnalysisReports.fxml", ((Node) event.getSource())); //charger dashboard
        } else {
            // ممنوع الوصول
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }
    }

// zedet fll modele class VenteHistory bch nzidou nobre de produit vendu
    @FXML private TableView<VenteHistoryDTO> tableHistory;
    @FXML private TableColumn<VenteHistoryDTO, Integer> colActionID;
    @FXML private TableColumn<VenteHistoryDTO, String> colDate;
    @FXML private TableColumn<VenteHistoryDTO, Integer> colProducts;
    @FXML private TableColumn<VenteHistoryDTO, Double> colTotal;
    @FXML private TableColumn<VenteHistoryDTO, Void> colActions;

    private VenteIM venteDAO = new VenteIM();
    private ConstituerIM constituerDAO = new ConstituerIM();

    @FXML
    public void initialize() {

        if (tableHistory != null) {
            setupColumns();
            loadHistoryData();
        }
    }

    private void setupColumns() {

        colActionID.setCellValueFactory(new PropertyValueFactory<>("numFacture"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateVente"));
        colProducts.setCellValueFactory(new PropertyValueFactory<>("nombreProduits"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // mtaa ll bouton view
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnView = new Button("👁");
            {
                btnView.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-cursor: hand; -fx-font-size: 18px;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btnView.setOnAction(event -> {

                        VenteHistoryDTO selected = getTableView().getItems().get(getIndex());
                        showDetailsPopup(selected.getNumFacture());
                    });
                    setGraphic(btnView);
                }
            }
        });
    }

    private void loadHistoryData() {
        ObservableList<VenteHistoryDTO> historyList = FXCollections.observableArrayList();
        List<Vente> ventes = venteDAO.getAllVentes();

        if (ventes != null) {
            for (Vente v : ventes) {
                // nombre de produit fll fatoura
                int nbr = constituerDAO.getNombreProduitsParFacture(v.getNumFacture());
                historyList.add(new VenteHistoryDTO(v, nbr));
            }

            SortedList<VenteHistoryDTO> sortedData = new SortedList<>(historyList);
            sortedData.comparatorProperty().bind(tableHistory.comparatorProperty());

            tableHistory.setItems(sortedData);
            tableHistory.getSortOrder().add(colActionID);
            colActionID.setSortType(TableColumn.SortType.DESCENDING);
        }
    }

    // hedhi mtaa ki nenzel aal view yheli ll fenetre ll jdida
    private void showDetailsPopup(int numFacture) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-background-color: white;");

        TableView<Constituer> detailTable = new TableView<>();

        TableColumn<Constituer, Integer> colRef = new TableColumn<>("Référence Produit");
        colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));

        TableColumn<Constituer, Integer> colQty = new TableColumn<>("Quantité Vendue");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantiteVendu"));

        detailTable.getColumns().addAll(colRef, colQty);
        detailTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // bch nekhdou ll stoura mtaa ll fatourat
        List<Constituer> details = constituerDAO.getLignesParFacture(numFacture);
        detailTable.setItems(FXCollections.observableArrayList(details));

        Label title = new Label("Détails de la Facture N°: " + numFacture);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        layout.getChildren().addAll(title, detailTable);

        Scene scene = new Scene(layout, 450, 400);
        stage.setScene(scene);
        stage.setTitle("Détails de la vente #" + numFacture);
        stage.show();
    }
}