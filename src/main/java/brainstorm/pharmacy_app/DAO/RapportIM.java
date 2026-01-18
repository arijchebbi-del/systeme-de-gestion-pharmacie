package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.*;

public class RapportIM {

    public float calculerCA(Date debut, Date fin) {
        float tot = 0;

        String sql = "SELECT SUM(PrixTotal) FROM vente v ON " +
                "WHERE v.DateVente BETWEEN ? AND ?";
        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, debut);
            ps.setDate(2, fin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tot = rs.getFloat(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return tot;
    }
    //rapport etat stock
    public void rapportEtatStock() {
        String query = "SELECT s.NumLot, p.NomProduit, s.Quantite, s.SeuilMinimal, " +
                " s.DerniereMiseAJour " +
                "FROM Stock s "+
                "JOIN Produit p ON s.Reference = p.Reference ";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Rapport d'état des stocks");
            while (rs.next()) {
                String produit = rs.getString("NomProduit");
                int quantite = rs.getInt("Quantite");
                int seuil = rs.getInt("SeuilMinimal");
                java.sql.Timestamp maj = rs.getTimestamp("DerniereMiseAJour");

                String etat = (quantite >= seuil) ? "Supérieur ou égal au seuil" : "Inférieur au seuil";
                int decalage = (quantite >= seuil) ? quantite - seuil : seuil - quantite;

                System.out.println("Produit : " + produit);
                System.out.println("Quantité actuelle : " + quantite);
                System.out.println("Seuil minimal : " + seuil);
                System.out.println("État : " + etat);
                System.out.println("Décalage : " + decalage);
                System.out.println("Dernière mise à jour : " + maj);
                System.out.println("fin rapport");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la génération du rapport : " + e.getMessage());
        }
    }









    public void Historique() {
        String sql = "SELECT v.NumFacture, v.DateVente, p.NomProduit, c.Quantite " +
                "FROM vente v " +
                "JOIN constituer c ON v.NumFacture = c.NumFacture " +
                "JOIN produit p ON c.Référence = p.Référence " +
                "ORDER BY v.DateVente DESC";

        try (Connection con = DBConnection.getAdminConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("HISTORIQUE DÉTAILLÉ DES VENTES");
            System.out.printf("%-10s | %-12s | %-20s | %-10s%n", "Facture", "Date", "Produit", "Quantité");


            while (rs.next()) {
                System.out.printf("%-10d | %-12s | %-20s | %-10d%n",
                        rs.getInt("NumFacture"),
                        rs.getDate("DateVente"),
                        rs.getString("NomProduit"),
                        rs.getInt("Quantite"));
            }
        } catch (SQLException e) {
            System.err.println("erreur lors de la lecture de l'historique : " + e.getMessage());
        }


    }
    public void rapportPerformanceFournisseurs() {
        String sql = "SELECT f.IdFournisseur, f.Nom, " + "COUNT(c.IdCommande) AS nbCommandes, " + "SUM(c.PrixTotal) AS valeurTotale, " + "SUM(CASE WHEN c.DateArrivee <= c.DateCommande THEN 1 ELSE 0 END) AS commandesATemps, " + "SUM(CASE WHEN c.DateArrivee > c.DateCommande THEN 1 ELSE 0 END) AS commandesEnRetard, " + "AVG(DATEDIFF(c.DateArrivee, c.DateCommande)) AS delaiMoyen " + "FROM Fournisseur f " + "LEFT JOIN Commande c ON f.IdFournisseur = c.IdFournisseur " + "GROUP BY f.IdFournisseur, f.Nom";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Rapport de performance des fournisseurs \n");
            while (rs.next()) {
                int id = rs.getInt("IdFournisseur");
                String nom = rs.getString("Nom");
                int nbCommandes = rs.getInt("nbCommandes");
                double valeurTotale = rs.getDouble("valeurTotale");
                int commandesATemps = rs.getInt("commandesATemps");
                int commandesEnRetard = rs.getInt("commandesEnRetard");
                double delaiMoyen = rs.getDouble("delaiMoyen");
                System.out.println("Fournisseur : " + nom + " (ID : " + id + ")");
                System.out.println("Nombre de commandes : " + nbCommandes);
                System.out.println("Valeur totale des commandes : " + valeurTotale + " DT");
                System.out.println("Commandes à temps : " + commandesATemps);
                System.out.println("Commandes en retard : " + commandesEnRetard);
                System.out.println("Délai moyen de livraison : " + String.format("%.2f", delaiMoyen) + " jours");
                System.out.println("fin rapport");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la génération du rapport : " + e.getMessage());
        }
    }
}
