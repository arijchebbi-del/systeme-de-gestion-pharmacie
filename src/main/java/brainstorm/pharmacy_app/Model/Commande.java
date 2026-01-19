package brainstorm.pharmacy_app.Model;
import java.sql.Date;
import java.util.Scanner;

public class Commande {
    private int idCommande;
    private Date dateCommande,dateArrivee;
    private float prixTotal;
    private int idEmploye;
    private int id_Fournisseur;
    private String etat; //cree,modifie,annulee,recue

    public Commande(int id,int f,int e,Date dc,Date da){
        this.idCommande=id;
        this.id_Fournisseur=f;
        this.idEmploye=e;
        this.dateArrivee=da;
        this.dateCommande=dc;
        this.etat="CREE";
        System.out.println("Commande cree");
    }
    public int getIdCommande() {return idCommande;}
    public Date getDateCommande() {return dateCommande;}
    public Date getDateArrivee() {return dateArrivee;}
    public float getPrixTotal() {return prixTotal;}
    public int getIdEmploye() {return idEmploye;}
    public int getIdFournisseur() {return id_Fournisseur;}
    public String getEtat() {return etat;}
    // Setters
    public void setIdCommande(int idCommande) {this.idCommande = idCommande;}
    public void setDateCommande(Date dateCommande) {this.dateCommande = dateCommande;}
    public void setDateArrivee(Date dateArrivee) {this.dateArrivee = dateArrivee;}
    public void setPrixTotal(float prixTotal) {this.prixTotal = prixTotal;}
    public void setIdEmploye(int employe) {this.idEmploye = employe;}
    public void setIdFournisseur(int fournisseur) {this.id_Fournisseur = fournisseur;}
    public void setEtat(String etat) {this.etat = etat;}

}


