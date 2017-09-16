package br.com.hrick.estoquepessoal.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.login.widget.LoginButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.api.LocationApi;
import br.com.hrick.estoquepessoal.api.UserApi;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.entity.User;
import br.com.hrick.estoquepessoal.exceptions.SqlExceptionCustom;
import br.com.hrick.estoquepessoal.location.ResultLocation;
import br.com.hrick.estoquepessoal.repository.StockRepository;
import br.com.hrick.estoquepessoal.repository.UserRepository;
import br.com.hrick.estoquepessoal.utils.ApiUtil;
import br.com.hrick.estoquepessoal.view.main.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterStockActivity extends BaseActivity implements View.OnClickListener {

    public static final String STOCK = "stock";

    @BindView(R.id.btRegister)
    Button btRegister;
    @BindView(R.id.tilName)
    TextInputLayout tilName;

    @BindView(R.id.tilStreet)
    TextInputLayout tilStreet;
    @BindView(R.id.tilNeighborhood)
    TextInputLayout tilNeighborhood;
    @BindView(R.id.tilNumberAddress)
    TextInputLayout tilNumberAddress;
    @BindView(R.id.progressBarHolder)
    LinearLayout llProgress;

    @BindView(R.id.tilResponsibleNumber)
    TextInputLayout tilResponsibleNumber;
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_stock);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(getString(R.string.title_register_stock));
        ButterKnife.bind(this);
        btRegister.setOnClickListener(this);
        StockRepository.init(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt(STOCK);
            try {
                stock = StockRepository.getInstance().getStock(id);
                if (stock != null) {
                    preencherCampos();
                }
            } catch (SQLException e) {
                showWarning(getString(R.string.erro_database));
            }
        } else {
            stock = new Stock();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void preencherCampos() {
        tilName.getEditText().setText(stock.getName());
        tilResponsibleNumber.getEditText().setText(stock.getResponsibleNumber());
    }

    @Override
    public void onClick(View v) {
        boolean validatedFields = true;
        String name = tilName.getEditText().getText().toString();
        String responsibleNumber = tilResponsibleNumber.getEditText().getText().toString();

        String street = tilStreet.getEditText().getText().toString();
        String neighborhood = tilNeighborhood.getEditText().getText().toString();
        String numberAddress = tilNumberAddress.getEditText().getText().toString();
        if (name.trim().isEmpty()) {
            validatedFields = false;
            tilName.setError(getString(R.string.error_field_required));
        }
        if (responsibleNumber.trim().isEmpty()) {
            validatedFields = false;
            tilResponsibleNumber.setError(getString(R.string.error_field_required));
        }

        if (street.trim().isEmpty()) {
            validatedFields = false;
            tilStreet.setError(getString(R.string.error_field_required));
        }
        if (neighborhood.trim().isEmpty()) {
            validatedFields = false;
            tilNeighborhood.setError(getString(R.string.error_field_required));
        }
        if (numberAddress.trim().isEmpty()) {
            validatedFields = false;
            tilNumberAddress.setError(getString(R.string.error_field_required));
        }


        if (validatedFields) {
            llProgress.setVisibility(View.VISIBLE);
            stock.setName(name);
            stock.setResponsibleNumber(responsibleNumber);
            LocationApi locationAPI = ApiUtil.getLocationAPIService();
//            String parameters = street.trim() + "-" +neighborhood.trim() + "-" + numberAddress.trim();
            String query = street.trim().replace(" ", "") + "-" +neighborhood.trim().replace(" ", "") + "-" + numberAddress.trim().replace(" ", "");
            try {
                query = URLEncoder.encode(query, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            locationAPI.getLocation(query, false)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResultLocation>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            llProgress.setVisibility(View.GONE);
                            showWarning(e.getMessage());
                            Log.e("getUser", "onError", e);
                        }

                        @Override
                        public void onNext(ResultLocation resultlocation) {
                            llProgress.setVisibility(View.GONE);

                            if (resultlocation.getStatus().equalsIgnoreCase("OK")) {
                                stock.setLocationStockLatitude(resultlocation.getResults().get(0).getGeometry().getLocation().getLat());
                                stock.setLocationStocklongitude(resultlocation.getResults().get(0).getGeometry().getLocation().getLng());
                                saveStock();
                            } else {
                                showWarning(getString(R.string.invalid_adress));
                            }
                        }
                    });

        }
    }

    private void saveStock() {
        try {
            StockRepository.getInstance().createStock(stock);
            finish();
        } catch (SQLException e) {
            showWarning(getString(R.string.erro_database));
            Log.e("RegisterStockActivity", "btRegister", e);
        }
    }
}
