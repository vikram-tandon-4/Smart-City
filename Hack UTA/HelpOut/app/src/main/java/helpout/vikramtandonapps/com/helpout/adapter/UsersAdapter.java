package helpout.vikramtandonapps.com.helpout.adapter;


import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import helpout.vikramtandonapps.com.helpout.R;
import helpout.vikramtandonapps.com.helpout.common.Api_Interface;
import helpout.vikramtandonapps.com.helpout.common.AppConstant;
import helpout.vikramtandonapps.com.helpout.model.MessageResponseModel;
import helpout.vikramtandonapps.com.helpout.model.UsersModel;
import helpout.vikramtandonapps.com.helpout.utils.CircleTransform;
import helpout.vikramtandonapps.com.helpout.utils.Utils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private ArrayList<UsersModel> userList;
    private Context mContext;
    private String ID="id";
    private Dialog mProgressDialog;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName;
        public ImageView ivMessage, ivProfilePicture;
        CardView container;

        public MyViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            container = (CardView) view.findViewById(R.id.categoryContainer);
        }

        public void clearAnimation() {
            container.clearAnimation();
        }
    }


    public UsersAdapter(ArrayList<UsersModel> userList, Context mContext) {
        this.userList = userList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvUserName.setText(userList.get(position).getFirstName() +" "+userList.get(position).getLastName());
        Picasso.with(mContext).load(userList.get(position).getImageUrl()).error(R.drawable.user).placeholder(R.drawable.user).transform(new CircleTransform()).into((holder).ivProfilePicture);
        holder.ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMessageApi();
                mProgressDialog = Utils.showProgressDialog(mContext);
            }
        });

        setAnimation(holder.container, position);
    }



    @Override
    public int getItemCount() {
        return userList.size();
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

    private void callMessageApi() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstant.BASE_URL)
                .setClient(new OkClient(new OkHttpClient())).setLogLevel(RestAdapter.LogLevel.FULL).build();
        Api_Interface apiInterface = restAdapter.create(Api_Interface.class);
        apiInterface.getMessage("x", new Callback<MessageResponseModel>() {
            @Override
            public void success(MessageResponseModel messageModel, Response response) {
                if (messageModel != null) {

                    if(messageModel.getStatus().equals("dfgvbsdfvjhbs"))
                    Toast.makeText(mContext,"Your message has been sent to the Volunteer", Toast.LENGTH_SHORT).show();

                    }
                    Utils.cancelProgressDialog(mProgressDialog);
                }

            @Override
            public void failure(RetrofitError error) {
                Utils.cancelProgressDialog(mProgressDialog);
                Log.e("show error", error.getMessage());
                Toast.makeText(mContext,mContext.getString(R.string.some_thing_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
