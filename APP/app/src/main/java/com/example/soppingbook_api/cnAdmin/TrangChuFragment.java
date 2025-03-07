package com.example.soppingbook_api.cnAdmin;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterProduct;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ProductFivorite;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.server.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangChuFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private EditText edtTimKiem;
    private FloatingActionButton btnThem;
    private LottieAnimationView lottieAnimationTrangChu;
    private HttpRequest requestHttp = new HttpRequest();
    private RecyclerView rcvProductTrangChu;
    private List<Product> productList;
    private LinearLayout lnViewThem;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    File file;
    AdapterProduct adapterProduct;
//    private String imgProduct;

    public TrangChuFragment() {
        // Required empty public constructor
    }

    public static TrangChuFragment newInstance() {
        TrangChuFragment fragment = new TrangChuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
        btnThem = view.findViewById(R.id.btnThem);
        lottieAnimationTrangChu = view.findViewById(R.id.lottieAnimationTrangChu);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rcvProductTrangChu = view.findViewById(R.id.rcvProductTrangChu);
        lnViewThem = view.findViewById(R.id.lnViewThem);

        adapterProduct = new AdapterProduct();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rcvProductTrangChu.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener( () -> {
            requestHttp.getApiService().getAllProduct().enqueue(getAllProduct);
            swipeRefreshLayout.setRefreshing(false);
        });

        String adminId = mAuth.getCurrentUser().getEmail().trim();
        if (adminId.equals("admin@gmail.com")) {
            lnViewThem.setVisibility(View.VISIBLE);
        } else {
            lnViewThem.setVisibility(View.GONE);
        }

        btnThem.setOnClickListener(view1 -> {
            showDialogAddProduct();
        });
        edtTimKiem.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), TimKiemActivity.class));
        });

        adapterProduct.setOnClickItemListener(new AdapterProduct.OnClickImageListener() {
            @Override
            public void onDeleteClick(String id) {
                deleteProductDialog(id);
            }

            @Override
            public void onAddFivotireClick(String idProduct) {
                ProductFivorite productFivorite = new ProductFivorite(idProduct);
                requestHttp.getApiService().addProducFivorite(productFivorite).enqueue(addProducFivorite);
            }
        });

        onResume();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        btnThem.setVisibility(View.GONE);
        requestHttp.getApiService().getAllProduct().enqueue(getAllProduct);
    }

    Callback<ResponeData<ProductFivorite>> addProducFivorite = new Callback<ResponeData<ProductFivorite>>() {
        @Override
        public void onResponse(Call<ResponeData<ProductFivorite>> call, Response<ResponeData<ProductFivorite>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getContext(), "Thêm sản phẩm yêu thích thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thêm sản phẩm yêu thích thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<ProductFivorite>> call, Throwable t) {
            Log.e("Error", "addProducFivorite: " + t.getMessage());
        }
    };

    Callback<ResponeData<List<Product>>> getAllProduct = new Callback<ResponeData<List<Product>>>() {
        @Override
        public void onResponse(Call<ResponeData<List<Product>>> call, Response<ResponeData<List<Product>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    productList = response.body().getData();
                    adapterProduct.setData(productList);
                    rcvProductTrangChu.setAdapter(adapterProduct);
                    if (!productList.isEmpty()) {
                        btnThem.setVisibility(View.VISIBLE);
                        lottieAnimationTrangChu.setVisibility(View.GONE);
                    }
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

    Callback<ResponeData<Product>> addProduct = new Callback<ResponeData<Product>>() {
        @Override
        public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
            if (response.isSuccessful() && response.body() != null) {
                // Thêm sản phẩm mới vào danh sách productList
                Product newProduct = response.body().getData();
                productList.add(newProduct);

                // Cập nhật lại Adapter để hiển thị sản phẩm mới
                adapterProduct.notifyItemInserted(productList.size() - 1);

                // Đảm bảo giao diện được làm mới
                adapterProduct.notifyDataSetChanged(); // Hoặc notifyItemInserted() tùy theo yêu cầu

                // Hiển thị thông báo thành công
                Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
            Log.e("Error", "addProduct: " + t.getMessage());
            Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    };

    Callback<ResponeData<Product>> deleteProduct = new Callback<ResponeData<Product>>() {
        @Override
        public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    requestHttp.getApiService().getAllProduct().enqueue(getAllProduct);
                    Toast.makeText(getContext(), "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
            Log.e("Error", "deleteProduct: " + t.getMessage());
        }
    };

    private void deleteProductDialog(String id) {
        Log.e("TAG", "deleteProductDialog: " + id);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Có", (dialogInterface, i) -> {
            requestHttp.getApiService().deleteProduct(id).enqueue(deleteProduct);
        });

        builder.setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    ImageView imgProduct;

    public void showDialogAddProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.layout_item_crud_product, null);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        imgProduct = view.findViewById(R.id.imgProduct);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtDescription = view.findViewById(R.id.edtDescription);
        EditText edtStatus = view.findViewById(R.id.edtStatus);
        Button btnXacNhan = view.findViewById(R.id.btnXacNhan);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        imgProduct.setOnClickListener(view1 -> chonImage());

        btnXacNhan.setOnClickListener(view1 -> {
            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), edtName.getText().toString().trim());
            RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), edtPrice.getText().toString().trim());
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), edtDescription.getText().toString().trim());
            RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"), edtStatus.getText().toString().trim());
            MultipartBody.Part images = null;
            if (file != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                images = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            }
            if (images != null) {
                requestHttp.getApiService().addProduct(name, price, description, status, images).enqueue(addProduct);
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Loi vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        });
        btnHuy.setOnClickListener(view1 -> dialog.dismiss());
        dialog.show();
    }

    private File createFileFromUri(Uri path, String name) {
        // Tạo tệp trong thư mục cache với tên cụ thể
        File _file = new File(getContext().getCacheDir(), name + ".png");
        try {
            // Mở InputStream từ URI
            InputStream in = getContext().getContentResolver().openInputStream(path);
            // Tạo OutputStream để ghi vào file
            OutputStream out = new FileOutputStream(_file);

            // Tạo buffer để đọc/ghi dữ liệu
            byte[] buf = new byte[1024];
            int len;

            // Đọc từ input và ghi vào output
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            // Đóng các stream
            out.close();
            in.close();

            // Trả về file được tạo
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Ghi lại lỗi tệp không tìm thấy
        } catch (IOException e) {
            e.printStackTrace(); // Ghi lại lỗi đọc/ghi tệp
        }

        // Trường hợp có lỗi, trả về null
        return null;
    }
    // Khởi tạo ActivityResultLauncher

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Kiểm tra kết quả trả về có hợp lệ không
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Lấy đường dẫn URI của ảnh
                            Uri imagePath = data.getData();

                            // Tạo file từ URI (hàm createFileFromUri phải được định nghĩa)
                            file = createFileFromUri(imagePath, "avatar");

                            // Sử dụng Glide để hiển thị ảnh trong ImageView
                            Glide.with(getContext())
                                    .load(file) // Load ảnh từ file
                                    .thumbnail(Glide.with(getContext()).load(R.drawable.img)) // Hiển thị ảnh thu nhỏ khi tải
                                    .centerCrop() // Cắt ảnh để vừa khít khung hình
//                                    .circleCrop() // Cắt ảnh thành hình tròn
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Không lưu trữ vào cache ổ đĩa
                                    .skipMemoryCache(true) // Bỏ qua cache trong bộ nhớ
                                    .into(imgProduct); // ImageView cần load ảnh
                        }
                    }
                }
            }
    );

    private void chonImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        getImage.launch(intent);
    }

//    public void showDialogAddProduct(Uri uri) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        View view = getLayoutInflater().inflate(R.layout.layout_item_crud_product, null);
//        builder.setView(view);
//        builder.setCancelable(false);
//        AlertDialog dialog = builder.create();
//
//        ImageView imgProduct = view.findViewById(R.id.imgProduct);
//        EditText edtName = view.findViewById(R.id.edtName);
//        EditText edtPrice = view.findViewById(R.id.edtPrice);
//        EditText edtDescription = view.findViewById(R.id.edtDescription);
//        EditText edtStatus = view.findViewById(R.id.edtStatus);
//        Button btnXacNhan = view.findViewById(R.id.btnXacNhan);
//        Button btnHuy = view.findViewById(R.id.btnHuy);
//
//        // Nếu có URI, hiển thị ngay ảnh trong ImageView
//        if (uri != null) {
//            Glide.with(getContext())
//                    .load(uri)
//                    .placeholder(R.drawable.icon_giohang)
//                    .error(R.drawable.ic_launcher_foreground)
//                    .into(imgProduct);
//        }
//        imgProduct.setOnClickListener(view2 -> {
//            checkOpenLibrary();
//        });
//
//        btnXacNhan.setOnClickListener(view1 -> {
//            String imageUri = uri != null ? uri.toString() : ""; // Chuyển URI thành String
//            String name = edtName.getText().toString();
//            int price = Integer.parseInt(edtPrice.getText().toString());
//            String description = edtDescription.getText().toString();
//            String status = edtStatus.getText().toString();
//
//            // Tạo đối tượng Product với imageUri là một phần tử trong danh sách
//
//            if (imageUri.isEmpty() || name.isEmpty() || price == 0 || description.isEmpty() || status.isEmpty()) {
//                Toast.makeText(getContext(), "Vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e("AddProduct", "Image: " + imageUri);
//                Log.e("AddProduct", "Name: " + name);
//                Log.e("AddProduct", "Price: " + price);
//                Log.e("AddProduct", "Description: " + description);
//                Log.e("AddProduct", "Status: " + status);
//
//                List<String> images = new ArrayList<>();
//                images.add(imageUri); // Thêm URI ảnh vào danh sách (bạn có thể thêm nhiều ảnh nếu cần)
//                Product product = new Product(images, name, price, description, status);
//                requestHttp.getApiService().addProduct(product).enqueue(addProduct);
//                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
//                productList.add(product);
//                dialog.dismiss();
//            }
//        });
//
//        btnHuy.setOnClickListener(view1 -> dialog.dismiss());
//
//        dialog.show();
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openLibrary();
//            } else {
//                Toast.makeText(getContext(), "Bạn cần cấp quyền để truy cập thư viện", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
//            Uri uri = data.getData();
//            if (uri != null) {
//                // Hiển thị dialog và đổ ảnh ngay lập tức
//                showDialogAddProduct(uri);
//            }
//        }
//    }
//
//    public void openLibrary() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, 100);
//    }
//
//    public void checkOpenLibrary() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (getContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                openLibrary();
//            } else {
//                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            }
//        } else {
//            openLibrary();
//        }
//    }
}