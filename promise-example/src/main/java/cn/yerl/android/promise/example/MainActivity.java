package cn.yerl.android.promise.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import cn.yerl.android.promise.core.Promise;
import cn.yerl.android.promise.core.PromiseCallback;
import cn.yerl.android.promise.core.PromiseCallbackWithResolver;
import cn.yerl.android.promise.core.PromiseResolver;
import cn.yerl.android.promise.example.model.Contact;
import cn.yerl.android.promise.http.PromiseHttp;
import cn.yerl.android.promise.http.PromiseRequest;
import cn.yerl.android.promise.http.PromiseResponse;
import cn.yerl.android.promise.http.logger.LogcatLogger;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PromiseHttp.client().setBaseUrl("http://192.168.0.120:8080");
        PromiseHttp.client().setLogger(new LogcatLogger());
        PromiseHttp.client().setCachePath(MainActivity.this.getCacheDir());
    }

    public void onClick(View view) throws Exception{

        PromiseRequest loginReq = PromiseRequest.GET("https://ssl.codesync.cn/mobilework/login/login?j_username=admin&j_password=11");

        PromiseHttp.client().execute(loginReq).then(new PromiseCallback<PromiseResponse, Object>() {
            @Override
            public Object call(PromiseResponse arg) {
                PromiseRequest request = PromiseRequest.GET("https://ssl.codesync.cn/mobilework/s?fileInid=2014815681&fileName=%E6%B5%8B%E8%AF%95%E9%99%84%E4%BB%B6%E4%B8%8B%E8%BD%BD.doc&stepInco=2018720162&service=flowAttach&flowInid=2012771234");
                request.addDownloadProgressListener(new PromiseRequest.OnProgressChanged() {
                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        Log.d("download", "bytesWritten:" + bytesWritten + ", totalSize: " + totalSize);
                    }
                });
                PromiseHttp.client().download(request).then(response -> {
                    System.out.println(response.getResponseFile().getName());
                    return null;
                }).error(error -> {
                    error.printStackTrace();
                });

                return null;
            }
        });


//
//
//        JSONObject loginArg = new JSONObject();
//        loginArg.put("name", "admin");
//        loginArg.put("password", "123qwe");
//
//        PromiseRequest request = PromiseRequest.POST("/auth/api/nameLogin")
//                .withRawBody(loginArg.toString());
//
//        // ======================= 登录
//        PromiseHttp.client().execute(request).then((PromiseResponse arg, PromiseResolver<PromiseResponse> resolver) ->{
//            try {
//                JSONObject token = new JSONObject(arg.getResponseString());
//
//                // ======================= 申请上传
//                JSONObject uploadArgs = new JSONObject();
//                uploadArgs.put("token", token.optString("token"));
//                uploadArgs.put("parent", 0);
//                uploadArgs.put("name", file.getName());
//                uploadArgs.put("size", file.length());
//                uploadArgs.put("overWrite", true);
//
//                PromiseRequest requestUpload = PromiseRequest.POST("/fs/api/requestUpload")
//                        .withRawBody(uploadArgs.toString());
//
//                PromiseHttp.client().execute(requestUpload).then((PromiseResponse response, PromiseResolver<PromiseResponse> _resolver) -> {
//                    try{
//                        JSONObject data = new JSONObject(response.getResponseString());
//                        if (data.optString("stat").equals("OK")){
//
//                            // ======== 上传文件
//                            PromiseRequest fileUpload = PromiseRequest.POST(data.optJSONArray("nodes").getJSONObject(0).optString("addr") + "/formUpload")
//                                    .withQueryParam("fileUploadId", data.optString("fileUploadId"))
//                                    .withBodyParam("size", file.length())
//                                    .withBodyParam("name", file.getName())
//                                    .withBodyParam("file", file);
//
//                            PromiseHttp.client().execute(fileUpload).then((PromiseResponse resp) ->{
//                                try{
//                                    JSONObject info = new JSONObject(resp.getResponseString());
//
//                                    // ========== 完成上传
//                                    JSONObject commitArg = new JSONObject();
//                                    commitArg.put("token", token.optString("token"));
//                                    commitArg.put("fileUploadId", data.optString("fileUploadId"));
//                                    commitArg.put("partCommitIds", info.optJSONArray("partCommitIds"));
//                                    commitArg.put("parent", 0);
//                                    commitArg.put("name", file.getName());
//                                    commitArg.put("size", file.length());
//
//                                    PromiseHttp.client().execute(PromiseRequest.POST("/fs/api/commitUpload").withRawBody(commitArg.toString())).pipe(_resolver);
//
//                                }catch (Exception ex){
//                                    throw new RuntimeException(ex);
//                                }
//                            });
//                        }
//                    }catch (Exception ex){
//                        throw new RuntimeException(ex);
//                    }
//                }).pipe(resolver);
//
//            }catch (Exception ex){
//                throw new RuntimeException(ex);
//            }
//        }).then((PromiseResponse response)->{
//            Toast.makeText(MainActivity.this, response.getResponseString(), Toast.LENGTH_LONG).show();
//            return null;
//        }).error(error ->{
//            Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//        });

//        PromiseRequest request = PromiseRequest.GET("https://ssl.codesync.cn/mobilework/login/login")
//            .withQueryParam("j_username", "admin")
//            .withQueryParam("j_password", "11");
//
//        PromiseHttp.client().execute(request).then(response -> {
//            try{
//                JSONObject json = new JSONObject(response.getResponseString());
//                if (!json.optBoolean("success")){
//                    throw new RuntimeException(json.optString("message"));
//                }
//            }catch (Exception ex){
//                throw new RuntimeException("Json解析异常");
//            }
//            return Promise.Void;
//        }).then((Object arg, PromiseResolver<PromiseResponse> resolver) -> {
//            PromiseRequest authRequest = PromiseRequest.GET("https://ssl.codesync.cn/mobile-oa/api/authorize");
//            PromiseHttp.client().execute(authRequest).pipe(resolver);
//        }).then(response ->{
//            System.out.println(response.getResponseString());
//            return Promise.Void;
//        }).always(() -> System.out.println("stop loading"));

//        PromiseHttp.client().execute(request).then(response ->{
//            try {
//                JSONObject jsObj = new JSONObject(response.getResponseString());
//                if (!jsObj.optBoolean("success")){
//                    throw new RuntimeException(jsObj.optString("message"));
//                }
//            } catch (JSONException e) {
//                throw  new RuntimeException("Json解析异常");
//            }
//            return null;
//        }).thenAsync(() -> {
//            PromiseRequest authRequest = PromiseRequest.GET("https://ssl.codesync.cn/mobile-oa/api/authorize");
//            return PromiseHttp.client().execute(authRequest).then(PromiseResponse::getResponseString).getResult();
//        }).always(() -> {
//            System.out.println("dismiss loading");
//        });

//        Promise.resolve("abc").then(arg -> {
//            return arg + "aaa";
//        });


//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, String>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                try {
//                    JSONObject jsObj = new JSONObject(arg.getResponseString());
//                    if (jsObj.optBoolean("success")){
//                        return "登录成功";
//                    }else {
//                        return new RuntimeException(jsObj.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    return new RuntimeException("Json解析异常");
//                }
//            }
//        }).then(new PromiseCallback<String, String>() {
//            @Override
//            public Object call(String arg) {
//                PromiseRequest authRequest = PromiseRequest.GET("https://ssl.codesync.cn/mobile-oa/api/authorize");
//                return PromiseHttp.client().execute(authRequest).then(new PromiseCallback<PromiseResponse, Object>() {
//                    @Override
//                    public Object call(PromiseResponse arg) {
//                        String result = arg.getResponseString();
//                        return result;
//                    }
//                });
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                arg.printStackTrace();
//                return null;
//            }
//        });


//        PromiseRequest request = PromiseRequest.GET("/mobilework/login/login")
//                .withQueryParam("j_username", "admin_xxcyj")
//                .withQueryParam("j_password", "11");
//
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, PromiseResponse>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                PromiseRequest request = PromiseRequest.GET("/PASystem/appMain?userName=%C4%AA")
//                        .withQueryParam("service", "com.minstone.pasystem.action.port.helper.PortCmd")
//                        .withQueryParam("func", "queryReleaseSchedule")
//                        .withQueryParam("pageNo", "")
//                        .withQueryParam("pageSize", "")
//                        .withQueryParam("startDate", "")
//                        .withQueryParam("endDate", "")
////                        .withQueryParam("userName", "莫")
//                        .setEncoding("GBK");
//                return PromiseHttp.client().execute(request);
//            }
//        }).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Log.d("response", arg.getResponseString());
//                return null;
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                arg.printStackTrace();
//                return null;
//            }
//        });
//                .setEncoding("GBK");


//        PromiseRequest request = PromiseRequest.GET("http://codesync.cn/api/v3/groups");
//
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                Toast.makeText(MainActivity.this, arg.getMessage(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        });
//        PromiseRequest request =PromiseRequest.POST("http://ma.minstone.com.cn/mobilework/login/login?aa=bb")
//                .withQueryParam("j_username", "admin")
//                .withQueryParam("j_password", "11");
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
////                arg.printStackTrace();
//                Toast.makeText(MainActivity.this, arg.getMessage(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        });

//        PromiseRequest request = PromiseRequest.GET("http://192.168.0.185:9081/mobilework/login/login")
//                .withQueryParam("j_username", "admin")
//                .withQueryParam("j_password", "11");
//
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//
//                PromiseRequest request =PromiseRequest.GET("http://192.168.0.185:9081/OAMessage/api/summary");
//                return PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//                    @Override
//                    public Object call(PromiseResponse arg) {
//                        Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                        return null;
//                    }
//                });
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                Toast.makeText(MainActivity.this, arg.getMessage(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        });
        // GET Success
//        PromiseRequest request = PromiseRequest.GET("/OATasks/andes/interface.json?test=abc").withQueryParam("hello", "haha").withQueryParam("bb", "bb");
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        });
//
        // GET Error
//        PromiseRequest request = PromiseRequest.GET("/apps/latest/576107d2e75e2d717d000014");
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                if (arg instanceof PromiseHttpException){
//                    PromiseHttpException ex = (PromiseHttpException)arg;
//                    Toast.makeText(MainActivity.this, ex.getResponse().getResponseString(), Toast.LENGTH_LONG).show();
//                }
//                return null;
//            }
//        });

        // POST
//        PromiseRequest request = PromiseRequest.POST("http://api.fir.im/apps")
//                .withBodyParam("type", "ios")
//                .withBodyParam("bundle_id", "cn.yerl.aa")
//                .withBodyParam("api_token", "f156b688dd49f664d85a5c5eac6597d4");
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                if (arg instanceof PromiseHttpException){
//                    PromiseHttpException ex = (PromiseHttpException)arg;
//                    Toast.makeText(MainActivity.this, ex.getResponse().getResponseString(), Toast.LENGTH_LONG).show();
//                }
//                return null;
//            }
//        });

        // POST Error
//        PromiseRequest request = PromiseRequest.POST("http://api.fir.im/apps")
//                .withBodyParam("type", "ios")
//                .withBodyParam("bundle_id", "cn.yerl.aa")
//                .withBodyParam("api_token", "f156b688dd49f664d85a5c5eac6a597d4");
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                if (arg instanceof PromiseHttpException){
//                    PromiseHttpException ex = (PromiseHttpException)arg;
//                    Toast.makeText(MainActivity.this, ex.getResponse().getResponseString(), Toast.LENGTH_LONG).show();
//                }
//                return null;
//            }
//        });

        // Download
//        PromiseRequest request = PromiseRequest.GET("https://raw.githubusercontent.com/alan-yeh/gradle-plugins/master/nexus-plugin/build.gradle");
//
//        request.addDownloadProgressListener(new PromiseRequest.OnProgressChanged() {
//            @Override
//            public void onProgress(long bytesWritten, long totalSize) {
//                Log.d("【Download】", "bytesWritten: "+ bytesWritten + "  totalSize: " + totalSize);
//            }
//        });
//
//        PromiseHttp.client().download(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                return null;
//            }
//        });

//        File file = new File(MainActivity.this.getCacheDir(), "测试文件.txt");
//
//        if (!file.exists()){
//            String content = "测试的文件内容";
//            FileWriter writer = new FileWriter(file);
//            writer.write(content);
//            writer.close();
//        }
//
//        PromiseRequest loginReq = PromiseRequest.GET("http://192.168.0.208:8888/mobilework/login/login")
//                .withQueryParam("j_username", "test")
//                .withQueryParam("j_password", "11");
//
//        PromiseHttp.client().execute(loginReq).then((PromiseResponse response, PromiseResolver<PromiseResponse> resolver) ->{
//            PromiseRequest uploadReq = PromiseRequest.POST("http://192.168.0.208:8888/ConferenceManagementSystem/appMain?service=com.minstone.cms.action.port.helper.PortCmd&func=meetingFileDataUpload")
//                    .withBodyParam("meetingId", "123456")
//                    .withBodyParam("attach", file);
////            uploadReq.setEncoding("GBK");
//
//            PromiseHttp.client().execute(uploadReq).pipe(resolver);
//        }).then((PromiseResponse resp, PromiseResolver<PromiseResponse> resolver) ->{
//            PromiseRequest request = PromiseRequest.GET("http://192.168.0.208:8888/ConferenceManagementSystem/appMain?service=com.minstone.cms.action.port.helper.PortCmd&func=accessoryList")
//                    .withQueryParam("meetingId", "123465");
//            PromiseHttp.client().execute(request).pipe(resolver);
//        }).then(resp ->{
//            Toast.makeText(MainActivity.this, resp.getResponseString(), Toast.LENGTH_LONG).show();
//            return null;
//        }).error(error ->{
//            Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//        });

        // Upload
//        PromiseRequest request = PromiseRequest.POST("http://ma.minstone.com.cn/MDDisk/file").withBodyParam("file", new File(MainActivity.this.getCacheDir().getAbsolutePath() + File.separator + "build.gradle"));
//        PromiseHttp.client().execute(request).then(new PromiseCallback<PromiseResponse, Object>() {
//            @Override
//            public Object call(PromiseResponse arg) {
//                Toast.makeText(MainActivity.this, arg.getResponseString(), Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }).error(new PromiseCallback<RuntimeException, Object>() {
//            @Override
//            public Object call(RuntimeException arg) {
//                return null;
//            }
//        });
    }

    //网络访问封装
//    private Promise<String> get(final String url, final RequestParams params){
//        return new Promise<String>(new PromiseCallbackWithResolver<Object, String>() {
//            @Override
//            public void call(Object arg, final PromiseResolver<String> resolver) {
//                new AsyncHttpClient().get(url, params, new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        resolver.resolve(null, new RuntimeException("网络访问错误", throwable));
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                        resolver.resolve(responseString, null);
//                    }
//                });
//            }
//        });
//    }
//
//    //Json解析与业务分离
//    private Promise<JSONObject> parseJson(final String responseString){
//        return new Promise<JSONObject>(new PromiseCallback<Object, JSONObject>() {
//            @Override
//            public JSONObject call(Object arg) {
//                try {
//                    return new JSONObject(responseString);
//                }catch (JSONException e){
//                    throw new RuntimeException("Json解析出错", e);
//                }
//            }
//        });
//    }
//
//    public Promise<List<Contact>> getContacts(){
//        RequestParams params = new RequestParams();
//        params.put("username", "username");
//        params.put("password", "password");
//        //登录
//        return get("xxx/login", params).then((String arg, PromiseResolver<JSONObject> resolver) -> {
//            parseJson(arg).pipe(resolver);
//        }).then((JSONObject arg, PromiseResolver<String> resolver)->{
//            if (arg.optInt("status") == 200){
//                //登录成功
//                RequestParams contactParams = new RequestParams();
//                contactParams.put("pageIndex", 1);
//                contactParams.put("pageSize", 20);
//                get("xxxx/contacts", contactParams).pipe(resolver);
//            }else {
//                throw new RuntimeException("登录失败");
//            }
//        }).then((String arg, PromiseResolver<JSONObject> resolver) ->{
//            parseJson(arg).pipe(resolver);
//        }).then((JSONObject arg) ->{
//            if (arg.optInt("status") == 200){
//                List<Contact> contacts = new ArrayList<Contact>();
//                //处理业务
//                //组装实体
//                //....
//                //....
//                return contacts;
//            }else {
//                throw new RuntimeException("数据获取失败");
//            }
//        });
//    }
}
