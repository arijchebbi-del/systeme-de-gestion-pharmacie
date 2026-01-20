package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ComposerIM implements ComposerDAO {

    @Override
    public void ajouterLigneCommande(int idCommande, int reference, int quantite) {
        String query = "INSERT INTO Composer (Reference, IdCommande, Quantite) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, reference);
            ps.setInt(2, idCommande);
            ps.setInt(3, quantite);

            ps.executeUpdate();
            System.out.println("Ligne de commande ajoutee dans 'composer'.");

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans ComposerIM : " + e.getMessage());
        }
    }
}