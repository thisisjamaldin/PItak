package com.nextinnovation.pitak.data;

import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.post.FavouritePostResponse;
import com.nextinnovation.pitak.model.post.PostCreate;
import com.nextinnovation.pitak.model.post.PostResponse;
import com.nextinnovation.pitak.model.post.PostSearch;
import com.nextinnovation.pitak.model.user.ProfileRequest;
import com.nextinnovation.pitak.model.user.ProfileResponse;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMainRepository {

    @POST("api/auth/signup")
    Call<User> signUp(@Body User user);

    @POST("api/auth/signin")
    Call<UserWhenSignedIn> signIn(@Body UserSignIn userSignIn);

    @POST("api/user/upload/profile/photo")
    Call<ProfileResponse> setUserProfile(@Body ProfileRequest profileRequest, @Header("Authorization") String token);

    @POST("api/user/driver/edit")
    Call<Void> editDriver(@Body UserWhenSignedIn userWhenSignedIn, @Header("Authorization") String token);

    @POST("api/user/passenger/edit")
    Call<Void> editClient(@Body UserWhenSignedIn userWhenSignedIn, @Header("Authorization") String token);

    @POST("api/advert/create")
    Call<Object> createPost(@Body PostCreate postCreate, @Header("Authorization") String token);

    @POST("api/advert/driver/search")
    Call<PostResponse> searchDriver(@Body PostSearch search, @Header("Authorization") String token, @Query("page") int page);

    @POST("api/advert/passenger/search")
    Call<PostResponse> searchPassenger(@Body PostSearch search, @Header("Authorization") String token, @Query("page") int page);

    @POST("api/advert/favourite/{advertId}")
    Call<Void> addToFavourite(@Path("advertId") long advertId, @Header("Authorization") String token);

    @DELETE("api/advert/favourite/{advertId}")
    Call<Void> deleteFromFavourite(@Path("advertId") long advertId, @Header("Authorization") String token);

    @GET("api/advert/favourite/get/list")
    Call<FavouritePostResponse> getFavourite(@Header("Authorization") String token);

    @GET("api/advert/get/myadverts")
    Call<PostResponse> getMyPosts(@Header("Authorization") String token);

    @GET("api/dictionary/carbrand/get/all")
    Call<CarResponse> getCars();

    @GET("api/dictionary/carmodel/get/byParent")
    Call<CarResponse> getCarsModel(@Query("parentId") long parentId);

    @GET("api/dictionary/cartype/get/all")
    Call<CarResponse> getCarTypes();
}
