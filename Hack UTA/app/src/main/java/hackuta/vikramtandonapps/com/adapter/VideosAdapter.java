package hackuta.vikramtandonapps.com.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hackuta.vikramtandonapps.com.R;
import hackuta.vikramtandonapps.com.activity.YoutubeActivity;
import hackuta.vikramtandonapps.com.models.WebDataModel;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {

    private ArrayList<WebDataModel> companyList;
    private Context mContext;
    private String VIDEO_ID="videoId";

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


    public VideosAdapter(ArrayList<WebDataModel> companyList, Context mContext) {
        this.companyList = companyList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imagetitle, parent, false);

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
                Intent intent = new Intent(mContext, YoutubeActivity.class);
                intent.putExtra(VIDEO_ID, companyList.get(position).getUrl());
                mContext.startActivity(intent);

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
}
