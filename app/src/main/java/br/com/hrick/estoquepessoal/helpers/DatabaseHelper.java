package br.com.hrick.estoquepessoal.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.Closeable;
import java.sql.SQLException;

import br.com.hrick.estoquepessoal.entity.Product;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.entity.User;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements Closeable {

    private static final String DATABASE_NAME = "estoque_pessoal.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    private Dao<User, String> userDao = null;
    private Dao<Product, Integer> productDao = null;
    private Dao<Stock, Integer> stockDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i("DatabaseHelper", "onCreate database");

            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Stock.class);
            TableUtils.createTable(connectionSource, Product.class);


        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, User.class, true);

            onCreate(db, connectionSource);
//            PreferenciasRepository preferenciasRepository = PreferenciasRepository_.getInstance_(context);
//            preferenciasRepository.logoff();
            //}
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<User, String> getUserDao() {
        if (null == userDao) {
            try {
                userDao = getDao(User.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }
    public Dao<Stock, Integer> getStockDao() {
        if (null == stockDao) {
            try {
                stockDao = getDao(Stock.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return stockDao;
    }
    public Dao<Product, Integer> getProductDao() {
        if (null == productDao) {
            try {
                productDao = getDao(Product.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return productDao;
    }


}
