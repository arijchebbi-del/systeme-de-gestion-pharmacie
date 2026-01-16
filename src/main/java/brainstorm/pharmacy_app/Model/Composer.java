package brainstorm.pharmacy_app.Model;


public class Composer {
    private Produit produit;
    private Commande commande;
    private int quantite;
    Composer(Commande Co,Produit Prod,int Quant){
        this.produit=Prod;
        this.quantite=Quant;
        this.commande=Co;
    }
    Commande getCommande(){return commande;}
    Produit getProduit(){
        return produit;
    }
    int getQuantite(){
        return quantite;
    }
    void setQuantite(int q){quantite=q;}
    void setCommande(Commande c){commande=c;}
    void setProduit(Produit p){produit=p;}
}
