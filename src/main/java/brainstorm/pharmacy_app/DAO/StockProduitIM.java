package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Model.StockProduit;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockProduitIM {

    public List<StockProduit> getAll(){
        String query="SELECT * FROM Stock s,Produit p WHERE p.Reference=s.Reference";
        List<StockProduit> StockProduits = new ArrayList<>();

        try (Connection conn = DBConnection.getEmployeeConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                StockProduit sp = new StockProduit();
                Produit p = new Produit();

                p.setReference(rs.getInt("p.Reference"));
                p.setNomProduit(rs.getString("NomProduit"));
                p.setCategorie(rs.getString("Categorie"));
                p.setType(rs.getString("Type"));
                p.setModeUtilisation(rs.getString("ModeUtilisation"));
                p.setOrdonnance(rs.getBoolean("Ordonnance")); // Mapping boolean
                p.setPrixAchat(rs.getFloat("PrixAchat"));     // Mapping float
                p.setPrixVente(rs.getFloat("PrixVente"));
                Stock s = new Stock(
                        rs.getInt("NumLot"),        // correspond à numLot
                        rs.getInt("s.Reference"),     // correspond à reference
                        rs.getInt("Quantite"),      // correspond à quantite
                        rs.getInt("SeuilMinimal")   // correspond à seuilMinimal
                );

                // Récupération du timestamp (dernière ligne de votre capture)
                s.setDerniereMiseAJour(rs.getTimestamp("derniereMiseAJour"));

                sp.setStock(s);
                sp.setProduit(p);
                StockProduits.add(sp);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return StockProduits;
    }

}
