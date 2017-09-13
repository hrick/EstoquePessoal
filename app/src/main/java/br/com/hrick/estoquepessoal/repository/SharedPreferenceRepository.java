package br.com.hrick.estoquepessoal.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.User;

/**
 * Created by Meg on 13/09/2017.
 */

public class SharedPreferenceRepository {

    private Context context;

    public SharedPreferenceRepository(Activity context) {
        this.context = context;
        UserRepository.init(context);
    }

    public void setUserLogged(User user) {
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences(context.getString(R.string.database_shared_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_key_user_logged), user.getUser());
        editor.apply();
    }

    public User getUserLogged() throws SQLException {
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences(context.getString(R.string.database_shared_name), Context.MODE_PRIVATE);
        String idUser = sharedPref.getString(context.getString(R.string.preference_key_user_logged), "");
        return UserRepository.getInstance().getUserId(idUser);

    }

    public String getUserName() {
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences(context.getString(R.string.database_shared_name), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.preference_key_user_logged), "");

    }
}
