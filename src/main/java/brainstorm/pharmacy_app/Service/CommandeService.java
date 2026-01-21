package brainstorm.pharmacy_app.Service;

import brainstorm.pharmacy_app.DAO.CommandeIM;
import brainstorm.pharmacy_app.DAO.StockIM;
import brainstorm.pharmacy_app.DAO.GererIM;
import brainstorm.pharmacy_app.DAO.ProduitIM;
import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Model.Produit;
import brainstorm.pharmacy_app.Model.Stock;
import brainstorm.pharmacy_app.Exceptions.QuantiteNegativeException;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class CommandeService {

    private CommandeIM commandeDAO = new CommandeIM();
    private StockIM stockDAO = new StockIM();
    private ProduitIM produitDAO = new ProduitIM();
    private GererIM gererDAO = new GererIM();

    /*public void passerCommande(Commande c) throws QuantiteNegativeException {
        if (c.getQuantiteComposer() <= 0) {
            throw new QuantiteNegativeException("La quantité commandée doit être positive");
        }

        c.setEtat("passer");
        commandeDAO.creation_c(c);
        System.out.println("Commande enregistrée avec l'état : " + c.getEtat());
    }*/

    public void annulerCommande(int idCommande) {
        if (idCommande > 0) {
            commandeDAO.annulation_c(idCommande);
            System.out.println(" Commande " + idCommande + " annulée.");
        }
    }

    /*public void receptionnerEtMettreAJourStock(Commande c, Produit p, Stock s, int idEmployeConnecte) throws QuantiteNegativeException {
        try {
            c.setEtat("Reçue");
            commandeDAO.modification_c(c);
            int quantiteRecue = c.getComposition().getQuantiteComposer();
            if (quantiteRecue < 0) {
                throw new QuantiteNegativeException("La quantite reçue ne peut pas etre negative");
            }
            if (produitDAO.existe(p.getReference())) {
                int stockActuel = stockDAO.getQuantiteByProduit(p.getReference());
                int nouvelleQuantite = stockActuel + quantiteRecue;
                s.setQuantite(nouvelleQuantite);
                s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis()));
                stockDAO.modification_s(s);
                gererDAO.enregistrerAction(idEmployeConnecte, s.getNumLot());
                System.out.println("Stock mis a jour pour : " + p.getNomProduit());
                if (nouvelleQuantite <= s.getSeuilMinimal()) {
                    System.out.println("Le produit " + p.getNomProduit() + " still sous le seuil minimal (" + s.getSeuilMinimal() + "). Stock actuel : " + nouvelleQuantite);
                }
            }
            else {
                System.out.println("Nouveau produit ");
                produitDAO.creation_p(p);

                s.setQuantite(quantiteRecue);
                s.setDerniereMiseAJour(new Timestamp(System.currentTimeMillis()));
                stockDAO.creation_s(s);

                gererDAO.enregistrerAction(idEmployeConnecte, s.getNumLot());

                if (quantiteRecue <= s.getSeuilMinimal()) {
                    System.out.println("Nouveau produit " + p.getNomProduit() + " ajouté avec une quantite inferieure au seuil minimal (" + s.getSeuilMinimal() + ")");
                }
            }

            System.out.println("Réception terminée");

        } catch (QuantiteNegativeException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erreur critique lors de la réception de commande : " + e.getMessage());
        }
    }*/

    // ===== NEW METHODS FOR ORDER CONTROL =====

    /**
     * Get all commandes from database
     * @return List of all commandes
     */
    public List<Commande> getAllCommandes() {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM Commande ORDER BY DateCommande DESC";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("IdCommande"),
                        rs.getInt("IdFournisseur"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateCommande"),
                        rs.getDate("DateArrivee")
                );

                commande.setPrixTotal(rs.getFloat("PrixTotal"));
                commande.setEtat(rs.getString("Etat"));

                commandes.add(commande);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des commandes: " + e.getMessage());
        }

        return commandes;
    }

    /**
     * Get all commandes with supplier information
     * @return List of commandes with supplier details
     */
    public List<Commande> getAllCommandesWithSupplier() {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT c.*, f.Nom as FournisseurNom, f.NumTel as FournisseurTel " +
                "FROM Commande c " +
                "LEFT JOIN Fournisseur f ON c.IdFournisseur = f.IdFournisseur " +
                "ORDER BY c.DateCommande DESC";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("IdCommande"),
                        rs.getInt("IdFournisseur"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateCommande"),
                        rs.getDate("DateArrivee")
                );

                commande.setPrixTotal(rs.getFloat("PrixTotal"));
                commande.setEtat(rs.getString("Etat"));

                // You might want to store supplier info in Commande or a DTO
                // For now, we'll just retrieve it

                commandes.add(commande);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des commandes avec fournisseur: " + e.getMessage());
        }

        return commandes;
    }

    /**
     * Update a commande
     * @param commande The commande to update
     */
    public void updateCommande(Commande commande) {
        if (commande.getIdCommande() <= 0) {
            System.err.println("ID de commande invalide pour la mise à jour");
            return;
        }

        commandeDAO.modification_c(commande);
    }

    /**
     * Update only the status of a commande
     * @param idCommande The commande ID
     * @param newStatus The new status
     */
    public void updateCommandeStatus(int idCommande, String newStatus) {
        String query = "UPDATE Commande SET Etat = ? WHERE IdCommande = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, newStatus);
            ps.setInt(2, idCommande);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Statut de la commande " + idCommande + " mis à jour à : " + newStatus);
            } else {
                System.out.println("Aucune commande trouvée avec l'ID : " + idCommande);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut de la commande: " + e.getMessage());
        }
    }

    /**
     * Get commande by ID
     * @param idCommande The commande ID
     * @return The commande or null if not found
     */
    public Commande getCommandeById(int idCommande) {
        String query = "SELECT * FROM Commande WHERE IdCommande = ?";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idCommande);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("IdCommande"),
                        rs.getInt("IdFournisseur"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateCommande"),
                        rs.getDate("DateArrivee")
                );

                commande.setPrixTotal(rs.getFloat("PrixTotal"));
                commande.setEtat(rs.getString("Etat"));

                return commande;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la commande: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get pending orders count (orders with status "CREE" or "passer")
     * @return Count of pending orders
     */
    public int getPendingOrdersCount() {
        String query = "SELECT COUNT(*) as count FROM Commande WHERE Etat IN ('CREE', 'passer')";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des commandes en attente: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get orders by status
     * @param status The status to filter by
     * @return List of commandes with the specified status
     */
    public List<Commande> getCommandesByStatus(String status) {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM Commande WHERE Etat = ? ORDER BY DateCommande DESC";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("IdCommande"),
                        rs.getInt("IdFournisseur"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateCommande"),
                        rs.getDate("DateArrivee")
                );

                commande.setPrixTotal(rs.getFloat("PrixTotal"));
                commande.setEtat(rs.getString("Etat"));

                commandes.add(commande);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du filtrage des commandes par statut: " + e.getMessage());
        }

        return commandes;
    }

    /**
     * Search commandes by keyword (in supplier name, phone, or order ID)
     * @param keyword The search keyword
     * @return List of matching commandes
     */
    public List<Commande> searchCommandes(String keyword) {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT c.* FROM Commande c " +
                "LEFT JOIN Fournisseur f ON c.IdFournisseur = f.IdFournisseur " +
                "WHERE f.Nom LIKE ? OR f.NumTel LIKE ? OR CAST(c.IdCommande AS CHAR) LIKE ? " +
                "ORDER BY c.DateCommande DESC";

        try (Connection con = DBConnection.getEmployeeConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("IdCommande"),
                        rs.getInt("IdFournisseur"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateCommande"),
                        rs.getDate("DateArrivee")
                );

                commande.setPrixTotal(rs.getFloat("PrixTotal"));
                commande.setEtat(rs.getString("Etat"));

                commandes.add(commande);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des commandes: " + e.getMessage());
        }

        return commandes;
    }
    public void createCommande(Commande commande) {
        commandeDAO.creation_c(commande);
    }
}