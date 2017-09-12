package br.com.hrick.estoquepessoal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.api.UserApi;
import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.repository.UserRepository;
import br.com.hrick.estoquepessoal.utils.ApiUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        UserRepository.init(this);
        carregar();
    }

    private void carregar() {
//        Animation anim = AnimationUtils.loadAnimation(this,
//                R.anim.animacao_splash);
//        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.ivLogoSplash);
//        if (iv != null) {
//            iv.clearAnimation();
//            iv.startAnimation(anim);
//        }
        UserApi userAPI = ApiUtil.getUserAPIService();
        userAPI.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Intent intent = new Intent(LauncherActivity.this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        LauncherActivity.this.finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getUser","onError",e);
                    }

                    @Override
                    public void onNext(User user) {
                        try {
                            UserRepository.getInstance().createUser(user);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }
}
