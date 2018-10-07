package hackuta.vikramtandonapps.com.activity;


import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hackuta.vikramtandonapps.com.R;


public class ContactActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;
    private LinearLayout llSendEmailLayout, llCallLayout;
    private ImageView ivEmailCopy, ivPhoneNumberCopy;
    private Toolbar toolbar;
    private TextView toolBarHeader;
    private ClipboardManager clipboard;
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 54321;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mContext = this;
        initializeViews();
    }
    private void initializeViews() {

        ivEmailCopy = (ImageView) findViewById(R.id.ivEmailCopy);
        ivPhoneNumberCopy = (ImageView) findViewById(R.id.ivPhoneNumberCopy);

        llSendEmailLayout = (LinearLayout) findViewById(R.id.llSendEmailLayout);
        llCallLayout = (LinearLayout) findViewById(R.id.llCallLayout);
        ivEmailCopy.setOnClickListener(this);
        ivPhoneNumberCopy.setOnClickListener(this);
        llSendEmailLayout.setOnClickListener(this);
        llCallLayout.setOnClickListener(this);

        // Gets a handle to the clipboard service.
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            /*
        *
        * initializing toolbar
        *
        * */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeader = (TextView) findViewById(R.id.toolBarHeader);
        toolBarHeader.setText(getResources().getString(R.string.contact_details));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onClick(View view) {
        ClipData clip;
        Animation animFadein;
        switch (view.getId()) {
            case R.id.ivEmailCopy:
                animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                ivEmailCopy.startAnimation(animFadein);
                // Creates a new text clip to put on the clipboard
                 clip = ClipData.newPlainText("simple text",getString(R.string.email));
                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, R.string.email_copied,Toast.LENGTH_LONG).show();
                break;

            case R.id.ivPhoneNumberCopy:
                animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                ivPhoneNumberCopy.startAnimation(animFadein);
                // Creates a new text clip to put on the clipboard
                 clip = ClipData.newPlainText("simple text",getString(R.string.phone_number));
                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, R.string.phone_number_copied,Toast.LENGTH_LONG).show();
                break;

            case R.id.llCallLayout:
                makeCall(getString(R.string.phone_number));
                break;

            case R.id.llSendEmailLayout:
                sendEmail();
                break;
        }
    }

    private void sendEmail()
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ getString(R.string.email)});
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        email.putExtra(Intent.EXTRA_TEXT, "*");

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    private void makeCall(String phoneNumber) {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this,
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(ContactActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ContactActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }


    /*
* permission call back for calling
* */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 54321: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e("permission granted", "yes");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e("permission granted", "NO");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
