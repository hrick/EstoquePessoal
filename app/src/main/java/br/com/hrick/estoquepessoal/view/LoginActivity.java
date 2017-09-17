package br.com.hrick.estoquepessoal.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.repository.SharedPreferenceRepository;
import br.com.hrick.estoquepessoal.repository.UserRepository;
import br.com.hrick.estoquepessoal.view.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN = 1234;
    @BindView(R.id.pbLogin)
    ProgressBar pbLogin;
    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.btLoginGoogle)
    SignInButton btLoginGoogle;
    @BindView(R.id.tilUser)
    TextInputLayout tilUser;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    GoogleApiClient mGoogleApiClient;
    SharedPreferenceRepository sharedPreferenceRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceRepository = new SharedPreferenceRepository(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        UserRepository.init(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btLoginGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
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

    private void userLogged(User userLogin){
        sharedPreferenceRepository.setUserLogged(userLogin);
        tilUser.setError(null);
        tilPassword.setError(null);
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("LoginActivityGoogle", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            User userLogin = new User();
            if (acct != null){
                userLogin.setUser(acct.getEmail());
                userLogin.setPassword(acct.getId());
            }
            userLogged(userLogin);
        } else {
            showWarning(getString(R.string.user_invalid));

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showWarning(connectionResult.getErrorMessage());
    }
}

