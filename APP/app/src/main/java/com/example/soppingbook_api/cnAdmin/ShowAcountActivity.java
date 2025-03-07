package com.example.soppingbook_api.cnAdmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterAcount;
import com.example.soppingbook_api.models.AcountModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShowAcountActivity extends AppCompatActivity {
    RecyclerView rcvAcount;
    AdapterAcount adapter;
    ArrayList<AcountModel> accountList;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    LottieAnimationView animatonAcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_acount);
        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view và đối tượng
        rcvAcount = findViewById(R.id.rcvAcount);
        animatonAcount = findViewById(R.id.animatonAcount);
        accountList = new ArrayList<>();
        adapter = new AdapterAcount(this, accountList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        // Cấu hình RecyclerView
        rcvAcount.setLayoutManager(layoutManager);
        rcvAcount.setAdapter(adapter);

        // Lấy danh sách tài khoản người dùng từ Firestore
        fetchAccountsFromFirestore();
    }

    private void fetchAccountsFromFirestore() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AcountModel user = document.toObject(AcountModel.class);
                            accountList.add(user);
                        }
                        if (!accountList.isEmpty()) {
                            animatonAcount.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        animatonAcount.setVisibility(View.VISIBLE);
                        Toast.makeText(ShowAcountActivity.this, "Lỗi khi lấy dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}