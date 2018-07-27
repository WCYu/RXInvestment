package com.rxjy.rxinvestment.activity.my;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.entity.PersonDataBean;
import com.rxjy.rxinvestment.entity.ZhuanZhengBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ZhuanZhengActivity extends BaseActivity {

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
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_sex)
    EditText etSex;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_time)
    EditText etTime;
    @Bind(R.id.et_hukou)
    TextView etHukou;
    @Bind(R.id.et_baoxian)
    TextView etBaoxian;
    @Bind(R.id.duty)
    EditText duty;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    private String cardno;
    private OptionsPickerView choiceData;

    String hukou;
    String baoxian;
    private OptionsPickerView baoXianData;
    private ZhuanZhengBean zhuanZhengBean;

    @Override
    public int getLayout() {
        return R.layout.activity_zhuan_zheng;
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
        tvTitle.setText("转正审批");
        showLoading();
        ButterKnife.bind(this);
        ArrayList<String> huKouList = new ArrayList<>();
        huKouList.add("本市城镇");
        huKouList.add("本市农村");
        huKouList.add("外埠城镇");
        huKouList.add("外埠农村");
        initChoiceData(huKouList);

        ArrayList<String> baoXianList = new ArrayList<>();
        baoXianList.add("续交");
        baoXianList.add("新增");
        baoXianList.add("放弃");
        initBaoXianData(baoXianList);

        getUserData();
        getCorrectionInformation();
    }

    private void initChoiceData(final ArrayList<String> arrayList) {

        choiceData = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                etHukou.setText(arrayList.get(options1).toString());
                hukou = arrayList.get(options1).toString();
            }
        }).build();

        choiceData.setPicker(arrayList);

    }

    private void initBaoXianData(final ArrayList<String> arrayList) {

        baoXianData = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                etBaoxian.setText(arrayList.get(options1).toString());
                baoxian = arrayList.get(options1).toString();
            }
        }).build();

        baoXianData.setPicker(arrayList);

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
                    etName.setText(body.getName());
                    etTime.setText(body.getTryHillockTime());
                    etPhone.setText(body.getPhone());
                    etSex.setText(body.getSex());
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

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.et_hukou, R.id.et_baoxian, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_hukou:
                choiceData.show();
                break;
            case R.id.et_baoxian:
                baoXianData.show();
                break;
            case R.id.btn_commit:
                if(TextUtils.isEmpty(hukou)){
                    ToastUtil.getInstance().toastCentent("请选择户口性质");
                    return;
                }

                if(TextUtils.isEmpty(baoxian)){
                    ToastUtil.getInstance().toastCentent("请选择保险情况");
                    return;
                }

                if(TextUtils.isEmpty(duty.getText().toString())){
                    ToastUtil.getInstance().toastCentent("请填写述职情况");
                    return;
                }

                if(zhuanZhengBean == null){
                    ToastUtil.getInstance().toastCentent("未获取到员工信息");
                    return;
                }

                int hege = zhuanZhengBean.getHege();

                if(hege == 1){//允许转正
                    commitData();
                }else {
                    ToastUtil.getInstance().toastCentent(zhuanZhengBean.getZhuanzhengTimeStr());
                }
                break;
        }
    }

    public void getCorrectionInformation() {
        Map map = new HashMap();
        map.put("u_kahao", App.cardNo);

        OkhttpUtils.doGet(PathUrl.ZHUANZHNEDATAGURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取转正状态", data);
                Gson gson = new Gson();
                zhuanZhengBean = gson.fromJson(data, ZhuanZhengBean.class);
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取转正状态失败",message);
                ToastUtil.getInstance().toastCentent("获取转正状态失败");
            }
        });
    }

    private void commitData() {
        Map map = new HashMap();
        map.put("u_kahao", cardno);
        map.put("shebao", baoxian);
        map.put("hukou", hukou);
        map.put("shuzhi", duty.getText().toString());
        map.put("u_zhuanzhengtime", zhuanZhengBean.getZhuanzhengTime());
        map.put("reason", "App提交转正");
        map.put("KaHao", "");
        OkhttpUtils.doPost(PathUrl.ZHUANZHNEGURL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag_申请转正_失败",e.getMessage().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                Log.e("tag_申请转正",string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if(status == 0){
                                ToastUtil.getInstance().toastCentent(msg);
                                App.u_start = 100010;
                                finish();
                            }else {
                                ToastUtil.getInstance().toastCentent(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
