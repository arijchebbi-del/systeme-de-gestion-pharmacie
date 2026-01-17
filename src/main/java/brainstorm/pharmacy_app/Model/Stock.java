package brainstorm.pharmacy_app.Model;
import java.sql.Timestamp;

public class Stock {
    private int numLot;
    private Timestamp derniereMiseAJour;
    private int quantite;
    private int seuilMinimal;
    private Produit produit;
    public Stock(int numLot, Produit produit, int quantite, int seuilMinimal) {
        this.numLot = numLot;
        this.produit = produit;
        this.quantite = quantite;
        this.seuilMinimal = seuilMinimal;
        this.derniereMiseAJour = new Timestamp(System.currentTimeMillis());}
    public int getNumLot() {return numLot;}
    public Timestamp getDerniereMiseAJour() {return derniereMiseAJour;}
    public int getQuantite() {return quantite;}
    public int getSeuilMinimal() {return seuilMinimal;}
    public Produit getProduit() {return produit;}

    public void setNumLot(int numLot) {this.numLot = numLot;}
    public void setDerniereMiseAJour(Timestamp derniereMiseAJour) {this.derniereMiseAJour = derniereMiseAJour;}
    public void setQuantite(int quantite) {this.quantite = quantite;}
    public void setSeuilMinimal(int seuilMinimal) {this.seuilMinimal = seuilMinimal;}
    public void setProduit(Produit produit) {this.produit = produit;}
}
