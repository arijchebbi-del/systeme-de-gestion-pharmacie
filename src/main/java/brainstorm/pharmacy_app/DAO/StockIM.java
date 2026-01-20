package brainstorm.pharmacy_app.DAO;


import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Utils.DBConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StockIM implements StockDAO {
    public void creation_s(Stock s) {


        String query = "INSERT INTO Stock (DerniereMiseAJour, Quantite, SeuilMinimal, Reference)VALUES (?, ?, ?, ?) ";


        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {


            ps.setTimestamp(1, s.getDerniereMiseAJour());
            ps.setInt(2, s.getQuantite());
            ps.setInt(3, s.getSeuilMinimal());
            ps.setInt(4, s.getNumLot());


            ps.executeUpdate();
            System.out.println("Stock ajouté");


        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }
    public void modification_s(Stock s) {


        String query = "UPDATE Stock SET DerniereMiseAJour = ?,Quantite = ?,SeuilMinimal = ? WHERE NumLot = ?";


        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {


            ps.setTimestamp(1, s.getDerniereMiseAJour());
            ps.setInt(2, s.getQuantite());
            ps.setInt(3, s.getSeuilMinimal());
            ps.setInt(4, s.getNumLot());


            ps.executeUpdate();
            System.out.println("Stock modifié");


        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }
    public void suppression_s(int numLot) {


        String query = "DELETE FROM Stock WHERE NumLot = ?";


        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {


            ps.setInt(1, numLot);
            ps.executeUpdate();
            System.out.println("Stock supprimé");


        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }
    public Stock chercherParNumLot(int numLot) {
        String query = "SELECT * FROM Stock WHERE NumLot = ?";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {


            ps.setInt(1, numLot);
            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                int quantite = rs.getInt("Quantite");
                int seuil = rs.getInt("SeuilMinimal");
                int ref = rs.getInt("Reference");
                Timestamp derniereMAJ = rs.getTimestamp("DerniereMiseAJour");





                Stock s = new Stock(numLot, ref, quantite, seuil);
                s.setDerniereMiseAJour(derniereMAJ);
                return s;
            }


        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
        return null;
    }
    public int getQuantiteByProduit(int reference) {
        String query = "SELECT Quantite FROM Stock WHERE Référence = ?";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, reference);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Stock> getToutLeStock() {
        List<Stock> listeStock = new ArrayList<>();
        String query = "SELECT * FROM Stock"; // La variable 'query' utilisée dans votre capture

        // Utilisation du bloc try-with-resources selon votre image
        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Utilisation du constructeur de votre classe Stock
                Stock s = new Stock(
                        rs.getInt("numLot"),        // correspond à numLot
                        rs.getInt("Reference"),     // correspond à reference
                        rs.getInt("Quantite"),      // correspond à quantite
                        rs.getInt("SeuilMinimal")   // correspond à seuilMinimal
                );

                // Récupération du timestamp (dernière ligne de votre capture)
                s.setDerniereMiseAJour(rs.getTimestamp("derniereMiseAJour"));

                listeStock.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans StockIM : " + e.getMessage());
            e.printStackTrace();
        }
        return listeStock;
    }

    public float getPrixProduitByRef(int ref) {
        float prix = 0.0f;
        String sql = "SELECT PrixVente FROM Produit WHERE Reference = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ref);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prix = rs.getFloat("PrixVente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prix;
    }

}
