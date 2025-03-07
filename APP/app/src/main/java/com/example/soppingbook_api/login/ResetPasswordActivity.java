package com.example.soppingbook_api.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.soppingbook_api.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button btnResetPassword;
    private EditText edtEmailResetPassword;
    private ImageView imgBackLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnResetPassword = findViewById(R.id.btnResetPassword);
        edtEmailResetPassword = findViewById(R.id.edtEmail);
        imgBackLogin = findViewById(R.id.imgBackLogin);
        imgBackLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnResetPassword.setOnClickListener(v -> {
            String email = edtEmailResetPassword.getText().toString();

            if (email.isEmpty()) {
                edtEmailResetPassword.setError("Vui lòng nhập email");
                return;
            }
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Vui lòng kiểm tra hộp thư trong email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        finish();
                    }else{
                        Toast.makeText(ResetPasswordActivity.this, "Lỗi nhận dạng email đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}