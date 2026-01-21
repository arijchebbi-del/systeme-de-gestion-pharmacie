package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class FournisseurIM implements FournisseurDAO{

    public void creation_f(Fournisseur f){
        String query = "INSERT INTO Fournisseur (Nom, NumTel, Email, Adresse, TypeProduit)\n" +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)){
            //ps.setInt(1, f.getId_Fournisseur()); zeyda auto increment
            ps.setString(1, f.getNom());
            ps.setString(2, f.getNumTelephone());
            ps.setString(3, f.getEmail());
            ps.setString(4, f.getAdresse());
            ps.setString(5, f.getTypeProduits());
            ps.executeUpdate();
            System.out.println("Fournisseur ajouté");


         }catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }

    }
    public void modification_f(Fournisseur f){
        String sql ="UPDATE Fournisseur SET Nom =?,NumTel =?,Email=?,Adresse=?,TypeProduit=? WHERE IdFournisseur=?";
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

    public boolean aDesCommandes(int idFournisseur) {
        String sql = "SELECT COUNT(*) FROM Commande WHERE IdFournisseur = ?";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFournisseur);
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
    public List<String> selectDistinctCategories() {

        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT typeProduit FROM Fournisseur";

        try (Connection conn = DBConnection.getEmployeeConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("typeProduit"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
    public List<Fournisseur> selectAll() {

        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM Fournisseur";

        try (Connection conn = DBConnection.getEmployeeConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Fournisseur f = new Fournisseur();

                f.setId_Fournisseur(rs.getInt("IdFournisseur"));
                f.setNom(rs.getString("Nom"));
                f.setNumTelephone(rs.getString("NumTel"));
                f.setEmail(rs.getString("Email"));
                f.setAdresse(rs.getString("Adresse"));
                f.setTypeProduits(rs.getString("TypeProduit"));

                fournisseurs.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fournisseurs;
    }
    public String getNomFournisseur(int id) {
        String nom = null;

        String query = "SELECT NomFournisseur FROM Fournisseur WHERE IdFournisseur = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nom = rs.getString("NomFournisseur");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nom du fournisseur : " + e.getMessage());
            e.printStackTrace();
        }

        return nom;
    }


}
