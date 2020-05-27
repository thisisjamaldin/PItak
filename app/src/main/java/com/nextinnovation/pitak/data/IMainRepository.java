package com.nextinnovation.pitak.data;

import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.post.FavouritePostResponse;
import com.nextinnovation.pitak.model.post.PostResponse;
import com.nextinnovation.pitak.model.post.PostSearch;
import com.nextinnovation.pitak.model.post.PostSingle;
import com.nextinnovation.pitak.model.user.ProfileResponse;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMainRepository {

    @Multipart
    @POST("api/auth/signup")
    Call<User> signUp(@Part MultipartBody.Part profile,
                      @Part ("username") RequestBody username,
                      @Part ("email") RequestBody email,
                      @Part ("password") RequestBody password,
                      @Part ("surname") RequestBody surname,
                      @Part ("name") RequestBody name,
                      @Part ("patronymic") RequestBody patronymic,
                      @Part ("userType") RequestBody userType,
                      @Part ("carCommonModel.carBrand.id") RequestBody carBrandId,
                      @Part ("carCommonModel.carModel.id") RequestBody carModelId,
                      @Part ("carCommonModel.carNumber") RequestBody carNumber,
                      @Part ("carCommonModel.carType.id") RequestBody carTypeId,
                      @Part ("countryModel.id") RequestBody countryId,
                      @Part ("cityModel.id") RequestBody cityId
    );

    @POST("api/auth/signin")
    Call<UserWhenSignedIn> signIn(@Body UserSignIn userSignIn);

    @Multipart
    @POST("api/user/upload/profile/photo")
    Call<ProfileResponse> setUserProfile(@Part MultipartBody.Part profile,
                                         @Header("Authorization") String token);
    @POST("api/user/remove/profile/photo")
    Call<Void> removeProfile(@Header("Authorization") String token);

    @POST("api/user/driver/edit")
    Call<Void> editDriver(@Body UserWhenSignedIn userWhenSignedIn, @Header("Authorization") String token);

    @POST("api/user/passenger/edit")
    Call<Void> editClient(@Body UserWhenSignedIn userWhenSignedIn, @Header("Authorization") String token);

    @Multipart
    @POST("api/advert/create")
    Call<Object> createPost(@Part MultipartBody.Part image,
                            @Part MultipartBody.Part image1,
                            @Part MultipartBody.Part image2,
                            @Part MultipartBody.Part image3,
                            @Part MultipartBody.Part image4,
                            @Part("title") RequestBody title,
                            @Part("text") RequestBody text,
                            @Part("amountPayment") RequestBody amountPayment,
                            @Part("fromPlace") RequestBody fromPlace,
                            @Part("toPlace") RequestBody toPlace,
                            @Part("advertType") RequestBody advertType,
                            @Part("numberOfSeat") RequestBody numberOfSeat,
                            @Part("sendDateTime") RequestBody sendDateTime,
                            @Part("arrivalDateTime") RequestBody arrivalDateTime,
                            @Header("Authorization") String token);

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

    @GET("api/dictionary/country/get/all")
    Call<CarResponse> getCountry();

    @GET("api/dictionary/city/get/byParent")
    Call<CarResponse> getCity(@Query("parentId") long parentId);

    @GET("api/advert/get/{advertId}")
    Call<PostSingle> getAdvert(@Path("advertId") long advertId, @Header("Authorization") String token);
}
