package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Vente;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface VenteDAO {
    public void creation_v(Vente v);
}
