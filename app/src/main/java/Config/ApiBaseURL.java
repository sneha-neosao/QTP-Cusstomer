package Config;


import static network.ApiInterface.branchcode;

public class ApiBaseURL
{

    public static String BASE_URL = "https://qtp.ae/QtpApiTesting/api/";
    public static String IMG_URL = "https://qtp.ae/QtpApiTesting/";
    public static String IMG_URL_NEW = "https://qtp.ae/QtpApiTesting/images/profile/";

//    public static String BASE_URL = "http://qtp.ae/QTPMobileApp/api/";
//    public static String IMG_URL = "http://qtp.ae/QTPMobileApp/";
//    public static String IMG_URL_NEW = "https://qtp.ae/QTPMobileApp/images/profile/";

    public static String SignUp = BASE_URL + "v2/registration";
    public static String Login = BASE_URL + "v2/login";
    public static String Login2 = BASE_URL + "v2/login2";
    public static String GetCustomer = BASE_URL + "v2/getCustomer";
    public static String ProfileUpdate = BASE_URL + "updateCustomer";
    public static String ProfileUpdate1 = BASE_URL + "v2/updateCustomer";
    public static String UpdatePassword = BASE_URL + "updatePassword";
    public static String USERBLOCKAPI = BASE_URL + "customerBlockCheck";
    public static String UpdateAddress = BASE_URL + "v2/updateAddress";
    public static String addAddress = BASE_URL + "v2/addAddress";
    public static String UpdateLatLong = BASE_URL + "updateLatLong";
    public static String forgotPasswort = BASE_URL + "forgotPasswort";

    public static String CategoriesWithSubCategories = BASE_URL + "categorywithsubcategory";
//    public static String Categories = BASE_URL + "category?BranchCode=" + branchcode;
    public static String SubCategories = BASE_URL + "geSubCategories";
//    public static String AllSubCategories = BASE_URL + "geAllSubCategories" + "?BranchCode=" + branchcode;
    public static String BANNER = BASE_URL + "mainBanner?BranchCode=" + branchcode;
    public static String Search = BASE_URL + "v2/searchProducts";

//    public static String FeatureProduct = BASE_URL + "v2/getFeaturedProducts?BranchCode=" + branchcode;
//
    public static String HomeDeal = BASE_URL + "v2/getTopSixDealOfTheDayProducts?BranchCode=" + branchcode;
//    public static String TopTodayDealAll = BASE_URL + "v2/getAllDealOfTheDayProducts?BranchCode=" + branchcode;
//
    public static String whatsnew = BASE_URL + "v2/getTopSixWhatsNewProducts?BranchCode=" + branchcode;
//    public static String whatsnewAll = BASE_URL + "v2/getAllWhatsNewProducts?BranchCode=" + branchcode;
//
    public static String topSelling = BASE_URL + "v2/getTopSixTopSellingProducts?BranchCode=" + branchcode;
//    public static String topSellingAll = BASE_URL + "v2/getAllTopSellingProducts?BranchCode=" + branchcode;
//
//    public static String recommended = BASE_URL + "v2/getRecommendationProducts?BranchCode=" + branchcode;

    public static String AddToFav = BASE_URL + "addFavourite";
    public static String getAllFav = BASE_URL + "v2/getFavouriteProductList?custId=74&top=4";

    public static String AddToRecentViews = BASE_URL + "addRecentViews";
    public static String topRecentViews = BASE_URL + "v2/getTopSixRecentViewsProducts";
    public static String topRecentViewsAll = BASE_URL + "v2/getAllTopRecentViewsProducts";

    public static String getProductsBySubCategory = BASE_URL + "v2/getProductsBySubcategory";
    public static String getProductsBySubCategoryCategory = BASE_URL + "v2/getProductsByCategorySubcategory";
    public static String getProductsByBanner = BASE_URL + "v2/getProductsByBanner";

    public static String getOrderStatus = BASE_URL + "getOrderStatus";

    public static String cancelOrder = BASE_URL + "cancelOrder";

    public static String getCountries = BASE_URL + "v2/getCountries";
    public static String getStates = BASE_URL + "v2/getStates";
    public static String getCities = BASE_URL + "v2/getCities";

    public static String CartProducts = BASE_URL + "v2/getCartProducts";
    public static String Cart = BASE_URL + "v2/cart";
    public static String clearCart = BASE_URL + "clearCart";
    public static String getExtraCharges = BASE_URL + "getExtraCharges";

    public static String OrderContinue = BASE_URL + "v2/bookorder";
    public static String OrderContinueOnline = BASE_URL + "v2/bookorderWithOnlinePayment";
    public static String OrderContinuePayment = BASE_URL + "v2/updatePaymentStatus";
    public static String OrderList = BASE_URL + "v2/getOrders";
    public static String checkCoupon = BASE_URL + "checkCoupon";
    public static String firstCoupon = BASE_URL + "firstCoupon";
//    public static String getOffersActive = BASE_URL + "getOffersActive" + "?BranchCode=" + branchcode;

    public static String OrderCalculation = BASE_URL + "v2/orderCalculation";
    public static String AlternatProductsByProductId = BASE_URL + "v2/getAlternatProductsByProductId";
    public static String getProductsBySubofSubcategory = BASE_URL + "v2/getProductsBySubofSubcategory";


}
