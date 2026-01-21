package brainstorm.pharmacy_app.DAO;

import brainstorm.pharmacy_app.Model.StockProduit;
import java.util.List;

public interface StockProduitDAO {
    List<StockProduit> selectAll();

}
