package brainstorm.pharmacy_app.Service;
import java.util.List;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.DAO.EmployeIM;
import brainstorm.pharmacy_app.Exceptions.IdEmployeNegativeException;
import brainstorm.pharmacy_app.Exceptions.AucunNomException;
import brainstorm.pharmacy_app.Exceptions.EmployeInexistantException;
import brainstorm.pharmacy_app.Exceptions.MotDePasseInvalideException;

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

    public Employe chercherEmploye(int id) throws IdEmployeNegativeException, EmployeInexistantException {
        if (id <= 0) {
            throw new IdEmployeNegativeException("ID invalide");
        }

        Employe e = empDAO.ChercherParId(id);
        if (e == null) {
            throw new EmployeInexistantException("Employé introuvable");
        }
        return e;
    }
    public void changerMotDePasse(int idEmploye, String nouveauMotDePasse) throws IdEmployeNegativeException ,MotDePasseInvalideException{

        if (idEmploye <= 0) {
            throw new IdEmployeNegativeException("ID invalide");
        }

        if (nouveauMotDePasse.length() < 6) {
            throw new MotDePasseInvalideException("Mot de passe trop court");
        }

        empDAO.updateMotDePasse(idEmploye, nouveauMotDePasse);
    }
}

