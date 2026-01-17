package brainstorm.pharmacy_app.DAO;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface EmployeDAO {
    public void creation_e(Employe e);
    public void modification_e(Employe e);
    public void suppression_e(int idEmploye);
}
