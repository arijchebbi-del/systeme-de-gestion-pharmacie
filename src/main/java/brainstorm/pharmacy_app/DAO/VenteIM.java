package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VenteIM implements VenteDAO {


    public void creation_v(Vente v) {
        String query = "INSERT INTO vente (DateVente, IdEmploye, PresenceOrd, PrixTotal) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDate(1, v.getDateVente());
            ps.setInt(2, v.getIdEmploye());
            ps.setBoolean(3, v.isPresenceOrd());
            ps.setDouble(4, v.getPrixTotal());


            ps.executeUpdate();
            System.out.println("En-tete de vente ajoute avec succes ");

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la creation de la vente : " + e.getMessage());
        }
    }
}