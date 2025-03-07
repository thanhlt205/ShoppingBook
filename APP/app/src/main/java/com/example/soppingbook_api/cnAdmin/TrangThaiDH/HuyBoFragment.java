package com.example.soppingbook_api.cnAdmin.TrangThaiDH;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterStatusHuyBo;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.models.StatusProduct;
import com.example.soppingbook_api.server.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HuyBoFragment extends Fragment {

    private RecyclerView rcvStatusProduct;
    private List<StatusProduct> listStatusProduct = new ArrayList<>();
    private HttpRequest httpRequest = new HttpRequest();
    private AdapterStatusHuyBo adapterStatusHuyBo;

    public HuyBoFragment() {
        // Required empty public constructor
    }

    public static HuyBoFragment newInstance() {
        return new HuyBoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_huy_bo, container, false);

        // Initializing RecyclerView
        rcvStatusProduct = view.findViewById(R.id.rcvStatusProduct);
        adapterStatusHuyBo = new AdapterStatusHuyBo();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcvStatusProduct.setLayoutManager(layoutManager);

        // Fetching data for "Hủy bỏ" status
        fetchStatusProductsByStatus("Đã hủy");

        return view;
    }

    private void fetchStatusProductsByStatus(String status) {
        // Fetch products by status "Hủy bỏ"
        httpRequest.getApiService()
                .getStatusProductsByStatus(status)
                .enqueue(new Callback<ResponeData<List<StatusProduct>>>() {
                    @Override
                    public void onResponse(Call<ResponeData<List<StatusProduct>>> call, Response<ResponeData<List<StatusProduct>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponeData<List<StatusProduct>> responeData = response.body();
                            if (responeData.getStatus() == 200) {
                                listStatusProduct = responeData.getData();

                                // Fetch product details for each status product
                                for (StatusProduct statusProduct : listStatusProduct) {
                                    getProductDetailsById(statusProduct.getIdProduct(), statusProduct);
                                }

                                // Update RecyclerView with fetched data
                                adapterStatusHuyBo.setData(listStatusProduct);
                                rcvStatusProduct.setAdapter(adapterStatusHuyBo);
                            } else {
                                Log.e("HuyBoFragment", "Error: " + responeData.getMessage());
                            }
                        } else {
                            Log.e("HuyBoFragment", "Failed to fetch products by status: Response unsuccessful.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeData<List<StatusProduct>>> call, Throwable t) {
                        Log.e("HuyBoFragment", "API call failed: " + t.getMessage());
                    }
                });
    }

    private void getProductDetailsById(String idProduct, StatusProduct statusProduct) {
        // Fetch product details by ID
        httpRequest.getApiService()
                .getProductId(idProduct)
                .enqueue(new Callback<ResponeData<Product>>() {
                    @Override
                    public void onResponse(Call<ResponeData<Product>> call, Response<ResponeData<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponeData<Product> responeData = response.body();
                            if (responeData.getStatus() == 200) {
                                Product product = responeData.getData();
                                statusProduct.setProductDetail(product);
                                Log.d("HuyBoFragment", "Fetched product details for ID: " + idProduct);

                                // Update RecyclerView after fetching product details
                                adapterStatusHuyBo.notifyDataSetChanged();
                            } else {
                                Log.e("HuyBoFragment", "Error: " + responeData.getMessage());
                            }
                        } else {
                            Log.e("HuyBoFragment", "Failed to fetch product details: Response unsuccessful.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeData<Product>> call, Throwable t) {
                        Log.e("HuyBoFragment", "API call failed: " + t.getMessage());
                    }
                });
    }

}
