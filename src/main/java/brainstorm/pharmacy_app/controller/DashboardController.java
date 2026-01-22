package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.CommandeIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import brainstorm.pharmacy_app.Model.Employe;

public class DashboardController {


    @FXML private Label lblStockAlerte;
    @FXML private Label lblPendingOrder;

    private StockIM stockDAO = new StockIM();
    private CommandeIM commandeDAO = new CommandeIM();


    @FXML
    public void initialize() {
        refreshDashboardStats();
    }

    private void refreshDashboardStats() {

        int alerteCount = stockDAO.nbrProduitsDessousSeuil();
        if (lblStockAlerte != null) {
            lblStockAlerte.setText(String.valueOf(alerteCount));
        }

        int pendingOrdersCount = commandeDAO.nbrPendingOrders();
        if (lblPendingOrder != null) {
            lblPendingOrder.setText(String.valueOf(pendingOrdersCount));
        }
    }

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

    @FXML private void chargerPointOfSaleim(MouseEvent event) { Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource())); }
    @FXML private void chargerProductControlim(MouseEvent event) { Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerStockDetailsim(MouseEvent event) { Navigation.navTo("/FXML/StockDetails.fxml",((Node) event.getSource())); }
    @FXML private void chargerHistoryim(MouseEvent event) { Navigation.navTo("/FXML/History.fxml",((Node) event.getSource())); }

    @FXML
    private void chargerAddProduct(ActionEvent event){
        ProductControlController pcc = new ProductControlController();
        Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource()));
        pcc.handleOpenAddPopup(event);
    }

    @FXML
    private void chargerAddSupplier(ActionEvent event){
        SuppliersControlController scc = new SuppliersControlController();
        Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource()));
        scc.openAddSupplier();
    }
}