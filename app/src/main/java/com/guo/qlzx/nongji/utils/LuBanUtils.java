package com.guo.qlzx.nongji.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * create by xuxx on 2018/6/26
 */
public class LuBanUtils {
    public void getFileList(Context context, final List<String> fileList, final OnCompressSuccessListener onCompressSuccessListener) {
        final List<File> list = new ArrayList();
        if(fileList.size()>0)
        for (int i = 0; i < fileList.size(); i++) {
            Luban.with(context)
                    .load(new File(fileList.get(i)))                                   // 传人要压缩的图片列表
                    .ignoreBy(100)                                  // 忽略不压缩图片的大小
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(File file) {
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                            list.add(file);
                            if (list.size() == fileList.size()) {
                                onCompressSuccessListener.onSuccess(list);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过程出现问题时调用
                        }
                    }).launch();    //启动压缩
        }
        else  onCompressSuccessListener.onSuccess(list);
    }

    public interface OnCompressSuccessListener {
        void onSuccess(List<File> list);
    }
}
