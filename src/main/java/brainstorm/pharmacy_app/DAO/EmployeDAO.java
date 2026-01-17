package brainstorm.pharmacy_app.DAO;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface EmployeDAO {
    public void creation_e(Employe e);
    public void modification_e(Employe e);
    public void suppression_e(int idEmploye);
}
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
    public void modification_e(Employe e){
        
    }






}
