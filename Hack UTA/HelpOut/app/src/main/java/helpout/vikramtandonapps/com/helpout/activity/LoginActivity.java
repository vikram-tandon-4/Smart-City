package helpout.vikramtandonapps.com.helpout.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    private TextInputLayout tilFirstName, tilLastName, tilPhone;
    private EditText etLastName, etFirstName, etPhone;
    private TextView tvDone, tvLogin, tvSignUp, tvTitle;
    private boolean signUp = true;
    private Dialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if (AppPreferences.alreadyLoggedIn(mContext)) {
            startActivity(new Intent(LoginActivity.this, CategoryActivity.class));
        }

        init();
    }

    private void init() {
        etLastName = (EditText) findViewById(R.id.etLastName);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        tilPhone = (TextInputLayout) findViewById(R.id.tilPhone);
        tilFirstName = (TextInputLayout) findViewById(R.id.tilFirstName);
        tilLastName = (TextInputLayout) findViewById(R.id.tilLastName);
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvLogin.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDone:
                validateUserData();
                break;

            case R.id.tvLogin:
                signUp = false;
                tvTitle.setText(getString(R.string.login));
                tvSignUp.setVisibility(View.VISIBLE);
                tvLogin.setVisibility(View.GONE);
                tilFirstName.setVisibility(View.GONE);
                tilLastName.setVisibility(View.GONE);
                Snackbar.make(tvDone, R.string.login, Snackbar.LENGTH_LONG).show();
                break;

            case R.id.tvSignUp:
                signUp = true;
                tvTitle.setText(getString(R.string.signup));
                tvSignUp.setVisibility(View.GONE);
                tvLogin.setVisibility(View.VISIBLE);
                tilFirstName.setVisibility(View.VISIBLE);
                tilLastName.setVisibility(View.VISIBLE);
                Snackbar.make(tvDone, R.string.login, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    private void validateUserData() {
        if (signUp) {
            if (TextUtils.isEmpty(etFirstName.getText())) {
                tilFirstName.setError(getString(R.string.error_message_first_name_empty));
            } else if (etFirstName.getText().length() < AppConstant.minimumAlphabets) {
                tilFirstName.setError(getString(R.string.error_message_first_name_minimum_characters));
            } else if (TextUtils.isEmpty(etLastName.getText())) {
                tilLastName.setError(getString(R.string.error_message_last_name_empty));
            } else if (etLastName.getText().length() < AppConstant.minimumAlphabets) {
                tilLastName.setError(getString(R.string.error_message_last_name_minimum_characters));
            } else if (TextUtils.isEmpty(etPhone.getText())) {
                tilPhone.setError(getString(R.string.error_message_phone_number));
            } else if (!PhoneNumberUtils.isGlobalPhoneNumber(etPhone.getText().toString())) {
                tilPhone.setError(getString(R.string.error_message_invalid_phone));
            } else {
                if (Utils.isNetworkAvailable(mContext)) {
                    callVerifyCodeApi(etFirstName.getText().toString(), etLastName.getText().toString(), etPhone.getText().toString(), signUp);
                } else {
                    Snackbar.make(etLastName, R.string.no_internet_title, Snackbar.LENGTH_LONG).show();
                }
            }
        } else {
            if (TextUtils.isEmpty(etPhone.getText())) {
                // Snackbar.make(etLastName, R.string.error_message_last_name_empty, Snackbar.LENGTH_LONG).show();
                tilPhone.setError(getString(R.string.error_message_phone_number));
            } else if (!PhoneNumberUtils.isGlobalPhoneNumber(etPhone.getText().toString())) {
                // Snackbar.make(etLastName, R.string.error_message_last_name_minimum_characters, Snackbar.LENGTH_LONG).show();
                tilPhone.setError(getString(R.string.error_message_invalid_phone));
            } else {
                callVerifyCodeApi("login", "login", etPhone.getText().toString(), signUp);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etFirstName.getText().length() > 1) {
                    tilFirstName.setErrorEnabled(false);
                    tilFirstName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etLastName.getText().length() > 1) {
                    tilLastName.setErrorEnabled(false);
                    tilLastName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPhone.getText().length() > 0) {
                    tilPhone.setErrorEnabled(false);
                    tilPhone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void callVerifyCodeApi(final String firstName, final String lastName, final String phone, final boolean signUp) {
        mProgressDialog = Utils.showProgressDialog(mContext);
        //create an adapter for retrofit with base url
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.BASE_URL).build();

        //creating a service for adapter with our GET class
        Api_Interface git = restAdapter.create(Api_Interface.class);
        String request = "";
        JSONObject _obj = new JSONObject();

        try {
            if (signUp) {
                _obj.put(AppConstant.FIRST_NAME, firstName);
                _obj.put(AppConstant.LAST_NAME, lastName);
            }
            _obj.put(AppConstant.PHONE_NUMBER, phone);
            request = _obj.toString();
            Log.e("Request", request);
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.cancelProgressDialog(mProgressDialog);
        }

        git.verifyCode(_obj, new Callback<MessageResponseModel>() {

            @Override
            public void success(MessageResponseModel messageResponseModel, Response response) {
                if (messageResponseModel != null) {
                    if (messageResponseModel.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Verification Code sent to your phone.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, VerifySmsCodeActivity.class);
                        if (signUp) {
                            intent.putExtra(AppConstant.FIRST_NAME, firstName);
                            intent.putExtra(AppConstant.LAST_NAME, lastName);
                        }
                        intent.putExtra(AppConstant.IS_SIGN_UP, signUp);
                        intent.putExtra(AppConstant.PHONE_NUMBER, phone);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        Log.e("Verify Sms api:: ", messageResponseModel.getStatus());
                    }
                }
                Utils.cancelProgressDialog(mProgressDialog);
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.cancelProgressDialog(mProgressDialog);
                Snackbar.make(tvDone, R.string.some_thing_went_wrong, Snackbar.LENGTH_LONG).show();
                Log.e("Error:", error.getMessage());
            }
        });
    }
}
