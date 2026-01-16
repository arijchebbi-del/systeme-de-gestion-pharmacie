package brainstorm.pharmacy_app.Model;

public class Produit {
    private int reference;
    private String nomProduit,categorie,type,modeUtilisation;
    private boolean ordonnance;
    private float prixAchat,prixVente;
    private int seuilMinimal;
    
    
    //Setters
    public void setReference(int r) {reference=r;}
    public void setNomProduit(String np) {nomProduit=np;}
    public void setCategorie(String c) {categorie=c;}
    public void setType(String t) {type=t;}
    public void setModeUtilisation(String mu) {modeUtilisation=mu;}
    public void setPrixAchat(float pa) {prixAchat=pa;}
    public void setPrixVente(float pv) {prixVente=pv;}
    public void setSeuilMinimal(int s) {seuilMinimal=s;}
    public void setOrdonnance(boolean o) {ordonnance=o;}
    
    //Getters
    public int getReference() {return reference;}
    public String getNomProduit() {return nomProduit;}
    public String getCategorie() {return categorie;}
    public String getType() {return type;}
    public String getModeUtilisation() {return modeUtilisation;}
    public float getPrixAchat() {return prixAchat;}
    public float getPrixVente() {return prixVente;}
    public float getSeuilMinimal() {return seuilMinimal;}
    public boolean getOrdonnance() {return ordonnance;}
    
}
