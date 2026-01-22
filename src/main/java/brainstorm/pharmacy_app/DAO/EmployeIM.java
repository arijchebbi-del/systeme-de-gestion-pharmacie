package brainstorm.pharmacy_app.DAO;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeIM implements EmployeDAO{
    public void creation_e(Employe e) {

        String query = "INSERT INTO Employe(MotDePasse, Role, HoraireDeTravail, NumTel, Prenom, Nom, Email,Username,Salaire) VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, e.getMotdePasse());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getHoraire());
            ps.setInt(4, e.getNumTelephoneEmploye());
            ps.setString(5, e.getPrenom());
            ps.setString(6, e.getNom());
            ps.setString(7, e.getEmail());
            ps.setString(8,e.getUsername());
            ps.setFloat(9,e.getSalaire());

            ps.executeUpdate();
            System.out.println("Employé ajouté");

        } catch (SQLException ex) {
            System.err.println("Erreur SQL: " + ex.getMessage());
        }
    }
    public void modification_e(Employe e) {
        String query = "UPDATE Employe SET MotDePasse = ?,Role = ?,HoraireDeTravail = ?,NumTel = ?,Prenom = ?,Nom = ?,Email = ?,Salaire = ? WHERE IdEmploye = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, e.getMotdePasse());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getHoraire());
            ps.setInt(4, e.getNumTelephoneEmploye());
            ps.setString(5, e.getPrenom());
            ps.setString(6, e.getNom());
            ps.setString(7, e.getEmail());
            ps.setFloat(8,e.getSalaire());
            ps.setInt(9, e.getIdEmploye());

            int rows = ps.executeUpdate();

            System.out.println("Employé modifié avec succès");

        } catch (SQLException ex) {
            System.err.println("Erreur SQL: " + ex.getMessage());
        }
    }

    public void suppression_e(int idEmploye){
        String sql = "DELETE FROM Employe WHERE IdEmploye = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmploye);
            ps.executeUpdate();
            System.out.println("Employe supprimé");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Employe ChercherParId(int idEmploye) {

        String sql = "SELECT * FROM Employe WHERE IdEmploye = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmploye);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employe e = new Employe();
                e.setIdemploye(rs.getInt("IdEmploye"));
                e.setNom(rs.getString("Nom"));
                e.setPrenom(rs.getString("Prenom"));
                e.setEmail(rs.getString("Email"));
                e.setRole(rs.getString("Role"));
                e.setHoraire(rs.getString("HoraireDeTravail"));
                e.setNumTelephone(rs.getInt("NumTel"));
                e.setMotdepasse(rs.getString("MotDePasse"));
                e.setSalaire(rs.getFloat("Salaire"));
                return e;
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }

        return null; // only if not found
    }
    public Employe ChercherParHoraire(int horaire) {
        String sql = "SELECT * FROM Employe WHERE Horaire = ?";
        return null;
    }
    public void changerMotDePasse(int idEmploye, String nouveauMotDePasse) {

        String sql = "UPDATE Employe SET MotDePasse = ? WHERE IdEmploye = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nouveauMotDePasse);
            ps.setInt(2, idEmploye);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Mot de passe mis à jour");
            } else {
                System.out.println("Aucun employé trouvé avec cet ID");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }

    // Ajoutez cette méthode à la fin de votre classe EmployeIM
    public Employe authentifier(String username, String motDePasse) {
        // La requête cherche un employé par son nom ET son mot de passe
        String sql = "SELECT * FROM Employe WHERE Username = ? AND MotDePasse = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, motDePasse);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si trouvé, on crée l'objet Employe pour le retourner au LoginController
                    Employe e = new Employe();
                    e.setIdEmploye(rs.getInt("IdEmploye"));
                    e.setNom(rs.getString("Nom"));
                    e.setPrenom(rs.getString("Prenom"));
                    e.setRole(rs.getString("Role"));
                    return e;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur Authentification : " + ex.getMessage());
        }
        return null; // Retourne null si aucun utilisateur n'est trouvé
    }

    public List<Employe> getAll() {

        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM Employe";

        try (Connection conn = DBConnection.getAdminConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employe e = new Employe();
                e.setIdemploye(rs.getInt("IdEmploye"));
                e.setNom(rs.getString("Nom"));
                e.setPrenom(rs.getString("Prenom"));
                e.setEmail(rs.getString("Email"));
                e.setRole(rs.getString("Role"));
                e.setHoraire(rs.getString("HoraireDeTravail"));
                e.setNumTelephone(rs.getInt("NumTel"));
                e.setMotdepasse(rs.getString("MotDePasse"));
                e.setSalaire(rs.getFloat("Salaire"));

                employes.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employes;
    }
}
