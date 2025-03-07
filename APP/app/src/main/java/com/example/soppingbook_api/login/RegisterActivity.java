package com.example.soppingbook_api.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.soppingbook_api.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword1, edtPassword2;
    private Button btnRegister;
    private TextView txtLogin;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Khởi tạo FirebaseApp nếu chưa khởi tạo
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        // Sử dụng FirebaseAuth hoặc các dịch vụ khác
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword1 = findViewById(R.id.edtPassword1);
        edtPassword2 = findViewById(R.id.edtPassword2);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        btnRegister.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password1 = edtPassword1.getText().toString();
            String password2 = edtPassword2.getText().toString();

            // Kiểm tra thông tin đăng nhập và thực hiện đăng nhập
            if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            } else {
                if (!password1.equals(password2)) {
                    Toast.makeText(this, "Hai mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                addFirebaseFirestore();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void addFirebaseFirestore() {
        // Lưu thông tin vào Firestore

        String email = edtEmail.getText().toString().trim();

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);

        db.collection("users")
                .document(userId)
                .set(user)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(RegisterActivity.this, "Thông tin người dùng đã lưu", Toast.LENGTH_SHORT).show();
//                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Lưu thông tin thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}