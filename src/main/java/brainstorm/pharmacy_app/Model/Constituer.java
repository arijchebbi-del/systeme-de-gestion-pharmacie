package brainstorm.pharmacy_app.Model;

public class Constituer {
    private Produit produit;
    private Vente vente;
    private int quantite;
    public Vente getVente(){
        return this.vente;
    }
    public Produit getProduit(){
        return this.produit;
    }
    public int getQuantite(){
        return this.quantite;
    }
}
