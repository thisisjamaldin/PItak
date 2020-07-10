package com.nextinnovation.pitak.data;

import com.nextinnovation.pitak.model.agreement.AgreementResponse;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.model.report.ReportRequest;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.post.FavouritePostResponse;
import com.nextinnovation.pitak.model.post.PostResponse;
import com.nextinnovation.pitak.model.post.PostSearch;
import com.nextinnovation.pitak.model.post.PostSingle;
import com.nextinnovation.pitak.model.report.ReportResponse;
import com.nextinnovation.pitak.model.user.EditUser;
import com.nextinnovation.pitak.model.user.ProfileResponse;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMainRepository {

    @Multipart
    @POST("api/auth/signup")
    Call<UserWhenSignedIn> signUp(@Part MultipartBody.Part profile,
                                  @Part("username") RequestBody username,
                                  @Part("email") RequestBody email,
                                  @Part("password") RequestBody password,
                                  @Part("surname") RequestBody surname,
                                  @Part("name") RequestBody name,
                                  @Part("patronymic") RequestBody patronymic,
                                  @Part("userType") RequestBody userType,
                                  @Part("carCommonModel.carBrand.id") RequestBody carBrandId,
                                  @Part("carCommonModel.carModel.id") RequestBody carModelId,
                                  @Part("carCommonModel.carNumber") RequestBody carNumber,
                                  @Part("carCommonModel.carType.id") RequestBody carTypeId,
                                  @Part("countryModel.id") RequestBody countryId,
                                  @Part("cityModel.id") RequestBody cityId
    );

    @POST("api/auth/signin")
    Call<UserWhenSignedIn> signIn(@Body UserSignIn userSignIn);

    @Multipart
    @POST("api/user/upload/profile/photo")
    Call<ProfileResponse> setUserProfile(@Part MultipartBody.Part profile,
                                         @Header("Authorization") String token);

    @Multipart
    @PUT("api/car/update/photo/{carId}")
    Call<Void> setCarImage(@Path("carId") long id, @Part List<MultipartBody.Part> image,
                                         @Header("Authorization") String token);

    @DELETE("api/car/remove/photo")
    Call<Void> deleteCarImage(@Query("carId") long carId, @Query("fileId") long fileId,
                                         @Header("Authorization") String token);

    @POST("api/user/remove/profile/photo")
    Call<Void> removeProfile(@Header("Authorization") String token);

    @POST("api/user/driver/edit")
    Call<Void> editDriver(@Body UserWhenSignedIn userWhenSignedIn, @Header("Authorization") String token);

    @POST("api/user/passenger/edit")
    Call<Void> editClient(@Body EditUser editUser, @Header("Authorization") String token);

    @Multipart
    @POST("api/advert/save")
    Call<Object> savePost(
            @Part("appAdvertModel.id") RequestBody id,
            @Part List<MultipartBody.Part> image,
            @Part("appAdvertModel.title") RequestBody title,
            @Part("appAdvertModel.text") RequestBody text,
            @Part("appAdvertModel.amountPayment") RequestBody amountPayment,
            @Part("appAdvertModel.fromPlace") RequestBody fromPlace,
            @Part("appAdvertModel.toPlace") RequestBody toPlace,
            @Part("appAdvertModel.typeService.id") RequestBody advertTypeId,
            @Part("appAdvertModel.typeService.name") RequestBody advertType,
            @Part("appAdvertModel.numberOfSeat") RequestBody numberOfSeat,
            @Part("appAdvertModel.sendDateTime") RequestBody sendDateTime,
            @Part("appAdvertModel.arrivalDateTime") RequestBody arrivalDateTime,
            @Part("appAdvertModel.carCommonModel.id") RequestBody markId,
            @Header("Authorization") String token);

    @POST("api/advert/driver/search")
    Call<PostResponse> searchDriver(@Body PostSearch search, @Header("Authorization") String token, @Query("page") int page, @Query("sort") String sort);

    @POST("api/advert/passenger/search")
    Call<PostResponse> searchPassenger(@Body PostSearch search, @Header("Authorization") String token, @Query("page") int page, @Query("sort") String sort);

    @POST("api/advert/all/adverts")
    Call<PostResponse> searchAnonymous(@Body PostSearch search, @Query("page") int page, @Query("sort") String sort);

    @POST("api/advert/favourite/{advertId}")
    Call<Void> addToFavourite(@Path("advertId") long advertId, @Header("Authorization") String token);

    @POST("api/report/advert/create")
    Call<Void> report(@Body ReportRequest report, @Header("Authorization") String token);

    @DELETE("api/advert/favourite/{advertId}")
    Call<Void> deleteFromFavourite(@Path("advertId") long advertId, @Header("Authorization") String token);

    @DELETE("api/advert/remove/myadvert/{id}")
    Call<Void> deleteMyPost(@Path("id") long id, @Header("Authorization") String token);

    @GET("api/car/remove")
    Call<Void> deleteMyCar(@Query("carId") long carId, @Header("Authorization") String token);

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

    @GET("api/advert/get/myadvert/{id}")
    Call<PostSingle> getMyAdvert(@Path("id") long id, @Header("Authorization") String token);

    @GET("api/user/me")
    Call<User> getMe(@Header("Authorization") String token);

    @GET("api/dictionary/reporttype/get/all")
    Call<ReportResponse> getReports();

    @GET("api/dictionary/agreement/html/get")
    Call<AgreementResponse> getAgreement();

    @GET("api/car/mycars")
    Call<NewCarResponse> getMyCars(@Header("Authorization") String token);

    @GET("api/dictionary/typeService/get/all")
    Call<CarResponse> getServices();

    @Multipart
    @POST("api/car/create")
    Call<Object> createCar(@Part List<MultipartBody.Part> image,
                           @Part("carCommonModel.id") RequestBody id,
                           @Part("carCommonModel.userId") RequestBody userId,
                           @Part("carCommonModel.carBrand.id") RequestBody markId,
                           @Part("carCommonModel.carBrand.name") RequestBody markName,
                           @Part("carCommonModel.carModel.id") RequestBody modelId,
                           @Part("carCommonModel.carModel.name") RequestBody modelName,
                           @Part("carCommonModel.carNumber") RequestBody number,
                           @Part("carCommonModel.carType.id") RequestBody typeId,
                           @Part("carCommonModel.carType.name") RequestBody typeName,
                           @Part("carCommonModel.carryCapacity") RequestBody capacity,
                           @Header("Authorization") String token);

}
