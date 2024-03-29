package ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.acmesupermarket.R;
import com.google.android.material.navigation.NavigationView;

import models.Client;
import ui.login.LoginViewModel;
import ui.shop.ShopViewModel;
import ui.transactions.TransactionsViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.shopFragment, R.id.transactionsFragment, R.id.profileFragment)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.homeFragment ||
                    destination.getId() == R.id.loginFragment ||
                    destination.getId() == R.id.personalDataFragment ||
                    destination.getId() == R.id.creditCardFragment) {
                toolbar.setVisibility(View.GONE);
            } else {
                toolbar.setVisibility(View.VISIBLE);
            }
        });

        final View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (toolbar.getVisibility() == View.VISIBLE) {
                setMenu();
                setupViewModelListeners();
            }
        });
    }

    private void setupViewModelListeners() {
        ViewModelProvider provider = ViewModelProviders.of(this);
        LoginViewModel loginViewModel = provider.get(LoginViewModel.class);
        ShopViewModel shopViewModel = provider.get(ShopViewModel.class);
        TransactionsViewModel transactionsViewModel = provider.get(TransactionsViewModel.class);

        ActionMenuItemView btn_sync = findViewById(R.id.action_sync);
        if (btn_sync != null) {
            btn_sync.setOnClickListener(v -> {
                Client client = loginViewModel.getClient();
                loginViewModel.syncDatabase();
                shopViewModel.syncDatabase(client);
                transactionsViewModel.syncDatabase(client);
            });

            Client clientData = loginViewModel.getClient();
            loginViewModel.client.observe(this, client -> loginViewModel.saveClient(client, getApplicationContext()));
            shopViewModel.vouchers.observe(this, vouchers -> shopViewModel.saveVouchers(clientData, vouchers, getApplicationContext()));
            transactionsViewModel.transactions.observe(this, transactions -> transactionsViewModel.saveTransactions(clientData, transactions, getApplicationContext()));
        }

    }

    private void setMenu() {
        ViewModelProvider provider = ViewModelProviders.of(this);
        LoginViewModel loginViewModel = provider.get(LoginViewModel.class);

        Client client = loginViewModel.getClient();

        TextView username = findViewById(R.id.username);
        TextView full_name = findViewById(R.id.user_full_name);
        if (username != null && full_name != null) {
            username.setText(client.getUsername());
            full_name.setText(client.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
