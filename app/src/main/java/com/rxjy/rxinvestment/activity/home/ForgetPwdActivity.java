package com.rxjy.rxinvestment.activity.home;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.custom.DownTimerButton;
import com.rxjy.rxinvestment.entity.BaseBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 忘记密码
* */

public class ForgetPwdActivity extends BaseActivity {

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
    @Bind(R.id.imageView6)
    ImageView imageView6;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.iv_phone_icon)
    ImageView ivPhoneIcon;
    @Bind(R.id.tv_phone_text)
    TextView tvPhoneText;
    @Bind(R.id.et_phone_num)
    EditText etPhoneNum;
    @Bind(R.id.tv_old_password_line)
    TextView tvOldPasswordLine;
    @Bind(R.id.iv_verification_code_icon)
    ImageView ivVerificationCodeIcon;
    @Bind(R.id.tv_verification_code_text)
    TextView tvVerificationCodeText;
    @Bind(R.id.btn_verification_code)
    DownTimerButton btnVerificationCode;
    @Bind(R.id.et_verification_code)
    EditText etVerificationCode;
    @Bind(R.id.tv_verification_code_line)
    TextView tvVerificationCodeLine;
    @Bind(R.id.iv_new_password_icon)
    ImageView ivNewPasswordIcon;
    @Bind(R.id.tv_new_password_text)
    TextView tvNewPasswordText;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.tv_new_password_line)
    TextView tvNewPasswordLine;
    @Bind(R.id.iv_confirm_password_icon)
    ImageView ivConfirmPasswordIcon;
    @Bind(R.id.tv_confirm_password_text)
    TextView tvConfirmPasswordText;
    @Bind(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @Bind(R.id.tv_confirm_password_line)
    TextView tvConfirmPasswordLine;
    @Bind(R.id.btn_forget_password)
    Button btnForgetPassword;

    @Override
    public int getLayout() {
        return R.layout.activity_forget_pwd;
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
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        if (phone != null) {
            etPhoneNum.setText(phone);
        }
        btnForgetPassword.setEnabled(false);
        tvTitle.setText("忘记密码");
        initLine();
        etPhoneNum.addTextChangedListener(new MyEditListener(etPhoneNum));
        etNewPassword.addTextChangedListener(new MyEditListener(etNewPassword));
        etVerificationCode.addTextChangedListener(new MyEditListener(etVerificationCode));
        etConfirmPassword.addTextChangedListener(new MyEditListener(etConfirmPassword));
    }

    private void initLine() {
        etPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //此处为得到焦点时
                    tvOldPasswordLine.setEnabled(true);
                } else {
                    //此处为失去焦点时
                    tvOldPasswordLine.setEnabled(false);
                }
            }
        });
        etVerificationCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //此处为得到焦点时
                    tvVerificationCodeLine.setEnabled(true);
                } else {
                    //此处为失去焦点时
                    tvVerificationCodeLine.setEnabled(false);
                }
            }
        });
        etNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //此处为得到焦点时
                    tvNewPasswordLine.setEnabled(true);
                } else {
                    //此处为失去焦点时
                    tvNewPasswordLine.setEnabled(false);
                }
            }
        });
        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //此处为得到焦点时
                    tvConfirmPasswordLine.setEnabled(true);
                } else {
                    //此处为失去焦点时
                    tvConfirmPasswordLine.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.btn_verification_code, R.id.btn_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_verification_code:
                String phone = etPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.getInstance().toastCentent("请输入手机号");
                    return;
                }
                if (!StringUtils.isMobileNO(phone)) {
                    ToastUtil.getInstance().toastCentent("请输入正确的手机号");
                    return;
                }
                DownTimerButton.setIsclick(1);
                getVCode(phone);
                break;
            case R.id.btn_forget_password:
                //判断两个密码一致性
                String phoneNum = etPhoneNum.getText().toString().trim();
                String verificationCode = etVerificationCode.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtil.getInstance().toastCentent("请输入手机号");
                    return;
                }
                if (!StringUtils.isMobileNO(phoneNum)) {
                    ToastUtil.getInstance().toastCentent("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(verificationCode)) {
                    ToastUtil.getInstance().toastCentent("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(newPassword)) {
                    ToastUtil.getInstance().toastCentent("请输入新密码");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    ToastUtil.getInstance().toastCentent("请输入确认密码");
                    return;
                }

                //密码最少6位
                if (newPassword.length() < 6) {
                    ToastUtil.getInstance().toastCentent("密码最少6位");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    ToastUtil.getInstance().toastCentent("两次密码输入不一致");
                    return;
                }

                //密码为连续数
                if (newPassword.equals("123456") || newPassword.equals("234567") || newPassword.equals("345678") || newPassword.equals("456789")) {
                    ToastUtil.getInstance().toastCentent("密码过于简单");
                    return;
                }
                showLoading();
                toFixPwd(phoneNum, newPassword, verificationCode);
                break;
        }
    }

    private void toFixPwd(String phoneNum, String newPassword, String verificationCode) {
        Map map = new HashMap();
        map.put("phone", phoneNum);
        map.put("newPassword", newPassword);
        map.put("vCode", verificationCode);
        OkhttpUtils.doPost(PathUrl.SETPSWURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_修改密码", data);
                BaseBean info = JSONUtils.toObject(data, BaseBean.class);
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                    finish();
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
                dismissLoading();
            }

            @Override
            public void error(String message) {
                Log.e("tag_修改密码失败", message);
                ToastUtil.getInstance().toastCentent("修改密码失败");
                dismissLoading();
            }
        });
        setProgressDialog(3000);
    }

    private void getVCode(String phone) {
        Map map = new HashMap();
        map.put("phone", phone);
        OkhttpUtils.doPost(PathUrl.GETCAODEURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取验证码", data);
                BaseBean info = JSONUtils.toObject(data, BaseBean.class);
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {

                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }

            }

            @Override
            public void error(String message) {
                Log.e("tag_验证码失败", message);

            }
        });
    }

    int phonetrue = 1, codetrue = 0, pwdtrue = 0, pwdsuretrue = 0;
    String pwd, pwdsure;

    private class MyEditListener implements TextWatcher {

        private EditText edittext;

        public MyEditListener(EditText edittext) {
            super();
            this.edittext = edittext;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            int lengths = arg0.length();
            switch (edittext.getId()) {
                case R.id.et_phone_num:
                    if (lengths == 11) {
                        phonetrue = 1;
                    } else {
                        phonetrue = 0;
                    }
                    break;
                case R.id.et_verification_code:
                    if (lengths > 0) {
                        codetrue = 1;
                    } else {
                        codetrue = 0;
                    }
                    break;
                case R.id.et_new_password:
                    pwd = etNewPassword.getText().toString();
                    if (lengths > 0) {
                        pwdtrue = 1;
                    } else {
                        pwdtrue = 0;
                    }
                    break;
                case R.id.et_confirm_password:
                    pwdsure = etConfirmPassword.getText().toString();
                    if (lengths > 0) {
                        pwdsuretrue = 1;
                    } else {
                        pwdsuretrue = 0;
                    }
                    break;
            }

            Log.e("phonetrue" + phonetrue, "codetrue" + codetrue + "pwdtrue" + pwdtrue + "pwdsuretrue" + pwdsuretrue);
            Log.e("pwd" + pwd, "pwdsure" + pwdsure);
            if (phonetrue == 1 && codetrue == 1 && pwdtrue == 1 && pwdsuretrue == 1 && !StringUtils.isEmpty(pwd) &&
                    !StringUtils.isEmpty(pwdsure) && pwd.equals(pwdsure)) {
                btnForgetPassword.setEnabled(true);
            } else {
                btnForgetPassword.setEnabled(false);
            }

        }
    }
}
