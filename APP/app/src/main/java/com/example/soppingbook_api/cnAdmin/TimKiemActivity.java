package com.example.soppingbook_api.cnAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soppingbook_api.MainActivity;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterProduct;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.server.HttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemActivity extends AppCompatActivity {

    EditText edtSearch;
    TextView txtXoaTatCaTimKiem;
    RecyclerView rcvListTimKiem;
    private HttpRequest requestHttp = new HttpRequest();
    private List<Product> productList;
    private AdapterProduct adapterProduct;
    private Handler handler = new Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tim_kiem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtSearch = findViewById(R.id.edtSearch);
        rcvListTimKiem = findViewById(R.id.rcvListTimKiem);
        edtSearch.requestFocus();
        txtXoaTatCaTimKiem = findViewById(R.id.txtXoaTatCaTimKiem);
        adapterProduct = new AdapterProduct();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcvListTimKiem.setLayoutManager(layoutManager);
        txtXoaTatCaTimKiem.setOnClickListener(v -> {
            Toast.makeText(this, "Dang chờ sử lý.", Toast.LENGTH_SHORT).show();
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                String search = s.toString().trim();
                searchRunnable = () -> {
                    if (!search.isEmpty()) {
                        requestHttp.getApiService().searchProduct(search).enqueue(searchProduct);
                    } else {
                        requestHttp.getApiService().getAllProduct().enqueue(getAllProduct);
                    }
                };
                handler.postDelayed(searchRunnable, 500); // Trì hoãn 500ms
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý ở đây
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHttp.getApiService().getAllProduct().enqueue(getAllProduct);
    }

    Callback<ResponeData<List<Product>>> getAllProduct = new Callback<ResponeData<List<Product>>>() {
        @Override
        public void onResponse(Call<ResponeData<List<Product>>> call, Response<ResponeData<List<Product>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    productList = response.body().getData();
                    adapterProduct.setData(productList);
                    rcvListTimKiem.setAdapter(adapterProduct);
//                    for (Product product : productList) {
//                        Log.e("TAG", "Product: " + product.toString()); // In toàn bộ thông tin của product
//                        Log.e("TAG", "Image: " + product.getImages()); // Kiểm tra xem trường image có giá trị không
//                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<List<Product>>> call, Throwable t) {
            Log.e("Error", "getAllProduct: " + t.getMessage());
        }
    };

    Callback<ResponeData<List<Product>>> searchProduct = new Callback<ResponeData<List<Product>>>() {
        @Override
        public void onResponse(Call<ResponeData<List<Product>>> call, Response<ResponeData<List<Product>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    productList = response.body().getData();
                    adapterProduct.setData(productList);
                    rcvListTimKiem.setAdapter(adapterProduct);
//                    for (Product product : productList) {
//                        Log.e("TAG", "Product: " + product.toString()); // In toàn bộ thông tin của product
//                        Log.e("TAG", "Image: " + product.getImages()); // Kiểm tra xem trường image có giá trị không
//                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<List<Product>>> call, Throwable t) {
            Log.e("Error", "searchProduct: " + t.getMessage());
        }
    };
}