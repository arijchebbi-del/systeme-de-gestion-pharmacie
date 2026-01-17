package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConstituerIM implements ConstituerDAO {

    @Override
    public void ajouterLigneVente(int numFacture, int reference, int quantite) {
        String query = "INSERT INTO constituer (Reference, NumFacture, Quantite) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, reference);
            ps.setInt(2, numFacture);
            ps.setInt(3, quantite);

            ps.executeUpdate();
            System.out.println("Ligne de vente ajoutée dans 'constituer'.");

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans ConstituerIM : " + e.getMessage());
        }
    }
}