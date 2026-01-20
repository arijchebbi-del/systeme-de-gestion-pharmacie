package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.RapportIM;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.Date;

public class ViewRevenueReportController {

    @FXML private Label lblTotalRevenue;
    @FXML private Label lblNbSales;
    @FXML private Label lblAverageBasket;

    @FXML private ListView<String> listTopEmployees;
    @FXML private ListView<String> listTopProducts;

    private RapportIM rapportIM = new RapportIM();

    public void showReport(Date debut, Date fin) {
        // Summary Labels
        lblTotalRevenue.setText(rapportIM.getTotalRevenue(debut, fin) + " DT");
        lblNbSales.setText(String.valueOf(rapportIM.getNumberOfSales(debut, fin)));
        lblAverageBasket.setText(rapportIM.getAverageBasket(debut, fin) + " DT");

        // Detailed Lists
        listTopEmployees.getItems().setAll(rapportIM.getTopEmployees(debut, fin));
        listTopProducts.getItems().setAll(rapportIM.getTopProducts(debut, fin));
    }
}

