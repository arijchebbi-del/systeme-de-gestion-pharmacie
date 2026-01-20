package brainstorm.pharmacy_app.controller;
import brainstorm.pharmacy_app.DAO.RapportIM;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ViewSupplierReportController {

    @FXML private ListView<String> listSuppliersPerformance;
    private RapportIM rapportIM = new RapportIM();
    public void showReport() {
        listSuppliersPerformance.getItems().setAll(rapportIM.getSupplierPerformanceSimple());
    }
}
