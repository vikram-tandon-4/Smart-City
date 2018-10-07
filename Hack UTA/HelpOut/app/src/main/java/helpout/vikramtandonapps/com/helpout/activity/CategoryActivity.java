package helpout.vikramtandonapps.com.helpout.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

import helpout.vikramtandonapps.com.helpout.R;
import helpout.vikramtandonapps.com.helpout.adapter.CategoryAdapter;
import helpout.vikramtandonapps.com.helpout.common.Api_Interface;
import helpout.vikramtandonapps.com.helpout.common.AppConstant;
import helpout.vikramtandonapps.com.helpout.common.AppPreferences;
import helpout.vikramtandonapps.com.helpout.model.CategoriesModel;
import helpout.vikramtandonapps.com.helpout.utils.Utils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class CategoryActivity extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    private TextView toolBarHeader;
    private RelativeLayout noNetworkLayout;
    private AppBarLayout appBar;
    private RecyclerView rvCategories;
    private Button btnTryAgain;
    private ArrayList<String> mCategories;
    private CategoryAdapter mAdapter;
    private Dialog mProgressDialog;
    private Dialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        mContext = this;
        initializeViews();

    }

    /*
    *
    *   initializing views
    *
     *  */
    private void initializeViews() {


        noNetworkLayout = (RelativeLayout) findViewById(R.id.noNetworkLayout);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCategories.setLayoutManager(mLayoutManager);
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layout = new GridLayoutManager(mContext, numberOfColumns());
        rvCategories.setLayoutManager(layout);

        Log.e("phone number pref ", AppPreferences.getUserPhoneNumber(mContext));

        /*
        *
        * initializing toolbar
        *
        * */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeader = (TextView) findViewById(R.id.toolBarHeader);
        toolBarHeader.setText(getResources().getString(R.string.category_toolbar_text));

        toolbar.inflateMenu(R.menu.logout_menu);
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.log:
                        confirmationDialog();
                        return true;
                }
                return false;
            }
        });

        checkNetworkAndSetData();

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                btnTryAgain.startAnimation(animFadein);
                checkNetworkAndSetData();
            }
        });
    }


    private void confirmationDialog() {
        confirmDialog = Utils.showConfirmationDialog(mContext);
        confirmDialog.show();

        final TextView btnYes = (TextView) confirmDialog.findViewById(R.id.okLogout);
        final TextView btnNo = (TextView) confirmDialog.findViewById(R.id.cancelLogout);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                logout();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
    }


    private void logout() {
        Utils.logout(CategoryActivity.this);
    }

    /***
     *
     * check network and set data
     *
     * ***/
    private void checkNetworkAndSetData() {
        if (Utils.isNetworkAvailable(mContext)) {
            noNetworkLayout.setVisibility(View.GONE);
            mProgressDialog = Utils.showProgressDialog(mContext);
            populateData();


        } else {
            noNetworkLayout.setVisibility(View.VISIBLE);
            appBar.setVisibility(View.GONE);
            rvCategories.setVisibility(View.GONE);
        }
    }

    private void populateData() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstant.BASE_URL)
                .setClient(new OkClient(new OkHttpClient())).setLogLevel(RestAdapter.LogLevel.FULL).build();
        Api_Interface apiInterface = restAdapter.create(Api_Interface.class);
        apiInterface.getCategories("x", new Callback<CategoriesModel>() {
            @Override
            public void success(CategoriesModel categoriesModel, Response response) {
                if (categoriesModel != null) {

                    mCategories = new ArrayList<>();
                    mProgressDialog.cancel();
                    mCategories = categoriesModel.getCategories();
                    if (mCategories.size() > 0) {
                        mAdapter = new CategoryAdapter(mCategories, mContext);
                        rvCategories.setAdapter(mAdapter);
                        appBar.setVisibility(View.VISIBLE);
                        rvCategories.setVisibility(View.VISIBLE);

                    }
                    Utils.cancelProgressDialog(mProgressDialog);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.cancelProgressDialog(mProgressDialog);
                Log.e("show error", error.getMessage());

            }
        });
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}