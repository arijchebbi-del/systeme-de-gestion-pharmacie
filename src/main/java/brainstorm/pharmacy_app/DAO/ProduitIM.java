package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitIM {
    public void creation_p(Produit p){
        String query = "INSERT INTO Produit (NomProduit, Categorie, Type, ModeUtilisation, Ordonnance, PrixAchat, PrixVente) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,p.getNomProduit());
            ps.setString(2,p.getCategorie());
            ps.setString(3,p.getType());
            ps.setString(4,p.getModeUtilisation());
            ps.setBoolean(5,p.getOrdonnance());
            ps.setFloat(6,p.getPrixAchat());
            ps.setFloat(7,p.getPrixVente());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setReference(rs.getInt(1)); // important! now p.getReference() matches the DB
            }
            System.out.println("Commande bien ajoutée");

        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }

    public void modification_p(Produit p) {
        String query = "UPDATE Produit SET NomProduit=?, Categorie=?, Type=?, ModeUtilisation=?, Ordonnance=?, PrixAchat=?, PrixVente=? " +
                "WHERE Reference=?";;

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1,p.getNomProduit());
            ps.setString(2,p.getCategorie());
            ps.setString(3,p.getType());
            ps.setString(4,p.getModeUtilisation());
            ps.setBoolean(5,p.getOrdonnance());
            ps.setFloat(6,p.getPrixAchat());
            ps.setFloat(7,p.getPrixVente());
            ps.setInt(8, p.getReference());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Produit modifié avec succès");
            } else {
                System.out.println("Aucun produit trouvé avec cette reference");
            }

        } catch (SQLException ex) {
            System.err.println("Erreur SQL: " + ex.getMessage());
        }
    }


    public void suppression_p(int reference){
        String sql = "DELETE FROM Produit WHERE Reference = ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reference);
            ps.executeUpdate();
            System.out.println("Produit supprimé");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // verifier s'il existe un produit
    public boolean existe(int reference) {
        String query = "SELECT COUNT(*) FROM Produit WHERE Reference = ?";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, reference);
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

    public Produit lire_p(int reference) {
        String sql = "SELECT * FROM Produit WHERE Reference = ?";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reference);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Produit p = new Produit();
                    p.setReference(rs.getInt("Reference"));
                    p.setNomProduit(rs.getString("NomProduit"));
                    p.setCategorie(rs.getString("Categorie"));
                    p.setType(rs.getString("Type"));
                    p.setModeUtilisation(rs.getString("ModeUtilisation"));
                    p.setOrdonnance(rs.getBoolean("Ordonnance")); // Mapping boolean
                    p.setPrixAchat(rs.getFloat("PrixAchat"));     // Mapping float
                    p.setPrixVente(rs.getFloat("PrixVente"));     // Mapping float
                    return p;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public List<Produit> selectAll() {

        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM Produit";

        try (Connection conn = DBConnection.getEmployeeConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Produit p = new Produit();
                p.setReference(rs.getInt("Reference"));
                p.setNomProduit(rs.getString("NomProduit"));
                p.setCategorie(rs.getString("Categorie"));
                p.setType(rs.getString("Type"));
                p.setModeUtilisation(rs.getString("ModeUtilisation"));
                p.setOrdonnance(rs.getBoolean("Ordonnance")); // Mapping boolean
                p.setPrixAchat(rs.getFloat("PrixAchat"));     // Mapping float
                p.setPrixVente(rs.getFloat("PrixVente"));
                produits.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }
    public String getNomProduitByRef(int reference) {
        String nom = "Unknown Product"; // default
        String sql = "SELECT Nom FROM Produit WHERE Reference = ?";

        try (Connection conn = DBConnection.getEmployeeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reference);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nom = rs.getString("Nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nom;
    }


}
