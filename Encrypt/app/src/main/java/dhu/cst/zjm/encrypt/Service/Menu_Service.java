package dhu.cst.zjm.encrypt.Service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import dhu.cst.zjm.encrypt.Action.ActionsCreator;
import dhu.cst.zjm.encrypt.Base.PathAndKey;
import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Models.EncryptFile;
import dhu.cst.zjm.encrypt.Views.UI_Menu_Action;
import dhu.cst.zjm.encrypt.WebApi.BaseUrl;
import dhu.cst.zjm.encrypt.WebApi.DownloadFileResponseBody;
import dhu.cst.zjm.encrypt.WebApi.ProgressListener;
import dhu.cst.zjm.encrypt.WebApi.UploadFileRequestBody;
import dhu.cst.zjm.encrypt.WebApi.WebService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/1/4.
 */

public class Menu_Service extends Service {

    private IBinder binder = new Menu_Service.MyBinder();
    private WebSocketClient webSocketClient;
    private Dispatcher dispatcher;
    private ActionsCreator actionsCreator;
    private int userId;

    public class MyBinder extends Binder {
        public Menu_Service getService() {
            return Menu_Service.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new Menu_Service.MyBinder();
        this.dispatcher = Dispatcher.get(EventBus.getDefault());
        actionsCreator = ActionsCreator.get(dispatcher);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setID(int id) {
        userId = id;
        try {
            Try_Connect_Menu();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void Try_Connect_Menu() throws URISyntaxException {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        final Queue<String> messageQueue = new LinkedList<String>();
        String uri = BaseUrl.BASEWEBSOCKET + BaseUrl.BASEIP + BaseUrl.BASEPORT + BaseUrl.WEB_SERVICE + userId;
        webSocketClient = new WebSocketClient(new URI(uri), new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("wlf", "已经连接到服务器【" + getURI() + "】");
                actionsCreator.Try_Connect_Menu_Success();
            }

            @Override
            public void onMessage(String s) {
                messageQueue.add(s);
                while (!messageQueue.isEmpty()) {
                    final String message = messageQueue.remove();
                    Log.e("wlf", "获取到服务器信息【" + message + "】");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            actionsCreator.Web_Socket_Menu_Message(message);
                        }
                    }).start();
                }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.e("wlf", "断开服务器连接【" + getURI() + "，状态码： " + i + "，断开原因：" + s + "】");
                actionsCreator.Web_Socket_Menu_Close(i, s);
            }

            @Override
            public void onError(Exception e) {
                Log.e("wlf", "连接发生了异常【异常原因：" + e.toString() + "】");
                actionsCreator.Web_Socket_Menu_Error(e);
            }
        };
        try {
            webSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
            actionsCreator.Try_Connect_Menu_Fail();
        }
    }

    public void Upload_File(int id, String path) {
        final String uploadId = id + "";
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        final WebService webService = retrofit.create(WebService.class);
        final Map<String, RequestBody> file = new HashMap<>();
        if (paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                String substring = paths.get(i).substring(paths.get(i).lastIndexOf("/") + 1, paths.get(i).length());
                file.put("file\"; filename=" + substring, new UploadFileRequestBody(new File(paths.get(i)), actionsCreator));
            }
        }

        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Call<ResponseBody> stringCall = webService.Upload_File(BaseUrl.UPLOAD_FILE, uploadId, file);
                stringCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        if (response.body() != null) {

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });
                return null;
            }
        }.execute();
    }

    public void Download_File(String json) {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {

            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Response orginalResponse = chain.proceed(chain.request());

                return orginalResponse.newBuilder()
                        .body(new DownloadFileResponseBody(orginalResponse.body(), new ProgressListener() {
                            @Override
                            public void onProgress(long progress, long total, boolean done) {
                                int i = (int) (progress * UI_Menu_Action.PROGRESS_MAX / total);
                                actionsCreator.Download_File_Progress(i);
                            }
                        }))
                        .build();
            }
        });

        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
        final WebService webService = retrofit.create(WebService.class);
        final String realName = getRealFileNameFromJson(json);
        Call<ResponseBody> responseBodyCall = webService.Download_File(BaseUrl.DOWNLOAD_FILE, json);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.body() != null) {
                    PathAndKey pathAndKey=new PathAndKey();
                    String mainPath = PathAndKey.getPath() + userId + "/";
                    pathAndKey.setPath(mainPath);
                    final String downloadPath = pathAndKey.getFileSavePath();
                    final ResponseBody body = response.body();
                    new AsyncTask<Void, Long, Void>() {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            boolean writtenToDisk = writeResponseBodyToDisk(body, downloadPath, realName);
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private String getRealFileNameFromJson(String json) {
        Gson gson = new Gson();
        EncryptFile get = gson.fromJson(json, new TypeToken<EncryptFile>() {
        }.getType());

        String fileName = get.getFileName();
        String[] s = fileName.split("\\.");
        String fileRealName = s[0];
        return fileRealName;
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String downloadPath, String realFileName) {
        try {
            String type = BaseUrl.DOWNLOAD_FILE_TYPE;
            File futureStudioIconFile = new File(downloadPath + File.separator + realFileName + type);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.i("Download Success ", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
