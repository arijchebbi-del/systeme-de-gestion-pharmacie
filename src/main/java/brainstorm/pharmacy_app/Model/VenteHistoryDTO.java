package brainstorm.pharmacy_app.Model;
import brainstorm.pharmacy_app.Model.Vente;

import java.util.Date;

public class VenteHistoryDTO {
    private Vente vente;
    private int nombreProduits;

    public VenteHistoryDTO(Vente vente, int nombreProduits) {
        this.vente = vente;
        this.nombreProduits = nombreProduits;
    }

    public int getNumFacture() { return vente.getNumFacture(); }
    public Date getDateVente() { return vente.getDateVente(); }
    public int getNombreProduits() { return nombreProduits; }
    public double getTotal() { return vente.getPrixTotal(); }
}

