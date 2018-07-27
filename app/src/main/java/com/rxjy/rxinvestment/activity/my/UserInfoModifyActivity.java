package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.IdentityInfoBean;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.entity.PersonDataBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.PhotoUtils;
import com.rxjy.rxinvestment.utils.SelectorUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
* 身份信息修改
* */

public class UserInfoModifyActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.iv_personicon)
    ImageView ivPersonicon;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.rl_idcardthree)
    RelativeLayout rlIdcardthree;
    @Bind(R.id.rl_idcardfour)
    RelativeLayout rlIdcardfour;
    @Bind(R.id.iv_idcardone)
    ImageView ivIdcardone;
    @Bind(R.id.iv_addone)
    ImageView ivAddone;
    @Bind(R.id.rl_idcardone)
    RelativeLayout rlIdcardone;
    @Bind(R.id.iv_idcardtwo)
    ImageView ivIdcardtwo;
    @Bind(R.id.iv_addtwo)
    ImageView ivAddtwo;
    @Bind(R.id.rl_idcardtwo)
    RelativeLayout rlIdcardtwo;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_idcard)
    EditText etIdcard;
    @Bind(R.id.et_oldaddress)
    EditText etOldaddress;
    @Bind(R.id.et_nowaddress)
    EditText etNowaddress;
    @Bind(R.id.tv_birth)
    TextView tvBirth;
    @Bind(R.id.rl_birth)
    RelativeLayout rlBirth;
    @Bind(R.id.tv_edu)
    TextView tvEdu;
    @Bind(R.id.rl_edu)
    RelativeLayout rlEdu;
    @Bind(R.id.tv_marrary)
    TextView tvMarrary;
    @Bind(R.id.rl_ismarry)
    RelativeLayout rlIsmarry;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.tv_tishi)
    TextView tvTishi;
    private ArrayList<String> imgList;
    private String birthup;

    int hasone, hastwo, hasthree;//第一张第二、三张图片是否有
    String xingXiangImg, cardZheng, cardFan;

    @Override
    public int getLayout() {
        return R.layout.activity_user_info_modify;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void intData() {
        tvTitle.setText("身份信息");
        getUserData();
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.tv_save, R.id.rl_idcardthree, R.id.rl_idcardone, R.id.iv_idcardtwo, R.id.rl_birth, R.id.rl_edu, R.id.rl_ismarry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                String name = etName.getText().toString();
                String idcard = etIdcard.getText().toString();
                String address = etOldaddress.getText().toString();
                String nowaddress = etNowaddress.getText().toString();
                String birth = birthup;
                String edu = tvEdu.getText().toString();
                String marrary = tvMarrary.getText().toString();
                if ((!StringUtils.isEmpty(name) && !name.equals("请输入")) && (!StringUtils.isEmpty(idcard) && !idcard.equals("请输入")) && (!StringUtils.isEmpty(address) && !address.equals("请输入"))) {
                    setUserInfo("6");
                }
                if (!StringUtils.isEmpty(nowaddress) && !nowaddress.equals("请输入")) {
                    setUserInfo("7");
                }
                if (!StringUtils.isEmpty(birth)) {
                    setUserInfo("8");
                }
                if (!StringUtils.isEmpty(edu)) {
                    setUserInfo("9");
                }
                if (!StringUtils.isEmpty(marrary)) {
                    setUserInfo("10");
                }
                finish();
                break;
            case R.id.tv_save://提交
                setUserInfo("2");
                break;
            case R.id.rl_idcardthree://形象照
                if (hasthree == 0) {
                    PhotoUtils.checkIco(1001);
                } else {
                    watchLargerImage(xingXiangImg, null, 0, "个人形象照", "");
                }
                break;
            case R.id.rl_idcardone://身份证正面
                if (hasthree == 0) {
                    PhotoUtils.checkIco(1002);
                } else {
                    watchLargerImage(cardZheng, null, 0, "身份证正面", "");
                }
                break;
            case R.id.iv_idcardtwo://身份证反面
                if (hasthree == 0) {
                    PhotoUtils.checkIco(1003);
                } else {
                    watchLargerImage(cardFan, null, 0, "身份证反面", "");
                }
                break;
            case R.id.rl_birth://生日
                SelectorUtils.timeSelector(tvBirth, "yyyy/MM/dd");
                break;
            case R.id.rl_edu://学历
                List edubglist = new ArrayList<>();
                edubglist.add("高中");
                edubglist.add("专科");
                edubglist.add("中专");
                edubglist.add("大专");
                edubglist.add("本科");
                edubglist.add("本科以上");
                SelectorUtils.dataSelector(tvEdu, edubglist);
                break;
            case R.id.rl_ismarry://婚否
                List ismarrrylist = new ArrayList<>();
                ismarrrylist.add("已婚");
                ismarrrylist.add("未婚");
                SelectorUtils.dataSelector(tvMarrary, ismarrrylist);
                break;
        }
    }

    private void setUserInfo(String type) {
        showLoading();
        final String name = etName.getText().toString();//6
        String idcard = etIdcard.getText().toString();//6
        String address = etOldaddress.getText().toString();//6
        String nowaddress = etNowaddress.getText().toString();//7
        String birth = birthup;//8
        String edu = tvEdu.getText().toString();//9
        String marrary = tvMarrary.getText().toString();//10
        Map map = new HashMap();
        map.put("CardNo", App.cardNo);
        map.put("Type", type);
        map.put("IdCard", idcard);
        map.put("IdCardName", name);
        map.put("Birthday", birth);
        map.put("IdCardAddress", address);
        map.put("NowAddress", nowaddress);
        map.put("Marriage", marrary);
        map.put("Xueli", edu);

        OkhttpUtils.doGet(this, PathUrl.SETUSERINFOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_修改用户信息", data);
                PersonBean info = JSONUtils.toObject(data, PersonBean.class);
                PersonDataBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    MySharedPreferences.getInstance().setName(name);
                    finish();
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
                dismissLoading();
            }

            @Override
            public void error(String message) {
                Log.e("tag_修改失败", message);
                dismissLoading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    Glide.with(UserInfoModifyActivity.this).load(imgList.get(0)).into(ivPersonicon);
                    uploadImg("4");
                    Log.e("图片：", imgList.get(0));
                }
                break;
            case 1002:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    Glide.with(UserInfoModifyActivity.this).load(imgList.get(0)).into(ivIdcardone);
                    uploadImg("13");
                    Log.e("图片：", imgList.get(0));
                }
                break;
            case 1003:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    Glide.with(UserInfoModifyActivity.this).load(imgList.get(0)).into(ivIdcardtwo);
                    uploadImg("17");
                    Log.e("图片：", imgList.get(0));
                }
                break;
        }
    }

    private void uploadImg(String type) {
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File(imgList.get(0));
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("facefile", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        builder.addFormDataPart("cardNo", App.cardNo);
        builder.addFormDataPart("type", type);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(PathUrl.USERINFOIMGURL).addHeader("Referer", "iPanda.Android")
                .addHeader("User-Agent", "CNTV_APP_CLIENT_CBOX_MOBILE")
                .post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag_图片上传_失败", e.getMessage().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("tag_图片上传", string);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
//                        JSONObject object = jsonObject.getJSONObject("Body");
//                        String url = object.getString("url");
////                            Log.e("tag_头像上传成功", url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUserData() {
        showLoading();
        Map map = new HashMap();
        map.put("Phone", MySharedPreferences.getInstance().getUserPhone());
        map.put("Type", "2");
        OkhttpUtils.doGet(this, PathUrl.USERINFOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取用户信息", data);
                IdentityInfoBean info = JSONUtils.toObject(data, IdentityInfoBean.class);
                IdentityInfoBean.BodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
//设置点击
                    if (body.getIsFinsh() == 1) {
                        hasthree = 1;
                        hasone = 1;
                        hastwo = 1;
                        etName.setEnabled(false);
                        etIdcard.setEnabled(false);
                        etOldaddress.setEnabled(false);
                        etNowaddress.setEnabled(false);
                        rlBirth.setEnabled(false);
                        rlEdu.setEnabled(false);
                        rlIsmarry.setEnabled(false);
                        tvSave.setVisibility(View.GONE);
                    } else {
                        //判断图片状态
                        String imageState = body.getImageState();
                        if (!StringUtils.isEmpty(imageState)) {
                            if (imageState.equals("1") || imageState.equals("4")) {
                                hasthree = 1;
                            } else {
                                hasthree = 0;
                            }
                        } else {
                            hasthree = 0;
                        }
//判断身份证状态
                        String shenFenDataState = body.getShenFenDataState();
                        if (!StringUtils.isEmpty(shenFenDataState)) {
                            if (shenFenDataState.equals("1") || shenFenDataState.equals("4")) {
                                hasone = 1;
                                hastwo = 1;
                                etName.setEnabled(false);
                                etIdcard.setEnabled(false);
                                etOldaddress.setEnabled(false);
                            } else {
                                hasone = 0;
                                hastwo = 0;
                                etName.setEnabled(true);
                                etIdcard.setEnabled(true);
                                etOldaddress.setEnabled(true);
                            }
                        } else {
                            hasone = 0;
                            hastwo = 0;
                            etName.setEnabled(true);
                            etIdcard.setEnabled(true);
                            etOldaddress.setEnabled(true);
                        }
//判断现住地址状态状态
                        String xianZhuZhiDataState = body.getXianZhuZhiDataState();
                        if (!StringUtils.isEmpty(xianZhuZhiDataState)) {
                            if (xianZhuZhiDataState.equals("1") || xianZhuZhiDataState.equals("4")) {
                                etNowaddress.setEnabled(false);
                            } else {
                                etNowaddress.setEnabled(true);
                            }
                        } else {
                            etNowaddress.setEnabled(true);
                        }
//判断生日状态
                        String shengRiDataState = body.getShengRiDataState();
                        if (!StringUtils.isEmpty(shengRiDataState)) {
                            if (shengRiDataState.equals("1") || shengRiDataState.equals("4")) {
                                rlBirth.setEnabled(false);
                            } else {
                                rlBirth.setEnabled(true);
                            }
                        } else {
                            rlBirth.setEnabled(true);
                        }
//判断学历状态
                        String xueLiDataState = body.getXueLiDataState();
                        if (!StringUtils.isEmpty(xueLiDataState)) {
                            if (xueLiDataState.equals("1") || xueLiDataState.equals("4")) {
                                rlEdu.setEnabled(false);
                            } else {
                                rlEdu.setEnabled(true);
                            }
                        } else {
                            rlEdu.setEnabled(true);
                        }
//判断婚否状态
                        String hunFouDataState = body.getHunFouDataState();
                        if (!StringUtils.isEmpty(hunFouDataState)) {
                            if (hunFouDataState.equals("1") || hunFouDataState.equals("4")) {
                                rlEdu.setEnabled(false);
                            } else {
                                rlEdu.setEnabled(true);
                            }
                        } else {
                            rlEdu.setEnabled(true);
                        }
                    }

                    //设置内容
                    if (!StringUtils.isEmpty(body.getIdCardName())) {
                        etName.setText(body.getIdCardName());
                    }
                    if (!StringUtils.isEmpty(body.getIdCard())) {
                        etIdcard.setText(body.getIdCard());
                    }
                    if (!StringUtils.isEmpty(body.getIdCardAddress())) {
                        etOldaddress.setText(body.getIdCardAddress());
                    }
                    if (!StringUtils.isEmpty(body.getNowAddress())) {
                        etNowaddress.setText(body.getNowAddress());
                    }
                    if (!StringUtils.isEmpty(body.getMarriage())) {
                        tvMarrary.setText(body.getMarriage());
                    }
                    if (!StringUtils.isEmpty(body.getBirthday())) {
                        String birth = body.getBirthday();
                        birthup = birth.replaceAll("-", "/");
                        birthup = birthup.replaceAll("年", "/");
                        birthup = birthup.replaceAll("月", "/");
                        birthup = birthup.replaceAll("日", "");
                        Log.e("birthup1", birthup);
                        tvBirth.setText(birth);
                    }
                    if (!StringUtils.isEmpty(body.getXueli())) {
                        tvEdu.setText(body.getXueli());
                    }

                    if (!StringUtils.isEmpty(body.getIdCardImageHeads())) {
                        ivAddone.setVisibility(View.GONE);
                        Glide.with(UserInfoModifyActivity.this).load(body.getIdCardImageHeads()).into(ivIdcardone);
                        cardZheng = body.getIdCardImageHeads();
                    }
                    if (!StringUtils.isEmpty(body.getIdCardImageTails())) {
                        ivAddtwo.setVisibility(View.GONE);
                        Glide.with(UserInfoModifyActivity.this).load(body.getIdCardImageTails()).into(ivIdcardtwo);
                        cardFan = body.getIdCardImageTails();
                    }
                    if (!StringUtils.isEmpty(body.getImage())) {
                        Glide.with(UserInfoModifyActivity.this).load(body.getImage()).into(ivPersonicon);
                        ivAdd.setVisibility(View.GONE);
                        xingXiangImg = body.getImage();
                    }
                    String imageText = body.getImageText();
                    if (!TextUtils.isEmpty(imageText)) {
                        tvTishi.setText(imageText);
                    }
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
                dismissLoading();
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取失败", message);
                dismissLoading();
            }
        });
        setProgressDialog(3000);
    }

}
