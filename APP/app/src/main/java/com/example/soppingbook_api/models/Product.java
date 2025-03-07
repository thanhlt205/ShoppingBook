package com.example.soppingbook_api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("_id")
    String id;
    @SerializedName("images")
    List<String> images;
    String name;
    int price;
    String description;
    String status;

    public Product() {
    }

    public Product(List<String> images, String name, int price, String description, String status) {
        this.images = images;
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
    }

    public Product(String id, List<String> images, String name, int price, String description, String status) {
        this.id = id;
        this.images = images;
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", images=" + images +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
