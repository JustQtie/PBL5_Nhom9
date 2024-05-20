package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.response.book.BookImageResponse;
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.response.book.BookResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApi {
    @POST("api/v1/products")
    Call<BookResponse> postBook(@Body Book requestData);

    @Multipart
    @POST("api/v1/products/uploads/{id}")
    Call<BookImageResponse> uploadFiles(
            @Path("id") Long productId,
            @Part List<MultipartBody.Part> files
    );

    @POST("api/v1/products/byuser/{id}")
    Call<GetBookResponse> getAllBookByUser(@Path("id") Long userId);

    @POST("api/v1/products/bynotuser/{id}")
    Call<GetBookResponse> getBooksNotUser(@Path("id") Long userId);

    @GET("api/v1/products")
    Call<GetBookResponse> getAllBook();

    @GET("api/v1/products")
    Call<GetBookResponse> getAllBook(@Query("keyword") String keyword);

    @GET("api/v1/products/{id}")
    Call<Book> getBookById(@Path("id") Long id);

    @DELETE("api/v1/products/{id}")
    Call<ResponseBody> deleteBook(@Path("id") Long id);

    @PUT("api/v1/products/{id}")
    Call<BookResponse> updateBook(@Path("id") Long id, @Body Book requestData);

    @DELETE("api/v1/products/thumbnails/{id}")
    Call<ResponseBody> deleteBookImgae(@Path("id") Long id);
}
