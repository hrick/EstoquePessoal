package br.com.hrick.estoquepessoal.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.api.UserApi;
import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.exceptions.SqlExceptionCustom;
import br.com.hrick.estoquepessoal.repository.SharedPreferenceRepository;
import br.com.hrick.estoquepessoal.repository.UserRepository;
import br.com.hrick.estoquepessoal.utils.ApiUtil;
import br.com.hrick.estoquepessoal.view.main.MainActivity;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LauncherActivity extends BaseActivity {

    SharedPreferenceRepository sharedPreferenceRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        UserRepository.init(this);
        sharedPreferenceRepository = new SharedPreferenceRepository(this);
        carregar();
    }

    private void carregar() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.anim_splash);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.ivLogoSplash);
        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }
        UserApi userAPI = ApiUtil.getUserAPIService();
        userAPI.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showWarningClose(e.getMessage(), LauncherActivity.this);
                        Log.e("getUser", "onError", e);
                    }

                    @Override
                    public void onNext(User user) {
                        try {
                            UserRepository.getInstance().createUser(user);
                            if (sharedPreferenceRepository.getUserLogged() != null){
                                Intent intent = new Intent(LauncherActivity.this,
                                        MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(LauncherActivity.this,
                                        LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);

                            }
                            LauncherActivity.this.finish();
                        } catch (SQLException e) {
                            onError(new SqlExceptionCustom(getString(R.string.erro_database)));
                        }
                    }
                });
    }

}
