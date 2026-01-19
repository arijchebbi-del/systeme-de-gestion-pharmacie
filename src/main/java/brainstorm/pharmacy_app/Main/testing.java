package brainstorm.pharmacy_app.Main;
import brainstorm.pharmacy_app.Exceptions.AucunNomException;
import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Service.FournisseurService;
import brainstorm.pharmacy_app.Service.ProduitService;

public class testing {
    public static void main(String[] args) {
        FournisseurService service = new FournisseurService();

        // 1️⃣ Ajouter
        Fournisseur f = new Fournisseur();
        f.setNom("Pharma Plus");
        f.setNumTelephone("22334455");
        f.setEmail("contact@pharma.tn");
        f.setAdresse("Tunis");
        f.setTypeProduits("Medicaments");

        service.ajouterFournisseur(f);

        // 2️⃣ Modifier
        f.setId_Fournisseur(1); // existing ID in DB
        f.setNom("Pharma Plus Modifié");
        service.modifierFournisseur(f);

        // 3️⃣ Supprimer
        service.supprimerFournisseur(1);
        ProduitService produitService = new ProduitService();

        // 1️⃣ Création d'un produit
        Produit p = new Produit();
        p.setNomProduit("Paracetamol");
        p.setCategorie("Antalgique");
        p.setType("Comprime");
        p.setModeUtilisation("Voie orale");
        p.setOrdonnance(false);
        p.setPrixAchat(2.5f);
        p.setPrixVente(4.0f);

        try {
            produitService.ajouterProduit(p);
        } catch (AucunNomException e) {
            System.err.println(e.getMessage());
        }

        // 2️⃣ Modification du produit
        p.setReference(1); // ⚠️ ID EXISTANT dans la base
        p.setPrixVente(4.5f);
        produitService.modifierProduit(p);

        // 3️⃣ Recherche du produit
        Produit produitTrouve = produitService.rechercherParReference(1);
        if (produitTrouve != null) {
            System.out.println("Produit trouvé : " + produitTrouve.getNomProduit()
                    + " | Prix vente: " + produitTrouve.getPrixVente());
        } else {
            System.out.println("Produit introuvable");
        }

        // 4️⃣ Suppression du produit
        produitService.supprimerProduit(1);
    }
    }

