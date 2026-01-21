package brainstorm.pharmacy_app.Model;
import brainstorm.pharmacy_app.DAO.ProduitIM;

import java.sql.Timestamp;

public class Stock {
    private int numLot;
    private Timestamp derniereMiseAJour;
    private int quantite;
    private int reference;

    public Stock(){}
    public Stock(int numLot, int ref, int quantite) {
        this.numLot = numLot;
        this.reference = ref;
        this.quantite = quantite;
        this.derniereMiseAJour = new Timestamp(System.currentTimeMillis());}


    public int getNumLot() {return numLot;}
    public Timestamp getDerniereMiseAJour() {return derniereMiseAJour;}
    public int getQuantite() {return quantite;}
    public int getReference() {return reference;}

    public void setNumLot(int numLot) {this.numLot = numLot;}
    public void setDerniereMiseAJour(Timestamp derniereMiseAJour) {this.derniereMiseAJour = derniereMiseAJour;}
    public void setQuantite(int quantite) {this.quantite = quantite;}
    public void setReference(int ref) {this.reference =ref;}

    public String getNomProduit(int ref) {
        ProduitIM produitIM = new ProduitIM();
        return produitIM.getNomProduitByRef(this.reference);
    }


}
