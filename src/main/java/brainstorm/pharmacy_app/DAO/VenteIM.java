package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.*;

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
        String sql = "UPDATE vente SET PrixTotal = ? WHERE NumFacture = ?";

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
}