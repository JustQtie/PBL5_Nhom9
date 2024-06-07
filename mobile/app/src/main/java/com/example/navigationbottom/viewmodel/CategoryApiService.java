package com.example.navigationbottom.viewmodel;

import android.content.Context;
import android.util.Log;

import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.category.GetCategoryResponse;
import com.example.navigationbottom.response.user.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class CategoryApiService {
    private CategoryApi api;

    private String authToken;

    public CategoryApiService(Context context){
        authToken = SessionManager.getInstance(context).getToken();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + authToken)
                        .build();
                Headers headers = newRequest.headers();
                for (int i = 0, size = headers.size(); i < size; i++) {
                    Log.d("RequestHeader", headers.name(i) + ": " + headers.value(i));
                }
                return chain.proceed(newRequest);
            }
        }).connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        api = new Retrofit.Builder()
                .client(client)
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(CategoryApi.class);
    }

    public Call<GetCategoryResponse> getListCategory(){
        return api.getListCategory();
    }
    public Call<Category> getCategoriesById(Long id){
        return api.getCategoryById(id);
    }

}
