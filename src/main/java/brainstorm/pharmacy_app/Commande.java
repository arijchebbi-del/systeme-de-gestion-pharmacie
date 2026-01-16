package brainstorm.pharmacy_app;
import brainstorm.pharmacy_app.Composer;
import brainstorm.pharmacy_app.Employe;
import brainstorm.pharmacy_app.Fournisseur;
import java.util.ArrayList;

public class Commande {
    private int idCommande;
    private String dateCommande,dateArrivee;
    private double prixTotal;
    private Employe employe;
    private Fournisseur fournisseur;
    private ArrayList<Composer>composition=new ArrayList<>();
    public Commande(int id,ArrayList<Composer> compos,Fournisseur f,Employe e,double p,String dc,String da){
        this.idCommande=id;
        this.composition=compos;
        this.fournisseur=f;
        this.employe=e;
        this.prixTotal=p;
        this.dateArrivee=da;
        this.dateCommande=dc;
        System.out.println("l'employe "+employe.getNom()+"a commande "+getComposition(idCommande)+" du founisseur : "+fournisseur.getNom()+"le "+dateCommande+" et qui arrivera le "+dateArrivee);
    }

}


