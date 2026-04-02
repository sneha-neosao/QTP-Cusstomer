package util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String CART_TABLE = "cart";
    public static final String WISHLIST_TABLE = "wishlist";
    //    public static final String COLUMN_ID = "product_id";
    public static final String VARIENT_ID = "varient_id";
    public static final String PRODUCT_ID = "ItemId";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_IMAGE = "product_image";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_REWARDS = "rewards";
    public static final String COLUMN_INCREAMENT = "increament";
    public static final String COLUMN_UNIT_VALUE = "unit_value";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SUPPLIERID = "supplierID";
    public static final String COLUMN_VATRATE = "vatRate";
    public static final String COLUMN_DESCRIPTION = "product_description";
    public static final String UNIT_ID = "unitID";
    private static String DB_NAME = "theyardgrocery";
    private static int DB_VERSION = 1;

    public    DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion )
    {
/*
        if (oldVersion < 4){
            String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                    + "(" + VARIENT_ID + " integer primary key, "
                    + PRODUCT_ID + " integer ,"
                    + COLUMN_QTY + " DOUBLE NOT NULL,"
                    + COLUMN_IMAGE + " TEXT , "
                    + COLUMN_NAME + " TEXT , "
                    + COLUMN_PRICE + " DOUBLE , "
                    + COLUMN_UNIT_VALUE + " TEXT , "
                    + COLUMN_REWARDS + " DOUBLE , "
                    + COLUMN_INCREAMENT + " DOUBLE , "
                    + COLUMN_STOCK + " DOUBLE , "
                    + COLUMN_TITLE + " TEXT , "
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_VATRATE + " TEXT,"
                    + COLUMN_SUPPLIERID +" TEXT "
                    + ")";

            db.execSQL(exe);
        }*/
       /* if(oldVersion < newVersion){

            String exe = "ALTER TABLE " + CART_TABLE
                    + "(" + VARIENT_ID + " integer primary key, "
                    + COLUMN_QTY + " DOUBLE NOT NULL,"
                    + COLUMN_IMAGE + " TEXT , "
                    + COLUMN_NAME + " TEXT , "
                    + COLUMN_PRICE + " DOUBLE , "
                    + COLUMN_UNIT_VALUE + " TEXT , "
                    + COLUMN_REWARDS + " DOUBLE , "
                    + COLUMN_INCREAMENT + " DOUBLE , "
                    + COLUMN_STOCK + " DOUBLE , "
                    + COLUMN_TITLE + " TEXT , "
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_VATRATE + " TEXT,"
                    + COLUMN_SUPPLIERID +" TEXT "
                    + ")";

            String wishlist = "CREATE TABLE IF NOT EXISTS " + WISHLIST_TABLE
                    + "(" + VARIENT_ID + " integer primary key, "
                    + COLUMN_IMAGE + " TEXT , "
                    + COLUMN_NAME + " TEXT , "
                    + COLUMN_UNIT_VALUE + " TEXT , "
                    + COLUMN_PRICE + " DOUBLE , "
                    + COLUMN_DESCRIPTION + " TEXT ,"
                    + COLUMN_SUPPLIERID +" TEXT "
                    + ")";

            db.execSQL(exe);
            db.execSQL(wishlist);

        }
        else {
*/

       /* String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(" + VARIENT_ID + " integer primary key, "
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + " TEXT , "
                + COLUMN_NAME + " TEXT , "
                + COLUMN_PRICE + " DOUBLE , "
                + COLUMN_UNIT_VALUE + " TEXT , "
                + COLUMN_REWARDS + " DOUBLE , "
                + COLUMN_INCREAMENT + " DOUBLE , "
                + COLUMN_STOCK + " DOUBLE , "
                + COLUMN_TITLE + " TEXT , "
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_VATRATE + " TEXT,"
                + COLUMN_SUPPLIERID +" TEXT "
                + ")";

        String wishlist = "CREATE TABLE IF NOT EXISTS " + WISHLIST_TABLE
                + "(" + VARIENT_ID + " integer primary key, "
                + COLUMN_IMAGE + " TEXT , "
                + COLUMN_NAME + " TEXT , "
                + COLUMN_UNIT_VALUE + " TEXT , "
                + COLUMN_PRICE + " DOUBLE , "
                + COLUMN_DESCRIPTION + " TEXT ,"
                + COLUMN_SUPPLIERID +" TEXT "
                + ")";

        db.execSQL(exe);
        db.execSQL(wishlist);*/
        }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(" + VARIENT_ID + " integer primary key, "
                + PRODUCT_ID + " integer ,"
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + " TEXT , "
                + COLUMN_NAME + " TEXT , "
                + COLUMN_PRICE + " DOUBLE , "
                + COLUMN_UNIT_VALUE + " TEXT , "
                + COLUMN_REWARDS + " DOUBLE , "
                + COLUMN_INCREAMENT + " DOUBLE , "
                + COLUMN_STOCK + " DOUBLE , "
                + COLUMN_TITLE + " TEXT , "
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_VATRATE + " TEXT,"
                + COLUMN_SUPPLIERID +" TEXT "
                + ")";

        String wishlist = "CREATE TABLE IF NOT EXISTS " + WISHLIST_TABLE
                + "(" + VARIENT_ID + " integer primary key, "
                + COLUMN_IMAGE + " TEXT , "
                + COLUMN_NAME + " TEXT , "
                + COLUMN_UNIT_VALUE + " TEXT , "
                + COLUMN_PRICE + " DOUBLE , "
                + COLUMN_DESCRIPTION + " TEXT ,"
                + COLUMN_SUPPLIERID +" TEXT "
                + ")";

        db.execSQL(exe);
        db.execSQL(wishlist);

    }

    public boolean setCart(HashMap<String, String> map, Integer Qty) {
        SQLiteDatabase db = getWritableDatabase();
        if (isInCart(map.get(VARIENT_ID))) {
            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + Qty + "' where " + VARIENT_ID + "=" + map.get(VARIENT_ID));
            //db.close();
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(VARIENT_ID, map.get(VARIENT_ID));
            values.put(PRODUCT_ID, map.get(PRODUCT_ID));
            values.put(COLUMN_QTY, Qty);
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_SUPPLIERID, map.get(COLUMN_SUPPLIERID));
            values.put(COLUMN_VATRATE, map.get(COLUMN_VATRATE));
            values.put(COLUMN_INCREAMENT, map.get(COLUMN_INCREAMENT));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
            values.put(COLUMN_REWARDS, map.get(COLUMN_REWARDS));
            values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
            values.put(COLUMN_STOCK, map.get(COLUMN_STOCK));
            values.put(COLUMN_TITLE, map.get(COLUMN_TITLE));
            values.put(COLUMN_DESCRIPTION, map.get(COLUMN_DESCRIPTION));
            db.insert(CART_TABLE, null, values);
            //db.close();
            return true;
        }
    }

    public boolean isInCart(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        //db.close();
        return cursor.getCount() > 0;
    }

    public String getCartItemQty(String id) {

        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QTY));
    }

    public String getInCartItemQty(String id) {
        if (isInCart(id)) {
            SQLiteDatabase db = getReadableDatabase();
            String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            //db.close();
            return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QTY));
        } else {

            return "0.0";
        }
    }


    public String getInCartItemQtys(String id) {
        if (isInCart(id)) {
            SQLiteDatabase db = getReadableDatabase();
            String qry = "Select *  from " + CART_TABLE + " where " + VARIENT_ID + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            //db.close();
            return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QTY));
        } else {
            return "0";
        }
    }

    public int getCartCount() {
        int count = 0;
        boolean inExp = false;
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        try {
            count = cursor.getCount();
        } catch (Exception e) {
            inExp = true;
            e.printStackTrace();
        } finally {
            cursor.close();
            if (inExp) {
                db = getReadableDatabase();
                cursor = db.rawQuery(qry, null);
                count = cursor.getCount();
                cursor.close();
            }

        }
        //db.close();
        return count;
    }

    public String getTotalAmount() {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_QTY + " * " + COLUMN_PRICE + ") as total_amount  from " + CART_TABLE +" WHERE stock='Stock'";
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        //String total = cursor.getString(cursor.getColumnIndexOrThrow("total_amount"));

        if (cursor.getString(cursor.getColumnIndexOrThrow("total_amount")) != null) {
            @SuppressLint("DefaultLocale")
            String total=String.format( "%.2f", Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("total_amount"))) );
            return total;
        } else {
            return "0.00";
        }
    }

    public String getTotalAmount1() {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_QTY + " * " + COLUMN_PRICE + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndexOrThrow("total_amount"));

        //db.close();

        if (total != null) {

            return total;
        } else {
            return "0";
        }
    }


    public ArrayList<HashMap<String, String>> getCartAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(VARIENT_ID, cursor.getString(cursor.getColumnIndexOrThrow(VARIENT_ID)));
            map.put(PRODUCT_ID, cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QTY)));
            map.put(COLUMN_SUPPLIERID, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPLIERID)));
            map.put(COLUMN_VATRATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VATRATE)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            map.put(COLUMN_REWARDS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REWARDS)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_INCREAMENT, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INCREAMENT)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            map.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            list.add(map);
            cursor.moveToNext();
        }

        //db.close();
        return list;
    }

    public ArrayList<HashMap<String, String>> getInStockCartItem() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE+ " where " + COLUMN_STOCK + " = " + "Stock";
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(VARIENT_ID, cursor.getString(cursor.getColumnIndexOrThrow(VARIENT_ID)));
            map.put(PRODUCT_ID, cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QTY)));
            map.put(COLUMN_SUPPLIERID, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPLIERID)));
            map.put(COLUMN_VATRATE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VATRATE)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            map.put(COLUMN_REWARDS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REWARDS)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_INCREAMENT, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INCREAMENT)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            map.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            list.add(map);
            cursor.moveToNext();
        }

        //db.close();
        return list;
    }

    public String getColumnRewards() {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "SELECT rewards FROM " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String reward = cursor.getString(cursor.getColumnIndexOrThrow("rewards"));
        //db.close();
        if (reward != null) {

            return reward;
        } else {
            return "0";
        }
    }

    public String getFavConcatString() {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String concate = "";
        for (int i = 0; i < cursor.getCount(); i++) {
            if (concate.equalsIgnoreCase("")) {
                concate = cursor.getString(cursor.getColumnIndexOrThrow(VARIENT_ID));
            } else {
                concate = concate + "_" + cursor.getString(cursor.getColumnIndexOrThrow(VARIENT_ID));
            }
            cursor.moveToNext();
        }
        return concate;
    }

    public void clearCart() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE);
        //db.close();
    }

    public void removeItemFromCart(String id) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE + " where " + VARIENT_ID + " = " + id);
        //db.close();
    }




    //wishlist
    public boolean setWishlist(HashMap<String, String> map) {
        SQLiteDatabase db = getWritableDatabase();
        if (isInWishlist(map.get(VARIENT_ID))) {
            db.execSQL("delete from " + WISHLIST_TABLE + " where " + VARIENT_ID + " = " + map.get(VARIENT_ID));
            //db.close();
            return true;
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put(VARIENT_ID, map.get(VARIENT_ID));
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
            values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
            values.put(COLUMN_DESCRIPTION, map.get(COLUMN_DESCRIPTION));
            values.put(COLUMN_SUPPLIERID, map.get(COLUMN_SUPPLIERID));
            db.insert(WISHLIST_TABLE, null, values);
            //db.close();
            return true;
        }
    }
    public boolean isInWishlist(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + WISHLIST_TABLE + " where " + VARIENT_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        //db.close();
        return cursor.getCount() > 0;
    }
    public ArrayList<HashMap<String, String>> getWishlistAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + WISHLIST_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(VARIENT_ID, cursor.getString(cursor.getColumnIndexOrThrow(VARIENT_ID)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            map.put(COLUMN_SUPPLIERID, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPLIERID)));
            list.add(map);
            cursor.moveToNext();
        }
        //db.close();
        return list;
    }

    public void clearWishlist() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + WISHLIST_TABLE);
        //db.close();
    }

    public void removeItemFromWishlist(String id) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + WISHLIST_TABLE + " where " + VARIENT_ID + " = " + id);
        //db.close();
    }

    public int getWishlistCount() {
        int count = 0;
        boolean inExp = false;
        SQLiteDatabase db = getReadableDatabase();
        String qry = "Select *  from " + WISHLIST_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        try {
            count = cursor.getCount();
        } catch (Exception e) {
            inExp = true;
            e.printStackTrace();
        } finally {
            cursor.close();
            if (inExp) {
                db = getReadableDatabase();
                cursor = db.rawQuery(qry, null);
                count = cursor.getCount();
                cursor.close();
            }

        }
        //db.close();
        return count;
    }

}
