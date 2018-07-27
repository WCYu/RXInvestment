package com.rxjy.rxinvestment.activity.more;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.entity.AdRedEnvelopesBean;
import com.rxjy.rxinvestment.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RedTaskActivity extends BaseActivity {
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
    @Bind(R.id.imageView12)
    ImageView imageView12;
    @Bind(R.id.tv_timesecound)
    TextView tvTimesecound;
    @Bind(R.id.tv_miao1)
    TextView tvMiao1;
    @Bind(R.id.tv_min)
    TextView tvMin;
    @Bind(R.id.tv_miao2)
    TextView tvMiao2;
    @Bind(R.id.tv_hour)
    TextView tvHour;
    @Bind(R.id.tv_theme)
    TextView tvTheme;
    @Bind(R.id.tv_reward)
    TextView tvReward;
    @Bind(R.id.tv_data)
    TextView tvData;
    @Bind(R.id.allowance)
    TextView allowance;
    @Bind(R.id.tv_addperson)
    TextView tvAddperson;
    @Bind(R.id.imageView13)
    ImageView imageView13;
    @Bind(R.id.tv_taskdesc)
    TextView tvTaskdesc;
    @Bind(R.id.imageView14)
    ImageView imageView14;

    private AdRedEnvelopesBean.BodyBean info;
    CountDownTimer timers;
    String t_hour = "";
    String t_min = "";
    String t_secound = "";
    int timemiao;

    @Override
    public int getLayout() {
        return R.layout.activity_red_task;
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
        tvTitle.setText("任务");
        Intent intent = getIntent();
        info = (AdRedEnvelopesBean.BodyBean) intent.getSerializableExtra("task");
        tvTheme.setText(info.getTwo_level());
        tvData.setText(info.getTask_num());
        allowance.setText(info.getTask_balance());
        tvAddperson.setText(info.getCreate_time());
        tvTaskdesc.setText(info.getContent_total());
        // tvAddperson.setText(info.getUser_name());
        String count_down = info.getCount_down();

        String timegets = info.getExecute_time();
        timemiao = StringUtils.getTimeMiaoCha(timegets);
        switch (StringUtils.CompareTime(timegets)) {
            case 2://超时或相同（正计时）
                rightTime();
                break;
            default:
                downTime();
                break;
        }
    }

    private void downTime() {//倒计时
        tvHour.setBackgroundResource(R.drawable.corner_green);
        tvMin.setBackgroundResource(R.drawable.corner_green);
        tvTimesecound.setBackgroundResource(R.drawable.corner_green);
        tvReward.setText("￥ " + info.getTask_award());
        tvReward.setTextColor(getResources().getColor(R.color.textgreen));
        timers = new CountDownTimer(timemiao * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hour = millisUntilFinished / (1000 * 60 * 60);//时
                long min = (millisUntilFinished - hour * (1000 * 60 * 60)) / (1000 * 60);
                long second = (millisUntilFinished - hour * (1000 * 60 * 60) - min * (1000 * 60)) / 1000;//秒

                t_hour = String.valueOf(hour);
                t_min = String.valueOf(min);
                t_secound = String.valueOf(second);
                if (t_hour.length() == 1) {
                    t_hour = "0" + t_hour;
                }
                if (t_min.length() == 1) {
                    t_min = "0" + t_min;
                }
                if (t_secound.length() == 1) {
                    t_secound = "0" + t_secound;
                }
                if (tvHour != null) {
                    tvHour.setText(t_hour);
                    tvMin.setText(t_min);
                    tvTimesecound.setText(t_secound);
                }

            }

            @Override
            public void onFinish() {
                rightTime();
            }
        };
        timers.start();
    }

    private void rightTime() {//正计时
        tvHour.setBackgroundResource(R.drawable.corner_red);
        tvMin.setBackgroundResource(R.drawable.corner_red);
        tvTimesecound.setBackgroundResource(R.drawable.corner_red);
        tvReward.setText("￥ 0");
        tvReward.setTextColor(getResources().getColor(R.color.text_blackfour));
        tvHour.setVisibility(View.GONE);
        tvMin.setVisibility(View.GONE);
        tvMiao1.setVisibility(View.GONE);
        tvMiao2.setVisibility(View.GONE);
        tvTimesecound.setText("任务到期");
    }

    @Override
    public void initAdapter() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        if (timers != null) {
            timers.cancel();
        }
        finish();
    }
}
