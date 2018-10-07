package hackuta.vikramtandonapps.com.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hackuta.vikramtandonapps.com.R;
import hackuta.vikramtandonapps.com.adapter.SocialAdapter;
import hackuta.vikramtandonapps.com.adapter.VideosAdapter;
import hackuta.vikramtandonapps.com.models.WebDataModel;
import hackuta.vikramtandonapps.com.utils.Utils;

public class VideosActivity extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    private TextView toolBarHeader;
    private RelativeLayout noNetworkLayout;
    private AppBarLayout appBar;
    private RecyclerView rvCategories;
    private Button btnTryAgain;
    private VideosAdapter mAdapter;

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
        toolBarHeader.setText("Videos");
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
            mAdapter = new VideosAdapter(data(), mContext);
            noNetworkLayout.setVisibility(View.GONE);
            populateData();

        }else{
            noNetworkLayout.setVisibility(View.VISIBLE);
            appBar.setVisibility(View.GONE);
            rvCategories.setVisibility(View.GONE);
        }
    }

    private void populateData() {
        appBar.setVisibility(View.VISIBLE);
        rvCategories.setVisibility(View.VISIBLE);
    }

    private ArrayList<WebDataModel> data(){
        ArrayList<WebDataModel> social = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("videos");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> appData = (List<HashMap<String, Object>>) dataSnapshot.getValue();

                ArrayList<WebDataModel> webDataModels = new ArrayList<>();
                for(int  i=0; i< appData.size(); i++){
                    WebDataModel webDataModel = new WebDataModel();
                    webDataModel.setImageUrl(String.valueOf(appData.get(i).get("imageUrl")));
                    webDataModel.setTitle(String.valueOf(appData.get(i).get("title")));
                    webDataModel.setUrl(String.valueOf(appData.get(i).get("url")));

                    webDataModels.add(webDataModel);
                }
//                WebDataModel webDataModel = new WebDataModel();
//                webDataModel.setTitle(String.valueOf(appData.get("title")));
//                webDataModel.setUrl(String.valueOf(appData.get("url")));
                mAdapter = new VideosAdapter(webDataModels, mContext);
                rvCategories.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("value", "Failed to read value.", error.toException());
            }
        });
        return social;

    }
}

