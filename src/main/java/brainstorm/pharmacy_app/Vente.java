package brainstorm.pharmacy_app;
import com.sun.org.apache.bcel.internal.Const;

import java.util.ArrayList;

public class Vente {
    private int numFacture;
    private String dateVente;
    private double prixTotal;
    private Employé employe;
    private ArrayList<Constituer> listProduit;
    public Vente(){}
    public Vente(int numFacture,String dateVente,Employé employe,ArrayList<Constituer> list){
        this.numFacture=numFacture;
        this.dateVente=dateVente;
        this.employe=employe;
        this.listProduit=list;
        int s=0;
        for (Constituer c : listProduit){
            //s=s+c.getProduit().getPrixVente();
        }
        prixTotal=s;
    }
    public int getNumFacture(){
        return this.numFacture;
    }
    public String getDateVente(){
        return this.dateVente;
    }
    public double getPrixTotal(){
        return this.prixTotal;
    }
    public Employé getEmploye(){
        return this.employe;
    }
    public void afficherVente(){
        System.out.println("Facture num : "+numFacture);
        System.out.println("Date : "+dateVente);
        System.out.println("Prix Total : "+prixTotal);
        //System.out.println("Employé : "+employe.getIdPersonne);
        System.out.println("Produits : ")
        for (Constituer c : listProduit){
            //System.out.println("- "+c.getProduit().getReference()+" : "+c.getQuantite());
        }
    }
}
