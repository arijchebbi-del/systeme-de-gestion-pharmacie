package brainstorm.pharmacy_app.Model;


public class Composer {
    private int reference;
    private int idCommande;
    private int quantite;
    public Composer(int idCo, int ref, int Quant){
        this.reference=ref;
        this.quantite=Quant;
        this.idCommande=idCo;
    }
    public int getIdCommande(){return idCommande;}
    public int getReference(){
        return reference;
    }
    public int getQuantiteComposer(){return quantite;}
    public void setQuantiteComposer(int q){quantite=q;}
    public void setIdCommande( int idCommande){this.idCommande=idCommande;}
    public void setProduit(int ref){reference=ref;}

}
