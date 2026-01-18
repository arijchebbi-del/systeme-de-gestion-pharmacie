package brainstorm.pharmacy_app.Service;

import brainstorm.pharmacy_app.DAO.VenteIM;
import brainstorm.pharmacy_app.DAO.ConstituerIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Exceptions.QuantiteNegativeException;

public class VenteService {

    private VenteIM venteDAO = new VenteIM();
    private ConstituerIM constituerDAO = new ConstituerIM();
    private StockIM stockDAO = new StockIM();


    public void faireVente(Vente v, Produit p, Stock s, int quantiteV) throws Exception {

        if (p.getOrdonnance()==true && v.getPresenceOrd()==false) {
            throw new Exception("Ce médicament necessite une ordonnance.");
        }


        int stockActuel = stockDAO.getQuantiteByProduit(p.getReference());
        if (stockActuel < quantiteV) {
            throw new Exception("Stock insuffisant quantite disponible : " + stockActuel);
        }

        if (quantiteV <= 0) {
            throw new QuantiteNegativeException("La quantite vendue doit etre positive");
        }

        venteDAO.creation_v(v);

        constituerDAO.ajouterLigneVente(v.getNumFacture(), p.getReference(), quantiteV);

        // mise à jour du stock
        int resteEnStock = stockActuel - quantiteV;
        s.setQuantite(resteEnStock);
        stockDAO.modification_s(s);

        System.out.println("Vente validee stock restant pour " + p.getNomProduit() + " : " + resteEnStock);

        if (resteEnStock <= p.getSeuilMinimal()) {
            System.out.println("Le seuil minimal est atteint pour " + p.getNomProduit() );
        }
    }
}