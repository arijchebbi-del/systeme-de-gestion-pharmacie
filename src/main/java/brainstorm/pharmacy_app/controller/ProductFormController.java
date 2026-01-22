package brainstorm.pharmacy_app.controller;

import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Model.Stock;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ProductFormController {
    @FXML private MFXTextField txtNom, txtCategorie, txtType, txtMode, txtPrixAchat, txtPrixVente, txtSeuilMinimal;
    @FXML private MFXCheckbox checkOrdonnance;

    private Produit currentProduit;
    private ProductControlController parent;
    private ProduitIM dao = new ProduitIM();
    private StockIM sdao = new StockIM();
    public void initData(Produit p, ProductControlController parentController) {
        this.parent = parentController;
        this.currentProduit = p;

        if (p != null) { // Mode Modification
            txtNom.setText(p.getNomProduit());
            txtCategorie.setText(p.getCategorie());
            txtType.setText(p.getType());
            txtMode.setText(p.getModeUtilisation());
            txtPrixAchat.setText(String.valueOf(p.getPrixAchat()));
            txtPrixVente.setText(String.valueOf(p.getPrixVente()));
            txtSeuilMinimal.setText(String.valueOf(p.getSeuilMinimal()));
            checkOrdonnance.setSelected(p.getOrdonnance());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentProduit == null) currentProduit = new Produit();

        currentProduit.setNomProduit(txtNom.getText());
        currentProduit.setCategorie(txtCategorie.getText());
        currentProduit.setType(txtType.getText());
        currentProduit.setModeUtilisation(txtMode.getText());
        currentProduit.setOrdonnance(checkOrdonnance.isSelected());
        currentProduit.setPrixAchat(Float.parseFloat(txtPrixAchat.getText()));
        currentProduit.setPrixVente(Float.parseFloat(txtPrixVente.getText()));
        currentProduit.setSeuilMinimal(Integer.parseInt(txtSeuilMinimal.getText()));

        if (currentProduit.getReference() == 0) {
            dao.creation_p(currentProduit); // Ajout

        } else {
            dao.modification_p(currentProduit); // Modif
        }

        parent.refreshTable();
        closeStage();
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) txtNom.getScene().getWindow()).close();
    }
}