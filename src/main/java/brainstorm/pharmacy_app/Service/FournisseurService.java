package brainstorm.pharmacy_app.Service;

import brainstorm.pharmacy_app.DAO.FournisseurIM;
import brainstorm.pharmacy_app.DAO.FournisseurDAO;
import brainstorm.pharmacy_app.Model.Fournisseur;
import java.util.List;

public class FournisseurService {

    private FournisseurDAO fournisseurDAO = new FournisseurIM();

    public void ajouterFournisseur(Fournisseur f) {
        if (f.getNom() == null || f.getNom().isEmpty()) {
            System.out.println(" Le nom du fournisseur est obligatoire");
            return;
        }

        fournisseurDAO.creation_f(f);
    }

    public void modifierFournisseur(Fournisseur f) {
        if (f.getId_Fournisseur() > 0) {
            fournisseurDAO.modification_f(f);
        }
    }
    
    public void supprimerFournisseur(int id) {
        if (fournisseurDAO.aDesCommandes(id)) {
            System.out.println("Suppression impossible fournisseur a des commandes existantes");

            return;
        }
        fournisseurDAO.suppression_f(id);
        System.out.println("Fournisseur supprime");
    }
}