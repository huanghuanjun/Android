package dhu.cst.zjm.encrypt.web_api;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.util.Map;

import dhu.cst.zjm.encrypt.base_data.map_key.MenuMap;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Streaming;

/**
 * Created by zjm on 2016/11/5.
 */

public interface WebService {

    @POST("{path}")
    Call<ResponseBody> Login_Internet(@Path("path") String path, @Body RequestBody json);

    @POST("{path}")
    Call<ResponseBody> Register_Try_Connect(@Path("path") String path);

    @FormUrlEncoded
    @POST("{path}")
    Call<ResponseBody> Get_Menu_File_List(@Path("path") String path, @Field(MenuMap.GET_MENU_FILE_LIST_KEY) String id);

    @POST("{path}")
    Call<ResponseBody> Get_Menu_File_Type(@Path("path") String path);

    @POST("{path}")
    Call<ResponseBody> Register_Try(@Path("path") String path, @Body RequestBody json);

    @Multipart
    @POST("{path}")
    Call<ResponseBody> Upload_File(@Path("path") String path, @Header(MenuMap.UPLOAD_FILE_ID_KEY) String id,
                                   @PartMap Map<String, RequestBody> fileMap);

    @FormUrlEncoded
    @Streaming
    @POST("{path}")
    Call<ResponseBody> Download_File(@Path("path") String path, @Field(MenuMap.DOWNLOAD_FILE_KEY) String json);

    @FormUrlEncoded
    @POST("{path}")
    Call<ResponseBody> Encrypt_File(@Path("path") String path,@Field(MenuMap.ENCRYPT_FILE_KEY) String json);

    @FormUrlEncoded
    @POST("{path}")
    Call<ResponseBody> Set_Encrypt_Inf(@Path("path") String path,@Field(MenuMap.SET_ENCRYPT_FILE_KEY) String json);



}
