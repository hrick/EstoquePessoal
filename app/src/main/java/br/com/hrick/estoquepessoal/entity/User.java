package br.com.hrick.estoquepessoal.entity;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by henrique.pereira on 12/09/2017.
 */

public class User {

    public static final String USER = "user";
    public static final String PASSWORD = "password";

    @SerializedName("usuario")
    @DatabaseField(id = true)
    private String user;
    @SerializedName("senha")
    @DatabaseField()
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
