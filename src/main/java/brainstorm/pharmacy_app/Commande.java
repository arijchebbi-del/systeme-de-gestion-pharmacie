package brainstorm.pharmacy_app;
import java.util.ArrayList;

public class Commande {
    private int idCommande;
    private String dateCommande,dateArrivee;
    private double prixTotal;
    private Employe employe;
    private Fournisseur fournisseur;
    private ArrayList<Composer>composition=new ArrayList<>();
    public Commande(int id,ArrayList<Composer> compos,Fournisseur f,Employe e,String dc,String da){
        this.idCommande=id;
        this.composition=compos;
        this.fournisseur=f;
        this.employe=e;
        this.dateArrivee=da;
        this.dateCommande=dc;
    }
    public ArrayList<Composer> getCompositions() {
        return composition;
    }

    public void afficherCommande() {
        System.out.println("Employé : " + employe.getNom());
        System.out.println("Fournisseur : " + fournisseur.getNom());
        System.out.println("Date : " + dateCommande);
        System.out.println("Produits :");

        for (Composer c : composition) {
            System.out.println("  - " + c.getProduit().getNomProduit() +" x " + c.getQuantite());
        }
    }
    void prixTotalCommande(){
        for (Composer c : composition){
            prixTotal+=((c.getProduit()).getPrixAchat())*(c.getQuantite());
        }
    }
    

}


