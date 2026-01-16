package brainstorm.pharmacy_app.Model;
import java.time.LocalDate;

public class Stock {
    private int numLot;
    private LocalDate derniereMiseAJour;
    private int quantite;
    private int seuilMinimal;
    private Produit produit;
    public Stock(int numLot, Produit produit, int quantite, int seuilMinimal) {
        this.numLot = numLot;
        this.produit = produit;
        this.quantite = quantite;
        this.seuilMinimal = seuilMinimal;
        this.derniereMiseAJour = LocalDate.now();
    }




}
