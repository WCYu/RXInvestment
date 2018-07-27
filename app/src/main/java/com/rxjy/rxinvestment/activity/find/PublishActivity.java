package com.rxjy.rxinvestment.activity.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.ArticleBean;
import com.rxjy.rxinvestment.entity.FigureImgBean;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.utils.GlideRoundTransform;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
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

public class PublishActivity extends BaseActivity {

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
    @Bind(R.id.article_title)
    EditText articleTitle;
    @Bind(R.id.publish_content)
    EditText publishContent;
    @Bind(R.id.complant_grid)
    ImageView complantGrid;
    @Bind(R.id.delete_image)
    ImageView deleteImage;

    private int CAMERA_INFO_IDENTIFICATION_PHOTO = 1000;
    private String icCardUpUrl = "";
    RequestOptions options;
    private int flag;
    ArrayList<String> list_imguplode;
    private String title;
    private String content;
    private String upimgstr = "";

    @Override
    public int getLayout() {
        return R.layout.activity_publish;
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
        list_imguplode = new ArrayList<>();
        options = new RequestOptions();
        options.centerCrop().transform(new GlideRoundTransform(this, 6));
        tvTitle.setText("撰写");
    }

    @Override
    public void initAdapter() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INFO_IDENTIFICATION_PHOTO) {
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            icCardUpUrl = localMedias.get(0).getCompressPath();
            Glide.with(this).load(icCardUpUrl).apply(options).into(complantGrid);
            deleteImage.setVisibility(View.VISIBLE);
            flag = 1;
        }

    }

    @OnClick({R.id.img_back, R.id.tv_right, R.id.complant_grid, R.id.delete_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                closeKeyboard();
                finish();
                break;
            case R.id.tv_right:
                closeKeyboard();
                //article_title
                if (!articleTitle.getText().toString().equals("") && !publishContent.getText().toString().equals("") && flag == 1) {
                    title = articleTitle.getText().toString();
                    content = publishContent.getText().toString();
                    list_imguplode.clear();
                    list_imguplode.add(icCardUpUrl);
                    upLoadImg(list_imguplode);
                } else {
                    ToastUtil.getInstance().toastCentent("资料不全");
                }
                break;
            case R.id.complant_grid:
                closeKeyboard();
                if (flag == 0) {
                    PictureSelector.create(PublishActivity.this)
                            .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                            .imageSpanCount(3)// 每行显示个数 int
                            .maxSelectNum(1)// 最大图片选择数量 int
                            .minSelectNum(1)// 最小选择数量 int
                            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                            .compress(true)// 是否压缩 true or fals
                            .isCamera(true)// 是否显示拍照按钮 true or false
                            .forResult(CAMERA_INFO_IDENTIFICATION_PHOTO);//结果回调onActivityResult code 
                }
                break;
            case R.id.delete_image:
                deleteImage.setVisibility(View.GONE);
                flag = 0;
                Glide.with(this).load(R.mipmap.addpictures).apply(options).into(complantGrid);
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
                Log.e("tag_图片上传_失败", e.getMessage().toString());
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
                                FigureImgBean info = JSONUtils.toObject(string, FigureImgBean.class);
                                for (int i = 0; i < info.getBody().size(); i++) {
                                    upimgstr = upimgstr + info.getBody().get(i) + ";";
                                }
                                upimgstr = upimgstr.substring(0, upimgstr.length() - 1);

                                psetArticleData(title, App.cardNo, App.name, upimgstr, content, "0");
                                dismissLoading();
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

    private void psetArticleData(String title, String cardNo, String name, String upimgstr, String content, String s) {
        Map map = new HashMap();
        map.put("name", title);
        map.put("cardno", cardNo);
        map.put("author", name);
        map.put("cover", upimgstr);
        map.put("content", content);
        map.put("flag", s);
        OkhttpUtils.doPost(PathUrl.FBWENZHANGURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_发表文章", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        ArticleBean info = JSONUtils.toObject(data, ArticleBean.class);
                        info.getBody();
                        ToastUtil.getInstance().toastCentent("发表成功");
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
                Log.e("tag_发表文章", message);

            }
        });

    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
