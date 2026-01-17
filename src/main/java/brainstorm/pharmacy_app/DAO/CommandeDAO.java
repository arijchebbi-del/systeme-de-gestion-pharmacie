package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Commande;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface CommandeDAO {
    public void creation_c(Commande c);
    public void modification_c(Commande c);
    public void annulation_c(Commande c);
    public void reception_c(Commande c);
}
