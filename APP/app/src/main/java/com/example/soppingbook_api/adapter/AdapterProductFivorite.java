package com.example.soppingbook_api.adapter;

import android.content.Intent;
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
import com.example.soppingbook_api.cnUser.DetailProductActivity;
import com.example.soppingbook_api.models.Product;
import com.example.soppingbook_api.models.ProductFivorite;

import java.util.List;

public class AdapterProductFivorite extends RecyclerView.Adapter<AdapterProductFivorite.ViewHolder> {
    List<Product> productArrayList;

    private OnClickImageListener clickImage;

    public interface OnClickImageListener {

        void onDeleteClick(String id);

        void onAddFivotireClick(String idPriduct);

    }

    public void setOnClickItemListener(OnClickImageListener clickImageListener) {
        this.clickImage = clickImageListener;
    }

    public void setData(List<Product> productArrayList) {
        this.productArrayList = productArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterProductFivorite.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rcv_giohang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductFivorite.ViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        if (product == null) {
            return;
        }
        Glide.with(holder.itemView.getContext())
                .load("http://" + product.getImages().get(0))
                .placeholder(R.drawable.icon_giohang)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgAnhSach);
        holder.txtTenSach.setText(product.getName());
        holder.txtGiaSach.setText("Giá: " + product.getPrice() + "đ");

        holder.itemView.setOnClickListener(view -> {
            String id = product.getId();
            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.setClass(holder.itemView.getContext(), DetailProductActivity.class);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.imgDeleteProduct.setOnClickListener(view -> {
            clickImage.onDeleteClick(product.getId());
        });
    }

    @Override
    public int getItemCount() {
        if (productArrayList != null) {
            return productArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnhSach, imgDeleteProduct;
        TextView txtTenSach, txtGiaSach;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhSach = itemView.findViewById(R.id.imgAnhSach);
            imgDeleteProduct = itemView.findViewById(R.id.imgDeleteProduct);
            txtTenSach = itemView.findViewById(R.id.txtTenSach);
            txtGiaSach = itemView.findViewById(R.id.txtGiaSach);
        }
    }
}
