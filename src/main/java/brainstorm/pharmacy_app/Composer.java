package brainstorm.pharmacy_app;
import brainstorm.pharmacy_app.Commande;

public class Composer {
    private Produit Produit;
    private Commande Commande;
    private int Quantite;
    Composer(Commande Co,Produit Prod,int Quant){
        this.Produit=Prod;
        this.Quantite=Quant;
        this.Commande=Co;
    }
}
