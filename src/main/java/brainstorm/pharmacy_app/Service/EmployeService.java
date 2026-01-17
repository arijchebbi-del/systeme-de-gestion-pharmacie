package brainstorm.pharmacy_app.Service;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.DAO.EmployeIM;
import brainstorm.pharmacy_app.Exceptions.IdEmployeNegativeException;
import brainstorm.pharmacy_app.Exceptions.AucunNomException;

public class EmployeService {

    private EmployeIM empDAO = new EmployeIM(); // DAO utilisé par le Service

    // Ajouter un employé avec validation simple
    public void ajouterEmploye(Employe e) throws AucunNomException{
        if (e.getNom() == null || e.getNom().isEmpty()) {
            throw new AucunNomException("Le nom est obligatoire");
        }
        empDAO.creation_e(e); // Appelle le DAO
        System.out.println("Employé ajouté via le service !");
    }

    // Modifier un employé
    public void modifierEmploye(Employe e) throws IdEmployeNegativeException{
        if (e.getIdEmploye() <= 0) {
            throw new IdEmployeNegativeException("ID invalide pour la modification");
        }
        empDAO.modification_e(e); // Appelle le DAO
        System.out.println("Employé modifié via le service !");
    }

    // Supprimer un employé
    public void supprimerEmploye(int id) throws IdEmployeNegativeException{
        if (id <= 0) {
            throw new IdEmployeNegativeException("ID invalide pour la suppression");
        }
        empDAO.suppression_e(id); // Appelle le DAO
        System.out.println("Employé supprimé via le service !");
    }
}

