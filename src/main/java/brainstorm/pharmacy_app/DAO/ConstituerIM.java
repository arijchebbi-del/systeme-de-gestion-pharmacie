package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Constituer;
import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstituerIM implements ConstituerDAO {

    public void ajouterLigneVente(Constituer c) {
        // Ajout de la colonne PrixVente pour respecter la 3FN
        String query = "INSERT INTO Constituer (NumFacture, Reference, QuantiteVendu, PrixVente) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {


            ps.setInt(1, c.getNumFacture());
            ps.setInt(2, c.getReference());
            ps.setInt(3, c.getQuantiteVendu());
            ps.setFloat(4, c.getPrixVente());

            ps.executeUpdate();
            System.out.println("Produit " + c.getReference() + " ajoute a la facture num" + c.getNumFacture());

        } catch (SQLException e) {
            System.err.println(" Erreur SQL dans ConstituerIM : " + e.getMessage());
        }
    }
    public boolean verifierPresenceProduit(int numFacture, int reference) {
        boolean existe = false;
        // Requête pour compter si la paire (facture, produit) existe dans la table constituer
        String sql = "SELECT COUNT(*) FROM Constituer WHERE NumFacture = ? AND Reference = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numFacture);
            ps.setInt(2, reference);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si le compte est supérieur à 0, le produit est présent
                    int count = rs.getInt(1);
                    if (count > 0) {
                        existe = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du produit : " + e.getMessage());
            e.printStackTrace();
        }

        return existe;
    }

    public void supprimerLigneVente(int numFacture, int reference) {

        String sql = "DELETE FROM Constituer WHERE NumFacture = ? AND Reference = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numFacture);
            ps.setInt(2, reference);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produit " + reference + " supprimé de la facture " + numFacture);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la ligne de vente : " + e.getMessage());
            e.printStackTrace();
        }
    }
}