//package com.example.demosdk3;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import vn.gzgame.sdk.utils.DialogUtils;
//import vn.gzgame.sdk.utils.NetworkUtils;
//import vn.gzgame.sdk.utils.ToastUtils;
//
//
//public class SeverRoleDialog1 extends DialogFragment implements View.OnClickListener {
//
//   private Activity mActivity;
//   private SeverRoleAdapter adapter;
//   private int countretry =0;
//
//    private ListView lv;
//    private Button btReload;
//    private TextView tv;
//    private String type;
//    private JSONArray jsonArray;
//
//    private String sId, sName, rId, rName, rLevel;
//    private OnChooseFinishedListener onChooseFinishedListener;
//
//
//    public SeverRoleDialog1(OnChooseFinishedListener onChooseFinishedListener) {
//        this.onChooseFinishedListener =onChooseFinishedListener;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        mActivity=activity;
//        super.onAttach(activity);
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        type ="sever";
//        Dialog dialog= new Dialog(mActivity, android.R.style.Theme_Holo_NoActionBar_Fullscreen);
//        dialog.getWindow().getAttributes().windowAnimations=R.style.dialogAnimSlideUpDown;
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
//        dialog.setContentView(R.layout.fragment_sever_role_dialog1);
//        dialog.show();
//        dialog.setCancelable(false);
//         dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//             @Override
//             public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                switch (i){
//                    case KeyEvent.KEYCODE_BACK:
//                        return  true;
//
//                }
//                 return false;
//             }
//         });
////         vInitUI(dialog);
////         iInitData();
////        vInitUI(dialog);
////        vInitUIData();
//        vInitUIData();
//        vInitUI(dialog);
//
//         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//             @Override
//             public void onItemClick(AdapterView<?> adapterView, View view, int a, long l) {
//                 SeverRoleItem item=adapter.getItem(a);
//                 if (type.equals("server")) {
//                     sId = item.id;
//                     sName = item.name;
//                     tv.setText("Choose role");
//                     jsonArray = item.jsonArray;
//                     adapter = new SeverRoleAdapter(mActivity);
//                     for(int i = 0; i < jsonArray.length(); i++){
//                         JSONObject json;
//                         try {
//                             json = jsonArray.getJSONObject(i);
//                             String id = json.getString("role_id");
//                             String name = json.getString("role_name");
//                             String level = "1";
//                             adapter.add(new SeverRoleItem(id, name, level, null));
//                             adapter.notifyDataSetChanged();
//                             lv.setAdapter(adapter);
//                             lv.setVisibility(View.VISIBLE);
//                         } catch (JSONException e) {
//                             // TODO Auto-generated catch block
//                             e.printStackTrace();
//                         }
//
//                     }
//                     type = "role";
//                 } else {
//                     rId = item.id;
//                     rName = item.name;
//                     rLevel = item.level;
//                     onChooseFinishedListener.onSuccess(sId, sName, rId, rName, rLevel);
//                     dismiss();
//                 }
//             }
//
//         });
//         return dialog;
//    }
//
//    private void vInitUI(Dialog dialog) {
//        tv = (TextView) dialog.findViewById(R.id.tv);
//        tv.setText("Choose server");
//
//        lv = (ListView) dialog.findViewById(R.id.lv);
//        btReload = (Button) dialog.findViewById(R.id.btReload);
//        btReload.setOnClickListener((View.OnClickListener) this);
//    }
//
//    private void vInitUIData() {
//        DialogUtils.vDialogLoadingShowProcessing(mActivity,true);
//        btReload.setVisibility(View.GONE);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final JSONArray jsonObject;
//                    jsonObject = new JSONArray("[{\"area_id\":1,\"area_name\":\"server 1\",\"roles\":[{\"role_id\":1,\"role_name\":\"character 1\"},{\"role_id\":2,\"role_name\":\"character 2\"}]},{\"area_id\":2,\"area_name\":\"server 2\",\"roles\":[{\"role_id\":3,\"role_name\":\"character 3\"},{\"role_id\":4,\"role_name\":\"character 4\"}]},{\"area_id\":3,\"area_name\":\"server 3\",\"roles\":[{\"role_id\":5,\"role_name\":\"character 5\"},{\"role_id\":6,\"role_name\":\"character 6\"}]}]");
////                 jsonObject = new JSONArray("20428770772eaf774e6d09f6cbda2427");
//                    DialogUtils.vDialogLoadingDismiss();
//                    // --
//                    vParseJson(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void vParseJson(JSONArray jsonObject) {
//        try {
//            if (jsonArray != null) {
//                adapter = new SeverRoleAdapter(mActivity);
//                for(int i = 0; i < jsonArray.length(); i++){
//                    JSONObject json = jsonArray.getJSONObject(i);
//                    String sId = json.getString("area_id");
//                    String sName = json.getString("area_name");
//                    adapter.add(new SeverRoleItem(sId, sName, "", json.getJSONArray("roles")));
//                }
//                adapter.notifyDataSetChanged();
//                lv.setAdapter(adapter);
//                lv.setVisibility(View.VISIBLE);
//            } else {
//                vRetry();
//            }
//        } catch (JSONException e) {
//            vRetry();
//            e.printStackTrace();
//        }
//    }
//
//    private void vRetry() {
//        if (NetworkUtils.isInternetConnected(mActivity) && countretry < 3) {
//            countretry = countretry + 1;
//            vInitUIData();
//        } else {
//            btReload.setVisibility(View.VISIBLE);
//        }
//        Log.i(SeverRoleDialog1.class.getSimpleName(), "Retry: " + Integer.toString(countretry));
//    }
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.btReload) {
//            if (NetworkUtils.isInternetConnected(mActivity)) {
//                vInitUIData();
//            } else {
//                ToastUtils.vToastErrorNetwork(mActivity);
//            }
//        }
//    }
//
//
//}