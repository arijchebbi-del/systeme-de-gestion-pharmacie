package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GererIM implements GererDAO {

    @Override
    public void enregistrerAction(int idEmploye, int numLot) {
        String sql = "INSERT INTO gerer (IdEmploye, NumLot) VALUES (?, ?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmploye);
            ps.setInt(2, numLot);
            ps.executeUpdate();
            System.out.println("Action de gestion enregistrée pour l'employé " + idEmploye);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement dans la table 'gerer' : " + e.getMessage());
        }
    }


}
