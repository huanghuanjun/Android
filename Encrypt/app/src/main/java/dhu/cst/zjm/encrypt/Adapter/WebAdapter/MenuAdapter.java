package dhu.cst.zjm.encrypt.Adapter.WebAdapter;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dhu.cst.zjm.encrypt.Action.ActionsCreator;
import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Base.MapKey.MenuMap;
import dhu.cst.zjm.encrypt.WebApi.BaseUrl;
import dhu.cst.zjm.encrypt.WebApi.WebService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lenovo on 2016/11/30.
 */

public class MenuAdapter {
    private static MenuAdapter instance;
    final Dispatcher dispatcher;
    private ActionsCreator actionsCreator;


    public MenuAdapter(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        actionsCreator = ActionsCreator.get(dispatcher);
    }

    public static MenuAdapter get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new MenuAdapter(dispatcher);
        }
        return instance;
    }


    public void Get_Menu_File_List(int id) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
        final WebService webService = retrofit.create(WebService.class);
        Call<ResponseBody> responseBodyCall = webService.Get_Menu_File_List(BaseUrl.GET_MENU_FILE_LIST, id + "");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.body() != null) {
                    String resp = null;
                    try {
                        resp = response.body().string();
                        actionsCreator.Get_Menu_File_List_Success(resp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void Get_Menu_File_Type() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
        final WebService webService = retrofit.create(WebService.class);
        Call<ResponseBody> responseBodyCall = webService.Get_Menu_File_Type(BaseUrl.GET_MENU_FILE_TYPE);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response.body() != null) {
                    String resp = null;
                    try {
                        resp = response.body().string();
                        actionsCreator.Get_Menu_File_Type_Success(resp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void Encrypt_File(String json) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
        final WebService webService = retrofit.create(WebService.class);
        Call<ResponseBody> responseBodyCall = webService.Encrypt_File(BaseUrl.ENCRYPT_FILE, json);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
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
    }

    public void Set_Encrypt_Inf(String json) {
        final String s=json;
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .client(client)
                .build();
        final WebService webService = retrofit.create(WebService.class);
        Call<ResponseBody> responseBodyCall = webService.Set_Encrypt_Inf(BaseUrl.SET_ENCRYPT_INF, json);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    if (response.body().string().equals(MenuMap.SET_ENCRYPT_FILE_SUCCESS_KEY)) {
                        actionsCreator.Set_Encrypt_Inf_Success(s);
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

}
