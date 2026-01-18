package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Utils.DBConnection;
<<<<<<< HEAD
=======
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
>>>>>>> 78cc8b5cede0ac1f0eec4c70173a7ddf01d41488

import java.sql.*;

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
<<<<<<< HEAD
    public Stock ChercherParNumLot(int numLot) {
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

                Produit produit = new Produit();
                produit.setReference(ref);

                Stock s = new Stock(numLot, produit, quantite, seuil);
                s.setDerniereMiseAJour(derniereMAJ);
                return s;
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
        return null;
    }

=======
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
>>>>>>> 78cc8b5cede0ac1f0eec4c70173a7ddf01d41488
}
