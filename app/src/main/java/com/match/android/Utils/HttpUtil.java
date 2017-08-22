package com.match.android.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Java on 2017/6/19.
 */

public class HttpUtil {
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");//mdiatype 这个需要和服务端保持一致 你需要看下你们服务器设置的ContentType 是不是这个，他们设置的是哪个 我们要和他们保持一致
    public static void sendOkHttpRequest(String address,RequestBody newRequestBody,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = newRequestBody;
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
        Request request1 = new Request.Builder().url(address).post(requestBody).build();
    }

    public static void uploadFile(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        File file = new File(Environment.getDownloadCacheDirectory(),"test.jpg");
        Log.d("upload",file.toString());
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        Log.d("upload","1");
//        ------WebKitFormBoundaryJ0iuGTPmhsoF94SF
//        Content-Disposition: form-data; name="file"; filename="timg.jpg"
//        Content-Type: image/jpeg
//
//                ------WebKitFormBoundaryJ0iuGTPmhsoF94SF
//        Content-Disposition: form-data; name="submit"

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addPart(Headers.of("Content-Disposition","form-data; name=\"username\""),RequestBody.create(null,"孙启鹏"))
                .addPart(Headers.of("Content-Type","image/jpeg"),fileBody)
                .addPart(Headers.of("Content-Disposition","form-data; name=\"submit\""),RequestBody.create(null,"孙启鹏"))
                .build();
        Log.d("upload","2");
        Request request = new Request.Builder().url(address).post(requestBody).build();
        Log.d("upload","3");
        client.newCall(request).enqueue(callback);
        Log.d("upload","4");
    }

}