package util;

import android.content.Context;

import static Config.BaseURL.APP_OTP_STATUS;
import static Config.BaseURL.CART_ID_FINAL;
import static Config.BaseURL.COUNTRY;
import static Config.BaseURL.CouponCode;
import static Config.BaseURL.CouponType;
import static Config.BaseURL.FROM_BOOK_ORDER;
import static Config.BaseURL.HouseBuilding;
import static Config.BaseURL.IsFirstTime;
import static Config.BaseURL.IsOrderTabLoaded;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.IS_LOGIN;
import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_NAME;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_PASSWORD;
import static Config.BaseURL.PAYMENT_PAYPAL;
import static Config.BaseURL.PAYMENT_RAZORPZY;
import static Config.BaseURL.SHIPPING_ADDRESS_POSITION;
import static Config.BaseURL.StreetArea;
import static Config.BaseURL.USER_CITY;
import static Config.BaseURL.USER_COUNTRY;
import static Config.BaseURL.USER_COUNTRY_CODE;
import static Config.BaseURL.USER_CURRENCY;
import static Config.BaseURL.USER_CURRENCY_CNTRY;
import static Config.BaseURL.USER_DOB;
import static Config.BaseURL.USER_EMAIL_SERVICE;
import static Config.BaseURL.USER_INAPP_SERVICE;
import static Config.BaseURL.USER_LANDMARK;
import static Config.BaseURL.USER_LANG;
import static Config.BaseURL.USER_LAT;
import static Config.BaseURL.USER_OTP;
import static Config.BaseURL.USER_PINCODE;
import static Config.BaseURL.USER_SKIP;
import static Config.BaseURL.KEY_ROLE;
import static Config.BaseURL.ADDRESS;
import static Config.BaseURL.KEY_SUPPLIERID;
import static Config.BaseURL.KEY_IMAGE;
import static Config.BaseURL.KEY_WALLET_Ammount;
import static Config.BaseURL.KEY_REWARDS_POINTS;
import static Config.BaseURL.KEY_PAYMENT_METHOD;
import static Config.BaseURL.TOTAL_AMOUNT;
import static Config.BaseURL.KEY_PINCODE;
import static Config.BaseURL.KEY_SOCITY_ID;
import static Config.BaseURL.KEY_SOCITY_NAME;
import static Config.BaseURL.KEY_HOUSE;
import static Config.BaseURL.USER_SMS_SERVICE;
import static Config.BaseURL.USER_STATE;
import static Config.BaseURL.USER_STATUS;
import static Config.BaseURL.USER_STOREID;
import static Config.BaseURL.USER_STREET;


import java.util.HashMap;

public class Session_management {


    SharedPreferenceUtil pref;


    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        pref = new SharedPreferenceUtil(context);

    }
//
    public void createLoginSession(String id, String email, String name, String mobile, String password,String address,String role,String supplierId) {
        pref.setBoolean(IS_LOGIN, true);
        pref.setString(KEY_ID, id);
        pref.setString(KEY_EMAIL, email);
        pref.setString(KEY_NAME, name);
        pref.setString(KEY_MOBILE, mobile);
        pref.setString(KEY_PASSWORD, password);
        pref.setBoolean(USER_SKIP, false);
        pref.setString(ADDRESS, address);
        pref.setString(KEY_ROLE, role);
        pref.setString(KEY_SUPPLIERID, supplierId);
        pref.save();
    }
//
//
//    public void createUpdateProfileSession(String id, String email, String name, String mobile, String address,String role,String supplierId,
//                                           String country,String state,String city,String landmark,
//                                           String street,String dob,String pincode,String countryCode) {
//
//        pref.setBoolean(IS_LOGIN, true);
//        pref.setString(KEY_ID, id);
//        pref.setString(KEY_EMAIL, email);
//        pref.setString(KEY_NAME, name);
//        pref.setString(KEY_MOBILE, mobile);
//        pref.setBoolean(USER_SKIP, false);
//        pref.setString(ADDRESS, address);
//        pref.setString(KEY_ROLE, role);
//        pref.setString(KEY_SUPPLIERID, supplierId);
//        pref.setString(USER_COUNTRY, country);
//        pref.setString(USER_CITY, city);
//        pref.setString(USER_STATE, state);
//        pref.setString(USER_LANDMARK, landmark);
//        pref.setString(USER_STREET, street);
//        pref.setString(USER_DOB, dob);
//        pref.setString(USER_PINCODE, pincode);
//        pref.setString(USER_COUNTRY_CODE, countryCode);
//        pref.save();
//
//    }
//
    public String getUserId()
    {
        return pref.getString(KEY_ID, "");
    }
    public void setUserid(String userId){
        pref.setString(KEY_ID, userId);
    }

    public String getUserFullName()
    {
        return pref.getString(KEY_NAME, "");
    }
    public void setUserFullName(String fullName){
        pref.setString(KEY_NAME, fullName);
    }

    public String getUserMobile()
    {
        return pref.getString(KEY_MOBILE, "");
    }
    public void setUserMobile(String mobile){
        pref.setString(KEY_MOBILE, mobile);
    }

    public String getUserEmail()
    {
        return pref.getString(KEY_EMAIL, "");
    }
    public void setUserEmail(String userEmail){
        pref.setString(KEY_EMAIL, userEmail);
    }

    public String getBookOrder()
    {
        return pref.getString(FROM_BOOK_ORDER, "");
    }
    public void setBookOrder(String fromBookOrder){
        pref.setString(FROM_BOOK_ORDER, fromBookOrder);
    }

    public int getShippingAddressPosition()
    {
        return pref.getInt(SHIPPING_ADDRESS_POSITION,0);
    }
//    public void setShippingAddressPosition(int shippingAddressPosition){
//        pref.setInt(SHIPPING_ADDRESS_POSITION, shippingAddressPosition);
//    }

    public boolean getIsOrderTabLoaded()
    {
        return pref.getBoolean(IsOrderTabLoaded, false);
    }
    public void setIsOrderTabLoaded(boolean isOrderTabLoaded){
        pref.setBoolean(IsOrderTabLoaded, isOrderTabLoaded);
    }

    public String getUserCountry()
    {
        return pref.getString(USER_COUNTRY, "");
    }
    public void setUserCountry(String country){
        pref.setString(USER_COUNTRY, country);
    }
    public String getUserState()
    {
        return pref.getString(USER_STATE, "");
    }
    public void setUserState(String state){
        pref.setString(USER_STATE, state);
    }
    public String getUserCity()
    {
        return pref.getString(USER_CITY, "");
    }
    public void setUserCity(String city){
        pref.setString(USER_CITY, city);
    }
    public String getUserLandmark()
    {
        return pref.getString(USER_LANDMARK, "");
    }
    public void setUserLandmark(String landmark){
        pref.setString(USER_LANDMARK, landmark);
    }
    public String getUserStreet()
    {
        return pref.getString(USER_STREET, "");
    }
    public void setUserStreet(String street){
        pref.setString(USER_STREET, street);
    }
    public String getUserDOB()
    {
        return pref.getString(USER_DOB, "");
    }
    public void setUserDOB(String dob){
        pref.setString(USER_DOB, dob);
    }
    public String getUserPinCode()
    {
        return pref.getString(USER_PINCODE, "");
    }
    public void setUserPinCode(String pincode){
        pref.setString(USER_PINCODE, pincode);
    }
    public String getUserCountryCode()
    {
        return pref.getString(USER_COUNTRY_CODE, "");
    }
    public void setUserCountryCode(String countryCode){
        pref.setString(USER_COUNTRY_CODE, countryCode);
    }




//    public void createLoginSession(String id, String email, String name, String mobile, String password, boolean skip,String address) {
//
//        pref.setBoolean(IS_LOGIN, false);
//        pref.setString(KEY_ID, id);
//        pref.setString(KEY_EMAIL, email);
//        pref.setString(KEY_NAME, name);
//        pref.setString(KEY_MOBILE, mobile);
//        pref.setString(KEY_PASSWORD, password);
//        pref.setBoolean(USER_SKIP, skip);
//        pref.setString(ADDRESS, address);
//        pref.setString(KEY_ROLE, "customer");
//        pref.save();
//
//    }

    public void setLocationPref(String lat, String lang) {
        pref.setString(USER_LAT, lat);
        pref.setString(USER_LANG, lang);
    }

    public String getLatPref() {
        return pref.getString(USER_LAT, "");
    }


    public String getLangPref() {
        return pref.getString(USER_LANG, "");
    }

    public String getLocationCity() {
        return pref.getString(USER_CITY, "");
    }

    public void setLocationCity(String city) {
        pref.setString(USER_CITY, city);
    }

    public void setCountry(String country){
        pref.setString(COUNTRY, country);
    }

    public String getCountry()
    {
        return pref.getString(COUNTRY, "");
    }

    public void setAddress(String address){
        pref.setString(ADDRESS, address);
    }

    public void setIsFirstTime(String isFirstTime){
        pref.setString(IsFirstTime, isFirstTime);
    }

    public String getIsFirstTime()
    {
        return pref.getString(IsFirstTime, "");
    }

    public String getAddress()
    {
        return pref.getString(ADDRESS, "");
    }

//    /**
//     * Get stored session data
//     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, pref.getString(KEY_ID, null));
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        user.put(KEY_WALLET_Ammount, pref.getString(KEY_WALLET_Ammount, null));
        user.put(KEY_REWARDS_POINTS, pref.getString(KEY_REWARDS_POINTS, null));
        user.put(KEY_PAYMENT_METHOD, pref.getString(KEY_PAYMENT_METHOD, ""));
        user.put(TOTAL_AMOUNT, pref.getString(TOTAL_AMOUNT, null));
        user.put(KEY_PINCODE, pref.getString(KEY_PINCODE, null));
        user.put(KEY_SOCITY_ID, pref.getString(KEY_SOCITY_ID, null));
        user.put(KEY_SOCITY_NAME, pref.getString(KEY_SOCITY_NAME, null));
        user.put(KEY_HOUSE, pref.getString(KEY_HOUSE, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(ADDRESS, pref.getString(ADDRESS, ""));
        user.put(KEY_SUPPLIERID, pref.getString(KEY_SUPPLIERID, ""));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, ""));
        // return user
        return user;
    }

//    public void logoutSession() {
//        SharedPreferences preferences =context.getSharedPreferences(BaseURL.MyPrefreance,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();
//        pref.clearAll();
//        DatabaseHandler db = new DatabaseHandler(context);
//        db.clearCart();
//        db.clearWishlist();
//
//        cleardatetime();
//
//        Intent logout = new Intent(context, MainDrawerActivity.class);
//        // Closing all the Activities
//        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        logout.putExtra("loadFrag",1);
//
//        context.startActivity(logout);
//    }


    public void cleardatetime() {
//        editor2.clear();
//        editor2.commit();
    }
//
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setLogin(boolean value) {
        pref.setBoolean(IS_LOGIN, value);
    }

    public boolean isSkip() {
        return pref.getBoolean(USER_SKIP, false);
    }


    public String userBlockStatus() {
        return pref.getString(USER_STATUS, "2");
    }

    public void setUserBlockStatus(String value) {
        pref.setString(USER_STATUS, value);
    }

    public void setEmailServer(String value) {
        pref.setString(USER_EMAIL_SERVICE, value);
    }

    public void setUserSMSService(String value) {
        pref.setString(USER_SMS_SERVICE, value);
    }

    public void setUserInAppService(String value) {
        pref.setString(USER_INAPP_SERVICE, value);
    }

    public String getEmailService() {
        return pref.getString(USER_EMAIL_SERVICE, "0");
    }

    public String getSMSService() {
        return pref.getString(USER_SMS_SERVICE, "0");
    }

    public String getINAPPService() {
        return pref.getString(USER_INAPP_SERVICE, "0");
    }

    public String userOtp() {
        return pref.getString(USER_OTP, "0");
    }

    public void setOtp(String value) {
        pref.setString(USER_OTP, value);
    }

    public String userId() {
        return pref.getString(KEY_ID, "");
    }

    public String role() {
        return pref.getString(KEY_ROLE, "customer");
    }

    public void setCartID(String value) {
        pref.setString(CART_ID_FINAL, value);
    }

    public String getCartId() {
        return pref.getString(CART_ID_FINAL, "");
    }

    public void setCouponCode(String value,String type){
        pref.setString(CouponCode,value);
        pref.setString(CouponType,type);
    }

    public String getCouponCode(){
        return pref.getString(CouponCode,"");
    }
    public String getCouponType(){
        return pref.getString(CouponType,"");
    }

    public String getCurrency() {
        return pref.getString(USER_CURRENCY, "");
    }

    public String getCurrencyCountry() {
        return pref.getString(USER_CURRENCY_CNTRY, "");
    }

    public void setCurrency(String name, String currency) {
        pref.setString(USER_CURRENCY, currency);
        pref.setString(USER_CURRENCY_CNTRY, name);
    }

    public void setStreetArea(String value) {
        pref.setString(StreetArea, value);
    }

    public String getStreetArea() {
        return pref.getString(StreetArea, "");
    }

    public void setHouseBuilding(String value) {
        pref.setString(HouseBuilding, value);
    }

    public String getHouseBuilding() {
        return pref.getString(HouseBuilding, "");
    }

    public String getOtpSatus() {
        return pref.getString(APP_OTP_STATUS, "1");
    }

    public void setOtpStatus(String value) {
        pref.setString(APP_OTP_STATUS, value);
    }

    public String getStoreId() {
        return pref.getString(USER_STOREID, "");
    }

    public void setStoreId(String storeId) {
        pref.setString(USER_STOREID, storeId);
    }

    public void setPaymentMethodOpt(String razorpay, String paypal) {
        pref.setString(PAYMENT_RAZORPZY, razorpay);
        pref.setString(PAYMENT_PAYPAL, paypal);
    }

    public String getPayPal() {
        return pref.getString(PAYMENT_PAYPAL, "0");
    }

    public String getRazorPay() {
        return pref.getString(PAYMENT_RAZORPZY, "0");
    }

    public boolean isFirstCouponUsed() {

        return pref.getBoolean("couponUsed", false);
    }

    public void setFirstCouponUsed(boolean firstCouponUsed) {

        pref.setBoolean("couponUsed", firstCouponUsed);
    }

    public boolean isMainPopUpVisible() {

        return pref.getBoolean("mainPopUp", false);
    }

    public void setMainPopUp(boolean mainPopUp) {

        pref.setBoolean("mainPopUp", mainPopUp);
    }

    public boolean isCartPopUpVisible() {

        return pref.getBoolean("cartPopUp", false);
    }

    public void setCartPopUp(boolean cartPopUp) {

        pref.setBoolean("cartPopUp", cartPopUp);
    }

    public String getCategoryPopUp() {

        return pref.getString("categoryPopUp","");
    }

    public void setCategoryPopUp(String categoryPopUp) {
        pref.setString("categoryPopUp", categoryPopUp);
    }

    public boolean isCategoryPopUpVisible() {

        return pref.getBoolean("categoryPopUp1", false);
    }

    public void setCategoryPopUp(boolean cartPopUp) {

        pref.setBoolean("categoryPopUp1", cartPopUp);
    }

//    public void saveShippingAddress(
//            String shippingCustomerId,
//            String shippingAddress,
//            String shippingAddress1,
//            String shippingCity,
//            String shippingState,
//            String shippingCountry,
//            String shippingLatitude,
//            String shippingLongitude,
//            String shippingCustomerMobile,
//            String shippingCustomerEmail,
//            String shippingProvince,
//            String shippingAddressType
//    )
//    {
//        pref.setString(SHIPPING_CUSTOMER_Id,shippingCustomerId);
//        pref.setString(SHIPPING_Address,shippingAddress);
//        pref.setString(SHIPPING_Address1,shippingAddress1);
//        pref.setString(SHIPPING_City,shippingCity);
//        pref.setString(SHIPPING_State,shippingState);
//        pref.setString(SHIPPING_Country,shippingCountry);
//        pref.setString(SHIPPING_Latitude,shippingLatitude);
//        pref.setString(SHIPPING_Longitude,shippingLongitude);
//        pref.setString(SHIPPING_CUSTOMER_Mobile,shippingCustomerMobile);
//        pref.setString(SHIPPING_CUSTOMER_Email,shippingCustomerEmail);
//        pref.setString(SHIPPING_Province,shippingProvince);
//        pref.setString(SHIPPING_AddressType,shippingAddressType);
//        pref.save();
//
//    }
//
//    public HashMap<String, String> getShippingAddress() {
//        HashMap<String, String> shipping = new HashMap<String, String>();
//        shipping.put(SHIPPING_CUSTOMER_Id,pref.getString(SHIPPING_CUSTOMER_Id,""));
//        shipping.put(SHIPPING_Address,pref.getString(SHIPPING_Address,""));
//        shipping.put(SHIPPING_Address1,pref.getString(SHIPPING_Address1,""));
//        shipping.put(SHIPPING_City,pref.getString(SHIPPING_City,""));
//        shipping.put(SHIPPING_State,pref.getString(SHIPPING_State,""));
//        shipping.put(SHIPPING_Country,pref.getString(SHIPPING_Country,""));
//        shipping.put(SHIPPING_Latitude,pref.getString(SHIPPING_Latitude,""));
//        shipping.put(SHIPPING_Longitude,pref.getString(SHIPPING_Longitude,""));
//        shipping.put(SHIPPING_CUSTOMER_Mobile,pref.getString(SHIPPING_CUSTOMER_Mobile,""));
//        shipping.put(SHIPPING_CUSTOMER_Email,pref.getString(SHIPPING_CUSTOMER_Email,""));
//        shipping.put(SHIPPING_Province,pref.getString(SHIPPING_Province,""));
//        shipping.put(SHIPPING_AddressType,pref.getString(SHIPPING_AddressType,""));
//        return shipping;
//    }


}