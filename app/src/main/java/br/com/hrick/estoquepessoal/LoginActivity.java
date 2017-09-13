package br.com.hrick.estoquepessoal;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.repository.SharedPreferenceRepository;
import br.com.hrick.estoquepessoal.repository.UserRepository;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.pbLogin)
    ProgressBar pbLogin;
    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.btFacebook)
    LoginButton btFacebook;
    @BindView(R.id.tilUser)
    TextInputLayout tilUser;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    SharedPreferenceRepository sharedPreferenceRepository;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceRepository = new SharedPreferenceRepository(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        UserRepository.init(this);
        callbackManager = CallbackManager.Factory.create();
        btFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Logou Face", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                showWarning(getString(R.string.action_canceled));
            }

            @Override
            public void onError(FacebookException error) {
                showWarning(error.getMessage());
            }
        });

        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = tilPassword.getEditText().getText().toString();
                String user = tilUser.getEditText().getText().toString();
                if (user.trim().isEmpty()) {
                    tilUser.setError(getString(R.string.error_user_login));
                } else if (password.trim().isEmpty()) {
                    tilPassword.setError(getString(R.string.error_password_login));
                } else {
                    try {
                        User userLogin = UserRepository.getInstance().getUser(user,password);
                        if(userLogin != null) {
                            sharedPreferenceRepository.setUserLogged(userLogin);
//                            Toast.makeText(LoginActivity.this, "Logou", Toast.LENGTH_LONG).show();
                            tilUser.setError(null);
                            tilPassword.setError(null);
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }else{
                            tilUser.setError(getString(R.string.error_invalid_user_or_password));
                            tilPassword.setError(getString(R.string.error_invalid_user_or_password));
                        }
                    } catch (SQLException e) {
                        showWarning(getString(R.string.erro_database));
                    }
                }
            }
        });


    }
}

