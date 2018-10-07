package helpout.vikramtandonapps.com.helpout.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import java.util.ArrayList;

import helpout.vikramtandonapps.com.helpout.R;
import helpout.vikramtandonapps.com.helpout.activity.UserDetailActivity;
import helpout.vikramtandonapps.com.helpout.common.AppConstant;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private ArrayList<String> categoryList;
    private Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCategory;
        CardView container;

        public MyViewHolder(View view) {
            super(view);
            tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            container = (CardView) view.findViewById(R.id.categoryContainer);
        }

        public void clearAnimation() {
            container.clearAnimation();
        }
    }


    public CategoryAdapter(ArrayList<String> categoryList, Context mContext) {
        this.categoryList = categoryList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvCategory.setText(categoryList.get(position));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra(AppConstant.CATEGORY, categoryList.get(position));
                mContext.startActivity(intent);

            }
        });

        setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
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
