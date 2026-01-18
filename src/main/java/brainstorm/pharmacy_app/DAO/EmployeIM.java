package brainstorm.pharmacy_app.DAO;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeIM implements EmployeDAO{
    public void creation_e(Employe e) {

        String query = "INSERT INTO Employe(MotDePasse, Role, HoraireDeTravail, NumTel, Prenom, Nom, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, e.getMotdePasse());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getHoraire());
            ps.setInt(4, e.getNumTelephoneEmploye());
            ps.setString(5, e.getPrenom());
            ps.setString(6, e.getNom());
            ps.setString(7, e.getEmail());

            ps.executeUpdate();
            System.out.println("Employé ajouté");

        } catch (SQLException ex) {
            System.err.println("Erreur SQL: " + ex.getMessage());
        }
    }
    public void modification_e(Employe e) {
        String query = "UPDATE Employe SET MotDePasse = ?,Role = ?,HoraireDeTravail = ?,NumTel = ?,Prenom = ?,Nom = ?,Email = ? WHERE IdEmploye = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, e.getMotdePasse());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getHoraire());
            ps.setInt(4, e.getNumTelephoneEmploye());
            ps.setString(5, e.getPrenom());
            ps.setString(6, e.getNom());
            ps.setString(7, e.getEmail());
            ps.setInt(8, e.getIdEmploye());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employé modifié avec succès");
            } else {
                System.out.println("Aucun employé trouvé avec cet ID");
            }

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
        return null;
    }
    public Employe ChercherParHoraire(int horaire) {
        String sql = "SELECT * FROM Employe WHERE Horaire = ?";
        return null;
    }
    public void updateMotDePasse(int idEmploye, String nouveauMotDePasse) {

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

}
