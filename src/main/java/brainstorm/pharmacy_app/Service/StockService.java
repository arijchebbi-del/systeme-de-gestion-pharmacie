package brainstorm.pharmacy_app.Service;
import brainstorm.pharmacy_app.Exceptions.QuantiteNegativeException;
import brainstorm.pharmacy_app.Exceptions.SeuilMinimalAtteintException;
import brainstorm.pharmacy_app.Exceptions.NumLotNegativeException;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.DAO.StockIM;
import java.sql.Timestamp;

public class StockService {

    private StockIM stockDAO = new StockIM(); // DAO utilisé par le service

    // Ajouter un stock avec validation simple
    public void ajouterStock(Stock s) throws QuantiteNegativeException,SeuilMinimalAtteintException{
        if (s.getQuantite() < 0) {
            throw new QuantiteNegativeException("La quantité ne peut pas être négative");
        }
        if (s.getSeuilMinimal() <= 0) {
            throw new SeuilMinimalAtteintException("Le seuil minimal doit être > 0");
        }
        // On met à jour automatiquement la date de dernière mise à jour
        s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis()));

        stockDAO.creation_s(s);
        System.out.println("Stock ajouté via le service !");
    }

    // Modifier un stock
    public void modifierStock(Stock s) throws NumLotNegativeException{
        if (s.getNumLot() <= 0) {
            throw new NumLotNegativeException("Numéro de lot invalide pour la modification");
        }
        s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis())); // mise à jour automatique
        stockDAO.modification_s(s);
        System.out.println("Stock modifié via le service !");
    }

    // Supprimer un stock par NumLot
    public void supprimerStock(int numLot) throws NumLotNegativeException{
        if (numLot <= 0) {
            throw new NumLotNegativeException("Numéro de lot invalide pour la suppression");
        }
        stockDAO.suppression_s(numLot);
        System.out.println("Stock supprimé via le service !");
    }
}
