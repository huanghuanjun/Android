package dhu.cst.zjm.encrypt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dhu.cst.zjm.encrypt.Models.ServerFile;
import dhu.cst.zjm.encrypt.R;

/**
 * Created by admin on 2017/1/3.
 */

public class Menu_File_Adapter extends BaseAdapter {
    private List<ServerFile> list = new ArrayList<>();
    private LayoutInflater mInflater;
    private OnItemClickListener listener;

    public Menu_File_Adapter(Context mContext, List<ServerFile> list) {
        mInflater = LayoutInflater.from(mContext);
        this.list = list;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ServerFile serverFile = list.get(position);
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lv_menu_file_list_item, null);
            viewHolder.rl_lv_menu_file_item=(RelativeLayout) convertView.findViewById(R.id.rl_lv_menu_file_item);
            viewHolder.tv_lv_menu_file_name = (TextView) convertView.findViewById(R.id.tv_lv_menu_file_name);
            viewHolder.tv_lv_menu_file_size = (TextView) convertView.findViewById(R.id.tv_lv_menu_file_size);
            viewHolder.tv_lv_menu_file_upload_time = (TextView) convertView.findViewById(R.id.tv_lv_menu_file_upload_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_lv_menu_file_name.setText(serverFile.getName());
        viewHolder.tv_lv_menu_file_size.setText(serverFile.getSize());
        viewHolder.tv_lv_menu_file_upload_time.setText(serverFile.getUploadTime());
        viewHolder.rl_lv_menu_file_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(position);
                }
            }
        });
        return convertView;
    }

    final static class ViewHolder {
        RelativeLayout rl_lv_menu_file_item;
        TextView tv_lv_menu_file_name;
        TextView tv_lv_menu_file_size;
        TextView tv_lv_menu_file_upload_time;
    }

    public interface OnItemClickListener {
        void onItemClick(int postion);
    }

}
