package com.example.soppingbook_api.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soppingbook_api.R;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.StatusProduct;

import java.util.List;

public class AdapterStatusHuyBo extends RecyclerView.Adapter<AdapterStatusHuyBo.ViewHolder> {
    List<StatusProduct> statusProductArrayList;

    public void setData(List<StatusProduct> statusProductArrayList) {
        this.statusProductArrayList = statusProductArrayList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdapterStatusHuyBo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rcv_status_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStatusHuyBo.ViewHolder holder, int position) {
        StatusProduct statusProduct = statusProductArrayList.get(position);
        if (statusProduct == null) {
            return;
        }
//        if ((statusProduct.getStatus()).equals("Chờ xử lý")) {
            holder.txtSoTheoDoiProductStatus.setText("Số theo dõi: " + statusProduct.getIdProduct());
            holder.txtTongTienProductStatus.setText("Tổng tiền: " + statusProduct.getPriceProduct() + "đ");
            holder.txtStatusProductStatus.setText(statusProduct.getStatus());
            holder.txtStatusProductStatus.setTextColor(Color.RED);

            Product product = statusProduct.getProduct();
            if (product != null) {
                holder.txtNameProductStatus.setText(product.getName());
                holder.txtGiaProductStatus.setText("Giá: " + product.getPrice() + "đ");

                Log.e("img", "imgStatusProduct: " + product.getImages());
                if (product.getImages() != null && !product.getImages().isEmpty()) {
                    Glide.with(holder.itemView.getContext())
                            .load("http://" + product.getImages().get(0))
                            .error(R.drawable.ic_launcher_foreground)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(holder.imgStatusProduct);
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Ảnh null", Toast.LENGTH_SHORT).show();
                }
//            }
        }
    }

    @Override
    public int getItemCount() {
        if (statusProductArrayList != null) {
//            int count = 0;
//            for (StatusProduct statusProduct : statusProductArrayList) {
//                if (statusProduct.getStatus().equals("Chờ xử lý")) {
//                    count++;
//                }
//            }
//            return count;
            return statusProductArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgStatusProduct;
        TextView txtSoTheoDoiProductStatus, txtGiaProductStatus, txtTongTienProductStatus, txtStatusProductStatus, txtNameProductStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStatusProduct = itemView.findViewById(R.id.imgProductStatus);
            txtSoTheoDoiProductStatus = itemView.findViewById(R.id.txtSoTheoDoiProductStatus);
            txtGiaProductStatus = itemView.findViewById(R.id.txtGiaProductStatus);
            txtTongTienProductStatus = itemView.findViewById(R.id.txtTongTienProductStatus);
            txtStatusProductStatus = itemView.findViewById(R.id.txtStatusProductStatus);
            txtNameProductStatus = itemView.findViewById(R.id.txtNameProductStatus);
        }
    }
}
