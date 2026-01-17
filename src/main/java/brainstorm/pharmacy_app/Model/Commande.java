package brainstorm.pharmacy_app.Model;
import java.sql.Date;
import java.util.Scanner;

public class Commande {
    private int idCommande;
    private Date dateCommande,dateArrivee;
    private float prixTotal;
    private Employe employe;
    private Fournisseur fournisseur;
    private String etat; //cree,modifie,annulee,recue
    private Composer composition;

    public Commande(int id,Composer compos,Fournisseur f,Employe e,Date dc,Date da){
        this.idCommande=id;
        this.composition=compos;
        this.fournisseur=f;
        this.employe=e;
        this.dateArrivee=da;
        this.dateCommande=dc;
        this.etat="CREE";
        System.out.println("Commande cree");
    }
    public int getIdCommande() {return idCommande;}
    public Date getDateCommande() {return dateCommande;}
    public Date getDateArrivee() {return dateArrivee;}
    public float getPrixTotal() {return prixTotal;}
    public Employe getEmploye() {return employe;}
    public Fournisseur getFournisseur() {return fournisseur;}
    public String getEtat() {return etat;}
    public Composer getComposition() {return composition;}
    // Setters
    public void setIdCommande(int idCommande) {this.idCommande = idCommande;}
    public void setDateCommande(Date dateCommande) {this.dateCommande = dateCommande;}
    public void setDateArrivee(Date dateArrivee) {this.dateArrivee = dateArrivee;}
    public void setPrixTotal(float prixTotal) {this.prixTotal = prixTotal;}
    public void setEmploye(Employe employe) {this.employe = employe;}
    public void setFournisseur(Fournisseur fournisseur) {this.fournisseur = fournisseur;}
    public void setEtat(String etat) {this.etat = etat;}
    public void setComposition(Composer composition) {this.composition = composition;}


}


