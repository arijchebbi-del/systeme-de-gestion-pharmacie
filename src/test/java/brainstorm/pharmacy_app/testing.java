package brainstorm.pharmacy_app;
import brainstorm.pharmacy_app.Exceptions.*;
import brainstorm.pharmacy_app.Model.*;
import brainstorm.pharmacy_app.Service.*;

import java.sql.Date;
import java.sql.Timestamp;
/*
public class testing {
    public static void main(String[] args) {
        /*FournisseurService service = new FournisseurService();

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
    }*/
        /*EmployeService service = new EmployeService();

        try {
            // 1️⃣ Création d’un employé
            Employe e1 = new Employe();
            e1.setNom("Ahmed");
            e1.setPrenom("Ben Ali");
            e1.setNumTelephone(56565656);
            e1.setMotdepasse("password123");

            //service.ajouterEmploye(e1);

            // ⚠️ Supposons que l’ID est auto-incrémenté
            int id = 1; // adapte selon ta BD

            // 2️⃣ Recherche
            Employe eTrouve = service.chercherEmploye(id);
            System.out.println("Employé trouvé : " + eTrouve.getNom());

            // 3️⃣ Modification
            eTrouve.setPrenom("Ali");
            service.modifierEmploye(eTrouve);

            // 4️⃣ Changement de mot de passe
            service.changerMotDePasse(id, "newpass123");
            System.out.println("Mot de passe changé avec succès");

            // 5️⃣ Suppression
            service.supprimerEmploye(id);


        //} catch (AucunNomException e) {
            //System.out.println("Erreur ajout : " + e.getMessage());

        } catch (
                IdEmployeNegativeException e) {
            System.out.println("Erreur ID : " + e.getMessage());

        } catch (
                EmployeInexistantException e) {
            System.out.println("Recherche : " + e.getMessage());

        } catch (
                MotDePasseInvalideException e) {
            System.out.println("Mot de passe : " + e.getMessage());
        }*/
        /*StockService stockService = new StockService();

        try {
            // ===== Create Produit (MUST exist in DB) =====
            ProduitService produitService = new ProduitService();
            produitService.supprimerProduit(12);
            // 1️⃣ Création d'un produit
            Produit p = new Produit();
            p.setNomProduit("Paracetamol");
            p.setCategorie("Antalgique");
            p.setType("Comprime");
            p.setModeUtilisation("Voie orale");
            p.setOrdonnance(false);
            p.setPrixAchat(2.5f);
            p.setPrixVente(4.0f);

            //p.setReference(1);// ⚠️ must exist in table Produit
            produitService.ajouterProduit(p);
            int ref=p.getReference();
            // ===== TEST 1 : Ajouter Stock =====
            System.out.println("=== TEST : Ajouter Stock ===");

            Stock stock = new Stock(
                    1,      // numLot
                    p,      // produit
                    50,      // quantite
                    10      // seuilMinimal
            );

            stockService.ajouterStock(stock);

            // ===== TEST 2 : Rechercher Stock =====
            System.out.println("\n=== TEST : Rechercher Stock ===");
            Stock s = stockService.chercherStockParNumLot(1);

            // ===== TEST 3 : Modifier Stock =====
            System.out.println("\n=== TEST : Modifier Stock ===");
            if (s != null) {
                s.setQuantite(3); // <= seuil → alert
                stockService.modifierStock(s);
            }

            // ===== TEST 4 : Supprimer Stock =====
            System.out.println("\n=== TEST : Supprimer Stock ===");
            stockService.supprimerStock(1);

        } catch (QuantiteNegativeException | NumLotNegativeException e) {
            System.err.println("Erreur métier : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*try {
            // ===== 1️⃣ Create a Produit (must exist before stock or sale) =====
            ProduitService produitService = new ProduitService();

            Produit p = new Produit();
            p.setNomProduit("Paracetamol");
            p.setCategorie("Antalgique");
            p.setType("Comprime");
            p.setModeUtilisation("Voie orale");
            p.setOrdonnance(false);
            p.setPrixAchat(2.5f);
            p.setPrixVente(4.0f);

            // Insert into DB and get generated reference
            produitService.ajouterProduit(p);
            int referenceProduit = p.getReference(); // assume your DAO sets this after insertion
            System.out.println("Produit ajouté avec reference: " + referenceProduit);
            EmployeService service = new EmployeService();
            Employe e1 = new Employe();
            e1.setNom("Ahmed");
            e1.setPrenom("Ben Ali");
            e1.setNumTelephone(56565656);
            e1.setMotdepasse("password123");

            service.ajouterEmploye(e1);

            // ===== 2️⃣ Create Stock for that Produit =====
            StockService stockService = new StockService();
            Stock s = new Stock(
                    1,       // NumLot
                    1,       // Produit
                    50,      // Quantite
                    10       // SeuilMinimal
            );
            stockService.ajouterStock(s);

            // ===== 3️⃣ Create a Vente =====
            VenteService venteService = new VenteService();
            Vente v = new Vente();
            v.setDateVente(new Date(System.currentTimeMillis()));
            v.setPresenceOrd(false);
            v.setIdEmploye(1);// No ordonnance for this test

            // ===== 4️⃣ Execute a Sale =====
            int quantiteVente = 5;
            venteService.faireVente(v, p, s, quantiteVente);

            System.out.println("\n=== TEST COMPLETED ===");

        } catch (QuantiteNegativeException e) {
            System.err.println("Erreur Quantité : " + e.getMessage());
        } catch (NumLotNegativeException e) {
            System.err.println("Erreur NumLot : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // ===== 1️⃣ Add Produit =====
            ProduitService produitService = new ProduitService();
            Produit p = new Produit();
            p.setNomProduit("Paracetamol");
            p.setCategorie("Antalgique");
            p.setType("Comprime");
            p.setModeUtilisation("Voie orale");
            p.setOrdonnance(false);
            p.setPrixAchat(2.5f);
            p.setPrixVente(4.0f);

            produitService.ajouterProduit(p); // DAO should set generated Reference
            System.out.println("Produit ajouté avec reference: " + p.getReference());

            // ===== 2️⃣ Add Stock =====
            StockService stockService = new StockService();
            Stock s = new Stock(
                    1,  // NumLot
                    p,  // Produit
                    50, // Quantite
                    10  // SeuilMinimal
            );
            stockService.ajouterStock(s);

            // ===== 3️⃣ Add Employé =====
            EmployeService employeService = new EmployeService();
            Employe e = new Employe();
            e.setNom("Ahmed");
            e.setPrenom("Ben Ali");
            e.setNumTelephone(56565656);
            e.setMotdepasse("password123");
            employeService.ajouterEmploye(e); // should set generated ID
            int employeId = e.getIdEmploye();
            System.out.println("Employé ajouté avec ID : " + employeId);

            // ===== 4️⃣ Pass a Commande =====
            Date now = new Date(System.currentTimeMillis());
            Commande c = new Commande(0, null, f, e, now, now);
            CommandeService commandeService = new CommandeService();
            int idCommande = 0; // 0 or placeholder, DB may generate ID
            int idFournisseur = 1; // or whatever is needed
            Date dateCommande = new Date(System.currentTimeMillis());
            String etat = "passer";
            int idEmploye = employeId;
            Composer composition = new Composer(p.getReference(), 20, s.getNumLot());
            Commande c = new Commande();
            composition.setQuantiteComposer(20); // Quantity ordered
            c.setComposition(composition);

            commandeService.passerCommande(c); // Etat = "passer"

            // ===== 5️⃣ Receptionner commande and update stock =====
            commandeService.receptionnerEtMettreAJourStock(c, p, s, employeId);

            // ===== 6️⃣ Make a Vente =====
            VenteService venteService = new VenteService();
            Vente v = new Vente();
            v.setDateVente(new Date(System.currentTimeMillis()));
            v.setPresenceOrd(false); // No ordonnance
            v.setIdEmploye(employeId);

            int quantiteVente = 5;
            venteService.faireVente(v, p, s, quantiteVente);

            System.out.println("\n=== FULL TEST COMPLETED SUCCESSFULLY ===");

        } catch (QuantiteNegativeException e) {
            System.err.println("Erreur Quantité : " + e.getMessage());
        } catch (NumLotNegativeException e) {
            System.err.println("Erreur NumLot : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    }


*/
