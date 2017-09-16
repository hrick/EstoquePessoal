package br.com.hrick.estoquepessoal.view;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.Product;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.repository.ProductRepository;
import br.com.hrick.estoquepessoal.repository.StockRepository;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterProductActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String STOCK_ID = "STOCK_ID";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    @BindView(R.id.btRegister)
    Button btRegister;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilBrand)
    TextInputLayout tilBrand;
    @BindView(R.id.tilQuantity)
    TextInputLayout tilQuantity;
    @BindView(R.id.tilResponsibleNumber)
    TextInputLayout tilResponsibleNumber;
    @BindView(R.id.cbUseResposibleNumberStock)
    CheckBox cbUseResposibleNumberStock;
    private Stock stock;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(getString(R.string.title_register_product));

        btRegister.setOnClickListener(this);
        StockRepository.init(this);
        ProductRepository.init(this);
        Bundle extras = getIntent().getExtras();
        cbUseResposibleNumberStock.setOnCheckedChangeListener(this);
        if (extras != null) {
            int stockId = extras.getInt(STOCK_ID);
            int productId = extras.getInt(PRODUCT_ID);
            try {
                stock = StockRepository.getInstance().getStock(stockId);
                product = ProductRepository.getInstance().getProduct(productId);
                if (product != null) {
                    preencherCampos();
                } else {
                    product = new Product();
                }
            } catch (SQLException e) {
                showWarning(getString(R.string.erro_database));
            }
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
        // todo data validade
        tilName.getEditText().setText(product.getName());
        tilResponsibleNumber.getEditText().setText(product.getResponsibleNumber());
        tilBrand.getEditText().setText(product.getBrand());
        tilQuantity.getEditText().setText(String.valueOf(product.getQuantity()));
    }

    @Override
    public void onClick(View v) {
        boolean validatedFields = true;
        String name = tilName.getEditText().getText().toString();
        String responsibleNumber = tilResponsibleNumber.getEditText().getText().toString();
        String brand = tilBrand.getEditText().getText().toString();
        int quantity = 0;
        if (!tilQuantity.getEditText().getText().toString().isEmpty())
            quantity = Integer.parseInt(tilQuantity.getEditText().getText().toString());
        if (name.trim().isEmpty()) {
            validatedFields = false;
            tilName.setError(getString(R.string.error_field_required));
        }
        if (responsibleNumber.trim().isEmpty()) {
            validatedFields = false;
            tilResponsibleNumber.setError(getString(R.string.error_field_required));
        }
        if (brand.trim().isEmpty()) {
            validatedFields = false;
            tilBrand.setError(getString(R.string.error_field_required));
        }
        if (quantity == 0) {
            validatedFields = false;
            tilQuantity.setError(getString(R.string.error_field_required));
        }
        if (validatedFields) {
            try {
                product.setName(name);
                product.setResponsibleNumber(responsibleNumber);
                product.setBrand(responsibleNumber);
                product.setQuantity(quantity);
                product.setStock(stock);
                ProductRepository.getInstance().createProduct(product);
                finish();
            } catch (SQLException e) {
                showWarning(getString(R.string.erro_database));
                Log.e("RegisterStockActivity", "btRegister", e);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (stock != null)
                tilResponsibleNumber.getEditText().setText(stock.getResponsibleNumber());
        }
    }
}
