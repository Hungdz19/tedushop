package com.example.demosdk3;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import vn.gzgame.sdk.ZXCGameSDK;
import vn.gzgame.sdk.dialogs.DialogLogin.OnLogoutListener;
import vn.gzgame.sdk.dialogs.DialogLogin.OnLoginListener;


import vn.gzgame.sdk.dialogs.DialogPayment.OnPaymentListener;
import vn.gzgame.sdk.utils.ScreenUtils;
import vn.gzgame.sdk.utils.ToastUtils;
import vn.gzgame.sdk.utils.Utils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ZXCGameSDK mSGameSDK;
    private Activity mActivity = this;
    TextView tvHeader;
    ScrollView scrollView;
    CallbackManager callbackManager;
    Button btLogin, btLogout, btPayment, btRotateScreen;
    LoginButton btFace;
    String serverId = "test_areaid";
    String serverName = "test_areaid";
    String roleName = "test_roleid";
    String roleId = "test_rolename";
    String roleLv = "test_rolelevel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btLogin = findViewById(R.id.btLogin);
        btLogout = findViewById(R.id.btLogout);
        btPayment = findViewById(R.id.btPayment);
        btRotateScreen = findViewById(R.id.btRotateScreen);


        mSGameSDK = new ZXCGameSDK(mActivity);
        vInitLogin();
        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                vn.gzgame.sdk.utils.AnimationUtils.vAnimationClick(view);
                mSGameSDK.login(mLoginListener);
            }
        });
    btLogout.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            vn.gzgame.sdk.utils.AnimationUtils.vAnimationClick(view);

               vInitLogout();
        }
    });

    btPayment.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {


          //  vn.gzgame.sdk.utils.AnimationUtils.vAnimationClick(view);
           mSGameSDK.setUserConfigBeforPayment(serverId, serverName, roleId, roleName, roleLv);
            String plf_product_id = "com.gzg.demo.coin1";
            mSGameSDK.pay(new OnPaymentListener() {
                @Override
                public void onSuccessful(String s) {
                    if (!s.equals("") && !s.equals("null")) {
                        Utils.toast(MainActivity.this, "Payment successful; orderId=" + s);
                    } else {
                        Utils.toast(MainActivity.this, "Payment successful");

                    }
                    updateUserInfo();


                }

                @Override
                public void onDismiss() {

                }
            },plf_product_id);
        }
    });
    btRotateScreen.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            vn.gzgame.sdk.utils.AnimationUtils.vAnimationClick(view);
            if (ScreenUtils.isPortrait(mActivity)) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

        }
    });
    }

    private void vInitLogin() {
        mSGameSDK.login(mLoginListener);
    }

    private void vInitLogout() {
        Log.e("this", "onLogoutSuccessful: ");
        mSGameSDK.logout(mLogoutListener);

    }

    private OnLoginListener mLoginListener = new OnLoginListener() {
        @Override
        public void onLoginSuccessful(String accountID, String
                accessToken) {
            new SeverRoleDialog(new OnChooseFinishedListener() {

                @Override
                public void onSuccess(String sId, String sName, String rId, String rName,
                                      String rLevel) {
                    // TODO Auto-generated method stub
                    //mSGameSDK.setUserConfig(sId, sName, rId, rName, rLevel);
                    serverId = sId;
                    serverName = sName;
                    roleId = rId;
                    roleName = rName;
                    roleLv = rLevel;
                    mSGameSDK.setUserConfig("a1", sName, "1000055", rName, rLevel);
                    Log.e("Demo", sId + " | " + rId + " | " + rName + " | "
                            + rLevel);
                }
            }).show(getFragmentManager(),"");
            btLogin.setVisibility(View.GONE);

            btLogout.setVisibility(View.VISIBLE);
            btPayment.setVisibility(View.VISIBLE);
            updateUserInfo();
            // --

// TODO Auto-generated method stub
        }

        @Override
        public void onLoginFailed(String reason) {
// TODO Auto-generated method stub
        }
    };
    private OnLogoutListener mLogoutListener = new OnLogoutListener() {

        @Override
        public void onLogoutSuccessful() {
            Log.e("this", "alo tôi ngh e ");
            mSGameSDK.login(mLoginListener);
        }
    };

    @Override
    protected void onResume() {
// TODO Auto-generated method stub
        mSGameSDK.onResume(getIntent());
        super.onResume();
    }

    @Override
    protected void onPause() {
// TODO Auto-generated method stub
        mSGameSDK.onPause();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int
            resultCode, Intent data) {
// TODO Auto-generated method stub

//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        mSGameSDK.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        mSGameSDK.onDestroy();
        super.onDestroy();
    }


    private void updateUserInfo() {

        TextView userName = (TextView) findViewById(R.id.userName);
        TextView userId = (TextView) findViewById(R.id.userId);
        userName.setText(Utils.getUserName(mActivity));
        userId.setText("ID: " + Utils.getUserId(mActivity));
    }

}