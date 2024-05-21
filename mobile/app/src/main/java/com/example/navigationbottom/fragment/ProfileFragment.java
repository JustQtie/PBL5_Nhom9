package com.example.navigationbottom.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.DetailHistoryProfileActivity;
import com.example.navigationbottom.activity.DetailSettingProfileActivity;
import com.example.navigationbottom.activity.LoginActivity;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.user.ChangePassResponse;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private LinearLayout btnDoiMatKhau, btnDoiThongTin, btnLichSuGiaoDich;
    private TextView tvSoDienThoai,tvHoVaTen, tvGioiTinh, tvDiaChi;
    private AppCompatButton btnLogout;
    private ShapeableImageView imgAnhDaiDien;
    private  Dialog dialog;
    private ProgressDialog progressDialog;
    private User userChuyenDi;
    private UserApiService userApiService;
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



        // Gọi API để lấy thông tin người dùng
        // Khởi tạo UserApiService
        userApiService = new UserApiService(requireContext());

        // Lấy thông tin người dùng từ máy chủ
        getUserProfile();


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

                Intent intent = new Intent(requireActivity(), DetailSettingProfileActivity.class);
                intent.putExtra("userChuyenDi", userChuyenDi);
                startActivity(intent);
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

        tvHoVaTen = mView.findViewById(R.id.tv_fullname_fragment_profile);
        tvSoDienThoai = mView.findViewById(R.id.tv_Sodienthoai_fragment_profile);
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

                ChangePassResponse requestData = new ChangePassResponse();
                requestData.setOldPassword(oldPassword);
                requestData.setNewPassword(newPassword);



                UserApiService apiService = new UserApiService(getActivity());

                // Giả sử bạn có ID người dùng lưu trữ trong Session hoặc một nơi nào đó
                Long userId = UserPreferences.getUser(getContext()).getId();

                // Gọi phương thức changePassword và xử lý kết quả
                apiService.changePassword(userId, requestData).enqueue(new Callback<ChangePassResponse>() {
                    @Override
                    public void onResponse(Call<ChangePassResponse> call, Response<ChangePassResponse> response) {
                        if (response.isSuccessful()) {
                            ChangePassResponse changePasswordResponse = response.body();
                            if (changePasswordResponse != null) {
                                long errorCodeLong = changePasswordResponse.getEc();

                                if (errorCodeLong == -1) {
                                    // Xử lý khi không thể thay đổi mật khẩu
                                    Toast.makeText(getActivity(), "Thay đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Xử lý khi thay đổi mật khẩu thành công (nếu cần)
                                    Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss(); // Đóng dialog sau khi thay đổi mật khẩu thành công
                                }
                            } else {
                                // Xử lý khi không nhận được dữ liệu phản hồi từ server
                                Toast.makeText(getActivity(), "Không nhận được dữ liệu từ máy chủ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Xử lý khi response không thành công
                            Toast.makeText(getActivity(), "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePassResponse> call, Throwable t) {
                        // Xử lý khi gặp lỗi
                        Toast.makeText(getActivity(), "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });


            }
        });

        dialog.show();

    }

    private void getUserProfile() {
        User user = UserPreferences.getUser(getContext());
        if (user != null) {
            userApiService.getUser(user.getId()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                         User currentUser = response.body();
                        if (currentUser != null) {
                            // Hiển thị thông tin người dùng trên giao diện
                            userChuyenDi = currentUser;
                            displayUserProfile(currentUser);
                        } else {
                            // Xử lý khi không có dữ liệu người dùng trả về
                            Toast.makeText(requireContext(), "No user data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Xử lý khi response không thành công
                        Toast.makeText(requireContext(), "Failed to get user profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Xử lý khi gặp lỗi
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Xử lý khi không tìm thấy người dùng đăng nhập
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

    }

    private void displayUserProfile(User user) {
        tvHoVaTen.setText(user.getFullname());
        tvDiaChi.setText(user.getAddress());
        tvSoDienThoai.setText(user.getPhone_number());
        tvGioiTinh.setText(user.getGender()?"Nam":"Nữ");

        try {
            String imageUrl = ApiService.BASE_URL + "api/v1/users/images/" + user.getThumbnail();

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_round_person_24) // Ảnh tạm thời khi đang tải
                        .error(R.drawable.ic_round_person_24) // Ảnh hiển thị khi có lỗi
                        .into(imgAnhDaiDien);
            } else {
                Glide.with(requireContext())
                        .load(R.drawable.ic_round_person_24)
                        .into(imgAnhDaiDien);
            }
        } catch (Exception e) {
            Glide.with(requireContext())
                    .load(R.drawable.ic_round_person_24)
                    .into(imgAnhDaiDien);
        }
    }
}




