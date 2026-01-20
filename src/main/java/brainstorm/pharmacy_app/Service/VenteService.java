package brainstorm.pharmacy_app.Service;

import brainstorm.pharmacy_app.DAO.VenteIM;
import brainstorm.pharmacy_app.DAO.ConstituerIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Model.Constituer;
import brainstorm.pharmacy_app.Exceptions.QuantiteNegativeException;

public class VenteService {

    private VenteIM venteDAO = new VenteIM();
    private ConstituerIM constituerDAO = new ConstituerIM();
    private StockIM stockDAO = new StockIM();

    public void faireVente(Vente v, Produit p, Stock s, int quantiteV) throws Exception {

        if (p.getOrdonnance() && !v.isPresenceOrd()) {
            throw new Exception("Ce produit necessite une ordonnance.");
        }


        if (quantiteV <= 0) {
            throw new QuantiteNegativeException("La quantite vendue doit etre positive.");
        }


        int stockActuel = s.getQuantite();
        if (stockActuel < quantiteV) {
            throw new Exception("Stock insuffisant quantite disponible : " + stockActuel);
        }


        double montantVente = p.getPrixVente() * quantiteV;
        v.setPrixTotal(montantVente);


        venteDAO.creation_v(v);


        Constituer ligne = new Constituer(
                v.getNumFacture(),
                p.getReference(),
                quantiteV,
                p.getPrixVente()
        );
        constituerDAO.ajouterLigneVente(ligne);


        int resteEnStock = stockActuel - quantiteV;
        s.setQuantite(resteEnStock);
        stockDAO.modification_s(s);

        System.out.println(" Vente validee. Prix Total : " + v.getPrixTotal() + " DT");
        System.out.println(" Stock restant pour " + p.getNomProduit() + " : " + resteEnStock);

        if (resteEnStock <= s.getSeuilMinimal()) {
            System.out.println(" Le seuil minimal (" + s.getSeuilMinimal() + ") est atteint pour le lot de " + p.getNomProduit());
        }
    }
}