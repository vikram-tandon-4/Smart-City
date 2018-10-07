package hackuta.vikramtandonapps.com.activity;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.zopim.android.sdk.api.ZopimChat;

import java.util.ArrayList;

import hackuta.vikramtandonapps.com.R;
import hackuta.vikramtandonapps.com.adapter.HomeAdapter;
import hackuta.vikramtandonapps.com.utils.Utils;

public class HomeActivity extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    private TextView toolBarHeader;
    private RelativeLayout noNetworkLayout;
    private AppBarLayout appBar;
    private RecyclerView rvCategories;
    private Button btnTryAgain;
    private ArrayList<String> mCompanies;
    private HomeAdapter mAdapter;

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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvCategories.setLayoutManager(mLayoutManager);
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layout = new GridLayoutManager(mContext, numberOfColumns());
        rvCategories.setLayoutManager(layout);


        /*
         *
         * initializing toolbar
         *
         * */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeader = (TextView) findViewById(R.id.toolBarHeader);
        toolBarHeader.setText(getResources().getString(R.string.app_name));

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
        ZopimChat.init("63qE83QVB5eKpuxpfig3mZAoWxdC1mrP");


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
        mAdapter = new HomeAdapter(category(), mContext, placeHolder());
        rvCategories.setAdapter(mAdapter);
        appBar.setVisibility(View.VISIBLE);
        rvCategories.setVisibility(View.VISIBLE);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 700;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private ArrayList<String> category(){
        ArrayList<String> companyName = new ArrayList<String>();
        companyName.add("Chat");
        companyName.add("Videos");
        companyName.add("Survey");
        companyName.add("Housing");
        companyName.add("Contact Details");
        companyName.add("Social Media");
        companyName.add("Transport");

    return companyName;
    }

    private ArrayList<Integer> placeHolder(){

        ArrayList<Integer> comapanyLogo = new ArrayList<Integer>();

        comapanyLogo.add(R.drawable.ic_chat_black_48dp);
        comapanyLogo.add(R.drawable.ic_video_library_black_48dp);
        comapanyLogo.add(R.drawable.ic_assignment_black_48dp);
        comapanyLogo.add(R.drawable.ic_home_black_48dp);
        comapanyLogo.add(R.drawable.ic_contact_mail_black_24dp);
        comapanyLogo.add(R.drawable.ic_forum_black_24dp);
        comapanyLogo.add(R.drawable.ic_directions_transit_black_24dp);

        return comapanyLogo;
    }

}

