package helpout.vikramtandonapps.com.helpout.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import helpout.vikramtandonapps.com.helpout.R;
import helpout.vikramtandonapps.com.helpout.adapter.UsersAdapter;
import helpout.vikramtandonapps.com.helpout.common.Api_Interface;
import helpout.vikramtandonapps.com.helpout.common.AppConstant;
import helpout.vikramtandonapps.com.helpout.common.AppPreferences;
import helpout.vikramtandonapps.com.helpout.model.UserResponseModel;
import helpout.vikramtandonapps.com.helpout.model.UsersModel;
import helpout.vikramtandonapps.com.helpout.utils.Utils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class UserDetailActivity extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    private TextView toolBarHeader;
    private RelativeLayout noNetworkLayout;
    private AppBarLayout appBar;
    private RecyclerView rvCategories;
    private Button btnTryAgain;
    private ArrayList<UsersModel> mUsers;
    private UsersAdapter mAdapter;
    private Dialog mProgressDialog;
    private String category = "";
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

        category = getIntent().getStringExtra(AppConstant.CATEGORY);

        noNetworkLayout = (RelativeLayout) findViewById(R.id.noNetworkLayout);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCategories.setLayoutManager(mLayoutManager);
        rvCategories.setItemAnimator(new DefaultItemAnimator());
//        GridLayoutManager layout = new GridLayoutManager(mContext, numberOfColumns());
//        rvCategories.setLayoutManager(layout);


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


//    private void logout() {
//        Utils.logout(UserDetailActivity.this);
//        confirmationDialog();
//    }

    /***
     *
     * check network and set data
     *
     * ***/
    private void checkNetworkAndSetData() {
        if (Utils.isNetworkAvailable(mContext)) {
            noNetworkLayout.setVisibility(View.GONE);
            populateData();


        } else {
            noNetworkLayout.setVisibility(View.VISIBLE);
            appBar.setVisibility(View.GONE);
            rvCategories.setVisibility(View.GONE);
        }
    }

    private void populateData() {

        mProgressDialog = Utils.showProgressDialog(mContext);
        //create an adapter for retrofit with base url
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.BASE_URL).build();

        //creating a service for adapter with our GET class
        Api_Interface git = restAdapter.create(Api_Interface.class);
        String request = "";
        JSONObject _obj = new JSONObject();

        try {
            _obj.put(AppConstant.IS_VOLUNTEER, false);
            _obj.put(AppConstant.CATEGORY, category);
            request = _obj.toString();
            Log.e("Request", request);
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.cancelProgressDialog(mProgressDialog);
        }

        git.getUsers(_obj, new Callback<UserResponseModel>() {

            @Override
            public void success(UserResponseModel userResponseModel, Response response) {
                if (userResponseModel != null && userResponseModel.getUsers().size() > 0) {

                    mAdapter = new UsersAdapter(userResponseModel.getUsers(), mContext);
                    rvCategories.setAdapter(mAdapter);
                    appBar.setVisibility(View.VISIBLE);
                    rvCategories.setVisibility(View.VISIBLE);

                }
                Utils.cancelProgressDialog(mProgressDialog);
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.cancelProgressDialog(mProgressDialog);
                Snackbar.make(rvCategories, R.string.some_thing_went_wrong, Snackbar.LENGTH_LONG).show();
                Log.e("Error:", error.getMessage());
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
                Utils.logout(UserDetailActivity.this);

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
    }
}