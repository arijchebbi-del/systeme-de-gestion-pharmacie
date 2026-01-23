package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteIM implements VenteDAO {


    public void creation_v(Vente v) {
        String query = "INSERT INTO Vente (DateVente, IdEmploye, PresenceOrd, PrixTotal) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {

            ps.setDate(1, v.getDateVente());
            ps.setInt(2, v.getIdEmploye());
            ps.setBoolean(3, v.isPresenceOrd());
            ps.setDouble(4, v.getPrixTotal());


            ps.executeUpdate();
            System.out.println("En-tete de vente ajoute avec succes ");
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                v.setNumFacture(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la creation de la vente : " + e.getMessage());
        }
    }
    public void updatePrixTotal(int numFacture, float total) {
        // mettre a jour ll total
        String sql = "UPDATE Vente SET PrixTotal = ? WHERE NumFacture = ?";

        try (Connection con = DBConnection.getAdminConnection(); // Utilisez votre classe de connexion
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setFloat(1, total);
            ps.setInt(2, numFacture);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du prix total : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Vente> getAllVentes() {
        List<Vente> listeVentes = new ArrayList<>();
        // On récupère les ventes triées par date (la plus récente en premier)
        String sql = "SELECT * FROM Vente ORDER BY DateVente DESC";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vente v = new Vente();
                v.setNumFacture(rs.getInt("NumFacture"));
                v.setDateVente(rs.getDate("DateVente"));
                v.setPrixTotal(rs.getDouble("PrixTotal"));
                v.setIdEmploye(rs.getInt("IdEmploye"));

                listeVentes.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
            e.printStackTrace();
        }
        return listeVentes;
    }

    public ResultSet getFacture(int numFacture) throws SQLException {
        String query = "SELECT p.NomProduit AS 'Produit', c.QuantiteVendu AS 'Quantité', " +
                "p.PrixVente AS 'PrixUnitaire(DT)', (c.QuantiteVendu * p.PrixVente) AS 'Total(DT)' " +
                "FROM Constituer c " +
                "JOIN Produit p ON c.Reference = p.Reference " +
                "WHERE c.NumFacture = ?";

        Connection con = brainstorm.pharmacy_app.Utils.DBConnection.getAdminConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, numFacture);
        return (ps.executeQuery());
    }
}