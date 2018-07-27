package com.rxjy.rxinvestment.utils;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.rxjy.rxinvestment.base.App;

/**
 * Created by 阿禹 on 2018/6/27.
 * 图片选择器
 */

public class PhotoUtils {

    public static void checkIco(int code) {

        PictureSelector.create(App.baseActivity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .scaleEnabled(false)// 裁剪是否可放大缩小图片 true or false
                .forResult(code);//结果回调onActivityResult code 
    }

    public static void checkManyIco(int code) {
        PictureSelector.create(App.baseActivity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .imageSpanCount(3)
                .minSelectNum(0)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .scaleEnabled(false)// 裁剪是否可放大缩小图片 true or false
                .forResult(code);//结果回调onActivityResult code 
    }

}
