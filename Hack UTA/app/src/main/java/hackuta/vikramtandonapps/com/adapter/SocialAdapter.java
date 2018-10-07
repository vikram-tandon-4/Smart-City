package hackuta.vikramtandonapps.com.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.HashMap;

import hackuta.vikramtandonapps.com.R;
import hackuta.vikramtandonapps.com.activity.ContactActivity;
import hackuta.vikramtandonapps.com.activity.WebViewActivity;
import hackuta.vikramtandonapps.com.models.WebDataModel;


public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.MyViewHolder> {

    private ArrayList<WebDataModel> companyList;
    private Context mContext;

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


    public SocialAdapter(ArrayList<WebDataModel> companyList, Context mContext) {
        this.companyList = companyList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvCompany.setText(companyList.get(position).getTitle());
        Picasso.with(mContext)
                .load(companyList.get(position).getImageUrl()).fit()
                .into(holder.ivCompanyLogo);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    socialExternalUrl(companyList.get(position).getUrl());

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

    private void socialExternalUrl(String url){

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        mContext.startActivity(i);
    }
}
