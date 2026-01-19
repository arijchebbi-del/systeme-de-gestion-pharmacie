package brainstorm.pharmacy_app.Model;

import java.sql.Date;

public class Vente {
    private int numFacture;
    private Date dateVente; //
    private int idEmploye;  // Clé étrangère vers l'employé
    private boolean presenceOrd;
    private double prixTotal;

    public Vente() {}

    public Vente(int numFacture, Date dateVente, int idEmploye, boolean presenceOrd, double prixTotal) {
        this.numFacture = numFacture;
        this.dateVente = dateVente;
        this.idEmploye = idEmploye;
        this.presenceOrd = presenceOrd;
        this.prixTotal = prixTotal;
    }

    // --- GETTERS ET SETTERS ---
    public int getNumFacture() { return numFacture; }
    public void setNumFacture(int numFacture) { this.numFacture = numFacture; }

    public Date getDateVente() { return dateVente; }
    public void setDateVente(Date dateVente) { this.dateVente = dateVente; }

    public int getIdEmploye() { return idEmploye; }
    public void setIdEmploye(int idEmploye) { this.idEmploye = idEmploye; }

    public boolean isPresenceOrd() { return presenceOrd; }
    public void setPresenceOrd(boolean presenceOrd) { this.presenceOrd = presenceOrd; }

    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    public void afficherEnTete() {
        System.out.println("Facture n° : " + numFacture + " | Date : " + dateVente + " | Employé : " + idEmploye);
    }
}