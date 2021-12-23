package com.example.demosdk3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.gzgame.sdk.utils.DialogUtils;
import vn.gzgame.sdk.utils.NetworkUtils;
import vn.gzgame.sdk.utils.ToastUtils;

import android.view.View.OnClickListener;

@SuppressLint("ValidFragment")
public class SeverRoleDialog extends DialogFragment implements OnClickListener {
    private Activity mActivity;
    private SeverRoleAdapter appAdapter;
    private int countRetry = 0;

    private ListView lv1;
    private Button btReload;
    private TextView tv;
    private String type;
    private JSONArray jsonArray;

    private String sId, sName, rId, rName, rLevel;
  private OnChooseFinishedListener onChooseFinishedListener;

    @SuppressLint("ValidFragment")
    public SeverRoleDialog(OnChooseFinishedListener onChooseFinishedListener) {
        this.onChooseFinishedListener = onChooseFinishedListener;
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        mActivity = activity;
        super.onAttach(activity);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        type = "server";
        Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Holo_NoActionBar_Fullscreen);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimSlideUpDown;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.dialogseverrole);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        return true;
                }
                return false;
            }
        });
        vInitUI(dialog);

        vInitData();
        lv1 = dialog.findViewById(R.id.lv1);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int p,
                                    long arg3) {
                // TODO Auto-generated method stub
                SeverRoleItem item = appAdapter.getItem(p);
                if (type.equals("server")) {
                    sId = item.id;
                    sName = item.name;
                    tv.setText("Choose role");
                    jsonArray = item.jsonArray;
                    appAdapter = new SeverRoleAdapter(mActivity);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json;
                        try {
                            json = jsonArray.getJSONObject(i);
                            String id = json.getString("role_id");
                            String name = json.getString("role_name");
                            String level = "1";
                            appAdapter.add(new SeverRoleItem(id, name, level, null));
                            appAdapter.notifyDataSetChanged();
                            lv1.setAdapter(appAdapter);
                            lv1.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    type = "role";
                } else {
                    rId = item.id;
                    rName = item.name;
                    rLevel = item.level;
                    onChooseFinishedListener.onSuccess(sId, sName, rId, rName, rLevel);
                    dismiss();
                }
            }
        });

//        btReload.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (NetworkUtils.isInternetConnected(mActivity)){
//                    vInitData();
//                }else {
//                    ToastUtils.vToastErrorNetwork(mActivity);
//                }
//            }
//        });
        return dialog;
//            @Override
//    public void onClick(View view) {
//
//    }
    }


    private void vInitUI(Dialog dialog) {
        tv = (TextView) dialog.findViewById(R.id.tv);
        tv.setText("Choose server");
        btReload = (Button) dialog.findViewById(R.id.btReload);
        btReload.setOnClickListener((View.OnClickListener) this);
    }

    private void vInitData() {
        // show progress
//        DialogUtils.vDialogLoadingShowProcessing(mActivity, true);
        // --
        DialogUtils.vDialogLoadingShowProcessing(mActivity,true);
        btReload.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONArray jsonObject;
                   jsonObject = new JSONArray("[{\"area_id\":1,\"area_name\":\"server 1\",\"roles\":[{\"role_id\":1,\"role_name\":\"character 1\"},{\"role_id\":2,\"role_name\":\"character 2\"}]},{\"area_id\":2,\"area_name\":\"server 2\",\"roles\":[{\"role_id\":3,\"role_name\":\"character 3\"},{\"role_id\":4,\"role_name\":\"character 4\"}]},{\"area_id\":3,\"area_name\":\"server 3\",\"roles\":[{\"role_id\":5,\"role_name\":\"character 5\"},{\"role_id\":6,\"role_name\":\"character 6\"}]}]");
//                 jsonObject = new JSONArray("20428770772eaf774e6d09f6cbda2427");
                    DialogUtils.vDialogLoadingDismiss();
                    // --
                    vParseJson(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void vParseJson(JSONArray jsonArray) {
        try {
            if (jsonArray != null) {
                appAdapter = new SeverRoleAdapter(mActivity);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = jsonArray.getJSONObject(i);
                    String sId = json.getString("area_id");
                    String sName = json.getString("area_name");
                    appAdapter.add(new SeverRoleItem(sId, sName, "", json.getJSONArray("roles")));
                }
                appAdapter.notifyDataSetChanged();
                lv1.setAdapter(appAdapter);
                lv1.setVisibility(View.VISIBLE);
            } else {
                vRetry();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            vRetry();
            e.printStackTrace();
        }

    }

    private void vRetry() {
        if (NetworkUtils.isInternetConnected(mActivity) && countRetry < 3) {
            countRetry = countRetry + 1;
            vInitData();
        } else {
            btReload.setVisibility(View.VISIBLE);
        }
        Log.i(SeverRoleDialog.class.getSimpleName(), "Retry: " + Integer.toString(countRetry));
    }

    public static final int TIMEOUT_CONNECTION = 15000;
    public static final int TIMEOUT_SOCKET = 15000;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btReload) {
            if (NetworkUtils.isInternetConnected(mActivity)) {
                vInitData();
            } else {
                ToastUtils.vToastErrorNetwork(mActivity);
            }
        }
    }


}
