package brainstorm.pharmacy_app.DAO;
import brainstorm.pharmacy_app.Model.Employe;

public interface EmployeDAO {
    public void creation_e(Employe e);
    public void modification_e(Employe e);
    public void suppression_e(int idEmploye);
}
