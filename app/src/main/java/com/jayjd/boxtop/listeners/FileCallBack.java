package com.jayjd.boxtop.listeners;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.FileConvert;

import java.io.File;

public abstract class FileCallBack extends AbsCallback<File> {

    private final FileConvert convert;    //文件转换类

    public FileCallBack() {
        this(null);
    }

    public FileCallBack(String destFileName) {
        this(null, destFileName);
    }

    public FileCallBack(File externalStoragePublicDirectory, String fileName) {
        convert = new FileConvert(externalStoragePublicDirectory.getAbsolutePath(), fileName);
        convert.setCallback(this);
    }

    @Override
    public File convertResponse(okhttp3.Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }
}
