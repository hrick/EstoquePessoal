package br.com.hrick.estoquepessoal.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.entity.Product;
import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.repository.ProductRepository;
import br.com.hrick.estoquepessoal.repository.StockRepository;
import br.com.hrick.estoquepessoal.view.main.ProductRecyclerViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class StockActivity extends BaseActivity implements View.OnClickListener, OnListProductClickListener, EasyPermissions.PermissionCallbacks {

    private static final int RC_CALL = 123;
    public static final String STOCK = "stock";
    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    @BindView(R.id.btRegister)
    Button btRegister;
    ProductRecyclerViewAdapter productAdapter;
    private Stock stock;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(getString(R.string.title_products));

        ButterKnife.bind(this);
        productAdapter = new ProductRecyclerViewAdapter(new ArrayList<Product>(), this, this);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(productAdapter);
        btRegister.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        StockRepository.init(this);
        ProductRepository.init(this);
        if (extras != null) {
            int stockId = extras.getInt(STOCK);
            try {
                stock = StockRepository.getInstance().getStock(stockId);
            } catch (SQLException e) {
                showWarning(getString(R.string.erro_database));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stock, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_location:
                Intent it = new Intent(this, MapsActivity.class);
                if (stock != null) {
                    it.putExtra(MapsActivity.LATITUDE, stock.getLocationStockLatitude());
                    it.putExtra(MapsActivity.LONGITUDE, stock.getLocationStocklongitude());
                    startActivity(it);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        registerProduct(null);
    }

    private void registerProduct(Product product) {
        Intent it = new Intent(this, RegisterProductActivity.class);
        if (stock != null) {
            it.putExtra(RegisterProductActivity.STOCK_ID, stock.getId());
            if (product != null) {
                it.putExtra(RegisterProductActivity.PRODUCT_ID, product.getId());
            }
            startActivity(it);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            productAdapter.updateListProducts(ProductRepository.getInstance().getProducts(stock));
        } catch (SQLException e) {
            showWarning(getString(R.string.erro_database));
        }
    }


    @Override
    public void onListProductClickListener(Product product) {
        registerProduct(product);
    }

    @Override
    public void onDeleteProductListener(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning)
                .setMessage(R.string.msg_delete_permanently)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            ProductRepository.getInstance().deleteProduct(product);
                            productAdapter.notifyDataSetChanged();
                        } catch (SQLException e) {
                            showWarning(getString(R.string.erro_database));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onListCallResponsibleClickListener(Product product) {
        makeCall();
        phoneNumber = product.getResponsibleNumber();
    }

    @AfterPermissionGranted(RC_CALL)
    private void makeCall() {
        if (hasCallPermission()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            this.startActivity(intent);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.msg_permission_call),
                    RC_CALL,
                    Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private boolean hasCallPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE);
    }


    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        Log.d("stockactivity", "onPermissionsGranted:" + i + ":" + list.size());

    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {
        Log.d("stockactivity", "onPermissionsDenied:" + i + ":" + list.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
