package helpout.vikramtandonapps.com.helpout.common;




import org.json.JSONObject;

import helpout.vikramtandonapps.com.helpout.model.CategoriesModel;
import helpout.vikramtandonapps.com.helpout.model.MessageResponseModel;
import helpout.vikramtandonapps.com.helpout.model.SignUpresponseModel;
import helpout.vikramtandonapps.com.helpout.model.UserResponseModel;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;


public interface Api_Interface {

    @FormUrlEncoded
    @POST("/users/register")
    void  signUp(@Field("data") JSONObject data, Callback<SignUpresponseModel> response);

    @FormUrlEncoded
    @POST("/users/login")
    void  login(@Field("data") JSONObject data, Callback<SignUpresponseModel> response);

    @GET("/categories")
    void getCategories( @Query("q") String abc, Callback<CategoriesModel> callback);

    @GET("/message")
    void getMessage( @Query("q") String abc, Callback<MessageResponseModel> callback);

    @FormUrlEncoded
    @POST("/users/query")
    void  getUsers(@Field("data") JSONObject data, Callback<UserResponseModel> response);

    @FormUrlEncoded
    @POST("/users/sms")
    void  verifyCode(@Field("data") JSONObject data, Callback<MessageResponseModel> response);

}
