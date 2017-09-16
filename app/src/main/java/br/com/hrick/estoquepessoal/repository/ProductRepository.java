package br.com.hrick.estoquepessoal.repository;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import br.com.hrick.estoquepessoal.entity.Product;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.helpers.DatabaseHelper;

/**
 * Created by henrique.pereira on 12/09/2017.
 */

public class ProductRepository {

    private static ProductRepository instance;
    private DatabaseHelper helper;

    public static void init(Context ctx) {
        if (null == instance) {
            instance = new ProductRepository(ctx);
        }
    }

    public static ProductRepository getInstance() {
        return instance;
    }

    private ProductRepository(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public void createProduct(Product product) throws SQLException {
        getHelper().getProductDao().createOrUpdate(product);
    }

    public List<Product> getProducts(Stock stock) throws SQLException {
        return getHelper().getProductDao().queryBuilder().where().eq(Product.STOCK,stock).query();
    }

    public Product getProduct(Integer id) throws SQLException {
        return getHelper().getProductDao().queryForId(id);
    }
    public void deleteProduct(Product product) throws SQLException {
        getHelper().getProductDao().delete(product);
    }
}
