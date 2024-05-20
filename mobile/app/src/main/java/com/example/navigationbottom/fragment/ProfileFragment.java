package com.example.navigationbottom.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.DetailHistoryProfileActivity;
import com.example.navigationbottom.activity.DetailSettingProfileActivity;
import com.example.navigationbottom.activity.LoginActivity;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.user.ChangePasswordResponse;
import com.example.navigationbottom.viewmodel.SessionManager;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.android.material.imageview.ShapeableImageView;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private LinearLayout btnDoiMatKhau, btnDoiThongTin, btnLichSuGiaoDich;
    private TextView tvSoDienThoai,tvNgaySinh, tvGioiTinh, tvDiaChi;
    private AppCompatButton btnLogout;
    private ShapeableImageView imgAnhDaiDien;
    private  Dialog dialog;
    private ProgressDialog progressDialog;
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
                String oldPassword = edtMatKhauCu.getText().toString();
                String newPassword = edtMatKhauMoi.getText().toString();
                String confirmPassword = edtNhapLaiMatKhauMoi.getText().toString();

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User requestData = new User();
                requestData.setPassword(oldPassword);
                requestData.setRetype_password(newPassword);

                UserApiService apiService = new UserApiService(getActivity());

                // Giả sử bạn có ID người dùng lưu trữ trong Session hoặc một nơi nào đó
//                Long userId = SessionManager.getInstance(getActivity()).getUserId();

//                apiService.changePassword(userId, requestData).enqueue(new Callback<ChangePasswordResponse>() {
//
//                    @Override
//                    public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
//                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
//                            Toast.makeText(getActivity(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        } else {
//                            Toast.makeText(getActivity(), "Đổi mật khẩu thất bại: " + (response.body() != null ? response.body().getMessage() : "Lỗi không xác định"), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
//                        Toast.makeText(getActivity(), "Đổi mật khẩu thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

            dialog.show();



            }
        });



    }
}