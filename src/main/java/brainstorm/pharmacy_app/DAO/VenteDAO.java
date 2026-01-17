package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface VenteDAO {
    public void creation_v(Vente v);
}
public class VenteIM implements VenteDAO{
    public void creation_v(Vente v){
        String query = "INSERT INTO Vente VALUES(?,?,?,?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)){
            //ps.setString(1, v.getNumFacture()); auto-increment
            ps.setString(2, v.getDateVente());
            ps.setDouble(3, v.getPrixTotal());
            ps.setInt(4, v.getEmploye().getIdPersonne());
            ps.executeUpdate();
            System.out.println("Vente ajoutée");


        }catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }

    }




}