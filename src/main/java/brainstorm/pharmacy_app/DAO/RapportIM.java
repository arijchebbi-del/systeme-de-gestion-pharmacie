package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Model.StockProduit;
import brainstorm.pharmacy_app.Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapportIM {

    public void rapportChiffreAffaires(Date debut, Date fin) {

        String sqlResume = "SELECT SUM(PrixTotal) AS total, COUNT(*) AS nbVentes, AVG(PrixTotal) AS panier " + "FROM Vente WHERE DateVente BETWEEN ? AND ?";

        String sqlOrdonnance = "SELECT p.Ordonnance, SUM(c.Quantite * p.PrixVente) AS ca " + "FROM Constituer c " + "JOIN Produit p ON c.Reference = p.Reference " + "JOIN Vente v ON c.NumFacture = v.NumFacture " + "WHERE v.DateVente BETWEEN ? AND ? " + "GROUP BY p.Ordonnance";

        String sqlTopEmployes = "SELECT e.Nom, e.Prenom, SUM(v.PrixTotal) AS ca " +
                "FROM Vente v " +
                "JOIN Employe e ON v.IdEmploye = e.IdEmploye " +
                "WHERE v.DateVente BETWEEN ? AND ? " +
                "GROUP BY e.IdEmploye " +
                "ORDER BY ca DESC " + "LIMIT 5";

        String sqlTopJours = "SELECT DateVente, COUNT(*) AS nbVentes, SUM(PrixTotal) AS ca " +
                "FROM Vente " +
                "WHERE DateVente BETWEEN ? AND ? " +
                "GROUP BY DateVente " +
                "ORDER BY nbVentes DESC " +
                "LIMIT 5";

        String sqlTopProduits = "SELECT p.NomProduit, SUM(c.Quantite) AS qte, " +
                "SUM(c.Quantite * p.PrixVente) AS ca " +
                "FROM Constituer c " +
                "JOIN Produit p ON c.Reference = p.Reference " +
                "JOIN Vente v ON c.NumFacture = v.NumFacture " +
                "WHERE v.DateVente BETWEEN ? AND ? " +
                "GROUP BY p.Reference, p.NomProduit " +
                "ORDER BY qte DESC " +
                "LIMIT 5";

        try (Connection con = DBConnection.getAdminConnection()) {

            System.out.println("***");
            System.out.println("   RAPPORT DE CHIFFRE D’AFFAIRES - PHARMACIE");
            System.out.println("   Période : " + debut + "  -->  " + fin);
            System.out.println("***\n");
            //general ca total et nb facture  panier moyen
            try (PreparedStatement ps = con.prepareStatement(sqlResume)) {
                ps.setDate(1, debut);
                ps.setDate(2, fin);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("RÉSUMÉ GÉNÉRAL");
                        System.out.println("Chiffre d'affaires total : " + rs.getFloat("total") + " DT");
                        System.out.println("Nombre de factures       : " + rs.getInt("nbVentes"));
                        System.out.println("Panier moyen             : " + rs.getFloat("panier") + " DT\n");
                    }
                }
            }
            // division avec ordonnance et sans ordonnance
            System.out.println("AVEC SANS ORDONNANCE");
            try (PreparedStatement ps = con.prepareStatement(sqlOrdonnance)) {
                ps.setDate(1, debut);
                ps.setDate(2, fin);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        boolean ord = rs.getBoolean("Ordonnance");
                        float ca = rs.getFloat("ca");

                        if (ord)
                            System.out.println(" - Avec ordonnance : " + ca + " DT");
                        else
                            System.out.println(" - Sans ordonnance : " + ca + " DT");
                    }
                }
            }

            //akther 5 employettt ydakhloulou flouss
            System.out.println("\n TOP 5 EMPLOYÉS VENDEURS");
            try (PreparedStatement ps = con.prepareStatement(sqlTopEmployes)) {
                ps.setDate(1, debut);
                ps.setDate(2, fin);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(" - " + rs.getString("Nom") + " " +
                                rs.getString("Prenom") + " : " +
                                rs.getFloat("ca") + " DT");
                    }
                }
            }

            //  akther ayamet ybi3 fehom
            System.out.println("\nJOURS AVEC LE PLUS DE VENTES");
            try (PreparedStatement ps = con.prepareStatement(sqlTopJours)) {
                ps.setDate(1, debut);
                ps.setDate(2, fin);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(" - " + rs.getDate("DateVente") +
                                " | Nb ventes : " + rs.getInt("nbVentes") +
                                " | CA : " + rs.getFloat("ca") + " DT");
                    }
                }
            }

            // Top 5produits vendus
            System.out.println("\nTOP 5 PRODUITS LES PLUS VENDUS");
            try (PreparedStatement ps = con.prepareStatement(sqlTopProduits)) {
                ps.setDate(1, debut);
                ps.setDate(2, fin);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(" - " + rs.getString("NomProduit") +
                                " | Quantité : " + rs.getInt("qte") +
                                " | CA : " + rs.getFloat("ca") + " DT");
                    }
                }
            }

            System.out.println("\n***");
            System.out.println("        FIN DU RAPPORT");
            System.out.println("***\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Inside RapportIM
    public List<Object[]> rapportEtatStockList() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT s.NumLot, p.NomProduit, s.Quantite, s.SeuilMinimal, s.DerniereMiseAJour " +
                "FROM Stock s JOIN Produit p ON s.Reference = p.Reference";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String produit = rs.getString("NomProduit");
                int quantite = rs.getInt("Quantite");
                int seuil = rs.getInt("SeuilMinimal");
                Timestamp maj = rs.getTimestamp("DerniereMiseAJour");

                String etat = (quantite >= seuil) ? "OK" : "LOW";
                int decalage = Math.abs(quantite - seuil);

                Object[] row = new Object[]{rs.getInt("NumLot"), produit, quantite, seuil, etat, decalage, maj};
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //rapport etat stock
    public List<StockProduit> rapportEtatStock() {return new StockProduitIM().getAll();}
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
    public float getTotalRevenue(Date debut, Date fin) {

        String sql = "SELECT SUM(PrixTotal) AS total FROM Vente WHERE DateVente BETWEEN ? AND ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public int getNumberOfSales(Date debut, Date fin) {

        String sql = "SELECT COUNT(*) AS nbVentes FROM Vente WHERE DateVente BETWEEN ? AND ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("nbVentes");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public float getAverageBasket(Date debut, Date fin) {

        String sql = "SELECT AVG(PrixTotal) AS panier FROM Vente WHERE DateVente BETWEEN ? AND ?";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("panier");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public List<String> getTopEmployees(Date debut, Date fin) {

        List<String> list = new ArrayList<>();

        String sql = "SELECT e.Nom, e.Prenom, SUM(v.PrixTotal) AS ca " +
                "FROM Vente v JOIN Employe e ON v.IdEmploye = e.IdEmploye " +
                "WHERE v.DateVente BETWEEN ? AND ? " +
                "GROUP BY e.IdEmploye ORDER BY ca DESC LIMIT 5";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String line = rs.getString("Nom") + " " +
                        rs.getString("Prenom") + " : " +
                        rs.getFloat("ca") + " DT";
                list.add(line);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<String> getTopProducts(Date debut, Date fin) {

        List<String> list = new ArrayList<>();

        String sql = "SELECT p.NomProduit, SUM(c.Quantite) AS qte, " +
                "SUM(c.Quantite * p.PrixVente) AS ca " +
                "FROM Constituer c JOIN Produit p ON c.Reference = p.Reference " +
                "JOIN Vente v ON c.NumFacture = v.NumFacture " +
                "WHERE v.DateVente BETWEEN ? AND ? " +
                "GROUP BY p.Reference, p.NomProduit ORDER BY qte DESC LIMIT 5";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String line = rs.getString("NomProduit") +
                        " | Qte: " + rs.getInt("qte") +
                        " | CA: " + rs.getFloat("ca") + " DT";
                list.add(line);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<String> getSupplierPerformance() {

        List<String> list = new ArrayList<>();

        String sql = "SELECT f.Nom, " +
                "COUNT(c.IdCommande) AS nbCommandes, " +
                "SUM(CASE WHEN c.DateArrivee <= c.DateCommande THEN 1 ELSE 0 END) AS commandesATemps, " +
                "SUM(CASE WHEN c.DateArrivee > c.DateCommande THEN 1 ELSE 0 END) AS commandesEnRetard, " +
                "COALESCE(SUM(c.PrixTotal), 0) AS valeurTotale, " +
                "COALESCE(AVG(DATEDIFF(c.DateArrivee, c.DateCommande)), 0) AS delaiMoyen " +
                "FROM Fournisseur f " +
                "LEFT JOIN Commande c ON f.IdFournisseur = c.IdFournisseur " +
                "GROUP BY f.IdFournisseur, f.Nom";

        try (Connection con = DBConnection.getAdminConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("Nom");
                int nbCmd = rs.getInt("nbCommandes");
                int onTime = rs.getInt("commandesATemps");
                int late = rs.getInt("commandesEnRetard");
                double totalValue = rs.getDouble("valeurTotale");
                double avgDelay = rs.getDouble("delaiMoyen");
                double pctOnTime = nbCmd > 0 ? (onTime * 100.0 / nbCmd) : 0;

                String line = nom +
                        " | Cmd: " + nbCmd +
                        " | On time: " + onTime +
                        " | Late: " + late +
                        " | Total Value: " + String.format("%.2f", totalValue) + " DT" +
                        " | Avg Delay: " + String.format("%.2f", avgDelay) + " days" +
                        " | % On Time: " + String.format("%.2f", pctOnTime) + "%";

                list.add(line);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public int getTotalSupplierOrders(Date debut, Date fin) {

        int total = 0;

        String sql = "SELECT COUNT(*) FROM Commande " +
                "WHERE DateCommande BETWEEN ? AND ?";

        try (
                Connection cnx = DBConnection.getEmployeeConnection();
                PreparedStatement ps = cnx.prepareStatement(sql);
        ) {
            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    public int getSuppliersOnTime(Date debut, Date fin) {

        int total = 0;

        String sql = "SELECT COUNT(*) FROM Commande " +
                "WHERE Etat = 'recue' " +  // "recue" = on time
                "AND DateCommande BETWEEN ? AND ?";

        try (
                Connection cnx = DBConnection.getEmployeeConnection();
                PreparedStatement ps = cnx.prepareStatement(sql);
        ) {
            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    public int getSuppliersLate(Date debut, Date fin) {

        int total = 0;

        String sql = "SELECT COUNT(*) FROM Commande " +
                "WHERE Etat = 'annulee' " +  // "annulee" = late
                "AND DateCommande BETWEEN ? AND ?";

        try (
                Connection cnx = DBConnection.getEmployeeConnection();
                PreparedStatement ps = cnx.prepareStatement(sql);
        ) {
            ps.setDate(1, debut);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }





}
