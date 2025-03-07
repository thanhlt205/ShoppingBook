package com.example.soppingbook_api.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.soppingbook_api.MainActivity;
import com.example.soppingbook_api.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtResetPassword, txtRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtResetPassword = findViewById(R.id.txtResetPassword);
        txtRegister = findViewById(R.id.txtRegister);

        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogged", false);
        if (isLogin) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            // Kiểm tra thông tin đăng nhập và thực hiện đăng nhập
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isLogged", true);  // Đánh dấu người dùng đã đăng nhập
                            editor.apply();
                            String emailKtra = edtEmail.getText().toString().trim();
                            if (emailKtra.equals("admin@gmail.com")) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công Admin", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công User", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        txtResetPassword.setOnClickListener(v -> {
            // Xử lý khi người dùng bấm "Quên mật khẩu"
            startActivity(new Intent(this, ResetPasswordActivity.class));
        });

        // Xử lý khi người dùng bấm "Đăng ký"
        txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

    }
}