package com.example.soppingbook_api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soppingbook_api.R;
import com.example.soppingbook_api.models.AcountModel;

import java.util.ArrayList;

public class AdapterAcount extends RecyclerView.Adapter<AdapterAcount.ViewHolder> {
    private Context context;
    private ArrayList<AcountModel> list;

    public AdapterAcount(Context context, ArrayList<AcountModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterAcount.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_acount, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAcount.ViewHolder holder, int position) {
        AcountModel acountModel = list.get(position);
        holder.txtAcountEmail.setText("Email: "+acountModel.getEmail());
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtAcountEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAcountEmail = itemView.findViewById(R.id.txtAcountEmail);
        }
    }
}
