package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StockIM implements StockDAO {
    public void creation_s(Stock s) {

        String query = "INSERT INTO Stock (DerniereMiseAJour, Quantite, Reference)VALUES (?, ?, ?, ?) ";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setTimestamp(1, s.getDerniereMiseAJour());
            ps.setInt(2, s.getQuantite());
            ps.setInt(3, s.getNumLot());

            ps.executeUpdate();
            System.out.println("Stock ajoute");

        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }
    public void modification_s(Stock s) {

        String query = "UPDATE StockSET DerniereMiseAJour = ?,Quantite = ?,WHERE NumLot = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setTimestamp(1, s.getDerniereMiseAJour());
            ps.setInt(2, s.getQuantite());
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
    // donner quantité de produit
    public int getQuantiteByProduit(int reference) {
        String query = "SELECT Quantité FROM Stock WHERE Référence = ?";
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
}
