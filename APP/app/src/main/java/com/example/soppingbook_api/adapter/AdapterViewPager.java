package com.example.soppingbook_api.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.soppingbook_api.cnAdmin.TrangThaiDH.DaGiaoHangFragment;
import com.example.soppingbook_api.cnAdmin.TrangThaiDH.DangXuLyFragment;
import com.example.soppingbook_api.cnAdmin.TrangThaiDH.HuyBoFragment;

public class AdapterViewPager  extends FragmentStateAdapter {
    public AdapterViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DangXuLyFragment(); // Fragment hiển thị đơn hàng đang xử lý
            case 1:
                return new DaGiaoHangFragment(); // Fragment hiển thị đơn hàng đã giao
            case 2:
                return new HuyBoFragment(); // Fragment hiển thị đơn hàng bị hủy
            default:
                return new DangXuLyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
