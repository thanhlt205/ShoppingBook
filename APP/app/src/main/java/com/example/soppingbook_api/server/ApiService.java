package com.example.soppingbook_api.server;

import com.example.soppingbook_api.models.Address;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ProductFivorite;
import com.example.soppingbook_api.models.ResponeData;
import com.example.soppingbook_api.models.StatusProduct;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String DATABASE_URL = "http://192.168.137.1:3000/";

    @GET("apiAddress/listAddress")
    Call<ResponeData<List<Address>>> getAllAddress();

    @POST("apiAddress/addAddress")
    Call<ResponeData<List<Address>>> addAddress(@Body Address address);

    @DELETE("apiAddress/deleteAddress/{id}")
    Call<ResponeData<Address>> deleteAddress(@Path("id") String id);

    @PUT("apiAddress/updateAddress/{id}")
    Call<ResponeData<Address>> updateAddress(@Path("id") String id, @Body Address address);

    @GET("apiAddress/getAddressId/{id}")
    Call<ResponeData<Address>> getAddressById(@Path("id") String id);

    // Call API Pruduct
    @GET("apiProduct/listProduct")
    Call<ResponeData<List<Product>>> getAllProduct();

    @GET("apiProduct/search")
    Call<ResponeData<List<Product>>> searchProduct(@Query("key") String key);

    @GET("apiProduct/getProduct/{id}")
    Call<ResponeData<Product>> getProductId(@Path("id") String id);

    @Multipart
    @POST("apiProduct/addProduct")
    Call<ResponeData<Product>> addProduct(@Part("name")RequestBody name,
                                          @Part("price")RequestBody price,
                                          @Part("description")RequestBody description,
                                          @Part("status")RequestBody status,
                                          @Part MultipartBody.Part images);

    @DELETE("apiProduct/deleteProduct/{id}")
    Call<ResponeData<Product>> deleteProduct(@Path("id") String id);

    // Call API StatusProduct
    @POST("apiStatusProduct/addStatusProduct")
    Call<ResponeData<StatusProduct>> addStatusProduct(@Body StatusProduct statusProduct);

    @GET("apiStatusProduct/listStatusProduct")
    Call<ResponeData<List<StatusProduct>>> getAllStatusProduct();

    @GET("/apiStatusProduct/getStatusProducts/{status}")
    Call<ResponeData<List<StatusProduct>>> getStatusProductsByStatus(@Path("status") String status);

    @PUT("apiStatusProduct/updateStatusProduct/{id}")
    Call<ResponeData<StatusProduct>> updateStatusProduct(@Path("id") String id, @Body StatusProduct statusProduct);

    // Call API fivorite
    @POST("apiProductFivorite/addProducFivorite")
    Call<ResponeData<ProductFivorite>> addProducFivorite(@Body ProductFivorite productFivorite);

    @GET("apiProductFivorite/getAllProducFivorite")
    Call<ResponeData<List<ProductFivorite>>> getAllProductFivorite();

    @DELETE("apiProductFivorite/deleteProductFivorite/{id}")
    Call<ResponeData<ProductFivorite>> deleteProductFivorite(@Path("id") String id);


}
