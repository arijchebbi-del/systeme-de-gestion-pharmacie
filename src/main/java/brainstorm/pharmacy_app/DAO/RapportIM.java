package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.*;

public class RapportIM {

    public float calculerCA(Date debut, Date fin) {
        float tot = 0;

        String sql = "SELECT SUM(PrixTotal) FROM vente v ON " +
                "WHERE v.DateVente BETWEEN ? AND ?";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, debut);
            ps.setDate(2, fin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tot = rs.getFloat(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return tot;
    }

    //  les produits quantite au dessous seuil
    public ResultSet getProduitAuDessous() throws SQLException {
        Connection con = DBConnection.getAdminConnection();
        String sql = "SELECT p.NomProduit, s.Quantité, p.SeuilMinimal " +
                "FROM produit p JOIN stock s ON p.Référence = s.Référence " +
                "WHERE s.Quantite <= p.SeuilMinimal";
        return con.createStatement().executeQuery(sql);
    }







    public void Historique() {
        String sql = "SELECT v.NumFacture, v.DateVente, p.NomProduit, c.Quantite " +
                "FROM vente v " +
                "JOIN constituer c ON v.NumFacture = c.NumFacture " +
                "JOIN produit p ON c.Référence = p.Référence " +
                "ORDER BY v.DateVente DESC";

        try (Connection con = DBConnection.getAdminConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("HISTORIQUE DÉTAILLÉ DES VENTES");
            System.out.printf("%-10s | %-12s | %-20s | %-10s%n", "Facture", "Date", "Produit", "Quantité");


            while (rs.next()) {
                System.out.printf("%-10d | %-12s | %-20s | %-10d%n",
                        rs.getInt("NumFacture"),
                        rs.getDate("DateVente"),
                        rs.getString("NomProduit"),
                        rs.getInt("Quantite"));
            }
        } catch (SQLException e) {
            System.err.println("erreur lors de la lecture de l'historique : " + e.getMessage());
        }


    }
}
