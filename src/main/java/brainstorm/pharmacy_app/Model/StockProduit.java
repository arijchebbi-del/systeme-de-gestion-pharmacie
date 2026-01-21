package brainstorm.pharmacy_app.Model;

public class StockProduit {
    private Stock s;
    private Produit p;

    public void setStock(Stock stock){
        s=stock;
    }
    public void setProduit(Produit produit){
        p=produit;
    }
    public Stock getStock(){return s;}
    public Produit getProduit(){return p;}


}
