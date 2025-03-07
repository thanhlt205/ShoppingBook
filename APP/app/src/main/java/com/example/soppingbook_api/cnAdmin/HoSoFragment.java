package com.example.soppingbook_api.cnAdmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soppingbook_api.R;
import com.example.soppingbook_api.cnUser.DiaChiActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HoSoFragment extends Fragment {
    Button btnDanhGia, btnDiaChi, btnThanhToan, btnShowAcount;
    TextView txtNameAcount;
    ConstraintLayout consViewShowAcount;
    private FirebaseAuth mAuth;
    public HoSoFragment() {
        // Required empty public constructor
    }

    public static HoSoFragment newInstance() {
        HoSoFragment fragment = new HoSoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ho_so, container, false);

        btnDanhGia = view.findViewById(R.id.btnDanhGia);
        btnDiaChi = view.findViewById(R.id.btnDiaChi);
        btnThanhToan = view.findViewById(R.id.btnThanhToan);
        txtNameAcount = view.findViewById(R.id.txtNameAcount);
        consViewShowAcount = view.findViewById(R.id.consViewShowAcount);
        btnShowAcount = view.findViewById(R.id.btnShowAcount);

        String name = mAuth.getCurrentUser().getEmail().trim();
        txtNameAcount.setText("chào! "+name);
        if(name.equals("admin@gmail.com")){
            consViewShowAcount.setVisibility(View.VISIBLE);
        } else {
            consViewShowAcount.setVisibility(View.GONE);
        }
        btnDanhGia.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DieuKhoanActivity.class));
        });
        btnDiaChi.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DiaChiActivity.class));
        });
        btnThanhToan.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VocherActivity.class));
        });
        btnShowAcount.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ShowAcountActivity.class));
        });
        return view;
    }
}