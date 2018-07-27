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

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.BankBean;
import com.rxjy.rxinvestment.entity.BankBodyBean;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.entity.PersonDataBean;
import com.rxjy.rxinvestment.entity.ZthreeBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.PhotoUtils;
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

public class EntryInformationActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.iv_bankone)
    ImageView ivBankone;
    @Bind(R.id.iv_addone)
    ImageView ivAddone;
    @Bind(R.id.rl_bankone)
    RelativeLayout rlBankone;
    @Bind(R.id.textView16)
    TextView textView16;
    @Bind(R.id.tv_bank)
    TextView tvBank;
    @Bind(R.id.rl_banktype)
    RelativeLayout rlBanktype;
    @Bind(R.id.textView17)
    TextView textView17;
    @Bind(R.id.et_bankname)
    EditText etBankname;
    @Bind(R.id.textView19)
    TextView textView19;
    @Bind(R.id.et_banknum)
    EditText etBanknum;
    @Bind(R.id.textView18)
    TextView textView18;
    @Bind(R.id.et_person)
    EditText etPerson;
    @Bind(R.id.textView20)
    TextView textView20;
    @Bind(R.id.et_relation)
    EditText etRelation;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.iv_addfour)
    ImageView ivAddfour;
    @Bind(R.id.rl_photo)
    RelativeLayout rlPhoto;
    @Bind(R.id.iv_degree)
    ImageView ivDegree;
    @Bind(R.id.iv_addtwo)
    ImageView ivAddtwo;
    @Bind(R.id.rl_degree)
    RelativeLayout rlDegree;
    @Bind(R.id.iv_bodycheck)
    ImageView ivBodycheck;
    @Bind(R.id.iv_addfive)
    ImageView ivAddfive;
    @Bind(R.id.rl_bodycheck)
    RelativeLayout rlBodycheck;
    @Bind(R.id.iv_leaveoffice)
    ImageView ivLeaveoffice;
    @Bind(R.id.iv_addthree)
    ImageView ivAddthree;
    @Bind(R.id.rl_leaveoffice)
    RelativeLayout rlLeaveoffice;
    @Bind(R.id.tv_save)
    TextView tvSave;

    int picone, pictwo, picthree, picfour, picfive;
    String bankImg, certificatesImg, graduationImg, examinationImg, quitImg;
    private ArrayList<String> imgList;
    private ArrayList<String> banklist;
    private ArrayList<String> branchlist;
    private OptionsPickerView pickerView;
    private String id;

    @Override
    public int getLayout() {
        return R.layout.activity_entry_information;
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
        tvTitle.setText("入职资料");
        getUserData();
        getBankList();
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.rl_bankone, R.id.rl_photo, R.id.rl_degree, R.id.rl_bodycheck, R.id.rl_leaveoffice, R.id.tv_save, R.id.tv_bank})
    public void onViewClicked(View view) {
        String bankname = tvBank.getText().toString();
        String bankusername = etBankname.getText().toString();
        String banknum = etBanknum.getText().toString();
        String personname = etPerson.getText().toString();
        String personcotact = etRelation.getText().toString();
        String personphone = etPhone.getText().toString();
        switch (view.getId()) {
            case R.id.img_back:
                if ((!StringUtils.isEmpty(bankname) && !bankname.equals("请选择")) && (!StringUtils.isEmpty(bankusername) && !bankusername.equals("请输入"))
                        && (!StringUtils.isEmpty(banknum) && !banknum.equals("请输入"))) {
                    setUserInfo("4");
                }
                if ((!StringUtils.isEmpty(personname) && !personname.equals("请输入")) && (!StringUtils.isEmpty(personcotact) && !personcotact.equals("请输入"))
                        && (!StringUtils.isEmpty(personphone) && !personphone.equals("请输入"))) {
                    setUserInfo("5");
                }
                finish();
                break;
            case R.id.rl_bankone://银行卡正面
                Log.e("银行卡正面", picone + "");
                if (picone == 1) {
                    watchLargerImage(bankImg, null, 0, "银行卡正面", "");
                } else {
                    PhotoUtils.checkIco(3001);
                }
                break;
            case R.id.rl_photo://证件照
                if (pictwo == 1) {
                    watchLargerImage(certificatesImg, null, 0, "证件照", "");
                } else {
                    PhotoUtils.checkIco(3002);
                }
                break;
            case R.id.rl_degree://毕业证
                if (picthree == 1) {
                    watchLargerImage(graduationImg, null, 0, "毕业证", "");
                } else {
                    PhotoUtils.checkIco(3003);
                }
                break;
            case R.id.rl_bodycheck://体检报告
                if (picfour == 1) {
                    watchLargerImage("", null, 0, "体检报告", "8");
                } else {
                    PhotoUtils.checkManyIco(3004);
                }
                break;
            case R.id.rl_leaveoffice://离职证明
                if (picfive == 1) {
                    watchLargerImage(quitImg, null, 0, "银行卡正面", "");
                } else {
                    PhotoUtils.checkIco(3005);
                }
                break;
            case R.id.tv_save://提交
                if (StringUtils.isEmpty(bankname) || bankname.equals("请选择")) {
                    ToastUtil.getInstance().toastCentent("请选择开户行！");
                    return;
                }
                if (StringUtils.isEmpty(bankusername) || bankusername.equals("请输入")) {
                    ToastUtil.getInstance().toastCentent("请输入开户名！");
                    return;
                }
                if (StringUtils.isEmpty(banknum) || banknum.equals("请输入")) {
                    ToastUtil.getInstance().toastCentent("请输入银行卡号！");
                    return;
                }
                if (StringUtils.isEmpty(personname) || personname.equals("请输入")) {
                    ToastUtil.getInstance().toastCentent("请输入紧急联系人！");
                    return;
                }
                if (StringUtils.isEmpty(personcotact) || personcotact.equals("请输入")) {
                    ToastUtil.getInstance().toastCentent("请输入联系人关系！");
                    return;
                }
                if (StringUtils.isEmpty(personphone) || personphone.equals("请输入")) {
                    ToastUtil.getInstance().toastCentent("请输入联系人电话！");
                    return;
                }
                setUserInfo("3");
                break;
            case R.id.tv_bank://银行列表
                pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tvBank.setText(banklist.get(options1));
                        id = branchlist.get(options1);
                    }
                }).build();
                pickerView.setPicker(banklist);
                pickerView.show();
                break;
        }
    }

    private void setUserInfo(String type) {
        showLoading();
        String bankname = tvBank.getText().toString();
        String bankusername = etBankname.getText().toString();
        String banknum = etBanknum.getText().toString();
        String personname = etPerson.getText().toString();
        String personcotact = etRelation.getText().toString();
        String personphone = etPhone.getText().toString();
        Map map = new HashMap();
        map.put("CardNo", App.cardNo);
        map.put("Type", type);
        map.put("BankCard", banknum);
        map.put("BankName", bankname);
        map.put("BankUserName", bankusername);
        map.put("UrgentPerson", personname);
        map.put("UrgentPhone", personphone);
        map.put("UrgentContact", personcotact);
        if (!TextUtils.isEmpty(id)) {
            map.put("u_bank_branch", id);
        }

        OkhttpUtils.doGet(this, PathUrl.SETUSERINFOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_修改用户信息", data);
                PersonBean info = JSONUtils.toObject(data, PersonBean.class);
                PersonDataBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
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
            case 3001:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    uploadImg("5", imgList);
                }
                break;
            case 3002:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    uploadImg("3", imgList);
                }
                break;
            case 3003:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    uploadImg("6", imgList);
                }
                break;
            case 3004:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCompressPath());
                    }
                    uploadImg("7", imgList);
                }
                break;
            case 3005:
                imgList = new ArrayList<>();
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < localMedias.size(); i++) {
                        imgList.add(localMedias.get(i).getCutPath());
                    }
                    uploadImg("8", imgList);
                }
                break;
        }
    }

    private void uploadImg(String type, ArrayList<String> arrayList) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < arrayList.size(); i++) {
            File file = new File(arrayList.get(i));
            builder.addFormDataPart("facefile", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
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
        map.put("Type", "3");
        OkhttpUtils.doGet(this, PathUrl.USERINFOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取用户信息", data);
                ZthreeBean info = JSONUtils.toObject(data, ZthreeBean.class);
                ZthreeBean.BodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {

                    if (body.getIsFinsh() == 1) {
                        tvSave.setVisibility(View.GONE);
                    }

                    try {
                        if (body.getIsFinsh() == 1 || body.getBankImageState().equals("1") || body.getBankImageState().equals("4")) {
                            picone = 1;
                        } else {
                            rlBankone.setEnabled(true);
                            picone = 0;
                        }
                    } catch (Exception e) {
                        rlBankone.setEnabled(true);
                        picone = 0;
                    }
                    try {
                        if (body.getIsFinsh() == 1 || body.getPassportPhotoState().equals("1") || body.getPassportPhotoState().equals("4")) {
                            picfour = 1;
                        } else {
                            picfour = 0;
                        }
                    } catch (Exception e) {
                        picfour = 0;
                    }
                    try {
                        if (body.getIsFinsh() == 1 || body.getDegreeCertificateState().equals("1") || body.getDegreeCertificateState().equals("4")) {
                            pictwo = 1;
                        } else {
                            pictwo = 0;
                        }
                    } catch (Exception e) {
                        rlDegree.setEnabled(true);
                        pictwo = 0;
                    }
                    try {
                        if (body.getIsFinsh() == 1 || body.getHealthCertificateState().equals("1") || body.getHealthCertificateState().equals("4")) {
                            picfive = 1;
                        } else {
                            picfive = 0;
                        }
                    } catch (Exception e) {
                        picfive = 0;
                    }
                    try {
                        if (body.getIsFinsh() == 1 || body.getResignationCertificateState().equals("1") || body.getResignationCertificateState().equals("4")) {
                            picthree = 1;
                        } else {
                            picthree = 0;
                        }
                    } catch (Exception e) {
                        picthree = 0;
                    }
                    try {
                        if (body.getIsFinsh() == 1 || body.getYinHangDataState().equals("1") || body.getYinHangDataState().equals("4")) {
                            rlBanktype.setEnabled(false);
                            etBankname.setEnabled(false);
                            etBanknum.setEnabled(false);
                        } else {
                            rlBanktype.setEnabled(true);
                            etBankname.setEnabled(true);
                            etBanknum.setEnabled(true);
                        }
                    } catch (Exception e) {
                        rlBanktype.setEnabled(true);
                        etBankname.setEnabled(true);
                        etBanknum.setEnabled(true);
                    }
                    try {
                        if (body.getIsFinsh() == 1 || body.getJinJiLianXiDataState().equals("1") || body.getJinJiLianXiDataState().equals("4")) {
                            etPerson.setEnabled(false);
                            etRelation.setEnabled(false);
                            etPhone.setEnabled(false);
                        } else {
                            etPerson.setEnabled(true);
                            etRelation.setEnabled(true);
                            etPhone.setEnabled(true);
                        }
                    } catch (Exception e) {
                        etPerson.setEnabled(true);
                        etRelation.setEnabled(true);
                        etPhone.setEnabled(true);
                    }

                    if (!StringUtils.isEmpty(body.getBankName())) {
                        tvBank.setText(body.getBankName());
                    }
                    if (!StringUtils.isEmpty(body.getBankCard())) {
                        etBanknum.setText(body.getBankCard());
                    }
                    if (!StringUtils.isEmpty(body.getBankUserName())) {
                        etBankname.setText(body.getBankUserName());
                    }
                    if (!StringUtils.isEmpty(body.getUrgentPerson())) {
                        etPerson.setText(body.getUrgentPerson());
                    }
                    if (!StringUtils.isEmpty(body.getUrgentContact())) {
                        etRelation.setText(body.getUrgentContact());
                    }
                    if (!StringUtils.isEmpty(body.getUrgentPhone())) {
                        etPhone.setText(body.getUrgentPhone());
                    }

                    if (!StringUtils.isEmpty(body.getBankImage())) {
                        Glide.with(EntryInformationActivity.this).load(body.getBankImage()).into(ivBankone);
                        ivAddone.setVisibility(View.GONE);
                        bankImg = body.getBankImage();
                    }
                    if (!StringUtils.isEmpty(body.getPassportPhoto())) {
                        Glide.with(EntryInformationActivity.this).load(body.getPassportPhoto()).into(ivPhoto);
                        ivAddfour.setVisibility(View.GONE);
                        certificatesImg = body.getPassportPhoto();
                    }
                    if (!StringUtils.isEmpty(body.getDegreeCertificate())) {
                        Glide.with(EntryInformationActivity.this).load(body.getDegreeCertificate()).into(ivDegree);
                        ivAddtwo.setVisibility(View.GONE);
                        graduationImg = body.getDegreeCertificate();
                    }
                    if (!StringUtils.isEmpty(body.getHealthCertificate())) {
                        Glide.with(EntryInformationActivity.this).load(body.getHealthCertificate()).into(ivBodycheck);
                        ivAddfive.setVisibility(View.GONE);
                        examinationImg = body.getHealthCertificate();
                    }
                    if (!StringUtils.isEmpty(body.getResignationCertificate())) {
                        Glide.with(EntryInformationActivity.this).load(body.getResignationCertificate()).into(ivLeaveoffice);
                        ivAddthree.setVisibility(View.GONE);
                        quitImg = body.getResignationCertificate();
                    }
                    id = body.getBankId();
                    dismissLoading();
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

    public void getBankList() {
        Map map = new HashMap();
        map.put("Card", App.cardNo);
        OkhttpUtils.doGet(this, PathUrl.BANKLISTURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取银行列表", data);
                BankBean info = JSONUtils.toObject(data, BankBean.class);
                ArrayList<BankBodyBean> body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    banklist = new ArrayList<>();
                    branchlist = new ArrayList<>();
                    for (int i = 0; i < body.size(); i++) {
                        banklist.add(body.get(i).getBank_name());
                        branchlist.add(body.get(i).getId() + "");
                    }
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取银行列表失败", message);
            }
        });
    }
}
