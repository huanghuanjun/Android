package dhu.cst.zjm.encrypt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import dhu.cst.zjm.encrypt.models.EncryptType;
import dhu.cst.zjm.encrypt.R;

/**
 * Created by zjm on 2017/1/8.
 */

public class Menu_File_Type_Adapter extends RecyclerSwipeAdapter<Menu_File_Type_Adapter.MyViewHolder> {

    private List<EncryptType> mData;
    private LayoutInflater mInflater;
    private OnItemClickListener downloadListener;
    private OnItemClickListener rightListener;
    private OnItemClickListener listener;
    private OnItemClickListener decryptListener;

    public Menu_File_Type_Adapter(Context context, List<EncryptType> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    public void setDownloadClickListener(OnItemClickListener listener) {
        this.downloadListener = listener;
    }

    public void setRightClickListener(OnItemClickListener listener) {
        this.rightListener = listener;
    }

    public void setDecryptClickListener(OnItemClickListener listener) {
        this.decryptListener = listener;
    }

    public void setClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(
                R.layout.rv_menu_file_type_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        setupMainView(holder, position);
        setupSwipeView(holder, position);
    }

    private void setupMainView(final MyViewHolder holder, final int position) {
        EncryptType encryptType = mData.get(position);
        holder.tv_rv_menu_file_type_name.setText(encryptType.getName());
    }

    private void setupSwipeView(final MyViewHolder holder, final int position) {
        //设置刷卡布局显示模式
        holder.sl_menu_file_type_item.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.b_menu_file_type_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadListener != null) {
                    downloadListener.onItemClick(position);
                }
            }
        });

        holder.b_menu_file_type_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightListener != null) {
                    rightListener.onItemClick(position);
                }
            }
        });
        holder.iv_menu_file_type_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        holder.b_menu_file_type_decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (decryptListener != null) {
                    decryptListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sl_menu_file_type_item;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_rv_menu_file_type_name;
        SwipeLayout sl_menu_file_type_item;
        Button b_menu_file_type_encrypt, b_menu_file_type_download, b_menu_file_type_decrypt;
        ImageView iv_menu_file_type_details;

        public MyViewHolder(View view) {
            super(view);
            sl_menu_file_type_item = (SwipeLayout) view.findViewById(R.id.sl_menu_file_type_item);
            tv_rv_menu_file_type_name = (TextView) view.findViewById(R.id.tv_ll_menu_file_type_name);
            b_menu_file_type_encrypt = (Button) view.findViewById(R.id.b_menu_file_type_encrypt);
            b_menu_file_type_download = (Button) view.findViewById(R.id.b_menu_file_type_download);
            iv_menu_file_type_details = (ImageView) view.findViewById(R.id.iv_menu_file_type_details);
            b_menu_file_type_decrypt = (Button) view.findViewById(R.id.b_menu_file_type_decrypt);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
