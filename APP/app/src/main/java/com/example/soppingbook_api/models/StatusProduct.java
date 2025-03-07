package com.example.soppingbook_api.models;

import com.google.gson.annotations.SerializedName;

public class StatusProduct {
    @SerializedName("_id")
    String id;
    String idProduct;
    String priceProduct;
    String status;

    Product product;

    public StatusProduct() {
    }

    public StatusProduct(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public StatusProduct(String id, String idProduct, String priceProduct, String status) {
        this.id = id;
        this.idProduct = idProduct;
        this.priceProduct = priceProduct;
        this.status = status;
    }

    public StatusProduct(String idProduct, String priceProduct, String status) {
        this.idProduct = idProduct;
        this.priceProduct = priceProduct;
        this.status = status;
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

    public String getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(String priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusProduct{" +
                "id='" + id + '\'' +
                ", idProduct='" + idProduct + '\'' +
                ", priceProduct='" + priceProduct + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Product getProduct() {
        return product;
    }

    public void setProductDetail(Product product) {
        this.product = product;
    }
}
