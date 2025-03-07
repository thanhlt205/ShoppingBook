package com.example.soppingbook_api.cnAdmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soppingbook_api.R;
import com.example.soppingbook_api.adapter.AdapterViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DonHangFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SwipeRefreshLayout swipeRefreshLayout;

    public DonHangFragment() {
        // Required empty public constructor
    }

    public static DonHangFragment newInstance() {
        DonHangFragment fragment = new DonHangFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don_hang, container, false);

        // Khởi tạo TabLayout và ViewPager2
         tabLayout = view.findViewById(R.id.tab_layout);
         viewPager = view.findViewById(R.id.view_pager);
         swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

         swipeRefreshLayout.setOnRefreshListener( () -> {
             swipeRefreshLayout.setRefreshing(false);
         });

        // Gán Adapter cho ViewPager2
        AdapterViewPager adapter = new AdapterViewPager(getActivity());
        viewPager.setAdapter(adapter);

        // Cho phép vuốt qua các tab
        viewPager.setUserInputEnabled(true);

        // Liên kết TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Đang xử lý");
                    break;
                case 1:
                    tab.setText("Đã giao hàng");
                    break;
                case 2:
                    tab.setText("Hủy bỏ");
                    break;
            }
        }).attach();

        return view;
    }

}