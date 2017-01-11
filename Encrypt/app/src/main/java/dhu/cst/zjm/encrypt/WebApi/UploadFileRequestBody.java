package dhu.cst.zjm.encrypt.WebApi;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;

import dhu.cst.zjm.encrypt.Action.ActionsCreator;
import dhu.cst.zjm.encrypt.Views.UI_Menu_Action;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by admin on 2017/1/6.
 */

public class UploadFileRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    private ActionsCreator actionsCreator;
    private BufferedSource mBufferedSource;
    private BufferedSink bufferedSink;

    public UploadFileRequestBody(File file, ActionsCreator actionsCreator) {
        this.mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        this.actionsCreator = actionsCreator;
    }

    public UploadFileRequestBody(RequestBody requestBody, ActionsCreator actionsCreator) {
        this.mRequestBody = requestBody;
        this.actionsCreator = actionsCreator;
    }

    //返回了requestBody的类型，想什么form-data/MP3/MP4/png等等等格式
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    //返回了本RequestBody的长度，也就是上传的totalLength
    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        mRequestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调上传接口
                int progress = (int) (bytesWritten * UI_Menu_Action.PROGRESS_MAX / contentLength);

                actionsCreator.Upload_File_Progress(progress);
            }
        };
    }
}
