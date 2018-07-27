package com.rxjy.rxinvestment.fragment.utils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.utils.AutoUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import butterknife.ButterKnife;

/**
 * Created by Mr on 16-8-3.
 * 所有普通的Fragment 继承 此基类
 */
public abstract class BaseFragment extends Fragment {

    private View view;

    public Toast toast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getInitId(), container, false);
        initView(view);
        initData();
        initAdapter();
        initLinstener();
        //初始化屏幕适配
        AutoUtils.auto(view);
        ButterKnife.bind(this, view);

        return view;
    }

    //获得布局
    public abstract int getInitId();
    //初始化控件
    public abstract void initView(View view);
    //初始化数据
    public abstract void initData();
    //初始化适配器
    public abstract void initAdapter();
    //初始化监听
    public abstract void initLinstener();

    private ProgressDialog dialog;

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.loading));
        dialog.show();
    }

    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setProgressDialog(final long l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            ToastUtil.getInstance().toastCentent("当前网络较差", getActivity());
                        }
                    }
                });
            }
        }).start();
    }

    public void setProgressDialog(final long l, final int size) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            if (size == 0) {
                                ToastUtil.getInstance().toastCentent("未获取到更多数据", getActivity());
                            } else {
                                ToastUtil.getInstance().toastCentent("当前网络较差", getActivity());
                            }
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(getClass().getSimpleName(), getClass().getSimpleName() + ":onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getClass().getSimpleName(), ":onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(), ":onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(), ":onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getClass().getSimpleName(), ":onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), ":onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(getClass().getSimpleName(), ":onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
        ButterKnife.unbind(this);
        Log.d(getClass().getSimpleName(), ":onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getClass().getSimpleName(), ":onDetach");
    }

}