package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommandeIM implements CommandeDAO{
    public void creation_c(Commande c){
        String query = "INSERT INTO Commande(PrixTotal,DateCommande,DateArrivee,Quantite,IdEmploye,IdFournisseur,Etat) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setFloat(1,c.getPrixTotal());
            ps.setDate(2,c.getDateCommande());
            ps.setDate(3,c.getDateArrivee());
            ps.setInt(4,c.getComposition().getQuantiteComposer());
            ps.setInt(5,c.getEmploye().getIdEmploye());
            ps.setInt(6,c.getFournisseur().getId_Fournisseur());
            ps.setString(7,c.getEtat());
            ps.executeUpdate();
            System.out.println("Commande bien ajoutée");
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }
    public void modification_c(Commande c){}
    public void annulation_c(Commande c){}
    public void reception_c(Commande c){}
}
