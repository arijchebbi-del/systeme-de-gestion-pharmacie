package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.Fournisseur;
import brainstorm.pharmacy_app.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public interface FournisseurDAO {
    public void creation_f(Fournisseur f );
    public void modification_f(Fournisseur f);
    public void suppression_f(int idFournisseur);
    public boolean aDesCommandes(int id);
    List<String> selectDistinctCategories();
    List<Fournisseur> selectAll();
}

