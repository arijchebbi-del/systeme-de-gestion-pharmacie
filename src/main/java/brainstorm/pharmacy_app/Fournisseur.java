package brainstorm.pharmacy_app;

public class Fournisseur {
    private int id_Fournisseur;
    private String nom;
    private String numTelephone;
    private String email;
    private String adresse;
    private String typeProduits;

    //Getters
    public int getId_Fournisseur() {return id_Fournisseur;}
    public String getNom() {return nom;}
    public String getNumTelephone() {return numTelephone;}
    public String getEmail() {return email;}
    public String getAdresse() {return adresse;}
    public String getTypeProduits() {return typeProduits;}

    //Setters
    public void setId_Fournisseur(int id_Fournisseur) {this.id_Fournisseur = id_Fournisseur;}
    public void setNom(String nom) {this.nom = nom;}
    public void setNumTelephone(String numTelephone) {this.numTelephone = numTelephone;}
    public void setEmail(String email) {this.email = email;}
    public void setAdresse(String adresse) {this.adresse = adresse;}
    public void setTypeProduits(String typeProduits) {this.typeProduits = typeProduits;}
}
