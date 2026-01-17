package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface FournisseurDAO {
    public void creation_f(Fournisseur f );
    public void modification_f(Fournisseur f);
    public void suppression_f(int idFournisseur);
}

public class FournisseurIM implements FournisseurDAO{

    public void creation_f(Fournisseur f){
        String query = "INSERT INTO Fournisseur VALUES(?,?,?,?,?,?)";

        try (Connection con = DBConnection.getAdminConnection();
        PreparedStatement ps = con.prepareStatement(query)){
            //ps.setInt(1, f.getId_Fournisseur()); zeyda auto increment
            ps.setString(2, f.getNom());
            ps.setString(3, f.getNumTelephone());
            ps.setString(4, f.getEmail());
            ps.setString(5, f.getAdresse());
            ps.setString(6, f.getTypeProduits());
            ps.executeUpdate();
            System.out.println("Fournisseur ajouté");


         }catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }

    }
    public void modification_f(Fournisseur f){
        String sql ="""
            UPDATE Fournisseur
            SET Nom =?,NumTel =?,Email=?,Adresse=?,TypeProduit=?
            WHERE IdFournisseur=?""";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, f.getNom());
            ps.setString(2, f.getNumTelephone());
            ps.setString(3, f.getEmail());
            ps.setString(4, f.getAdresse());
            ps.setString(5, f.getTypeProduits());
            ps.setInt(6, f.getId_Fournisseur());

            ps.executeUpdate();
            System.out.println("Fournisseur modifié");

        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }

        

    }
    public void suppression_f(int IdFournisseur) {
        String sql = "DELETE FROM Fournisseur WHERE IdFournisseur = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, IdFournisseur);
            ps.executeUpdate();
            System.out.println("Fournisseur supprimé");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
