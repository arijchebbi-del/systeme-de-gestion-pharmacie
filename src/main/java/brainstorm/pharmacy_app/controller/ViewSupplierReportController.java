package brainstorm.pharmacy_app.controller;
import brainstorm.pharmacy_app.DAO.RapportIM;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ViewSupplierReportController {

    @FXML private ListView<String> listSuppliersPerformance;
    private RapportIM rapportIM = new RapportIM();
    public void showReport() {
        listSuppliersPerformance.getItems().setAll(rapportIM.getSupplierPerformance());
    }
    @FXML private Button btnCancel; // bouton pour  cancel
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
