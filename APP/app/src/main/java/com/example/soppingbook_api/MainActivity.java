package com.example.soppingbook_api;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.soppingbook_api.login.LoginActivity;
import com.example.soppingbook_api.cnAdmin.DonHangFragment;
import com.example.soppingbook_api.cnAdmin.ProductFivoriteFragment;
import com.example.soppingbook_api.cnAdmin.HoSoFragment;
import com.example.soppingbook_api.cnAdmin.TrangChuFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        frameLayout = findViewById(R.id.frameLayout);
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.navigationView);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        txtEmail.setText("hi! "+email);

        // Set up the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // Initialize DrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set default fragment if no savedInstanceState
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new TrangChuFragment())
                    .commit();
        }

        // Navigation item click listener
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment fragment = null;

            // Using if-else instead of switch
            if (item.getItemId() == R.id.trangChu) {
                toolbar.setTitle("Trang chủ");
                fragment = new TrangChuFragment();
            } else if (item.getItemId() == R.id.gioHang) {
                toolbar.setTitle("Giỏ hàng yêu thích");
                fragment = new ProductFivoriteFragment();
            } else if (item.getItemId() == R.id.donHang) {
                toolbar.setTitle("Đơn hàng");
                fragment = new DonHangFragment();
            } else if (item.getItemId() == R.id.hoSo) {
                toolbar.setTitle("Hồ sơ");
                fragment = new HoSoFragment();
            } else if (item.getItemId() == R.id.dangXuat) {
                showLogoutDialog();
                return true;
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
            }
            drawerLayout.close();
            return true;
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Thông báo")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setCancelable(true)
                .setPositiveButton("Có", (dialog, which) -> {
                    getApplicationContext()
                            .getSharedPreferences("Login", MODE_PRIVATE)
                            .edit()
                            .putBoolean("isLogged", false)
                            .apply();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    dialog.dismiss();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
