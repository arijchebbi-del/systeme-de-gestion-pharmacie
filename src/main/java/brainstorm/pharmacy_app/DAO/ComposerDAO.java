package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Model.Produit;

public interface ComposerDAO {
    void ajouterLigneCommande(int idCommande, int reference, int quantite);
}