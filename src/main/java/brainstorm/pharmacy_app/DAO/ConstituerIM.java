package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Constituer;
import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConstituerIM implements ConstituerDAO {

    public void ajouterLigneVente(Constituer c) {
        // Ajout de la colonne PrixVente pour respecter la 3FN
        String query = "INSERT INTO constituer (NumFacture, Référence, QuantitéVendu, PrixVente) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {


            ps.setInt(1, c.getNumFacture());
            ps.setInt(2, c.getReference());
            ps.setInt(3, c.getQuantiteVendu());
            ps.setFloat(4, c.getPrixVente());

            ps.executeUpdate();
            System.out.println("Produit " + c.getReference() + " ajoute a la facture num" + c.getNumFacture());

        } catch (SQLException e) {
            System.err.println(" Erreur SQL dans ConstituerIM : " + e.getMessage());
        }
    }
}