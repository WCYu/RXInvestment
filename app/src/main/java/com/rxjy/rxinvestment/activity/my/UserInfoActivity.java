package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.entity.PersonDataBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 个人资料
* */

public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout rlBackground;
    @Bind(R.id.ic_icon)
    ImageView icIcon;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_trydate)
    TextView tvTrydate;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.rl_sex)
    RelativeLayout rlSex;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.rl_phone)
    RelativeLayout rlPhone;
    @Bind(R.id.et_email)
    TextView etEmail;
    @Bind(R.id.rl_email)
    RelativeLayout rlEmail;
    @Bind(R.id.tv_ismarry)
    TextView tvIsmarry;
    @Bind(R.id.rl_ismarry)
    RelativeLayout rlIsmarry;
    @Bind(R.id.tv_ztwo)
    TextView tvZtwo;
    @Bind(R.id.rl_identity)
    RelativeLayout rlIdentity;
    @Bind(R.id.tv_zthree)
    TextView tvZthree;
    @Bind(R.id.rl_shenqing)
    RelativeLayout rlShenqing;
    @Bind(R.id.tv_join)
    TextView tvJoin;
    @Bind(R.id.tv_gcid)
    TextView tvGcid;
    @Bind(R.id.tv_team)
    TextView tvTeam;
    @Bind(R.id.tv_userId)
    TextView tvUserId;

    private SimpleAdapter simpleAdapter;

    private ArrayList<String> imgList;

    @Override
    public int getLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void intData() {
        showLoading();
        tvTitle.setText("个人资料");
        rlBackground.setBackgroundColor(getResources().getColor(R.color.touming));
        getUserData();
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.ic_icon, R.id.rl_identity, R.id.rl_shenqing})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_back://返回
                finish();
                break;
            case R.id.ic_icon:
                checkIco(10001);
                break;
            case R.id.rl_identity:
                intent = new Intent(UserInfoActivity.this, UserInfoModifyActivity.class);
                break;
            case R.id.rl_shenqing:
                intent = new Intent(UserInfoActivity.this, EntryInformationActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    private void checkIco(int code) {

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .scaleEnabled(false)// 裁剪是否可放大缩小图片 true or false
                .forResult(code);//结果回调onActivityResult code 

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10001:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    //上传。。。。
                    Glide.with(UserInfoActivity.this).load(imgList.get(0)).apply(RequestOptions.circleCropTransform()).into(icIcon);
                    Log.e("图片：", imgList.get(0));
                }
                break;
        }
    }

    public void getUserData() {
        Map map = new HashMap();
        map.put("Phone", MySharedPreferences.getInstance().getUserPhone());
        map.put("Type", "1");
        OkhttpUtils.doGet(PathUrl.USERDATAURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取用户信息", data);
                PersonBean info = JSONUtils.toObject(data, PersonBean.class);
                PersonDataBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    tvUsername.setText(body.getName());
                    tvName.setText(body.getName());
                    tvPhone.setText(body.getPhone());
                    tvSex.setText(body.getSex());
                    tvUserId.setText(body.getCardNo());
                    tvTeam.setText(body.getPostName());
                    tvGcid.setText(body.getDepartName());
                    tvTrydate.setText(body.getTryHillockTime());
                    if (body.getZ2IsFinsh() == 1) {
                        tvZtwo.setVisibility(View.GONE);
                    } else {
                        tvZtwo.setVisibility(View.VISIBLE);
                    }
                    if (body.getZ3IsFinsh() == 1) {
                        tvZthree.setVisibility(View.GONE);
                    } else {
                        tvZthree.setVisibility(View.VISIBLE);
                    }
                    if (!TextUtils.isEmpty(body.getImage())) {
                        Glide.with(UserInfoActivity.this).load(body.getImage()).apply(RequestOptions.circleCropTransform()).into(icIcon);
                    }
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
                dismissLoading();
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取用户信息失败", message);
                dismissLoading();
                ToastUtil.getInstance().toastCentent("获取用户信息失败");
            }
        });
        setProgressDialog(3000);
    }

}
