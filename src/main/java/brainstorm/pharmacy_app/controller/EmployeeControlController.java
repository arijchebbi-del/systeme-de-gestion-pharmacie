package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Service.EmployeService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EmployeeControlController {

    @FXML private TableView<Employe> tableEmployes;
    @FXML private TableColumn<Employe, Integer> colId;
    @FXML private TableColumn<Employe, String> colNom;
    @FXML private TableColumn<Employe, String> colPrenom;
    @FXML private TableColumn<Employe, String> colTel;
    @FXML private TableColumn<Employe, String> colEmail;
    @FXML private TableColumn<Employe, String> colHoraire;
    @FXML private TableColumn<Employe, Void> colView;
    @FXML private TableColumn<Employe, Void> colDelete;

    @FXML private MFXTextField searchField;
    @FXML private MFXButton btnAdd;

    private EmployeService employeService = new EmployeService();
    private ObservableList<Employe> employeList = FXCollections.observableArrayList();
    private FilteredList<Employe> filteredData;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("idEmploye"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("numTelephoneEmploye"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
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
        // تنجم تزيد getAllEmployes بعد
    }

    private void applyFilter() {
        String s = searchField.getText().toLowerCase();
        filteredData.setPredicate(e ->
                e.getNom().toLowerCase().contains(s) ||
                        e.getPrenom().toLowerCase().contains(s) ||
                        e.getEmail().toLowerCase().contains(s) ||
                        String.valueOf(e.getNumTelephoneEmploye()).contains(s) ||
                        e.getHoraire().toLowerCase().contains(s)
        );
    }

    // ---------- Add ----------
    private void openAddEmploye() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddEmployePopUp.fxml"));
            Parent root = loader.load();

            AddEmployeeController c = loader.getController();
            c.setParentController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter Employé");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void refreshTable() {
        loadEmployes();
    }

    // ---------- View ----------
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
            stage.setTitle("Employé");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ---------- Delete ----------
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
