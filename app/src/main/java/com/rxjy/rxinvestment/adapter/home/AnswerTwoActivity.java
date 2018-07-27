package com.rxjy.rxinvestment.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.custom.MyGridView;
import com.rxjy.rxinvestment.entity.Person;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerTwoActivity extends BaseActivity {

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
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_image)
    ImageView headImage;
    @Bind(R.id.titile_one)
    TextView titileOne;
    @Bind(R.id.Answer_grid)
    MyGridView AnswerGrid;
    @Bind(R.id.titile_two)
    TextView titileTwo;
    @Bind(R.id.Answer_grid_two)
    MyGridView AnswerGridTwo;
    @Bind(R.id.titile_there)
    TextView titileThere;
    @Bind(R.id.Answer_grid_there)
    MyGridView AnswerGridThere;
    @Bind(R.id.sunmit)
    Button sunmit;

    private AnswerAdapter myAdapter;
    private AnswerAdapter twoAdapter;
    private AnswerAdapter thereAdapter;
    private String answer;
    List<Person> datas = new ArrayList();//数据集合
    List<Person> datatwo = new ArrayList();//数据集合
    List<Person> datathere = new ArrayList();//数据集合

    @Override
    public int getLayout() {
        return R.layout.activity_answer_two;
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
        imgBack.setVisibility(View.INVISIBLE);
        tvTitle.setText("瑞祥装饰");
        answer = getIntent().getStringExtra("answer");
        headImage.setImageResource(R.mipmap.shili);

        int[] screenSize = getScreenSize(this);
        int heghit = screenSize[0];
        int v = (int) (heghit * 0.6);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) headImage.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = v;
        headImage.setLayoutParams(linearParams);
        headTitle.setText("");

        //地域人脉
        titileOne.setText("1、地域人脉");
        titileTwo.setText("2、计划投资");
        titileThere.setText("3、投资形式");
        datas.add(new Person("较好", 23));
        datas.add(new Person("一般", 24));
        datas.add(new Person("无", 25));
        datatwo.add(new Person("20万", 26));
        datatwo.add(new Person("40万", 27));
        datatwo.add(new Person("60万", 28));
        datathere.add(new Person("合伙", 29));
        datathere.add(new Person("独资", 30));
        initListener();
    }

    private void initListener() {
        AnswerGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int currentNum = -1;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Person person : datas) { //遍历list集合中的数据
                    person.setChecked(false);//全部设为未选中
                }
                if (currentNum == -1) { //选中
                    datas.get(position).setChecked(true);
                    currentNum = position;
                } else if (currentNum == position) { //同一个item选中变未选中
                    datas.get(position).setChecked(true);
                    currentNum = position;
                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                    for (Person person : datas) {
                        person.setChecked(false);
                    }
                    datas.get(position).setChecked(true);
                    currentNum = position;
                }
                // Toast.makeText(parent.getContext(),datas.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                myAdapter.notifyDataSetChanged();//刷新adapter
            }
        });
        AnswerGridTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int currentNum = -1;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Person person : datatwo) { //遍历list集合中的数据
                    person.setChecked(false);//全部设为未选中
                }
                if (currentNum == -1) { //选中
                    datatwo.get(position).setChecked(true);
                    currentNum = position;
                } else if (currentNum == position) { //同一个item选中变未选中
                    datatwo.get(position).setChecked(true);
                    currentNum = position;
                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                    for (Person person : datatwo) {
                        person.setChecked(false);

                    }
                    datatwo.get(position).setChecked(true);
                    currentNum = position;
                }
                // Toast.makeText(parent.getContext(),datas.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                twoAdapter.notifyDataSetChanged();//刷新adapter
            }
        });
        AnswerGridThere.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int currentNum = -1;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Person person : datathere) { //遍历list集合中的数据
                    person.setChecked(false);//全部设为未选中
                }
                if (currentNum == -1) { //选中
                    datathere.get(position).setChecked(true);
                    currentNum = position;
                } else if (currentNum == position) { //同一个item选中变未选中
                    datathere.get(position).setChecked(true);
                    currentNum = position;
                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                    for (Person person : datathere) {
                        person.setChecked(false);
                    }
                    datathere.get(position).setChecked(true);
                    currentNum = position;
                }
                // Toast.makeText(parent.getContext(),datas.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                thereAdapter.notifyDataSetChanged();//刷新adapter
            }
        });
    }

    @Override
    public void initAdapter() {
        twoAdapter = new AnswerAdapter(this, datatwo);
        thereAdapter = new AnswerAdapter(this, datathere);
        myAdapter = new AnswerAdapter(this, datas);
        AnswerGrid.setAdapter(myAdapter);
        AnswerGridTwo.setAdapter(twoAdapter);
        AnswerGridThere.setAdapter(thereAdapter);

    }

    @OnClick({R.id.img_back, R.id.sunmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                break;
            case R.id.sunmit:
                StringBuffer stringbuffer = new StringBuffer(answer);
                StringBuffer string = new StringBuffer();

                for (Person pos : datas) {
                    boolean checked = pos.isChecked();
                    if (checked) {
                        stringbuffer.append(pos.getId() + ",");
                        string.append("1");
                    }
                }
                for (Person pos : datatwo) {
                    boolean checked = pos.isChecked();
                    if (checked) {
                        stringbuffer.append(pos.getId() + ",");
                        string.append("2");
                    }
                }
                for (Person pos : datathere) {
                    boolean checked = pos.isChecked();
                    if (checked) {
                        stringbuffer.append(pos.getId() + ",");
                        string.append("3");
                    }
                }
                if (string.toString().equals("123")) {
                    startActivity(new Intent(this, AnswerThereActivity.class).putExtra("answer", stringbuffer.toString()));
                    finish();
                } else {
                    ToastUtil.getInstance().toastBottom("请答完全部题");
                }
                break;
        }
    }

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

}
