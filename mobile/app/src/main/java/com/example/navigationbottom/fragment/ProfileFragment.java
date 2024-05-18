package com.example.navigationbottom.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.DetailHistoryProfileActivity;
import com.example.navigationbottom.activity.DetailSettingProfileActivity;
import com.example.navigationbottom.activity.LoginActivity;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {
    private LinearLayout btnDoiMatKhau, btnDoiThongTin, btnLichSuGiaoDich;
    private TextView tvSoDienThoai,tvNgaySinh, tvGioiTinh, tvDiaChi;
    private AppCompatButton btnLogout;
    private ShapeableImageView imgAnhDaiDien;
    private  Dialog dialog;

    private View mView;
    public ProfileFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI();


        // xu ly
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialogDoiMatKhau(Gravity.CENTER);
            }
        });


        btnDoiThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), DetailSettingProfileActivity.class));
            }
        });

        btnLichSuGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), DetailHistoryProfileActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });


        return mView;
    }

    private void initUI() {
        btnDoiMatKhau = mView.findViewById(R.id.linearLayout_doimatkhau_fragment_profile);
        btnDoiThongTin = mView.findViewById(R.id.linearLayout_Thaydoithongtin_fragment_profile);
        btnLichSuGiaoDich = mView.findViewById(R.id.linearLayout_lichsugiaodich_fragment_profile);
        btnLogout = mView.findViewById(R.id.btn_logout_fragment_profile);

        tvSoDienThoai = mView.findViewById(R.id.tv_Sodienthoai_fragment_profile);
        tvNgaySinh = mView.findViewById(R.id.tv_ngaysinh_fragment_profile);
        tvGioiTinh = mView.findViewById(R.id.tv_gioitinh_fragment_profile);
        tvDiaChi = mView.findViewById(R.id.tv_diachi_fragment_profile);
        imgAnhDaiDien = mView.findViewById(R.id.si_anh_avata_fragment_profile);
    }


    private void showCustomAlertDialogDoiMatKhau(int gravity) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout_doimk);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        //  cai dat thuoc tinh o day

        EditText edtMatKhauCu = dialog.findViewById(R.id.edt_matkhaucu_doimatkhau_dialog_doimk);
        EditText edtMatKhauMoi = dialog.findViewById(R.id.edt_matkhaumoi_doimatkhau_dialog_doimk);
        EditText edtNhapLaiMatKhauMoi = dialog.findViewById(R.id.edt_nhapLaiMatKhau_doimatkhau_dialog_doimk);

        AppCompatButton btnDoiMatKhau = dialog.findViewById(R.id.btn_dialog_xacnhan);

        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khi nhan thi se lay du lieu do textView để thay đổi mat khau

            }
        });



        dialog.show();

    }
}