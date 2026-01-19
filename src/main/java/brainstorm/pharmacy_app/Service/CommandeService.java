package brainstorm.pharmacy_app.Service;

import brainstorm.pharmacy_app.DAO.CommandeIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.DAO.GererIM;
import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Exceptions.QuantiteNegativeException;
import java.sql.Timestamp;

public class CommandeService {


    private CommandeIM commandeDAO = new CommandeIM();
    private StockIM stockDAO = new StockIM();
    private ProduitIM produitDAO = new ProduitIM(); // Vérifiez si c'est produitIM ou produitDAO
    private GererIM gererDAO = new GererIM();

    /*public void passerCommande(Commande c) throws QuantiteNegativeException {

        if (c.getQuantiteComposer() <= 0) {
            throw new QuantiteNegativeException("La quantité commandée doit être positive");
        }

        c.setEtat("passer");

        commandeDAO.creation_c(c);

        System.out.println("Commande enregistrée avec l'état : " + c.getEtat());
    }


    public void annulerCommande(int idCommande) {
        if (idCommande > 0) {
            commandeDAO.annulation_c(idCommande);
            System.out.println(" Commande " + idCommande + " annulée.");
        }
    }
    public void receptionnerEtMettreAJourStock(Commande c, Produit p, Stock s, int idEmployeConnecte) throws QuantiteNegativeException {

        try {
            c.setEtat("Reçue");
            commandeDAO.modification_c(c);
            int quantiteRecue = c.getComposition().getQuantiteComposer();
            if (quantiteRecue < 0) {
                throw new QuantiteNegativeException("La quantite reçue ne peut pas etre negative");
            }
            if (produitDAO.existe(p.getReference())) {
                int stockActuel = stockDAO.getQuantiteByProduit(p.getReference());
                int nouvelleQuantite = stockActuel + quantiteRecue;
                s.setQuantite(nouvelleQuantite);
                s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis()));
                stockDAO.modification_s(s);
                gererDAO.enregistrerAction(idEmployeConnecte, s.getNumLot());
                System.out.println("Stock mis a jour pour : " + p.getNomProduit());
                if (nouvelleQuantite <= s.getSeuilMinimal()) {
                    System.out.println("Le produit " + p.getNomProduit() + " still sous le seuil minimal (" + s.getSeuilMinimal() + "). Stock actuel : " + nouvelleQuantite);
                }
            }
            else {
                System.out.println("Nouveau produit ");
                produitDAO.creation_p(p);

                s.setQuantite(quantiteRecue);
                s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis()));
                stockDAO.creation_s(s);

                gererDAO.enregistrerAction(idEmployeConnecte, s.getNumLot());

                if (quantiteRecue <= s.getSeuilMinimal()) {
                    System.out.println("Nouveau produit " + p.getNomProduit() + " ajouté avec une quantite inferieure au seuil minimal (" + s.getSeuilMinimal() + ")");
                }
            }

            System.out.println("Réception terminée");

        } catch (QuantiteNegativeException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erreur critique lors de la réception de commande : " + e.getMessage());
        }
    }*/

}
