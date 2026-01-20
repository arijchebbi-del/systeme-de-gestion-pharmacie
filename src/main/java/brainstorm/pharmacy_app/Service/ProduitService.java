package brainstorm.pharmacy_app.Service;

import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Exceptions.AucunNomException;

import java.util.List;

public class ProduitService {

    private ProduitIM produitDAO = new ProduitIM();

    public void ajouterProduit(Produit p) throws AucunNomException {
        if (p.getNomProduit() == null || p.getNomProduit().isEmpty()) {
            throw new AucunNomException("Le nom du produit est obligatoire");
        }

        produitDAO.creation_p(p);
        System.out.println("Produit " + p.getNomProduit() + " ajoute au catalogue.");
    }

    public void modifierProduit(Produit p) {
        if (produitDAO.existe(p.getReference())) {
            produitDAO.modification_p(p);
            System.out.println("Mise a jour du produit effectuee");
        }
    }


    public void supprimerProduit(int reference) {
        produitDAO.suppression_p(reference);
        System.out.println("Produit supprime du catalogue");
    }

    public Produit rechercherParReference(int ref) {
        return produitDAO.lire_p(ref);
    }




}
