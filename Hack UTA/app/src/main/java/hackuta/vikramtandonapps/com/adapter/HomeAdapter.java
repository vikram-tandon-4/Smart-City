package hackuta.vikramtandonapps.com.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import hackuta.vikramtandonapps.com.R;
import hackuta.vikramtandonapps.com.activity.ContactActivity;
import hackuta.vikramtandonapps.com.activity.SocialActivity;
import hackuta.vikramtandonapps.com.activity.VideosActivity;
import hackuta.vikramtandonapps.com.activity.WebViewActivity;
import hackuta.vikramtandonapps.com.activity.YoutubeActivity;
import hackuta.vikramtandonapps.com.models.WebDataModel;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private ArrayList<String> companyList;
    private ArrayList<Integer> companyLogoList;
    private Context mContext;

    private String URL = "url";
    private String url ="www.google.com";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCompany;
        public ImageView ivCompanyLogo;
        CardView container;

        public MyViewHolder(View view) {
            super(view);
            tvCompany = (TextView) view.findViewById(R.id.tvCompany);
            ivCompanyLogo = (ImageView) view.findViewById(R.id.ivCompanyLogo);
            container = (CardView) view.findViewById(R.id.categoryContainer);
        }

        public void clearAnimation() {
            container.clearAnimation();
        }
    }


    public HomeAdapter(ArrayList<String> companyList, Context mContext, ArrayList<Integer> companyLogoList) {
        this.companyList = companyList;
        this.companyLogoList = companyLogoList;
        this.mContext = mContext;
        // Obtain the FirebaseAnalytics instance.
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvCompany.setText(companyList.get(position));
//        Picasso.with(mContext)
//                .load(companyLogoList.get(position)).fit()
//                .into(holder.ivCompanyLogo);
        holder.ivCompanyLogo.setImageResource(companyLogoList.get(position));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position == 0){
                    mContext.startActivity(new Intent(mContext, ZopimChatActivity.class));
                }
                if(position == 1){
                    mContext.startActivity(new Intent(mContext, VideosActivity.class));
                }
                if(position == 2){
                    loadWebView("survey");
                }
                if(position == 3){
                    loadWebView("housingData");
                }
                if(position == 4){
                    mContext.startActivity(new Intent(mContext, ContactActivity.class));
                }
                if(position == 5){
                    mContext.startActivity(new Intent(mContext, SocialActivity.class));
                }
                if(position == 6){
                    loadWebView("transportData");
                }
            }
        });

        setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
    }
    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        (holder).clearAnimation();
    }

    private void loadWebView(String key){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(key);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> appData = (HashMap<String, Object>) dataSnapshot.getValue();
                WebDataModel webDataModel = new WebDataModel();
                webDataModel.setTitle(String.valueOf(appData.get("title")));
                webDataModel.setUrl(String.valueOf(appData.get("url")));

                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(URL,webDataModel);
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("value", "Failed to read value.", error.toException());
            }
        });
    }
}
