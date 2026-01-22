package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.EmployeIM;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Service.EmployeService;
import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class EmployeeControlController {
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
        } }
    @FXML
    private void chargerAnalysisReports(ActionEvent event) {

        Employe current = User.getInstance() != null ? User.getInstance().getUser() : null;
        if (current != null && "admin".equalsIgnoreCase(current.getRole())) {

            Navigation.navTo("/FXML/AnalysisReports.fxml", ((Node) event.getSource())); //charger dashboard
        }
        else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Accès refusé");
            alert.setHeaderText("Accès interdit");
            alert.setContentText("Seul un administrateur peut accéder à cette page.");
            alert.show();
        }
    }


    @FXML private TableView<Employe> tableEmployes;
    @FXML private TableColumn<Employe, Integer> colId;
    @FXML private TableColumn<Employe, String> colNom;
    @FXML private TableColumn<Employe, String> colPrenom;
    @FXML private TableColumn<Employe, String> colTel;
    @FXML private TableColumn<Employe, Integer> colSalary;
    @FXML private TableColumn<Employe, String> colHoraire;
    @FXML private TableColumn<Employe, Void> colView;
    @FXML private TableColumn<Employe, Void> colDelete;

    @FXML private MFXTextField searchField;
    @FXML private MFXButton btnAdd;

    private EmployeService employeService = new EmployeService();
    private EmployeIM employeDAO= new EmployeIM();
    private ObservableList<Employe> employeList = FXCollections.observableArrayList();
    private FilteredList<Employe> filteredData;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("idEmploye"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("numTelephoneEmploye"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        colHoraire.setCellValueFactory(new PropertyValueFactory<>("horaire"));

        loadEmployes();

        filteredData = new FilteredList<>(employeList, p -> true);
        searchField.textProperty().addListener((obs, o, n) -> applyFilter());

        SortedList<Employe> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableEmployes.comparatorProperty());
        tableEmployes.setItems(sortedData);

        addViewButtonToTable();
        addDeleteButtonToTable();

        btnAdd.setOnAction(e -> openAddEmploye());
    }

    private void loadEmployes() {
        employeList.clear();
        refreshTable();
    }

    public void refreshTable() {
        employeList.setAll(employeDAO.getAll());
        if (tableEmployes != null) {
            tableEmployes.refresh();
        }
    }

    private void applyFilter() {
        String s = searchField.getText().toLowerCase();
        filteredData.setPredicate(e ->
                e.getNom().toLowerCase().contains(s) ||
                        e.getPrenom().toLowerCase().contains(s) ||
                        String.valueOf(e.getSalaire()).contains(s) ||
                        String.valueOf(e.getNumTelephoneEmploye()).contains(s) ||
                        e.getHoraire().toLowerCase().contains(s)
        );
    }

    // bouton taa add
    private void openAddEmploye() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddEmployeePopUp.fxml"));
            Parent root = loader.load();

            AddEmployeeController c = loader.getController();
            c.setParentController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Ajouter Employé");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    // bouton taa vuew
    private void addViewButtonToTable() {
        colView.setCellFactory(p -> new TableCell<>() {
            private final Button btn = new Button("View");
            {
                btn.setOnAction(e -> {
                    Employe emp = getTableView().getItems().get(getIndex());
                    openViewEmploye(emp);
                });
            }
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openViewEmploye(Employe e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ViewEmploye.fxml"));
            Parent root = loader.load();

            ViewEmployeeController c = loader.getController();
            c.setEmploye(e);
            c.setParentController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Employé");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // bouton taa delete
    private void addDeleteButtonToTable() {
        colDelete.setCellFactory(p -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction(e -> {
                    Employe emp = getTableView().getItems().get(getIndex());
                    deleteEmploye(emp);
                });
            }
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteEmploye(Employe e) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Supprimer Employé ?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                employeService.supprimerEmploye(e.getIdEmploye());
                loadEmployes();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
