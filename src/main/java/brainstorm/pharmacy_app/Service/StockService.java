package brainstorm.pharmacy_app.Service;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Exceptions.NumLotNegativeException;
import brainstorm.pharmacy_app.Exceptions.QuantiteNegativeException;
import brainstorm.pharmacy_app.Model.Stock;

import java.sql.Timestamp;

public class StockService {

    private StockIM stockDAO = new StockIM();

    public void ajouterStock(Stock s) throws QuantiteNegativeException {

        if (s.getQuantite() < 0) {
            throw new QuantiteNegativeException("La quantite ne peut pas etre negative");
        }

        s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis()));
        stockDAO.creation_s(s);

        if (s.getQuantite() <= s.getSeuilMinimal()) {
            System.out.println(
                    "ALERTE STOCK : le lot " + s.getNumLot() + " a atteint le seuil minimal (" + s.getSeuilMinimal() + "). Quantité actuelle : " + s.getQuantite());
        }

        System.out.println("Stock ajouté via le service");
    }

    public void modifierStock(Stock s) throws NumLotNegativeException {

        if (s.getNumLot() <= 0) {
            throw new NumLotNegativeException("Numéro de lot invalide pour la modification");
        }

        s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis()));
        stockDAO.modification_s(s);

        //Verification needed after modification (who said the modification was successful)?
        //You can verify by comparing s with chercherStockParNumLot(s.NumLot)

        if (s.getQuantite() <= s.getSeuilMinimal()) {
            System.out.println(
                    " ALERTE STOCK : le lot " + s.getNumLot() + " a atteint le seuil minimal (" + s.getSeuilMinimal() + "). Quantité actuelle : " + s.getQuantite());
        }

        System.out.println("Stock modifié via le service !");
    }

    public void supprimerStock(int numLot) throws NumLotNegativeException {

        if (numLot <= 0) {
            throw new NumLotNegativeException("Numéro de lot invalide pour la suppression");
        }
        //Verification Needed to verify if numLot exist in DB before trying to delete it!
        stockDAO.suppression_s(numLot);
        //Verification NEEDED to verify if the row was deleted successfully or not!
        System.out.println("Stock supprimé via le service !");
    }

    public Stock chercherStockParNumLot(int numLot) throws NumLotNegativeException {

        if (numLot <= 0) {
            throw new NumLotNegativeException("Numéro de lot invalide pour la recherche");
        }

        Stock s = stockDAO.chercherParNumLot(numLot);

        if (s == null) {
            System.out.println("Aucun stock trouvé pour le lot : " + numLot);
        } else {
            System.out.println("Stock trouvé : Lot " + s.getNumLot() + " | Quantité : " + s.getQuantite() + " | Seuil minimal : " + s.getSeuilMinimal());
        }

        return s;
    }
}
