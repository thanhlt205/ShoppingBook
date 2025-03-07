package com.example.soppingbook_api.cnUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.server.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {

    private TextView txtNameDetailProduct, txtPriceDetailProduct, txtDescriptionDetailProduct;
    private ImageView imgDetailProduct, imgBack;
    private Button btnBuyProduct;
    private String idProduct;

    private HttpRequest httpRequest = new HttpRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtNameDetailProduct = findViewById(R.id.txtNameDetailProduct);
        txtPriceDetailProduct = findViewById(R.id.txtPriceDetailProduct);
        txtDescriptionDetailProduct = findViewById(R.id.txtDescriptionDetailProduct);
        imgDetailProduct = findViewById(R.id.imgDetailProduct);
        btnBuyProduct = findViewById(R.id.btnBuyProduct);
        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(view -> finish());

        btnBuyProduct.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("idProduct", idProduct);
            Log.e("idProduct", "idProduct: " + idProduct );
            intent.setClass(this, PayActivity.class);
            startActivity(intent);
        });

        idProduct = getIntent().getStringExtra("id");
        Log.e("IDDDDDDDDĐ", "IDDDDDDDDĐ: " + idProduct);
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRequest.getApiService().getProductId(idProduct).enqueue(getProductId);
    }

    Callback<ResponeData<Product>> getProductId = new Callback<ResponeData<Product>>() {
        @Override
        public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
            if (response.isSuccessful() && response.body() != null) {
                Product product = response.body().getData();
                if (product != null) {
                    // Cập nhật UI với thông tin sản phẩm
                    txtNameDetailProduct.setText(product.getName());
                    txtPriceDetailProduct.setText("Giá: " + product.getPrice() + "đ");
                    txtDescriptionDetailProduct.setText(product.getDescription());

                    // Nếu sản phẩm có hình ảnh, tải hình ảnh bằng Glide
                    if (product.getImages() != null && !product.getImages().isEmpty()) {
                        Glide.with(DetailProductActivity.this)
                                .load("http://" + product.getImages().get(0))
                                .placeholder(R.drawable.icon_giohang)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(imgDetailProduct);
                    }
                }
            } else {
                Log.e("Error", "Không thể lấy thông tin sản phẩm từ response");
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
            Log.e("Error", "Lỗi khi gọi API: " + t.getMessage());
        }
    };
}