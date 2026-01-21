package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Composer; // Ajout import
import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Ajout import
import java.sql.SQLException;
import java.util.ArrayList; // Ajout import
import java.util.List; // Ajout import

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

    // AJOUT : Recupere les produits d'une commande pour la reception en stock et l'affichage des details
    public List<Composer> getProduitsParCommande(int idCommande) {
        List<Composer> liste = new ArrayList<>();
        String query = "SELECT * FROM Composer WHERE IdCommande = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Utilisation du constructeur Composer(int reference, int idCommande, int quantite)
                Composer ligne = new Composer(
                        rs.getInt("Reference"),
                        rs.getInt("IdCommande"),
                        rs.getInt("Quantite")
                );
                liste.add(ligne);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recuperation des lignes (Composer) : " + e.getMessage());
        }
        return liste;
    }
}