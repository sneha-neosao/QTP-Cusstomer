package network;

import ModelClass.ForgotEmailModel;
import ModelClass.NotifyModelUser;
import ModelClass.PaymentVia;
import ModelClass.ResponseUpdateProfilePic;
import PaymentDataModels.ResponseOrderModel;
import PaymentDataModels1.ResponseOrderModel1;
import network.Request.RequestCheckCoupon;
import network.Request.RequestExtraCharges;
import network.Response.ResAddress;
import network.Response.ResNotification;
import network.Response.ResProductDetail;
import network.Response.ResRecentSearch;
import network.Response.ResReels;
import network.Response.ResReviews;
import network.Response.ResSearchSuggestion;
import network.Response.ResponseAddDeliveryRatingReview;
import network.Response.ResponseAddRatingReview;
import network.Response.ResponseAddRecentViews;
import network.Response.ResponseCoupon;
import network.Response.ResponseDeleteRecentSearch;
import network.Response.ResponseExtraCharges;
import network.Response.ResponseGetAllSubOfSubCategories;
import network.Response.ResponseGetAlternatProductsByProductId;
import network.Response.ResponseGetCounts;
import network.Response.ResponseGetFaqList;
import network.Response.ResponseGetSlider;
import network.Response.ResponseGetUserVouchers;
import network.Response.ResponseIncrementFaqCount;
import network.Response.ResponseLifetimeOffers;
import network.Response.ResponseMainPopUp;
import network.Response.ResponseReferEarnDetails;
import network.Response.ResponseReferEarnStatus;
import network.Response.ResponseTermsCondition;
import network.Response.ResponseUpdateOrderStatus;
import network.Response.ResponseUpdatePaymentStatus;
import network.Response.RestItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    String branchcode = "B001";

    @POST("forgot_password")
    @FormUrlEncoded
    Call<ForgotEmailModel> getEmailOtp(@Field("user_email") String user_email,@Field("user_phone") String user_phone);

    @GET("checkotponoff")
    Call<ForgotEmailModel> getOtpOnOffStatus();

    @GET("pymnt_via")
    Call<PaymentVia> getPaymentVia();

    @GET("v2/getFavouriteProductList")
    Call<RestItem> getFavouriteProductList(@Query("custId") String custId,
                                           @Query("top") String top);

    @POST("notifyby")
    @FormUrlEncoded
    Call<NotifyModelUser> getNotifyUser(@Field("user_id") String user_id);

    @Multipart
    @POST("updateProfilePic")
    Call<ResponseUpdateProfilePic> updateProfilePic(@Part MultipartBody.Part files, @Part("custId") RequestBody custId);

    @GET("getLifetimeOffers")
    Call<ResponseLifetimeOffers> getLifetimeOffers(@Query("BranchCode") String branchcode);

    @POST("checkCoupon")
    Call<ResponseCoupon> checkCoupon(@Body RequestCheckCoupon requestCheckCoupon);

    @POST("getExtraCharges")
    Call<ResponseExtraCharges> getExtraCharges(@Body RequestExtraCharges requestExtraCharges);


    @POST("firstCoupon")
    Call<ResponseCoupon> firstCoupon(@Body RequestCheckCoupon requestCheckCoupon);

    @POST("getProductsByBanner")
    @FormUrlEncoded
    Call<ResponseBody> getProductsByBanner(@Field("bannerId") String bannerId);

    @POST("updateFirebaseToken")
    @FormUrlEncoded
    Call<ResponseBody> updateFirebaseToken(@Field("custID") String custID, @Field("AccessToken") String AccessToken);

    /* @POST("getAccessToken")
    Call<ResponseOrderModel> createOrderService(@Body CreatePaymentOrderDto paymentOrderDto);*/

    @POST("v2/bookorderWithOnlinePayment")
    Call<ResponseOrderModel> createOrderService(@Body RequestBookPayment paymentOrderDto);

    @POST("v2/bookorderWithOnlinePayment")
    Call<ResponseOrderModel1> bookorderWithOnlinePayment
            (@Field("SubTotal") String SubTotal,@Field("discount") String discount,@Field("email") String email,@Field("OrderStatus") String OrderStatus,
             @Field("couponType") String couponType,
             @Field("tax") String tax,@Field("CMCode") String CMCode,@Field("shipping") String shipping,@Field("Total") String Total,
             @Field("country") String country,
             @Field("grandtotal") String grandtotal,@Field("FirstName") String FirstName,@Field("DecidedExisitingLimit") String DecidedExisitingLimit,
             @Field("Promo") String Promo,@Field("AddressLine1") String AddressLine1,
             @Field("CMID") String CMID,@Field("City") String City,@Field("custID") String custID,
             @Field("Mobile") String Mobile,@Field("DeviceName") String DeviceName,
             @Field("latitude") String latitude,@Field("longitude") String longitude,@Field("appVersion") String appVersion,@Field("Province") String Province,@Field("AddressType") String AddressType
    );

    @POST("v2/getProductDetailsByProductId")
    @FormUrlEncoded
    Call<ResProductDetail> getProductDetailsByProductId(@Field("itemID") String itemID);

    @GET("v2/getProductReview")
    Call<ResReviews> getProductReview(@Query("itemID") String itemID,
                                      @Query("offset") String offset);

    @GET("v2/getSearchSuggessions")
    Call<ResSearchSuggestion> getSearchSuggessions(@Query("keywords") String keywords);

    @GET("v2/getRecentSearches")
    Call<ResRecentSearch> getRecentSearches(@Query("custID") String custID);

    @GET("v2/getReelsList")
    Call<ResReels> getReelsList();

    @POST("v2/getNotifications")
    @FormUrlEncoded
    Call<ResNotification> getNotifications(@Field("UserID") String UserID);

    @POST("v2/getCustomerAddresses")
    @FormUrlEncoded
    Call<ResAddress> getCustomerAddresses(@Field("custID") String custID);


    @POST("v2/addRatingReview")
    @FormUrlEncoded
    Call<ResponseAddRatingReview> addRatingReview(@Field("custID") String custID, @Field("orderID") String orderID,
                                                  @Field("rating") String rating, @Field("review") String review);

    @POST("v2/addProductRatingReview")
    @FormUrlEncoded
    Call<ResponseAddRatingReview> addProductRatingReview(@Field("custID") String custID,
                                                         @Field("orderID") String orderID,
                                                         @Field("itemID") String itemID,
                                                         @Field("rating") String rating,
                                                         @Field("review") String review);

    @POST("v2/addDeliveryRatingReview")
    @FormUrlEncoded
    Call<ResponseAddDeliveryRatingReview> addDeliveryRatingReview(@Field("custID") String custID, @Field("orderID") String orderID,
                                                                  @Field("rating") String rating, @Field("DeliveryMenId") String DeliveryMenId);




    @POST("v2/updatePaymentStatus")
    @FormUrlEncoded
    Call<ResponseUpdatePaymentStatus> updatePaymentStatus(@Field("OrderID") String OrderID,@Field("IsPaymentSuccessful") String IsPaymentSuccessful,
             @Field("OrderStatus") String OrderStatus,
                     @Field("PaymentGatewayRef") String PaymentGatewayRef,
                     @Field("cartID") String cartID,
                     @Field("DecidedExisitingLimit") String DecidedExisitingLimit,
                     @Field("CMID") String CMID,
                     @Field("couponType") String couponType,
                     @Field("CMCode") String CMCode,
                     @Field("Promo") String Promo,
                     @Field("custID") String custID);



    @POST("v2/updateOrderStatus")
    @FormUrlEncoded
    Call<ResponseUpdateOrderStatus> updateOrderStatus( @Field("OrderID") String OrderID,
                                                       @Field("custID") String custID,
                                                       @Field("OrderStatus") String OrderStatus,
                                                       @Field("OrderTransactionType") String OrderTransactionType,
                                                       @Field("Promo") String Promo,
                                                       @Field("CMCode") String CMCode,
                                                       @Field("couponType") String couponType,
                                                       @Field("CMID") String CMID,
                                                       @Field("DecidedExisitingLimit") String DecidedExisitingLimit);

    @POST("v2/getAlternatProductsByProductId")
    @FormUrlEncoded
    Call<ResponseGetAlternatProductsByProductId> getAlternatProductsByProductId(@Field("itemID") String itemID);

    @POST("v2/deleteRecentSearch")
    @FormUrlEncoded
    Call<ResponseDeleteRecentSearch> deleteRecentSearch(@Field("userID") String userID,@Field("rid") String rid);

    @GET("v2/getFaqList")
    Call<ResponseGetFaqList> getFaqList();

    @GET("v2/getSlider")
    Call<ResponseGetSlider> getSlider();

    @POST("v2/incrementFaqCount")
    @FormUrlEncoded
    Call<ResponseIncrementFaqCount> incrementFaqCount(@Field("FAQID") String FAQID);

    @POST("v2/getUserVouchers")
    @FormUrlEncoded
    Call<ResponseGetUserVouchers> getUserVouchers(@Field("custID") String custID);

    @GET("v2/geAllSubOfSubCategories")
    Call<ResponseGetAllSubOfSubCategories> getAllSubOfSubCategories();

    @POST("v2/getCounts")
    @FormUrlEncoded
    Call<ResponseGetCounts> getCounts(@Field("UserID") String UserID);

    @POST("addRecentViews")
    @FormUrlEncoded
    Call<ResponseAddRecentViews> addRecentViews(@Field("custID") String custID, @Field("ItemId") String ItemId);

    @GET("v2/mainPopup")
    Call<ResponseMainPopUp> getMainPopup(@Query("type") String type);

    @POST("v2/getMyReferDetails")
    @FormUrlEncoded
    Call<ResponseReferEarnDetails> getMyReferDetails(@Field("custID") String custID);

    @POST("v2/checkAndCreateUserVouchers")
    @FormUrlEncoded
    Call<ResponseReferEarnDetails> checkAndCreateUserVouchers(@Field("custID") String custID);

    @GET("v2/getReferTermsAndCondition")
    Call<ResponseTermsCondition>getReferTermsAndCondition();

    @POST("v2/getReferEarnStatus/")
    @FormUrlEncoded
    Call<ResponseReferEarnStatus>getReferEarnStatus(@Field("custID") String custID);


}
