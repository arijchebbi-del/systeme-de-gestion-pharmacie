package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommandeIM implements CommandeDAO{
    public void creation_c(Commande c){
        String query = "INSERT INTO Commande(PrixTotal,DateCommande,DateArrivee,IdEmploye,IdFournisseur,Etat) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setDouble(1,c.getPrixTotal());
            ps.setDate(2,c.getDateCommande());
            ps.setDate(3,c.getDateArrivee());
            ps.setInt(4,c.getIdEmploye());
            ps.setInt(5,c.getIdFournisseur());
            ps.setString(6,c.getEtat());
            ps.executeUpdate();
            System.out.println("Commande bien ajoutée");
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }
    public void modification_c(Commande c){
        String query = "UPDATE Commande SET PrixTotal = ?,DateCommande = ?,DateArrivee = ?,IdEmploye = ?,IdFournisseur = ?,Etat = ? WHERE IdCommande = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setFloat(1,c.getPrixTotal());
            ps.setDate(2,c.getDateCommande());
            ps.setDate(3,c.getDateArrivee());
            ps.setInt(4,c.getIdEmploye());
            ps.setInt(5,c.getIdFournisseur());
            ps.setString(6,c.getEtat());



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




    public void annulation_c(int idCommande){

        String sql = "DELETE FROM Commande WHERE IdCommande = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCommande);
            ps.executeUpdate();
            System.out.println("Commande annulée");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
