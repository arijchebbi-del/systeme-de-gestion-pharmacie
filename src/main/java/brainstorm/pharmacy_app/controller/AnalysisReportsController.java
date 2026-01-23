package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.RapportIM;
import brainstorm.pharmacy_app.DAO.StockProduitIM;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Model.StockProduit;
import brainstorm.pharmacy_app.Utils.DBConnection;
import brainstorm.pharmacy_app.Utils.PdfReportGenerator;
import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class AnalysisReportsController {

    // nav
    @FXML private void handleLogout(ActionEvent event){
        User.cleanUser();
        Navigation.navTo("/FXML/Login.fxml",((Node) event.getSource()));}
    @FXML private void chargerDashboard(ActionEvent event) { Navigation.navTo("/FXML/Dashboard.fxml",((Node) event.getSource())); }
    @FXML private void chargerPointOfSale(ActionEvent event) { Navigation.navTo("/FXML/PointOfSale.fxml",((Node) event.getSource())); }
    @FXML private void chargerProductControl(ActionEvent event) { Navigation.navTo("/FXML/ProductControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerStockDetails(ActionEvent event) { Navigation.navTo("/FXML/StockDetails.fxml",((Node) event.getSource())); }
    @FXML private void chargerOrderControl(ActionEvent event) { Navigation.navTo("/FXML/OrderControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerSuppliersControl(ActionEvent event) { Navigation.navTo("/FXML/SuppliersControl.fxml",((Node) event.getSource())); }
    @FXML private void chargerHistory(ActionEvent event) { Navigation.navTo("/FXML/History.fxml",((Node) event.getSource())); }
    @FXML private void chargerEmployeesControl(ActionEvent event) {
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;
        //verifie que l utilisateur est un admin pour quil puisse acceder aau rapport
        if(current != null && "admin".equalsIgnoreCase(current.getRole())) {
            Navigation.navTo("/FXML/EmployeesControl.fxml", ((Node) event.getSource()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        } }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {
        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;
        if (current != null && "admin".equalsIgnoreCase(current.getRole())) {
            Navigation.navTo("/FXML/AnalysisReports.fxml", ((Node) event.getSource())); //charger dashboard
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }
    }


    // date
    @FXML private MFXDatePicker dateDebut;
    @FXML private MFXDatePicker dateFin;

    //stock
    @FXML private Label lblTotalProducts;
    @FXML private Label lblLowStockProducts;
    @FXML private Label lblOutOfStockProducts;

    //revenue
    @FXML private Label lblTotalRevenue;
    @FXML private Label lblNbSales;
    @FXML private Label lblAverageBasket;

    //fournisseur
    @FXML private Label lblTotalSuppliersOrders;
    @FXML private Label lblSuppliersOnTime;
    @FXML private Label lblSuppliersLate;

    private RapportIM rapportIM = new RapportIM();
    private ObservableList<StockProduit> stockProduitList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        ActionEvent event = new ActionEvent();
        showSummaries(event);
    }
    // stock labels + uapdate
    private void updateStockSummary() {
        int totalProducts = stockProduitList.size();
        int lowStockProducts = 0;
        int outOfStockProducts = 0;

        for (StockProduit sp : stockProduitList) {
            if ("LOW".equals(sp.getEtat())) {
                lowStockProducts++;
            }
            if (sp.getStock().getQuantite() == 0) {
                outOfStockProducts++;
            }
        }

        lblTotalProducts.setText(String.valueOf(totalProducts));
        lblLowStockProducts.setText(String.valueOf(lowStockProducts));
        lblOutOfStockProducts.setText(String.valueOf(outOfStockProducts));
    }
    @FXML
    private void showSummaries(ActionEvent event){
        showStockSummary(event);
        showRevenueSummary(event);
        showSuppliersSummary(event);
    }
    @FXML
    private void showStockSummary(ActionEvent event) {
        Date debut,fin;
        if (dateDebut.getValue() == null && dateFin.getValue() == null) {
            debut= Date.valueOf(LocalDate.of(2000, 1, 1));
            fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
        }
        else if(dateDebut.getValue() == null && dateFin.getValue() != null){
            debut= Date.valueOf(LocalDate.of(2000, 1, 1));
            fin   = Date.valueOf(dateFin.getValue());
        }
        else if(dateDebut.getValue() != null && dateFin.getValue() == null){
            debut = Date.valueOf(dateDebut.getValue());
            fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
        }
        else{
            debut = Date.valueOf(dateDebut.getValue());
            fin   = Date.valueOf(dateFin.getValue());
        }
        System.out.println(debut);
        System.out.println(fin);

        stockProduitList.setAll(StockProduitIM.getStockByPeriod(debut, fin));
        updateStockSummary();
    }

    @FXML
    private void showRevenueSummary(ActionEvent event) {
        Date debut,fin;
        if (dateDebut.getValue() == null && dateFin.getValue() == null) {
            debut= Date.valueOf(LocalDate.of(2000, 1, 1));
            fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
        }
        else if(dateDebut.getValue() == null && dateFin.getValue() != null){
            debut= Date.valueOf(LocalDate.of(2000, 1, 1));
            fin   = Date.valueOf(dateFin.getValue());
        }
        else if(dateDebut.getValue() != null && dateFin.getValue() == null){
            debut = Date.valueOf(dateDebut.getValue());
            fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
        }
        else{
            debut = Date.valueOf(dateDebut.getValue());
            fin   = Date.valueOf(dateFin.getValue());
        }
        lblTotalRevenue.setText(rapportIM.getTotalRevenue(debut, fin) + " DT");
        lblNbSales.setText(String.valueOf(rapportIM.getNumberOfSales(debut, fin)));
        lblAverageBasket.setText(rapportIM.getAverageBasket(debut, fin) + " DT");
    }

    // SUPPLIERS
    @FXML
    private void showSuppliersSummary(ActionEvent event) {
        Date debut,fin;
        if (dateDebut.getValue() == null && dateFin.getValue() == null) {
            debut= Date.valueOf(LocalDate.of(2000, 1, 1));
            fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
        }
        else if(dateDebut.getValue() == null && dateFin.getValue() != null){
            debut= Date.valueOf(LocalDate.of(2000, 1, 1));
            fin   = Date.valueOf(dateFin.getValue());
        }
        else if(dateDebut.getValue() != null && dateFin.getValue() == null){
            debut = Date.valueOf(dateDebut.getValue());
            fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
        }
        else{
            debut = Date.valueOf(dateDebut.getValue());
            fin   = Date.valueOf(dateFin.getValue());
        }
        lblTotalSuppliersOrders.setText(
                String.valueOf(rapportIM.getTotalSupplierOrders(debut, fin))
        );
        lblSuppliersOnTime.setText(
                String.valueOf(rapportIM.getSuppliersOnTime(debut, fin))
        );
        lblSuppliersLate.setText(
                String.valueOf(rapportIM.getSuppliersLate(debut, fin))
        );
    }

    // FULL REVENUE REPORT
    @FXML
    private void openFullRevenueReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ViewRevenueReport.fxml"));
            Parent root = loader.load();

            ViewRevenueReportController controller = loader.getController();
            Date debut,fin;
            if (dateDebut.getValue() == null && dateFin.getValue() == null) {
                debut= Date.valueOf(LocalDate.of(2000, 1, 1));
                fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
            }
            else if(dateDebut.getValue() == null && dateFin.getValue() != null){
                debut= Date.valueOf(LocalDate.of(2000, 1, 1));
                fin   = Date.valueOf(dateFin.getValue());
            }
            else if(dateDebut.getValue() != null && dateFin.getValue() == null){
                debut = Date.valueOf(dateDebut.getValue());
                fin   = Date.valueOf(LocalDate.of(3000, 1, 1));
            }
            else{
                debut = Date.valueOf(dateDebut.getValue());
                fin   = Date.valueOf(dateFin.getValue());
            }

            controller.showReport(debut, fin);

            Stage stage = new Stage();
            double scaleX = 0.87;
            double scaleY = 0.9;
            root.setScaleX(scaleX);
            root.setScaleY(scaleY);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Full Revenue Report");
            stage.setScene(new Scene(root));
            stage.show();
            String rev = lblTotalRevenue.getText();
            String sales = lblNbSales.getText();
            String avg = lblAverageBasket.getText();


            //PDF
            String query = "SELECT v.NumFacture AS 'InvoiceID', v.DateVente AS 'Date', " +
                    "e.Nom AS 'Employee', v.PrixTotal AS 'AmountDT' " +
                    "FROM Vente v " +
                    "JOIN Employe e ON v.IdEmploye = e.IdEmploye " +
                    "WHERE v.DateVente BETWEEN ? AND ? " +
                    "ORDER BY v.DateVente DESC";

            try (Connection conn = DBConnection.getAdminConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setDate(1, debut);
                pstmt.setDate(2, fin);
                ResultSet rs = pstmt.executeQuery();
                PdfReportGenerator.generateReport("Full Revenue Analysis Report", rs, rev, sales, avg);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //FULL STOCK REPORT
    @FXML
    private void openStockReport(ActionEvent event) throws SQLException {


            PdfReportGenerator.generateStockReport("Full Stock Report", rapportIM.getFullStockReportData());

    }
    //Full Suppliers Report
    @FXML
    private void openFullSuppliersReport(ActionEvent event) throws SQLException {
            PdfReportGenerator.generateReport("Full Suppliers Report", rapportIM.getFullSuppliersReportData());
    }

}

