package brainstorm.pharmacy_app.DAO;
import brainstorm.pharmacy_app.Model.Employe;
import brainstorm.pharmacy_app.Model.Produit;
public interface ProduitDAO {
    public void creation_p(Produit p );
    public void modification_p(Produit p);
    public void suppression_p();
    public boolean existe(int reference);
    public String getProduitByRef(int reference);
}
