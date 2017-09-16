package br.com.hrick.estoquepessoal.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.sql.SQLException;

import br.com.hrick.estoquepessoal.entity.Stock;
import br.com.hrick.estoquepessoal.repository.StockRepository;
import br.com.hrick.estoquepessoal.view.AboutActivity;
import br.com.hrick.estoquepessoal.view.BaseActivity;
import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.repository.SharedPreferenceRepository;
import br.com.hrick.estoquepessoal.view.RegisterStockActivity;
import br.com.hrick.estoquepessoal.view.StockActivity;
import br.com.hrick.estoquepessoal.view.main.fragments.StockFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, StockFragment.OnListFragmentInteractionListener {
    SharedPreferenceRepository sharedPreferenceRepository;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferenceRepository = new SharedPreferenceRepository(this);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerStock();
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        TextView tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        tvUserName.setText(sharedPreferenceRepository.getUserName());
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showStock(Stock stock) {
        Intent it = new Intent(this, StockActivity.class);

        if (stock != null){
            it.putExtra(StockActivity.STOCK, stock.getId());
        }

        startActivity(it);
    }
    private void registerStock() {
        Intent it = new Intent(this, RegisterStockActivity.class);
        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this,
                    AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            sharedPreferenceRepository.doLogout();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Stock item) {
        showStock(item);
    }

    @Override
    public void onDeleteStockListener(final Stock item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning)
                .setMessage(R.string.msg_delete_permanently)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            StockRepository.getInstance().deleteStock(item);
                            StockFragment fragment = (StockFragment) getFragmentManager().findFragmentById(R.id.article_fragment);
                            fragment.notifyList();
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
}
