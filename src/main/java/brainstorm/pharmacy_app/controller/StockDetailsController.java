package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.DAO.RapportIM;
import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.Model.StockProduit;
import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

public class StockDetailsController {

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
        // تجيب المستخدم اللي متسجل
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;

        if(current != null && "admin".equalsIgnoreCase(current.getRole())) {
            // يسمح بالوصول
            Navigation.navTo("/FXML/EmployeeControl.fxml", ((Node) event.getSource()));
        } else {
            // ممنوع الوصول
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }//charger dashboard
    }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        Navigation.navTo("/FXML/AnalysisReports.fxml",((Node) event.getSource())); //charger dashboard
    }


    @FXML
    private TableView<StockProduit> tableStock;

    @FXML
    private TableColumn<StockProduit, Integer> colNumLot;
    @FXML
    private TableColumn<StockProduit, String> colProduit;
    @FXML
    private TableColumn<StockProduit, Integer> colQuantite;
    @FXML
    private TableColumn<StockProduit, Integer> colSeuil;
    @FXML
    private TableColumn<StockProduit, String> colEtat;
    @FXML
    private TableColumn<StockProduit, Integer> colDecalage;
    @FXML
    private TableColumn<StockProduit, String> colDerniereMAJ;

    @FXML
    private MFXTextField searchField;
    @FXML
    private MFXComboBox<String> filterCombo;
    @FXML
    private MFXButton btnRefresh;
    //hedhouma lalbe fehom infos khater azyen


    private RapportIM rapportIM = new RapportIM();
    private ObservableList<StockProduit> stockList = FXCollections.observableArrayList();
    private FilteredList<StockProduit> filteredData;

    private ProduitIM produitIM = new ProduitIM(); // to fetch product names

    @FXML
    public void initialize() {

        // map col
        if (colNumLot != null && colProduit != null && colQuantite != null) {
            colNumLot.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue().getStock().getNumLot()).asObject()
            );

            colProduit.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue().getProduit().getNomProduit())
            );

            colQuantite.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue().getStock().getQuantite()).asObject()
            );

            colSeuil.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue().getProduit().getSeuilMinimal()).asObject()
            );

            colEtat.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue().getEtat())
            );
            colDecalage.setCellValueFactory(cd ->
                    new SimpleIntegerProperty(cd.getValue().getDecalage()).asObject()
            );
            colDerniereMAJ.setCellValueFactory(cd -> {
                Timestamp ts = cd.getValue().getStock().getDerniereMiseAJour();
                String str = (ts == null) ? "" : ts.toString();  // format as needed
                return new SimpleStringProperty(str);
            });

            // ycolori l etat hasb low wala ok
            colEtat.setCellFactory(column -> new TableCell<StockProduit, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (item.equals("LOW")) {
                            setStyle("-fx-background-color: lightcoral; -fx-alignment: CENTER;");
                        }
                    }
                }
            });

            // charg donnes
            loadStock();

            // combo mfalter
            filterCombo.getItems().clear();
            filterCombo.getItems().addAll("Tous", "OK", "LOW");
            filterCombo.setValue("Tous");

            // list mfaltra
            filteredData = new FilteredList<>(stockList, sp -> true);

            // Listeners for search and filter
            searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
            filterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

            // Sorted list for column sorting
            SortedList<StockProduit> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
            tableStock.setItems(sortedData);

            // Refresh button
            btnRefresh.setOnAction(e -> loadStock());
        }
    }

    // load stock ml rapportetat stokc
    private void loadStock() {
        stockList.clear();
        stockList.addAll(rapportIM.rapportEtatStock()); // returns List<Stock>
    }

    // recherche et filre
    private void applyFilter() {
        filteredData.setPredicate(stockProduit -> {
            Stock stock = stockProduit.getStock();
            String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
            String selectedEtat = filterCombo.getValue();

            // Search matches reference or product name
            boolean matchesReference = String.valueOf(stock.getReference()).contains(searchText);
            boolean matchesNom = stockProduit.getProduit().getNomProduit().toLowerCase().contains(searchText);
            boolean matchesSearch = matchesReference || matchesNom;

            // Etat filter
            boolean matchesEtat = selectedEtat == null || selectedEtat.equals("Tous") || stockProduit.getEtat().equals(selectedEtat);

            return matchesSearch && matchesEtat;
        });
    }

    // refresh
    public void refreshTable() {
        loadStock();
    }


}



