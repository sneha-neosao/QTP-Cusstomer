package util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.jsoup.Jsoup;


public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

        String newVersion = "";
        String currentVersion = "";
        WSCallerVersionListener mWsCallerVersionListener;
        boolean isVersionAvailabel;
        boolean isAvailableInPlayStore;
        Context mContext;
        String mStringCheckUpdate = "";

        public GooglePlayStoreAppVersionNameLoader(Context mContext, WSCallerVersionListener callback) {
            mWsCallerVersionListener = callback;
            this.mContext = mContext;
        }

//        +mContext.getPackageName()
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                isAvailableInPlayStore = true;
                if (isNetworkAvailable(mContext)) {
                    // mStringCheckUpdate = Jsoup.connect("https://play.google.com/store/apps/details?id=videoeditor.videomaker.slideshow.fotoplay")
                    mStringCheckUpdate = Jsoup.connect("https://play.google.com/store/apps/details?id="+mContext.getPackageName())
                            .timeout(10000)
                            .get()
                            .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                            .first()
                            .ownText();
                    return mStringCheckUpdate;
                }

            } catch (Exception e) {
                isAvailableInPlayStore = false;
                return mStringCheckUpdate;
            } catch (Throwable e) {
                isAvailableInPlayStore = false;
                return mStringCheckUpdate;
            }
            return mStringCheckUpdate;
        }

        @Override
        protected void onPostExecute(String string) {
            if (isAvailableInPlayStore == true) {
                newVersion = string;
                Log.e("new Version", newVersion);
                checkApplicationCurrentVersion();
                if (currentVersion.equalsIgnoreCase(newVersion)) {
                    isVersionAvailabel = false;
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.app_upto_date), Toast.LENGTH_LONG).show();
                } else {
                    isVersionAvailabel = true;
                }
                mWsCallerVersionListener.onGetResponse(isVersionAvailabel);
            }
        }

        /**
         * Method to check current app version
         */
        public void checkApplicationCurrentVersion() {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            assert packageInfo != null;
            currentVersion = packageInfo.versionName;
            Log.e("currentVersion", currentVersion);
        }

        /**
         * Method to check internet connection
         * @param context
         * @return
         */
        public boolean isNetworkAvailable(Context context) {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        }
    }