package br.com.hrick.estoquepessoal.repository;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.helpers.DatabaseHelper;

/**
 * Created by henrique.pereira on 12/09/2017.
 */

public class UserRepository {

    private static UserRepository instance;
    private DatabaseHelper helper;

    public static void init(Context ctx) {
        if (null == instance) {
            instance = new UserRepository(ctx);
        }
    }

    public static UserRepository getInstance() {
        return instance;
    }

    private UserRepository(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public void createUser(User user) throws SQLException {
        getHelper().getUserDao().createOrUpdate(user);
    }

    public User getUser(String userName, String password) throws SQLException {
        return getHelper().getUserDao().queryBuilder()
                .where().eq(User.USER,userName)
                .and().eq(User.PASSWORD, password).queryForFirst();
    }

    public User getUserId(String idUser) throws SQLException {
        return getHelper().getUserDao().queryForId(idUser);
    }
}
