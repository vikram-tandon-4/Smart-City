package hackuta.vikramtandonapps.com.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import hackuta.vikramtandonapps.com.R;
import hackuta.vikramtandonapps.com.adapter.SocialAdapter;
import hackuta.vikramtandonapps.com.models.WebDataModel;
import hackuta.vikramtandonapps.com.utils.Utils;

public class SocialActivity extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    private TextView toolBarHeader;
    private RelativeLayout noNetworkLayout;
    private AppBarLayout appBar;
    private RecyclerView rvCategories;
    private Button btnTryAgain;
    private SocialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext =this;
        initializeViews();
    }

    private void initializeViews() {

        noNetworkLayout = (RelativeLayout)findViewById(R.id.noNetworkLayout);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        rvCategories = (RecyclerView)findViewById(R.id.rvCategories);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCategories.setLayoutManager(mLayoutManager);
        rvCategories.setItemAnimator(new DefaultItemAnimator());



        /*
         *
         * initializing toolbar
         *
         * */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeader = (TextView) findViewById(R.id.toolBarHeader);
        toolBarHeader.setText("Connect on Social media");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        toolbar.inflateMenu(R.menu.home);
//        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.favorite:
//                        startActivity(new Intent(HomeActivity.this, FavoriteActivity.class));
//                        return true;
//                    case R.id.whatsHot:
//                        startActivity(new Intent(HomeActivity.this, WhatsHotActivity.class));
//                        return true;
//                }
//                return false;
//            }
//        });



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

    /***
     *
     * check network and set data
     *
     * ***/
    private void checkNetworkAndSetData()
    {
        if(Utils.isNetworkAvailable(mContext))
        {
            noNetworkLayout.setVisibility(View.GONE);
            populateData();

        }else{
            noNetworkLayout.setVisibility(View.VISIBLE);
            appBar.setVisibility(View.GONE);
            rvCategories.setVisibility(View.GONE);
        }
    }

    private void populateData() {
        mAdapter = new SocialAdapter(socialMedia(), mContext);
        rvCategories.setAdapter(mAdapter);
        appBar.setVisibility(View.VISIBLE);
        rvCategories.setVisibility(View.VISIBLE);
    }

    private ArrayList<WebDataModel> socialMedia(){
        ArrayList<WebDataModel> social = new ArrayList<>();

        WebDataModel webDataModel = new WebDataModel();
        webDataModel.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Facebook_logo_%28square%29.png/600px-Facebook_logo_%28square%29.png");
        webDataModel.setTitle("Facebook");
        webDataModel.setUrl("https://www.facebook.com/CityofArlington");
        social.add(webDataModel);

        webDataModel = new WebDataModel();
        webDataModel.setImageUrl("https://warriortradingnews.com/wp-content/uploads/2018/07/TWTR-Logo-696x591.png");
        webDataModel.setTitle("Twitter");
        webDataModel.setUrl("https://twitter.com/cityofarlington");
        social.add(webDataModel);


        webDataModel = new WebDataModel();
        webDataModel.setImageUrl("data:image/png;base64,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");
        webDataModel.setTitle("Pinterest");
        webDataModel.setUrl("http://pinterest.com/cityofarlington");

        webDataModel = new WebDataModel();
        webDataModel.setImageUrl("https://content.linkedin.com/content/dam/brand/site/img/logo/logo-hero.png");
        webDataModel.setTitle("LinkedIn");
        webDataModel.setUrl("https://www.linkedin.com/company/city-of-arlington");


        social.add(webDataModel);


        return social;

    }
}

