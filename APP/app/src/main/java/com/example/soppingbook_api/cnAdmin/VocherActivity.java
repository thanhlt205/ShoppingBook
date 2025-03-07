package com.example.soppingbook_api.cnAdmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.soppingbook_api.R;

public class VocherActivity extends AppCompatActivity {

    ConstraintLayout layoutVoucher50, layoutVoucherPreeShip;
    String voucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layoutVoucher50 = findViewById(R.id.layoutVoucher50);
        layoutVoucherPreeShip = findViewById(R.id.layoutVoucherPreeShip);
        layoutVoucher50.setOnClickListener(v -> {
            voucher = "50%";
            Intent intent = new Intent();
            intent.putExtra("voucher", voucher);
            setResult(RESULT_OK, intent);
            finish();
        });

        layoutVoucherPreeShip.setOnClickListener(v -> {
            voucher = "PreeShip";
            Intent intent = new Intent();
            intent.putExtra("voucher", voucher);
            setResult(RESULT_OK, intent);
            finish();
        });

    }
}