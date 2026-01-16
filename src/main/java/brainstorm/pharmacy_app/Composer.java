package brainstorm.pharmacy_app;


public class Composer {
    private Produit produit;
    private Commande commande;
    private int quantite;
    Composer(Commande Co,Produit Prod,int Quant){
        this.produit=Prod;
        this.quantite=Quant;
        this.commande=Co;
    }
    Produit getProduit(){
        return produit;
    }
    int getQuantite(){
        return quantite;
    }
    void setQuantite(int q){quantite=q;}
}
