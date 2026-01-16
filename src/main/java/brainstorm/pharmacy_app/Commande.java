package brainstorm.pharmacy_app;
import java.util.ArrayList;
import java.util.Scanner;

public class Commande {
    private int idCommande;
    private String dateCommande,dateArrivee;
    private double prixTotal;
    private Employe employe;
    private Fournisseur fournisseur;
    private String etat; //cree,modifie,annulee,recue
    private ArrayList<Composer>composition=new ArrayList<>();
    public Commande(int id,ArrayList<Composer> compos,Fournisseur f,Employe e,String dc,String da){
        this.idCommande=id;
        this.composition=compos;
        this.fournisseur=f;
        this.employe=e;
        this.dateArrivee=da;
        this.dateCommande=dc;
        this.etat="CREE";
        System.out.println("Commande cree");
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
    public String getEtat(){
        return etat;
    }
    public void annulerCommande(){
        if(etat.equals("RECUE")){
            System.out.println("Vous avez recu la commande ! il est impossible de l'annuler");
        }
        else{
            etat="ANNULEE";
            System.out.println("Commande est annulee");
        }
    }
    public void modifierCommande(){
        if (etat.equals("ANNULEE")||etat.equals("RECUE")){
            System.out.println("vous ne pouvez pas modifier la commande");
            return;
        }
        Scanner sc=new Scanner(System.in);
        int choix;
        do{
            System.out.println("*** Modification de la Commande ***");
            System.out.println("1)Ajouter un produit");
            System.out.println("2)Modifier la quantite un produit");
            System.out.println("3)Supprimer un produit");
            System.out.println("4)Quitter");
            System.out.println("Que souhaitez vous faire(entrez le numero de votre choix) ");
            choix=sc.nextInt();
            switch(choix) {
                case 1:
                    System.out.println("Ajout du produit");
                    Produit p = new Produit();
                    System.out.println("Quelle est sa quantite :");
                    int qu = sc.nextInt();
                    composition.add(new Composer(this, p, qu));
                    System.out.println("produit ajoutee");
                case 2:
                    System.out.println("donner la reference de produit a commander");
                    int r= sc.nextInt();
                    if (chercherProduitDansCommande(r)==-1){
                        System.out.println("vous n avez pas commander ce produit");
                    }
                    else{
                        System.out.println("Quelle est la nouvelle quantite :");
                        int q= sc.nextInt();
                        Composer c;
                        c=composition.get(chercherProduitDansCommande(r));
                        c.setQuantite(q);
                        System.out.println("la quantite du produit est modifiee");
                    }
                case 3:
                    System.out.println("donner la reference de produit a commander");
                    int re= sc.nextInt();
                    if (chercherProduitDansCommande(re)==-1){
                        System.out.println("ce produit est deja inexistant!");
                    }
                    else{
                        composition.remove(composition.get(chercherProduitDansCommande(re)));
                        System.out.println("le produit supprime de la commande");
                    }
                case 4:
                    return;
                default:
                    System.out.println("choix invalide!!!");
            }
        }while(choix!=4);
    }
    int chercherProduitDansCommande(int ref){
        for (Composer c :composition){
            if (c.getProduit().getReference()==ref) {
                return composition.indexOf(c);
            }
        }
        return 0;
    }
    
}


