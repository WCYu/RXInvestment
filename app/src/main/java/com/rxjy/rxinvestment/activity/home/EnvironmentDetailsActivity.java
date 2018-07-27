package com.rxjy.rxinvestment.activity.home;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.home.ImageAddAdapter;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.custom.MyGridView;
import com.rxjy.rxinvestment.entity.EnvirImgBean;
import com.rxjy.rxinvestment.entity.FigureImgBean;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.entity.ResultBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
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
* 环境上传
* */
public class EnvironmentDetailsActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.publish)
    ImageView publish;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.imageView23)
    ImageView imageView23;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.gv_top)
    RelativeLayout gvTop;
    @Bind(R.id.ed_idea)
    EditText edIdea;
    @Bind(R.id.iv_picexample)
    ImageView ivPicexample;
    @Bind(R.id.rl_viewimg)
    RelativeLayout rlViewimg;
    @Bind(R.id.gv_img)
    MyGridView gvImg;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;

    String getid, getcontentid, getareaname;
    ArrayList<String> list_img;
    ArrayList<String> list_imguplode;
    List<LocalMedia> list_lm;
    ArrayList<String> watchimglist;
    ImageAddAdapter imageAddAdapter;
    private String idea;
    private String upimgstr = "";

    @Override
    public int getLayout() {
        return R.layout.activity_environment_details;
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
        tvTitle.setText("");
        Intent intent = getIntent();
        getid = intent.getStringExtra("getid");
        getcontentid = intent.getStringExtra("getcontentid");
        getareaname = intent.getStringExtra("getareaname");
        tvTitle.setText(getareaname);
        tvSubmit.setText("提交");
        gvTop.setVisibility(View.GONE);
        getImg(getid);
        list_img = new ArrayList<>();
        list_imguplode = new ArrayList<>();
        list_img.add("");
        list_lm = new ArrayList<>();
        showImageAdd();
    }

    private void getImg(String getid) {
        Map map = new HashMap();
        map.put("ID", getid);
        OkhttpUtils.doGet(this, PathUrl.GETXINGXIANIMGGURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取展示图片", data);
                EnvirImgBean info = JSONUtils.toObject(data, EnvirImgBean.class);
                ArrayList<EnvirImgBean.BodyBean> body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    if (body != null && body.size() > 0) {
                        watchimglist = new ArrayList<>();
                        watchimglist.add(body.get(0).getImgUrl());
                        Glide.with(EnvironmentDetailsActivity.this).load(body.get(0).getImgUrl()).into(ivPicexample);
                    }
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取展示图片失败", message);
            }
        });

    }

    private void showImageAdd() {
        imageAddAdapter = new ImageAddAdapter(this, list_img);
        gvImg.setAdapter(imageAddAdapter);
        gvImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == list_img.size() - 1) {//点击添加图片
                    PictureSelector.create(EnvironmentDetailsActivity.this)
                            .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                            .imageSpanCount(3)// 每行显示个数 int
                            .minSelectNum(0)
                            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                            .enableCrop(false)//是否剪裁
                            .compress(true)
                            .selectionMedia(list_lm)
                            .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .scaleEnabled(false)// 裁剪是否可放大缩小图片 true or false
                            .forResult(666);//结果回调onActivityResult code 
                }
            }
        });
    }

    @Override
    public void initAdapter() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 666:
                if (resultCode == RESULT_OK) {
                    Log.e("请求开始", list_img.size() + "");
                    list_img.clear();
                    list_imguplode.clear();
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    list_lm = localMedias;
                    for (int i = 0; i < localMedias.size(); i++) {
                        list_img.add(localMedias.get(i).getCompressPath());
                        list_imguplode.add(localMedias.get(i).getCompressPath());
                    }
                    list_img.add("");
                    imageAddAdapter.notifyDataSetChanged();
                    Log.e("请求结束", list_img.size() + "");
                }
                break;
        }
    }

    @OnClick({R.id.img_back, R.id.tv_submit, R.id.iv_picexample})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_submit:
                idea = edIdea.getText().toString();
                if (StringUtils.isEmpty(idea)) {
                    ToastUtil.getInstance().toastCentent("请输入这一刻的想法！");
                    return;
                }
                if (list_imguplode.size() <= 0) {
                    ToastUtil.getInstance().toastCentent("请选择图片！");
                    return;
                }
                if (list_imguplode.size() < 2) {
                    ToastUtil.getInstance().toastCentent("请至少上传两张图片！");
                    return;
                }
                tvSubmit.setEnabled(false);
                upLoadImg(list_imguplode);
                break;
            case R.id.iv_picexample:
                if (watchimglist != null && watchimglist.size() > 0) {
                    watchLargerImage("", watchimglist, 0, "环境", "");
                }
                break;
        }
    }

    private void upLoadImg(ArrayList<String> list_imguplode) {
        showLoading();
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (int i = 0; i < list_imguplode.size(); i++) {
            File file = new File(list_imguplode.get(i));
            builder.addFormDataPart("facefile", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));

        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(PathUrl.RS_API_HOST + "ActionApi/TZManage/UploadImagesToTZ").addHeader("Referer", "iPanda.Android")
                .addHeader("User-Agent", "CNTV_APP_CLIENT_CBOX_MOBILE")
                .post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag_头像上传_失败", e.getMessage().toString());
                dismissLoading();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                Log.e("tag_头像上传", string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            int statusCode = jsonObject.getInt("StatusCode");
                            if (statusCode == 0) {
                                Log.e("tag_头像上传", "成功");
                                dismissLoading();
                                FigureImgBean info = JSONUtils.toObject(string, FigureImgBean.class);
                                ArrayList<String> body = info.getBody();
                                for (int i = 0; i < body.size(); i++) {
                                    upimgstr = upimgstr + body.get(i) + ";";
                                }
                                upimgstr = upimgstr.substring(0, upimgstr.length() - 1);
                                upLoadData(idea, upimgstr, getid, getcontentid, App.cardNo, App.name, "2", App.region_id, getareaname);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        setProgressDialog(3000);
    }

    private void upLoadData(String idea, String upimgstr, String getid, String getcontentid, String cardNo, String name, String s, int region_id, String getareaname) {
        Map map = new HashMap();
        map.put("EvaluateTxt", idea);
        map.put("Urls", upimgstr);
        map.put("Id", getid);
        map.put("ContentId", getcontentid);
        map.put("OperatorCardNo", cardNo);
        map.put("OperatorName", name);
        map.put("Type", s);
        map.put("RegionId", region_id);
        map.put("AreaName", getareaname);

        OkhttpUtils.doGet(this, PathUrl.HJTIJIAOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_环境提交", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        ResultBean info = JSONUtils.toObject(data, ResultBean.class);
                        ToastUtil.getInstance().toastBottom("提交成功");
                        finish();
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_环境提交失败", message);
            }
        });
    }
}
