package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Utils.DBConnection;
import brainstorm.pharmacy_app.Utils.User;
import brainstorm.pharmacy_app.nav.Navigation;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import java.sql.*; // Import modifié pour inclure Statement et List
import java.util.ArrayList;
import java.util.List;

public class CommandeIM implements CommandeDAO {
    public void creation_c(Commande c) {
        // Ajout de RETURN_GENERATED_KEYS pour récupérer l'ID auto-incrémenté
        String query = "INSERT INTO Commande(PrixTotal,DateCommande,DateArrivee,IdEmploye,IdFournisseur,Etat) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) { // Ajout paramètre keys
            ps.setDouble(1, c.getPrixTotal());
            ps.setDate(2, c.getDateCommande());
            ps.setDate(3, c.getDateArrivee());
            ps.setInt(4, c.getIdEmploye());
            ps.setInt(5, c.getIdFournisseur());
            ps.setString(6, c.getEtat());
            ps.executeUpdate();

            // Ajout : Récupération de l'ID généré pour l'objet Commande
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setIdCommande(rs.getInt(1));
            }

            System.out.println("Commande bien ajoutée");
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }

    public void modification_c(Commande c) {
        String query = "UPDATE Commande SET PrixTotal = ?,DateCommande = ?,DateArrivee = ?,IdEmploye = ?,IdFournisseur = ?,Etat = ? WHERE IdCommande = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setFloat(1, c.getPrixTotal());
            ps.setDate(2, c.getDateCommande());
            ps.setDate(3, c.getDateArrivee());
            ps.setInt(4, c.getIdEmploye());
            ps.setInt(5, c.getIdFournisseur());
            ps.setString(6, c.getEtat());
            ps.setInt(7, c.getIdCommande()); // Ajout du paramètre pour le WHERE

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("commande modifiée avec succès");
            } else {
                System.out.println("Aucune commande trouvée avec cet ID");
            }

        } catch (SQLException ex) {
            System.err.println("Erreur SQL: " + ex.getMessage());
        }
    }

    public void annulation_c(int idCommande) {


        String sql = "DELETE FROM Composer WHERE IdCommande = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCommande);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "DELETE FROM Commande WHERE IdCommande = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCommande);
            ps.executeUpdate();
            System.out.println("Commande annulée");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNomFournisseur(int idFournisseur) {
        String query = "SELECT Nom, Prenom FROM Fournisseur WHERE IdFournisseur = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idFournisseur);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("Nom");
                    String prenom = rs.getString("Prenom");


                    String nomAffichage = (nom != null) ? nom : "";
                    String prenomAffichage = (prenom != null) ? prenom : "";


                    return (nomAffichage + " " + prenomAffichage).trim();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du fournisseur: " + e.getMessage());
        }
        return "Inconnu";
    }


    public List<Commande> getAllCommandes() {
        List<Commande> liste = new ArrayList<>();
        String query = "SELECT * FROM Commande ORDER BY DateCommande DESC";
        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Commande c = new Commande(
                        rs.getInt("IdCommande"),
                        rs.getInt("IdFournisseur"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateCommande"),
                        rs.getDate("DateArrivee")
                );
                c.setPrixTotal(rs.getFloat("PrixTotal"));
                c.setEtat(rs.getString("Etat"));
                liste.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // bouton recieve
    public void updateEtat(int idCommande, String nouvelEtat) {
        String query = "UPDATE Commande SET Etat = ? WHERE IdCommande = ?";
        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nouvelEtat);
            ps.setInt(2, idCommande);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getNomEmploye(int idEmploye) {
        String query = "SELECT Nom, Prenom FROM Employe WHERE IdEmploye = ?";
        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idEmploye);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Nom") + " " + rs.getString("Prenom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ID: " + idEmploye;
    }


    public int getLastId(){
        String query = "SELECT IdCommande FROM Commande ORDER BY IdCommande DESC";
        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                return rs.getInt("IdCommande");
            }
            }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int nbrPendingOrders() {
        int count = 0;

        String sql = "SELECT COUNT(*) AS total FROM commande WHERE Etat != 'RECUE'";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des commandes en attente : " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }
}