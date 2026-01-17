package brainstorm.pharmacy_app.DAO;
import brainstorm.pharmacy_app.Model.Stock;
public interface StockDAO {
    public void creation_s(Stock s);
    public void modification_s(Stock s);
    public void suppression_s(int numLot);
}
