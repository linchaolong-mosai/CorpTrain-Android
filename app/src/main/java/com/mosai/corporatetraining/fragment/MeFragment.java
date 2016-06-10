package com.mosai.corporatetraining.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.activity.ChangePasswordActivity;
import com.mosai.corporatetraining.activity.FeedbackActivity;
import com.mosai.corporatetraining.activity.LoginActivity;
import com.mosai.corporatetraining.activity.MainActivity;
import com.mosai.corporatetraining.activity.PersonalInfoActivity;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.util.AppManager;
import com.mosai.corporatetraining.util.DeleteDirectory;
import com.mosai.corporatetraining.util.LogUtils;
import com.mosai.corporatetraining.util.Utils;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.ui.CircleImageView;
import com.mosai.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * me
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private MaterialDialog mldgSignout;
    private Context mContext;
    private Button btnSignout;
    private TextView tvName, tvPersonalInfo, tvChangePassword, tvFeedback,tvClearMaterials;
    private CircleImageView ivHeadpotrait;
    private DisplayImageOptions options;
    public MeFragment() {
        // Required empty public constructor
    }
    private void setImageloaderOptions(){
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.blank_user_small)
                .showImageOnFail(R.drawable.blank_user_small)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }
    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        LogUtils.i("onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i("onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.i("onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.i("onCreateView");
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.i("onViewCreated");
        tvName = ViewUtil.findViewById(view, R.id.tvName);
        tvPersonalInfo = ViewUtil.findViewById(view, R.id.tvPersonalInfo);
        tvChangePassword = ViewUtil.findViewById(view, R.id.tvChangePassword);
        tvFeedback = ViewUtil.findViewById(view, R.id.tvFeedback);
        ivHeadpotrait = ViewUtil.findViewById(view,R.id.ivHeadpotrait);
        btnSignout = ViewUtil.findViewById(view,R.id.btn_signout);
        tvClearMaterials = ViewUtil.findViewById(view,R.id.tvClearOffline);
        setImageloaderOptions();
        initListener();
        initData();
    }

    private void initListener() {
        tvPersonalInfo.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        btnSignout.setOnClickListener(this);
        tvClearMaterials.setOnClickListener(this);
    }

    private void initData() {
        ImageLoader.getInstance().displayImage(UserPF.getInstance().getAvatarUrl(),ivHeadpotrait,options);
        mldgSignout = new MaterialDialog(mContext)
//                .setTitle("Tips")
                .setMessage(getString(R.string.signout_message))
                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mldgSignout.dismiss();
                        signout();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mldgSignout.dismiss();
                    }
                });
    }
    private void signout(){
        UserPF.getInstance().putString(UserPF.PASSWORD,"");
        AppManager.getAppManager().finishAllActivity();
        startActivity(new Intent(mContext, LoginActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        LogUtils.i("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i("onResume");
        tvName.setText(UserPF.getInstance().getString(UserPF.USER_NAME, ""));
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i("onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i("onDetach");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.i("onHiddenChanged hidden=" + hidden);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPersonalInfo:
                startActivityForResult(new Intent(getActivity(), PersonalInfoActivity.class),0);
                break;
            case R.id.tvChangePassword:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.tvFeedback:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.btn_signout:
                mldgSignout.show();
                break;
            case R.id.tvClearOffline:
                clearMaterials();
                break;
            default:
                break;
        }
    }

    private void clearMaterials() {
        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.clear_offline_course_materials_tips))
                .setPositiveButton(mContext.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AsyncTask<Void,Void,Boolean>(){
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                ((MainActivity)mContext).showTextProgressDialog(mContext.getString(R.string.clearing));
                            }

                            @Override
                            protected void onPostExecute(Boolean aVoid) {
                                super.onPostExecute(aVoid);
                                ((MainActivity)mContext).dismissTextProgressDialog();
                                ToastUtils.showToast(mContext,mContext.getString(aVoid?R.string.delete_success:R.string.delete_fail));
                            }

                            @Override
                            protected Boolean doInBackground(Void... params) {

                                return DeleteDirectory.deleteDir(new File(Utils.getMaterialsDir(mContext)));
                            }
                        }.execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
