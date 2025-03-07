package com.example.soppingbook_api.cnUser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterAddress;
import com.example.soppingbook_api.models.Address;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.server.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaChiActivity extends AppCompatActivity {

    private HttpRequest httpRequest = new HttpRequest();
    private List<Address> addressList = new ArrayList<>();
    private RecyclerView rcvDiaChi;
    private ImageView imgAddDiaChi, imgBack;
    private AdapterAddress adapterAddress;
    private LottieAnimationView lottieAnimationDiaChi;
    private String idProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dia_chi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idProduct = getIntent().getStringExtra("idProduct");

        rcvDiaChi = findViewById(R.id.rcvDiaChi);
        imgAddDiaChi = findViewById(R.id.imgAddDiaChi);
        imgBack = findViewById(R.id.imgBack);
        lottieAnimationDiaChi = findViewById(R.id.lottieAnimationDiaChi);

        adapterAddress = new AdapterAddress();
        adapterAddress.setListener(new AdapterAddress.PutGetListener() {
            @Override
            public void putAddress(String idAddress) {
                Intent intent = new Intent();
                intent.putExtra("idAddress", idAddress);
                intent.setClass(DiaChiActivity.this, PayActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void putProduct() {
                if (idProduct != null) {
                    Intent intent = new Intent();
                    intent.putExtra("putProductId", idProduct);
                    Log.e("putProductId", "putProductId: " + idProduct );
                    intent.setClass(DiaChiActivity.this, PayActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rcvDiaChi.setLayoutManager(layoutManager);
        httpRequest.getApiService().getAllAddress().enqueue(getAllAddress);


        imgBack.setOnClickListener(v -> finish());
        imgAddDiaChi.setOnClickListener(v -> showDialogAdd());
        adapterAddress.setOnClickItemListener(new AdapterAddress.OnClickItemListener() {
            @Override
            public void onDeleteLongClick(String id) {
                deleteAddress(id);
            }

            @Override
            public void onUpdateClick(String id, String name, String phone, String address) {
                updateAddress(id, name, phone, address);
            }
        });
    }

    Callback<ResponeData<List<Address>>> getAllAddress = new Callback<ResponeData<List<Address>>>() {
        @Override
        public void onResponse(Call<ResponeData<List<Address>>> call, Response<ResponeData<List<Address>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    addressList = response.body().getData();
                    adapterAddress.setData(addressList);
                    rcvDiaChi.setAdapter(adapterAddress);
                    if (!addressList.isEmpty()) {
                        imgAddDiaChi.setVisibility(View.VISIBLE);
                        lottieAnimationDiaChi.setVisibility(View.GONE);
                    }
//                    for (Address address : addressList) {
//                        Log.e("TAG", "getAllAddress: " + address.getId());
//                        Log.e("TAG", "getAllAddress: " + address.getName());
//                        Log.e("TAG", "getAllAddress: " + address.getPhone());
//                        Log.e("TAG", "getAllAddress: " + address.getAddress());
//                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<List<Address>>> call, Throwable t) {
            Log.e("Error", "getAllAddress: " + t.getMessage());
        }
    };

    Callback<ResponeData<List<Address>>> addAddress = new Callback<ResponeData<List<Address>>>() {
        @Override
        public void onResponse(Call<ResponeData<List<Address>>> call, Response<ResponeData<List<Address>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    // Gọi lại API để cập nhật danh sách
                    httpRequest.getApiService().getAllAddress().enqueue(addAddress);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<List<Address>>> call, Throwable t) {
            Log.e("Error", "addAddress: " + t.getMessage());
        }
    };

    Callback<ResponeData<Address>> updateAddress = new Callback<ResponeData<Address>>() {
        @Override
        public void onResponse(Call<ResponeData<Address>> call, Response<ResponeData<Address>> response) {
            // gọi hàm API cập nhật danh sách địa chỉ
            httpRequest.getApiService().getAllAddress().enqueue(getAllAddress);
        }

        @Override
        public void onFailure(Call<ResponeData<Address>> call, Throwable t) {
            Log.e("Error", "updateAddress: " + t.getMessage());
        }
    };

    Callback<ResponeData<Address>> deleteAddress = new Callback<ResponeData<Address>>() {
        @Override
        public void onResponse(Call<ResponeData<Address>> call, Response<ResponeData<Address>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    // Gọi lại API để cập nhật danh sách
                    httpRequest.getApiService().getAllAddress().enqueue(getAllAddress);
                }
            } else {
                Log.e("Error", "deleteAddress failed: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Address>> call, Throwable t) {
            Log.e("Error", "deleteAddress: " + t.getMessage());
        }
    };

    private void showDialogAdd() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_item_crud_diachi, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        Button btnXacNhan = view.findViewById(R.id.btnXacNhan);
        Button btnHuy = view.findViewById(R.id.btnHuy);
        TextView edtName = view.findViewById(R.id.edtName);
        TextView edtSdt = view.findViewById(R.id.edtSdt);
        TextView edtAddress = view.findViewById(R.id.edtAddress);

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        btnXacNhan.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String phone = edtSdt.getText().toString();
            String address = edtAddress.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || !phone.matches("\\d+") || phone.length() != 10) {
                Toast.makeText(this, "Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
            } else {
                Address address1 = new Address(name, phone, address);
                httpRequest.getApiService().addAddress(address1).enqueue(addAddress);
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                addressList.add(address1);
                adapterAddress.setData(addressList);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateAddress(String id, String name, String phone, String address) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_item_crud_diachi, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        Button btnXacNhan = view.findViewById(R.id.btnXacNhan);
        Button btnHuy = view.findViewById(R.id.btnHuy);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtSdt = view.findViewById(R.id.edtSdt);
        EditText edtAddress = view.findViewById(R.id.edtAddress);

        edtName.setText(name);
        edtSdt.setText(phone);
        edtAddress.setText(address);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnXacNhan.setOnClickListener(v -> {
            String name1 = edtName.getText().toString();
            String phone1 = edtSdt.getText().toString();
            String address1 = edtAddress.getText().toString();

            if (name1.isEmpty() || phone1.isEmpty() || address1.isEmpty() || !phone1.matches("\\d+") || phone1.length() != 10) {
                Toast.makeText(this, "Vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Address address2 = new Address(id, name1, phone1, address1);
                Log.e("TAG", "id: " + address2.getId());
                Log.e("TAG", "name: " + address2.getName());
                Log.e("TAG", "phone: " + address2.getPhone());
                Log.e("TAG", "address: " + address2.getAddress());
                httpRequest.getApiService().updateAddress(id, address2).enqueue(updateAddress);
                Toast.makeText(this, "Cập nhật thành công.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteAddress(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa địa chỉ này chứ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", (dialog, which) -> {
            httpRequest.getApiService().deleteAddress(id).enqueue(deleteAddress);
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}