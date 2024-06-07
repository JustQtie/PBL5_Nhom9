package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.SliderAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.model.SliderData;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.BookImageApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.example.navigationbottom.viewmodel.OrderApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemHistoryActivity extends AppCompatActivity {

    private TextView loai, tieude, gia, tacGia, nguoiBan, diaChiNhanHang, soLuong, phuongThucThanhToan, tongTienThanhToan, soDienThoaiNguoiBan;
    private ImageView back;

    private CategoryApiService categoryApiService;

    private UserApiService userApiService;

    List<String> imageUrlString = new ArrayList<>();

    private BookImageApiService bookImageApiService;

    private Order order;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);

        initComponent();

        categoryApiService = new CategoryApiService(this);
        bookImageApiService = new BookImageApiService(this);
        userApiService = new UserApiService(this);

        order =  (Order) getIntent().getSerializableExtra("order");
        book =  (Book) getIntent().getSerializableExtra("book");

        if(order != null && book != null){
            categoryApiService.getCategoriesById(book.getCategory_id()).enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    Category category = response.body();
                    if(category != null){
                        loai.setText(category.getName());
                    }
                }
                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Log.e("Error", "Not load category");
                }
            });
            tieude.setText(book.getName());
            gia.setText(book.getPrice().toString()+" VND");
            tacGia.setText(book.getAuthor());
            diaChiNhanHang.setText(order.getShipping_address());
            soLuong.setText(String.valueOf(order.getNumber_of_product()));
            phuongThucThanhToan.setText(order.getPayment_method());
            tongTienThanhToan.setText(String.valueOf(order.getTotal_money()));
            userApiService.getUser(book.getUser_id()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if(user.getEc().equals("0")){
                        nguoiBan.setText(user.getFullname());
                        soDienThoaiNguoiBan.setText(user.getPhone_number());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Error", "Not load user");
                }
            });
        }

        fillImageSlide();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void fillImageSlide(){
        // Chuyển đổi danh sách Uri thành danh sách đường dẫn chuỗi
        bookImageApiService = new BookImageApiService(this);
        bookImageApiService.getThumbnailsByProductId(book.getId()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    imageUrlString = response.body();
                    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

                    // initializing the slider view.
                    SliderView sliderView = findViewById(R.id.slider);

                    // adding the urls inside array list
                    for (String path : imageUrlString) {
                        path = ApiService.BASE_URL + "api/v1/products/images/" + path;
                        sliderDataArrayList.add(new SliderData(path));
                    }

                    // passing this array list inside our adapter class.
                    SliderAdapter adapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);

                    // below method is used to set auto cycle direction in left to
                    // right direction you can change according to requirement.
                    sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                    // below method is used to
                    // setadapter to sliderview.
                    sliderView.setSliderAdapter(adapter);

                    // below method is use to set
                    // scroll time in seconds.
                    sliderView.setScrollTimeInSec(3);

                    // to set it scrollable automatically
                    // we use below method.
                    sliderView.setAutoCycle(true);

                    // to start autocycle below method is used.
                    sliderView.startAutoCycle();
                }else {
                    Log.e("Hello", String.valueOf("Response not success"));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toasty.error(ItemHistoryActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
    }

    private void initComponent() {
        loai = findViewById(R.id.tv_itemHistory_loai);
        tieude = findViewById(R.id.tv_tieuDe_itemHistory);
        gia = findViewById(R.id.tv_gia_itemHistory);
        tacGia = findViewById(R.id.tv_tacGia_itemHistory);
        nguoiBan = findViewById(R.id.tv_nguoiBan_itemHistory);
        diaChiNhanHang = findViewById(R.id.tv_diaChiNhanHang_itemHistory);
        soLuong = findViewById(R.id.tv_soLuong_itemHistory);
        phuongThucThanhToan = findViewById(R.id.tv_phuongThucThanhToan_itemHistory);
        tongTienThanhToan = findViewById(R.id.tv_tongTien_itemHistory);
        soDienThoaiNguoiBan = findViewById(R.id.tv_soDienThoaiNguoiBan_itemHistory);
        back = findViewById(R.id.iv_back_itemHistory);

    }
}