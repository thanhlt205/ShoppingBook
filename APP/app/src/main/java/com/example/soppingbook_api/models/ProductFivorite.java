package com.example.soppingbook_api.models;

import com.google.gson.annotations.SerializedName;

public class ProductFivorite {
    @SerializedName("_id")
    private String id;
    private String idProduct;

    public ProductFivorite() {
    }

    public ProductFivorite(String id, String idProduct) {
        this.id = id;
        this.idProduct = idProduct;
    }

    public ProductFivorite(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public String toString() {
        return "ProductFivorite{" +
                "id='" + id + '\'' +
                ", idProduct='" + idProduct + '\'' +
                '}';
    }
}
