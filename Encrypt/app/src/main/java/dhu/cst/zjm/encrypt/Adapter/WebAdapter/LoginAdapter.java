package dhu.cst.zjm.encrypt.Adapter.WebAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dhu.cst.zjm.encrypt.Action.ActionsCreator;
import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Base.MapKey.LoginMap;
import dhu.cst.zjm.encrypt.WebApi.BaseUrl;
import dhu.cst.zjm.encrypt.WebApi.WebService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2016/11/6.
 */

public class LoginAdapter {
    private static LoginAdapter instance;
    final Dispatcher dispatcher;
    private ActionsCreator actionsCreator;

    public LoginAdapter(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        actionsCreator = ActionsCreator.get(dispatcher);
    }

    public static LoginAdapter get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new LoginAdapter(dispatcher);
        }
        return instance;
    }

    public void Login_Internet(String json) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
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

    private void responseLogin(String resp) {
            actionsCreator.Login_Internet_Resp(resp);
    }

    public void Register_Try_Connect() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
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

    public void Register_Try(String json) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
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
