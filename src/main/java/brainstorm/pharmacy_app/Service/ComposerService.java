package brainstorm.pharmacy_app.Service;


import brainstorm.pharmacy_app.Model.Composer;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComposerService {
    private Connection connection;

    public ComposerService() throws SQLException {
        this.connection = DBConnection.getAdminConnection();
    }

    public void ajouterComposer(Composer composer) {
        String query = "INSERT INTO composer (idCommande, reference, quantite) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, composer.getIdCommande());
            ps.setInt(2, composer.getReference());
            ps.setInt(3, composer.getQuantiteComposer());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout du composant: " + e.getMessage());
        }
    }

    public List<Composer> getAllComposers() {
        List<Composer> composers = new ArrayList<>();
        String query = "SELECT * FROM composer";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idCommande = rs.getInt("idCommande");
                int reference = rs.getInt("reference");
                int quantite = rs.getInt("quantite");

                composers.add(new Composer(idCommande, reference, quantite));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return composers;
    }

    public List<Composer> getComposersByOrderId(int orderId) {
        List<Composer> composers = new ArrayList<>();
        String query = "SELECT * FROM composer WHERE idCommande = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCommande = rs.getInt("idCommande");
                    int reference = rs.getInt("reference");
                    int quantite = rs.getInt("quantite");

                    composers.add(new Composer(idCommande, reference, quantite));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return composers;
    }

    public void updateComposer(Composer composer) {
        String query = "UPDATE composer SET quantite = ? WHERE idCommande = ? AND reference = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, composer.getQuantiteComposer());
            ps.setInt(2, composer.getIdCommande());
            ps.setInt(3, composer.getIdProduit());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du composant: " + e.getMessage());
        }
    }

    public void deleteComposer(int idCommande, int reference) {
        String query = "DELETE FROM composer WHERE idCommande = ? AND reference = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idCommande);
            ps.setInt(2, reference);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du composant: " + e.getMessage());
        }
    }

    public boolean exists(int idCommande, int reference) {
        String query = "SELECT COUNT(*) FROM composer WHERE idCommande = ? AND reference = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idCommande);
            ps.setInt(2, reference);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}