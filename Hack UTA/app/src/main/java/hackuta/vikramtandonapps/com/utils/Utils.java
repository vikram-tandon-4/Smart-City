package hackuta.vikramtandonapps.com.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Utils {

    /**
     * Method is used to check the internet connection
     *
     * @param pContext
     * @return
     */
    public static boolean isNetworkAvailable(Context pContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * display progress dialog
     *
     * @param pContext
     * @return
     */
//    public static Dialog showProgressDialog(Context pContext) {
//        Dialog progressDialog = new Dialog(pContext);
//        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        progressDialog.setContentView(R.layout.progress_layout);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        return progressDialog;
//    }
//
//    /**
//     * cancel progress dialog
//     *
//     * @param progressDialog
//     */
//    public static void cancelProgressDialog(Dialog progressDialog) {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }
}
