package com.example.navigationbottom.viewmodel;

import android.content.Context;
import android.util.Log;

import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.response.book.BookImageResponse;
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.response.book.BookResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class BookApiService {
    private BookApi api;
    private String authToken;

    public BookApiService(Context context){
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
        })      .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        api = new Retrofit.Builder()
                .client(client)
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(BookApi.class);
    }

    public Call<BookResponse> postBook(@Body Book requestData){
        Log.d("RequestData", new Gson().toJson(requestData));
        return api.postBook(requestData);
    }

    public Call<BookImageResponse> uploadFileImage(Long id, List<MultipartBody.Part> files){
        Log.d("RequestData", files.toString());
        return api.uploadFiles(id, files);
    }

    public Call<GetBookResponse> getBookByUserId(Long id){
        return api.getAllBookByUser(id);
    }

    public Call<GetBookResponse> getBooksNotUser(Long id){
        return api.getBooksNotUser(id);
    }

    public Call<GetBookResponse> getAllBook(){
        return api.getAllBook();
    }

    public Call<GetBookResponse> getAllBook(String key){
        return api.getAllBook();
    }

    public Call<Book> getBookById(Long id){
        return api.getBookById(id);
    }

    public Call<ResponseBody> deleteBook(Long id){
        return api.deleteBook(id);
    }

    public Call<BookResponse> updateBook(Long id, @Body Book requestData){
        Log.d("RequestData", new Gson().toJson(requestData));
        return api.updateBook(id, requestData);
    }

    public Call<ResponseBody> deleteBookThumbnail(Long id){
        return api.deleteBookImgae(id);
    }

}
