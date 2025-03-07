package com.example.soppingbook_api.cnAdmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterProductFivorite;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ProductFivorite;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.server.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFivoriteFragment extends Fragment {

    private RecyclerView rcvProductFivorite;
    private String idProduct = "";
    private HttpRequest httpRequest = new HttpRequest();
    private AdapterProductFivorite adapterProductFivorite;
    private List<Product> listProduct = new ArrayList<>();

    public ProductFivoriteFragment() {
        // Required empty public constructor
    }

    public static ProductFivoriteFragment newInstance() {
        ProductFivoriteFragment fragment = new ProductFivoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_fivorite, container, false);
        rcvProductFivorite = view.findViewById(R.id.rcvProductFivorite);
        adapterProductFivorite = new AdapterProductFivorite();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcvProductFivorite.setLayoutManager(layoutManager);
        rcvProductFivorite.setAdapter(adapterProductFivorite);

        adapterProductFivorite.setOnClickItemListener(new AdapterProductFivorite.OnClickImageListener() {
            @Override
            public void onDeleteClick(String id) {
//                deleteProductFivorite(id);
                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddFivotireClick(String idPriduct) {

            }
        });


        httpRequest.getApiService().getAllProductFivorite().enqueue(getAllProductFivorite);
        Log.e("TAG", "onResponse: " + idProduct );
        return view;
    }

    Callback<ResponeData<List<ProductFivorite>>> getAllProductFivorite = new Callback<ResponeData<List<ProductFivorite>>>() {
        @Override
        public void onResponse(Call<ResponeData<List<ProductFivorite>>> call, Response<ResponeData<List<ProductFivorite>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    List<ProductFivorite> listProductFivorite = response.body().getData();

                    if (listProductFivorite != null && !listProductFivorite.isEmpty()) {
                        // Lấy tất cả idProduct từ danh sách
                        List<String> idProductList = new ArrayList<>();
                        for (ProductFivorite productFivorite : listProductFivorite) {
                            idProductList.add(productFivorite.getIdProduct());
                        }

                        Log.e("TAG", "Danh sách ID sản phẩm yêu thích: " + idProductList);
                        fetchProductsByIds(idProductList);
                    } else {
                        Log.e("TAG", "Không có sản phẩm yêu thích.");
                    }
                } else {
                    Log.e("TAG", "Lỗi: " + response.body().getMessage());
                }
            } else {
                Log.e("TAG", "Lỗi khi gọi API getAllProductFivorite.");
            }
        }

        @Override
        public void onFailure(Call<ResponeData<List<ProductFivorite>>> call, Throwable t) {
            Log.e("Error", "getAllProductFivorite: " + t.getMessage());
        }
    };

    private void fetchProductsByIds(List<String> idProductList) {
        for (String idProduct : idProductList) {
            httpRequest.getApiService().getProductId(idProduct).enqueue(new Callback<ResponeData<Product>>() {
                @Override
                public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                        Product product = response.body().getData();
                        listProduct.add(product);
                        adapterProductFivorite.setData(listProduct);
                        Log.e("TAG", "Chi tiết sản phẩm: " + product.toString());

                        // Tiếp tục xử lý sản phẩm (ví dụ thêm vào danh sách hiển thị)
                    } else {
                        Log.e("TAG", "Không thể lấy thông tin sản phẩm cho ID: " + idProduct);
                    }
                }

                @Override
                public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
                    Log.e("Error", "Không thể lấy thông tin sản phẩm cho ID " + idProduct + ": " + t.getMessage());
                }
            });
        }
    }

//    private void deleteProductFivorite(String id){
//        httpRequest.getApiService().deleteProductFivorite(id).enqueue(new Callback<ResponeData<ProductFivorite>>() {
//            @Override
//            public void onResponse(Call<ResponeData<ProductFivorite>> call, Response<ResponeData<ProductFivorite>> response) {
//                if (response.isSuccessful()){
//                    if (response.body().getStatus() == 200){
//                        Toast.makeText(getContext(), "Xóa sản phẩm yêu thích thành công", Toast.LENGTH_SHORT).show();
//                        httpRequest.getApiService().getAllProductFivorite().enqueue(getAllProductFivorite);
//                    } else {
//                        Toast.makeText(getContext(), "Xóa sản phẩm yêu thích thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getContext(), "Lỗi id sản phẩm yêu thích" + id, Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponeData<ProductFivorite>> call, Throwable t) {
//                Log.e("Error", "deleteProductFivorite: " + t.getMessage());
//            }
//        });
//    }
}