package brainstorm.pharmacy_app.Model;

public class Constituer {
    private int numFacture;
    private int reference;

    private int quantiteVendu;
    private float prixVente;

    // Constructeurs
    public Constituer() {}

    public Constituer(int numFacture, int reference, int quantiteVendu, float prixApplique) {
        this.numFacture = numFacture;
        this.reference = reference;
        this.quantiteVendu = quantiteVendu;
        this.prixVente = prixApplique;
    }

    // Getters et Setters
    public int getNumFacture() { return numFacture; }
    public void setNumFacture(int numFacture) { this.numFacture = numFacture; }

    public int getReference() { return reference; }
    public void setReference(int reference) { this.reference = reference; }

    public int getQuantiteVendu() { return quantiteVendu; }
    public void setQuantiteVendu(int quantiteVendu) { this.quantiteVendu = quantiteVendu; }

    public float getPrixVente() { return prixVente; }
    public void setPrixApplique(float prixApplique) { this.prixVente = prixApplique; }
}
