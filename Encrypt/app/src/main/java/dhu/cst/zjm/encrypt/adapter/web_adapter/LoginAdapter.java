package dhu.cst.zjm.encrypt.adapter.web_adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dhu.cst.zjm.encrypt.action.ActionsCreator;
import dhu.cst.zjm.encrypt.dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.base_data.map_key.LoginMap;
import dhu.cst.zjm.encrypt.base_data.BaseUrl;
import dhu.cst.zjm.encrypt.web_api.WebService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by zjm on 2016/11/6.
 */

public class LoginAdapter {
    private static LoginAdapter instance;
    final Dispatcher dispatcher;
    private ActionsCreator actionsCreator;
    private Retrofit retrofit;

    public LoginAdapter(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        actionsCreator = ActionsCreator.get(dispatcher);
        init();
    }

    public static LoginAdapter get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new LoginAdapter(dispatcher);
        }
        return instance;
    }

    private void init() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
    }

    /**
     * 用户登录
     *
     * @param json Map<String, String>的json
     */
    public void Login_Internet(String json) {
        WebService webService = retrofit.create(WebService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse(BaseUrl.CONTENT_TYPE_JSON), json);
        Call<ResponseBody> responseBodyCall = webService.Login_Internet(BaseUrl.LOGIN_INTERNET, requestBody);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    if (response.body() != null) {
                        String resp = response.body().string();
                        responseLogin(resp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /**
     * 返回登录情况
     *
     * @param resp 登录情况信息
     */
    private void responseLogin(String resp) {
        actionsCreator.Login_Internet_Resp(resp);
    }

    /**
     * 注册前进行网络连接测试
     */
    public void Register_Try_Connect() {
        WebService webService = retrofit.create(WebService.class);
        Call<ResponseBody> responseBodyCall = webService.Register_Try_Connect(BaseUrl.REGISTER_TRY_Connect);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    if (response.body() != null) {
                        String s = response.body().string();
                        if (s.equals(LoginMap.REGISTER_TRY_IP_SUCCESS_VALUE)) {
                            actionsCreator.Register_Try_Connect_Success();
                        } else {
                            actionsCreator.Register_Try_Connect_Fail();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 注册用户
     *
     * @param json Map<String, String>的json
     */
    public void Register_Try(String json) {
        WebService webService = retrofit.create(WebService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse(BaseUrl.CONTENT_TYPE_JSON), json);
        Call<ResponseBody> responseBodyCall = webService.Register_Try(BaseUrl.REGISTER_TRY, requestBody);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    if (response.body() != null) {
                        String resp = response.body().string();
                        responseRegister(resp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 读取json并返回注册状态
     *
     * @param resp Map<String, String>的json
     */
    private void responseRegister(String resp) {
        Gson gson = new Gson();
        Map<String, String> register = gson.fromJson(resp, new TypeToken<Map<String, String>>() {
        }.getType());
        String id = register.get(LoginMap.REGISTER_ID_KEY);
        String state = register.get(LoginMap.REGISTER_STATE_KEY);
        if (state.equals(LoginMap.REGISTER_STATE_SUCCESS_VALUE)) {
            actionsCreator.Register_Success(id);
        } else {
            actionsCreator.Register_Fail();
        }
    }
}
