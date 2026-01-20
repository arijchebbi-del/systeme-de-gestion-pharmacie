package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.RapportIM;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.sql.Date;

public class ViewRevenueReportController {

    @FXML private Label lblTotalRevenue;
    @FXML private Label lblNbSales;
    @FXML private Label lblAverageBasket;

    @FXML private ListView<String> listTopEmployees;
    @FXML private ListView<String> listTopProducts;

    private RapportIM rapportIM = new RapportIM();

    public void showReport(Date debut, Date fin) {
        // touskieee
        lblTotalRevenue.setText(rapportIM.getTotalRevenue(debut, fin) + " DT");
        lblNbSales.setText(String.valueOf(rapportIM.getNumberOfSales(debut, fin)));
        lblAverageBasket.setText(rapportIM.getAverageBasket(debut, fin) + " DT");
        listTopEmployees.getItems().setAll(rapportIM.getTopEmployees(debut, fin));
        listTopProducts.getItems().setAll(rapportIM.getTopProducts(debut, fin));
    }
    @FXML private Button btnCancel; // teebt fi hietiii
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}

