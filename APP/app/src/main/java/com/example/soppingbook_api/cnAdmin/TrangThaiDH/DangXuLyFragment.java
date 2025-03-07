package com.example.soppingbook_api.cnAdmin.TrangThaiDH;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterStatusDangXuLy;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.models.StatusProduct;
import com.example.soppingbook_api.server.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangXuLyFragment extends Fragment {

    private RecyclerView rcvStatusProduct;
    private List<StatusProduct> listStatusProduct = new ArrayList<>();
    private HttpRequest httpRequest = new HttpRequest();
    private AdapterStatusDangXuLy adapterStatusDangXuLy;

    public DangXuLyFragment() {
        // Required empty public constructor
    }

    public static DangXuLyFragment newInstance() {
        return new DangXuLyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_xu_ly, container, false);

        rcvStatusProduct = view.findViewById(R.id.rcvStatusProduct);
        adapterStatusDangXuLy = new AdapterStatusDangXuLy();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcvStatusProduct.setLayoutManager(layoutManager);

        // Lấy sản phẩm theo trạng thái "Chờ xử lý"
        fetchStatusProductsByStatus("Chờ xử lý");

        adapterStatusDangXuLy.setOnItemClickListener(new AdapterStatusDangXuLy.OnItemClickListener() {
            @Override
            public void onUpdatestatus(String id, String status) {
                showDialogUpdateStatus(id, status);
            }
        });

        return view;
    }

    private void fetchStatusProductsByStatus(String status) {
        httpRequest.getApiService().getStatusProductsByStatus(status).enqueue(new Callback<ResponeData<List<StatusProduct>>>() {
            @Override
            public void onResponse(Call<ResponeData<List<StatusProduct>>> call, Response<ResponeData<List<StatusProduct>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponeData<List<StatusProduct>> responeData = response.body();
                    if (responeData.getStatus() == 200) {
                        listStatusProduct = responeData.getData();

                        // Lấy chi tiết sản phẩm
                        for (StatusProduct statusProduct : listStatusProduct) {
                            getProductDetailsById(statusProduct.getIdProduct(), statusProduct);
                        }

                        // Cập nhật danh sách lên RecyclerView
                        adapterStatusDangXuLy.setData(listStatusProduct);
                        rcvStatusProduct.setAdapter(adapterStatusDangXuLy);
                    } else {
                        Log.e("DangXuLyFragment", "Error: " + responeData.getMessage());
                    }
                } else {
                    Log.e("DangXuLyFragment", "Failed to fetch products by status: Response unsuccessful.");
                }
            }

            @Override
            public void onFailure(Call<ResponeData<List<StatusProduct>>> call, Throwable t) {
                Log.e("DangXuLyFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    private void getProductDetailsById(String idProduct, StatusProduct statusProduct) {
        httpRequest.getApiService().getProductId(idProduct).enqueue(new Callback<ResponeData<Product>>() {
            @Override
            public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponeData<Product> responeData = response.body();
                    if (responeData.getStatus() == 200) {
                        Product product = responeData.getData();
                        statusProduct.setProductDetail(product);
                        Log.d("DangXuLyFragment", "Fetched product details for ID: " + idProduct);

                        // Cập nhật RecyclerView
                        adapterStatusDangXuLy.notifyDataSetChanged();
                    } else {
                        Log.e("DangXuLyFragment", "Error: " + responeData.getMessage());
                    }
                } else {
                    Log.e("DangXuLyFragment", "Failed to fetch product details: Response unsuccessful.");
                }
            }

            @Override
            public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
                Log.e("DangXuLyFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    public void showDialogUpdateStatus(String id, String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.layout_item_crud_status_product, null);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        RadioButton rdoDangXuLy, rdoDangGiaoHang, rdoDaHuy;
        Button btnXacNhan, btnHuy;
        rdoDangXuLy = view.findViewById(R.id.rdoDangXuLy);
        rdoDangGiaoHang = view.findViewById(R.id.rdoDangGiaoHang);
        rdoDaHuy = view.findViewById(R.id.rdoDaHuy);
        btnXacNhan = view.findViewById(R.id.btnXacNhan);
        btnHuy = view.findViewById(R.id.btnHuy);

        // Đặt trạng thái mặc định cho các RadioButton dựa trên trạng thái hiện tại
        switch (status) {
            case "Chờ xử lý":
                rdoDangXuLy.setChecked(true);
                break;
            case "Đã giao hàng":
                rdoDangGiaoHang.setChecked(true);
                break;
            case "Đã hủy":
                rdoDaHuy.setChecked(true);
                break;
            default:
                // Nếu không có trạng thái hợp lệ thì không thay đổi gì
                break;
        }

        // Handle confirmation button click
        btnXacNhan.setOnClickListener(view1 -> {
            String newStatus = "";
            if (rdoDangXuLy.isChecked()) {
                newStatus = "Chờ xử lý";
            } else if (rdoDangGiaoHang.isChecked()) {
                newStatus = "Đã giao hàng";
            } else if (rdoDaHuy.isChecked()) {
                newStatus = "Đã hủy";
            }

            // Nếu trạng thái không thay đổi, không gửi yêu cầu cập nhật
            if (!newStatus.equals(status)) {
                // Tạo đối tượng StatusProduct với id và trạng thái mới
                StatusProduct statusProduct = new StatusProduct(id, newStatus);

                // Gọi API để cập nhật trạng thái
                httpRequest.getApiService().updateStatusProduct(id, statusProduct).enqueue(new Callback<ResponeData<StatusProduct>>() {
                    @Override
                    public void onResponse(Call<ResponeData<StatusProduct>> call, Response<ResponeData<StatusProduct>> response) {
                        if (response.isSuccessful()) {
                            ResponeData<StatusProduct> responeData = response.body();
                            if (responeData.getStatus() == 200) {
                                Toast.makeText(getContext(), "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                                Log.e("showDialogUpdateStatus", "Error: " + responeData.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeData<StatusProduct>> call, Throwable t) {
                        Log.e("showDialogUpdateStatus", "Error: " + t.getMessage());
                    }
                });
            } else {
                Toast.makeText(getContext(), "Trạng thái không thay đổi", Toast.LENGTH_SHORT).show();
            }

            // Dismiss the dialog after the action
            dialog.dismiss();
        });

        // Handle cancel button click
        btnHuy.setOnClickListener(view1 -> dialog.dismiss());
    }
}
