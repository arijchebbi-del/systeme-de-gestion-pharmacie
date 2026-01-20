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
        String sql = "SELECT DISTINCT typeProduits FROM fournisseur";

        try (Connection conn = DBConnection.getEmployeeConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("typeProduits"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
    public List<Fournisseur> selectAll() {

        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur";

        try (Connection conn = DBConnection.getEmployeeConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Fournisseur f = new Fournisseur();

                f.setId_Fournisseur(rs.getInt("id_Fournisseur"));
                f.setNom(rs.getString("nom"));
                f.setNumTelephone(rs.getString("numTelephone"));
                f.setEmail(rs.getString("email"));
                f.setAdresse(rs.getString("adresse"));
                f.setTypeProduits(rs.getString("typeProduits"));

                fournisseurs.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fournisseurs;
    }


}
