package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


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

        String query = "UPDATE StockSET DerniereMiseAJour = ?,Quantite = ?,SeuilMinimal = ?WHERE NumLot = ?";

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
}
