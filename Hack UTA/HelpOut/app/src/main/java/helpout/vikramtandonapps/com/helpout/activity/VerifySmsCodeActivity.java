package helpout.vikramtandonapps.com.helpout.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import helpout.vikramtandonapps.com.helpout.R;
import helpout.vikramtandonapps.com.helpout.common.Api_Interface;
import helpout.vikramtandonapps.com.helpout.common.AppConstant;
import helpout.vikramtandonapps.com.helpout.common.AppPreferences;
import helpout.vikramtandonapps.com.helpout.model.MessageResponseModel;
import helpout.vikramtandonapps.com.helpout.model.SignUpresponseModel;
import helpout.vikramtandonapps.com.helpout.utils.Utils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VerifySmsCodeActivity extends AppCompatActivity{

    private TextView tvVerifyCode;
    private EditText etVerifyCode;
    private TextInputLayout tilVerifyCode;
    private Context mContext;
    String phoneNumber="",firstName="",lastName="";
    private Dialog mProgressDialog;
    private boolean signUp=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        mContext= this;



            signUp =getIntent().getBooleanExtra(AppConstant.IS_SIGN_UP,false);

        if(getIntent().getStringExtra(AppConstant.PHONE_NUMBER)!= null)
        {
            phoneNumber =  getIntent().getStringExtra(AppConstant.PHONE_NUMBER);
        }
        if(signUp) {
            if (getIntent().getStringExtra(AppConstant.FIRST_NAME) != null) {
                firstName = getIntent().getStringExtra(AppConstant.FIRST_NAME);
            }
            if (getIntent().getStringExtra(AppConstant.LAST_NAME) != null) {
                lastName = getIntent().getStringExtra(AppConstant.LAST_NAME);
            }
        }

        tvVerifyCode = (TextView) findViewById(R.id.tvVerifyCode);
        etVerifyCode = (EditText) findViewById(R.id.etVerifyCode);
        tilVerifyCode = (TextInputLayout) findViewById(R.id.tilVerifyCode);

        tvVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etVerifyCode.getText())) {
                    tilVerifyCode.setError(getString(R.string.error_message_code_empty));
                } else if (etVerifyCode.getText().length() < 4) {
                    tilVerifyCode.setError(getString(R.string.error_message_code_empty));
                }else{
                    if (Utils.isNetworkAvailable(mContext)) {
                        if(signUp) {
                            callSignUp();
                        }else{
                            callLogin();
                        }
                    }else{
                        Snackbar.make(tilVerifyCode, R.string.no_internet_title, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private void callLogin() {
        mProgressDialog = Utils.showProgressDialog(mContext);
        //create an adapter for retrofit with base url
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.BASE_URL).build();

        //creating a service for adapter with our GET class
        Api_Interface git = restAdapter.create(Api_Interface.class);
        String request = "";
        JSONObject _obj = new JSONObject();

        try {
            _obj.put(AppConstant.PHONE_NUMBER, phoneNumber);
            _obj.put(AppConstant.CODE, etVerifyCode.getText().toString());
            request = _obj.toString();
            Log.e("Request", request);
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.cancelProgressDialog(mProgressDialog);
        }

        git.login(_obj, new Callback<SignUpresponseModel>() {

            @Override
            public void success(SignUpresponseModel signUpresponseModel, Response response)
            {
                if(signUpresponseModel!=null) {
                    if (signUpresponseModel.getStatus().equals("success")) {
                        Toast.makeText(mContext,"Success", Toast.LENGTH_SHORT).show();
                        AppPreferences.setUserPhoneNumber(mContext, phoneNumber);
                        AppPreferences.setLogin(mContext,true);
                        Utils.login(VerifySmsCodeActivity.this);

                    } else {
                        Toast.makeText(mContext,"Login Failed", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("Login response::",signUpresponseModel.getStatus());
                }
                Utils.cancelProgressDialog(mProgressDialog);
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.cancelProgressDialog(mProgressDialog);
                Snackbar.make(tvVerifyCode, R.string.some_thing_went_wrong, Snackbar.LENGTH_LONG).show();
                Log.e("Error:",error.getMessage());
            }
        });
    }


    /**
     * call Sign Up API
     */
    private void callSignUp() {
        mProgressDialog = Utils.showProgressDialog(mContext);
        //create an adapter for retrofit with base url
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.BASE_URL).build();

        //creating a service for adapter with our GET class
        Api_Interface git = restAdapter.create(Api_Interface.class);
        String request = "";
        JSONObject _obj = new JSONObject();

        try {
            _obj.put(AppConstant.FIRST_NAME, firstName);
            _obj.put(AppConstant.LAST_NAME, lastName);
            _obj.put(AppConstant.PHONE_NUMBER, phoneNumber);
            _obj.put(AppConstant.CODE, etVerifyCode.getText().toString());
            request = _obj.toString();
            Log.e("Request", request);
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.cancelProgressDialog(mProgressDialog);
        }

        git.signUp(_obj, new Callback<SignUpresponseModel>() {

            @Override
            public void success(SignUpresponseModel signUpresponseModel, Response response)
            {
                if(signUpresponseModel!=null) {
                    if (signUpresponseModel.getStatus().equals("success")) {
                        Toast.makeText(mContext,"Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(VerifySmsCodeActivity.this, CategoryActivity.class));
                    }else if(signUpresponseModel.getStatus().equals("alreadyRegistered")){
                        Toast.makeText(mContext,"Already registered.Please Login.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mContext,"SignUp Failed", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("SignUp response::",signUpresponseModel.getStatus());
                }
                Utils.cancelProgressDialog(mProgressDialog);
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.cancelProgressDialog(mProgressDialog);
                Snackbar.make(tvVerifyCode, R.string.some_thing_went_wrong, Snackbar.LENGTH_LONG).show();
                Log.e("Error:",error.getMessage());
            }
        });
    }
    }

