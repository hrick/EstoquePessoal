package br.com.hrick.estoquepessoal.repository;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.helpers.DatabaseHelper;

/**
 * Created by henrique.pereira on 12/09/2017.
 */

public class StockRepository {

    private static StockRepository instance;
    private DatabaseHelper helper;

    public static void init(Context ctx) {
        if (null == instance) {
            instance = new StockRepository(ctx);
        }
    }

    public static StockRepository getInstance() {
        return instance;
    }

    private StockRepository(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public void createStock(Stock stock) throws SQLException {
        getHelper().getStockDao().createOrUpdate(stock);
    }

    public List<Stock> getStocks() throws SQLException {
        return getHelper().getStockDao().queryBuilder().query();
    }
    public Stock getStock(Integer id) throws SQLException {
       return getHelper().getStockDao().queryForId(id);
    }
    public void deleteStock(Stock stock) throws SQLException {
        getHelper().getStockDao().delete(stock);
    }
}
