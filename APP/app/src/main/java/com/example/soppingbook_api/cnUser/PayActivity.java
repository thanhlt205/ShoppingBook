package com.example.soppingbook_api.cnUser;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.cnAdmin.TrangChuFragment;
import com.example.soppingbook_api.cnAdmin.VocherActivity;
import com.example.soppingbook_api.models.Address;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.models.StatusProduct;
import com.example.soppingbook_api.server.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {

    private ImageView imgBack, imgPayProduc, imgVoucher;
    private TextView txtName, txtPhone, txtAddress, txtThayDoiAddress, txtPayName, txtPayPrice, txtVanChuyen, txtGiaSach, txtGiamGia, txtTongTien, txtMaVoucher;
    private RadioButton rdoNhanHang, rdoTaiKhoan;
    private Button btnPayXacNhan;
    private String idAddress = "";
    private String idProduct = "";
    private String getProductIdDiaChi = "";
    private ConstraintLayout layoutVocher;
    private String voucher = "";
    private int phiVanChuyen = 35;
    private int giamGia = 0;
    private int tongTien = 0;
    private int giaSanPham = 0;

    private HttpRequest httpRequest = new HttpRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtPayName = findViewById(R.id.txtPayName);
        txtPayPrice = findViewById(R.id.txtPayPrice);
        txtVanChuyen = findViewById(R.id.txtVanChuyen);
        txtGiaSach = findViewById(R.id.txtGiaSach);
        txtGiamGia = findViewById(R.id.txtGiamGia);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtThayDoiAddress = findViewById(R.id.txtThayDoiAddress);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        rdoNhanHang = findViewById(R.id.rdoNhanHang);
        rdoTaiKhoan = findViewById(R.id.rdoTaiKhoan);
        btnPayXacNhan = findViewById(R.id.btnPayXacNhan);
        imgBack = findViewById(R.id.imgBack);
        imgPayProduc = findViewById(R.id.imgPayProduc);
        layoutVocher = findViewById(R.id.layoutVoucher);
        imgVoucher = findViewById(R.id.imgVoucher);
        txtMaVoucher = findViewById(R.id.txtMaVoucher);

        txtVanChuyen.setText(String.valueOf(phiVanChuyen));
        txtGiamGia.setPaintFlags(txtGiamGia.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        imgBack.setOnClickListener(v -> finish());

        layoutVocher.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(PayActivity.this, VocherActivity.class);
            startActivityForResult(intent, 100);
        });

        txtThayDoiAddress.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(PayActivity.this, DiaChiActivity.class);
            startActivityForResult(intent, 10);
        });

        rdoNhanHang.setOnClickListener(v -> rdoNhanHang.setChecked(true));

        btnPayXacNhan.setOnClickListener(view -> {
            if (idProduct == null || idProduct.isEmpty()) {
                Toast.makeText(this, "Chưa có sản phẩm nào được chọn!", Toast.LENGTH_SHORT).show();
                return;
            }

            String priceProduct = String.valueOf(tongTien);

            StatusProduct statusProduct = new StatusProduct(idProduct, priceProduct, "Chờ xử lý");
            httpRequest.getApiService().addStatusProduct(statusProduct).enqueue(new Callback<ResponeData<StatusProduct>>() {
                @Override
                public void onResponse(Call<ResponeData<StatusProduct>> call, Response<ResponeData<StatusProduct>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(PayActivity.this, "Mua hàng thành công! Hãy chờ ngày nhận hàng.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PayActivity.this, "Đã xảy ra lỗi. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<StatusProduct>> call, Throwable t) {
                    Log.e("Error", "addStatusProduct: " + t.getMessage());
                }
            });
        });
//
//        idAddress = getIntent().getStringExtra("idAddress");
//        Log.e("IDPay", "idAddress: " + idAddress);
        idProduct = getIntent().getStringExtra("idProduct");
        Log.e("IDPay", "idProduct: " + idProduct);
        getProductIdDiaChi = getIntent().getStringExtra("putProductId");
        Log.e("IDPay", "idProduct: " + getProductIdDiaChi);
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRequest.getApiService().getAddressById(idAddress).enqueue(getAddressById);

        if (idProduct == null && getProductIdDiaChi == null) {
            Log.e("Error", "Không có id sản phẩm");
        } else {
            if (idProduct == null) {
                httpRequest.getApiService().getProductId(idProduct).enqueue(getProductId);
            } else if (getProductIdDiaChi == null) {
                httpRequest.getApiService().getProductId(idProduct).enqueue(getProductId);
            }
        }
        capNhatTongTien();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            voucher = data.getStringExtra("voucher");
            MaVocher(voucher);
        }
        if (requestCode == 10 && resultCode == RESULT_OK){
            idAddress = data.getStringExtra("idAddress");
            onResume();
        }
    }

    Callback<ResponeData<Address>> getAddressById = new Callback<ResponeData<Address>>() {
        @Override
        public void onResponse(Call<ResponeData<Address>> call, Response<ResponeData<Address>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Address address = response.body().getData();
                    if (address != null) {
                        txtName.setText("Họ và tên: " + address.getName());
                        txtPhone.setText("Số điện thoại: " + address.getPhone());
                        txtAddress.setText(address.getAddress());
                        Log.e("TAG", "address.getName(): " + address.getName());
                        Log.e("TAG", "address.getPhone(): " + address.getPhone());
                        Log.e("TAG", "onResponse: " + address.getAddress());
                    } else {
                        Log.e("Error", "Address is null" + response.message());
                    }
                }
            } else {
                Log.e("Error", "getAddressById failed: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Address>> call, Throwable t) {
            Log.e("Error", "getAddressById: " + t.getMessage());
        }
    };

    Callback<ResponeData<Product>> getProductId = new Callback<ResponeData<Product>>() {
        @Override
        public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Product product = response.body().getData();

                    if (product != null) {
                        txtPayName.setText(product.getName());
                        txtPayPrice.setText("Giá: " + product.getPrice() + "đ");
                        giaSanPham = product.getPrice();
                        capNhatTongTien(); // Gọi hàm cập nhật tổng tiền

                        // Nếu sản phẩm có hình ảnh, tải hình ảnh bằng Glide
                        if (product.getImages() != null && !product.getImages().isEmpty()) {
                            Glide.with(PayActivity.this)
                                    .load("http://" + product.getImages().get(0))
                                    .placeholder(R.drawable.icon_giohang)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(imgPayProduc);
                        }
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
            Log.e("Error", "getProductId: " + t.getMessage());
        }
    };

    private void MaVocher(String voucher) {
        if (voucher.equals("50%")) {
            giamGia = giaSanPham / 2; // Giảm giá 50%
            txtVanChuyen.setPaintFlags(txtGiamGia.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            txtGiamGia.setText(String.valueOf(giamGia));
            txtMaVoucher.setText("Giảm giá sản phẩm: 50%");
            imgVoucher.setImageResource(R.drawable.vocher_50);
        } else if (voucher.equals("PreeShip")) {
            giamGia = phiVanChuyen; // Miễn phí vận chuyển
            txtVanChuyen.setPaintFlags(txtGiamGia.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtGiamGia.setText(String.valueOf(giamGia));
            txtMaVoucher.setText("Giảm giá: Free Ship");
            imgVoucher.setImageResource(R.drawable.vocher_pree_ship);
        } else {
            Toast.makeText(this, "Lỗi voucher xin vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            giamGia = 0;
            txtGiamGia.setText(String.valueOf(giamGia));
            txtMaVoucher.setText("Lỗi nhận mã");
            imgVoucher.setImageResource(R.drawable.ic_launcher_foreground);
        }
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        tongTien = giaSanPham + phiVanChuyen - giamGia;
        txtGiaSach.setText(String.valueOf(giaSanPham));
        txtTongTien.setText(String.valueOf(tongTien));
        Log.e("TongTien", "Tổng tiền: " + tongTien);
    }

    private void getAllContent() {
        // Lấy đường dẫn ảnh
        String imageUrl = "";
        if (imgPayProduc.getDrawable() != null) {
            imageUrl = "Đường dẫn ảnh hiển thị không thể lấy trực tiếp từ ImageView, hãy sử dụng Glide hoặc lưu URL từ dữ liệu sản phẩm.";
        }

        // Lấy tên sản phẩm
        String productName = txtPayName.getText().toString();

        // Lấy giá sản phẩm
        String productPrice = txtPayPrice.getText().toString();

        // Lấy tổng tiền
        String totalPrice = txtTongTien.getText().toString();

        // In ra log (hoặc sử dụng để xử lý tiếp)
        Log.d("getAllContent", "Image URL: " + imageUrl);
        Log.d("getAllContent", "Product Name: " + productName);
        Log.d("getAllContent", "Product Price: " + productPrice);
        Log.d("getAllContent", "Total Price: " + totalPrice);

        // Nếu muốn xử lý tiếp, bạn có thể truyền dữ liệu này sang các nơi khác
        // Ví dụ: Gửi dữ liệu qua một Intent hoặc API
    }
}