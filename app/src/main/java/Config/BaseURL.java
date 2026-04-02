package Config;


public class BaseURL {
    //public static final String MyPrefreance = "my_preprence";
    public static final String MyPrefreance = "GOGrocer";
    public static final String PREFS_NAME = "GroceryLoginPrefs";
    public static final String PREFS_NAME2 = "GroceryLoginPrefs2";
    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";
    public static final String KEY_EMAIL = "user_email";
    public static final String FROM_BOOK_ORDER = "book_order";
    public static final String SHIPPING_ADDRESS_POSITION = "shipping_address_position";
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String WALLET_TOTAL_AMOUNT = "WALLET_TOTAL_AMOUNT";
    public static final String COUPON_TOTAL_AMOUNT = "COUPON_TOTAL_AMOUNT";
    public static final String KEY_ID = "user_id";
    public static final String CART_ID_FINAL = "cart_id_final";
    public static final String KEY_MOBILE = "user_phone";
    public static final String KEY_IMAGE = "user_image";
    public static final String KEY_WALLET_Ammount = "wallet_ammount";
    public static final String KEY_REWARDS_POINTS = "rewards_points";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_SOCITY_ID = "Socity_id";
    public static final String KEY_ROLE = "role";
    public static final String KEY_SUPPLIERID = "supplierId";
    public static final String KEY_SOCITY_NAME = "socity_name";
    public static final String KEY_HOUSE = "house_no";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String USER_SKIP = "user_skip";
    public static final String USER_CURRENCY_CNTRY = "user_currency_country";
    public static final String USER_CURRENCY = "user_currency";
    public static final String USER_LAT = "user_lat";
    public static final String USER_LANG = "user_lang";
    public static final String USER_CITY = "user_city";
    public static final String USER_STOREID = "user_storeid";
    public static final String PAYMENT_PAYPAL = "payment_paypal";
    public static final String PAYMENT_RAZORPZY = "payment_razorpay";
    public static final String APP_OTP_STATUS = "user_otp_search";
    public static final String ADDRESS = "address";

    public static final String USER_COUNTRY = "user_country";
    public static final String USER_STATE = "user_state";
    public static final String USER_LANDMARK = "user_landmark";
    public static final String USER_STREET = "user_street";
    public static final String USER_DOB = "user_dob";
    public static final String USER_PINCODE = "user_pincode";
    public static final String USER_COUNTRY_CODE = "user_country_code";
    public static final String USER_ADDRESS_TYPE = "user_address_type";
    public static final String CouponCode = "CouponCode";
    public static final String CouponType = "CouponType";



  //targetview
    public static final String IsFirstTime = "isFirstTime";
    public static final String IsOrderTabLoaded = "isOrderTabLoaded";

    //adreeessss
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String StreetArea = "streetarea";
    public static final String HouseBuilding = "HouseBuilding";
    public static final String LAT = "lat";
    public static final String LONG = "long";
    public static final String KEY_STORE_COUNT = "STORE_COUNT";

    //Store Selection
    public static final String KEY_NOTIFICATION_COUNT = "NOTIFICATION_COUNT";
    //Firebase
    public static final String SHARED_PREF = "ah_firebase";
    public static final String TOPIC_GLOBAL = "global";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String KEY_PASSWORD = "password";
    public static final String USER_STATUS = "user_status";
    public static final String USER_OTP = "user_otp";
    public static final String USER_EMAIL_SERVICE = "user_email_service";
    public static final String USER_SMS_SERVICE = "user_sms_service";
    public static final String USER_INAPP_SERVICE = "user_inapp_service";
    //City and Store Id
    public static final String CITY_ID = "CITY_ID";
    public static final String STORE_ID = "STORE_ID";
    static final String APP_NAME = "GoGrocer";


//    public static String BASE_URL = "https://gogrocer.tecmanic.com/api/";
//    public static String IMG_URL = "https://gogrocer.tecmanic.com/";
//    public static String BANN_IMG_URL = "https://gogrocer.tecmanic.com/";
//    public static String BANNER_IMG_URL = "https://gogrocer.tecmanic.com/";

//    public static String BASE_URL = "https://thecodecafe.in/gogrocer-ver2.0/api/";
//    public static String IMG_URL = "https://thecodecafe.in/gogrocer-ver2.0/";
//    public static String BANN_IMG_URL = "https://gogrocer.tecmanic.com/";
//    public static String BANNER_IMG_URL = "https://thecodecafe.in/gogrocer-ver2.0/";

    public static String BASE_URL = "https://gogrocer.tecmanic.com/api/";
    public static String IMG_URL = "https://gogrocer.tecmanic.com/";
    public static String BANN_IMG_URL = IMG_URL;
    public static String BANNER_IMG_URL = IMG_URL;

    public static String SignUp = BASE_URL + "register";
    public static String SignUpOtp = BASE_URL + "verify_phone";

    public static String Login = BASE_URL + "login";
    public static String USERBLOCKAPI = BASE_URL + "user_block_check";
    public static String forget_password = BASE_URL + "forget_password";
    public static String verify_otp = BASE_URL + "verify_otp";
    public static String ChangePass = BASE_URL + "change_password";

    public static String HomeTopSelling = BASE_URL + "top_selling";
    public static String HomeRecent = BASE_URL + "recentselling";
    public static String HomeDeal = BASE_URL + "dealproduct";
    public static String redeem_rewards = BASE_URL + "redeem_rewards";
    public static String BANNER = BASE_URL + "banner";
    public static String secondary_banner = BASE_URL + "secondary_banner";

    public static String Categories = BASE_URL + "catee";
    public static String ProductVarient = BASE_URL + "varient";
    public static String Search = BASE_URL + "search";

    public static String CityListUrl = BASE_URL + "city";
    public static String SocietyListUrl = BASE_URL + "society";
    public static String Add_address = BASE_URL + "add_address";
    public static String ShowAddress = BASE_URL + "show_address";
    public static String SelectAddressURL = BASE_URL + "select_address";
    public static String EditAddress = BASE_URL + "edit_address";
    public static String DELETE_ORDER_URL = BASE_URL + "cancelling_reasons";
    public static String delete_order = BASE_URL + "delete_order";
    public static String delivery_info = BASE_URL + "delivery_info";

    public static String CalenderUrl = BASE_URL + "timeslot";

    public static String WALLET_REFRESH = BASE_URL + "walletamount?user_id=";
    public static String RecharegeWallet = BASE_URL + "recharge_wallet";
    public static String myprofile = BASE_URL + "myprofile";
    public static String OrderDoneUrl = BASE_URL + "completed_orders";
    public static String PendingOrderUrl = BASE_URL + "ongoing_orders";

    public static String AboutUrl = BASE_URL + "appaboutus";
    public static String topsix = BASE_URL + "topsix";
    public static String TermsUrl = BASE_URL + "appterms";

    public static String delete_all_notification = BASE_URL + "delete_all_notification";
    public static String SupportUrl = BASE_URL + "appterms";

    public static String EDIT_PROFILE_URL = BASE_URL + "profile_edit";
    public static String cat_product = BASE_URL + "cat_product";
    public static String OrderContinue = BASE_URL + "make_an_order";
    public static String MinMaxOrder = BASE_URL + "minmax";
    public static String rewardlines = BASE_URL + "rewardlines";

    public static String Wallet_CHECKOUT = BASE_URL + "";
    public static String ADD_ORDER_URL = BASE_URL + "checkout";
    public static String COUPON_CODE = BASE_URL + "couponlist";
    public static String apply_coupon = BASE_URL + "apply_coupon";
    public static String whatsnew = BASE_URL + "whatsnew";

    public static String NoticeURl = BASE_URL + "notificationlist";
    public static String updatenotifyby = BASE_URL + "updatenotifyby";
    public static String currencyApi = BASE_URL + "currency";

    public static String SHIPPING_CUSTOMER_Id="shippingCustomerId";
    public static String SHIPPING_Address="shippingAddress";
    public static String SHIPPING_Address1="shippingAddress1";
    public static String SHIPPING_City="shippingCity";
    public static String SHIPPING_State="shippingState";
    public static String SHIPPING_Country="shippingCountry";
    public static String SHIPPING_Latitude="shippingLatitude";
    public static String SHIPPING_Longitude="shippingLongitude";
    public static String SHIPPING_CUSTOMER_Mobile="shippingCustomerMobile";
    public static String SHIPPING_CUSTOMER_Email="shippingCustomerEmail";
    public static String SHIPPING_Province="shippingProvince";
    public static String SHIPPING_AddressType="shippingAddressType";

}
