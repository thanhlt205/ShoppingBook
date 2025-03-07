package com.example.soppingbook_api.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soppingbook_api.R;
import com.example.soppingbook_api.cnUser.DetailProductActivity;
import com.example.soppingbook_api.cnUser.DiaChiActivity;
import com.example.soppingbook_api.cnUser.PayActivity;
import com.example.soppingbook_api.models.Address;

import java.util.List;

public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.ViewHolder> {
    private List<Address> addressArrayList;

    private PutGetListener listener;

    public interface PutGetListener {
        void putAddress(String idAddress);

        void putProduct();
    }

    public void setListener(PutGetListener listener) {
        this.listener = listener;
    }

    private OnClickItemListener clickItemListener;

    public interface OnClickItemListener {

        void onDeleteLongClick(String id);

        void onUpdateClick(String id, String name, String phone, String address);

    }

    public void setOnClickItemListener(OnClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    public void setData(List<Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterAddress.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_diachi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAddress.ViewHolder holder, int position) {
        Address address = addressArrayList.get(position);
        if (address == null) {
            Log.e("LOG", "address is null: " + addressArrayList.size());
            return;
        }
        holder.txtName.setText("Họ tên: " + address.getName());
        holder.txtPhone.setText("SĐT: " + address.getPhone());
        holder.txtAddress.setText(address.getAddress());

        holder.itemView.setOnLongClickListener(v -> {
            clickItemListener.onDeleteLongClick(address.getId());
            return false;
        });
        holder.txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemListener.onUpdateClick(address.getId(), address.getName(), address.getPhone(), address.getAddress());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("idAddress", address.getId());
                Log.e("AdapterAddress", "idAddress: " + address.getId());
                ((Activity) view.getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) view.getContext()).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (addressArrayList != null) {
            return addressArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone, txtAddress, txtUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtUpdate = itemView.findViewById(R.id.txtUpdate);
        }
    }
}
