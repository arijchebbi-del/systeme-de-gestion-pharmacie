package brainstorm.pharmacy_app.Model;


public class Composer {
    private Produit produit;
    private Commande commande;
    private int quantite;
    Composer(Commande Co, Produit Prod, int Quant){
        this.produit=Prod;
        this.quantite=Quant;
        this.commande=Co;
    }
    Commande getCommande(){return commande;}
    Produit getProduit(){
        return produit;
    }
    public int getQuantiteComposer(){return quantite;}
    public void setQuantiteComposer(int q){quantite=q;}
    public void setCommande(Commande c){commande=c;}
    public void setProduit(Produit p){produit=p;}

}
