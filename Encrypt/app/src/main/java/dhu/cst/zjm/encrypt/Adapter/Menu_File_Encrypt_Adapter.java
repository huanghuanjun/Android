package dhu.cst.zjm.encrypt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dhu.cst.zjm.encrypt.Models.EncryptInf;
import dhu.cst.zjm.encrypt.R;


/**
 * Created by admin on 2017/1/7.
 */

public class Menu_File_Encrypt_Adapter extends BaseAdapter {
    private List<EncryptInf> list = new ArrayList<>();
    private LayoutInflater mInflater;

    public Menu_File_Encrypt_Adapter(Context mContext, List<EncryptInf> list) {
        mInflater = LayoutInflater.from(mContext);
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        EncryptInf encryptInf = list.get(position);
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lv_menu_file_encrypt_item, null);
            viewHolder.iv_menu_file_encrypt_state = (ImageView) convertView.findViewById(R.id.iv_menu_file_encrypt_state);
            viewHolder.tv_menu_file_encrypt_inf = (TextView) convertView.findViewById(R.id.tv_menu_file_encrypt_inf);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (encryptInf.getState()) {
            case 1:
                viewHolder.iv_menu_file_encrypt_state.setImageResource(R.drawable.ic_trending_neutral_black_48dp);
                break;
            case 2:
                viewHolder.iv_menu_file_encrypt_state.setImageResource(R.drawable.ic_done_black_48dp);
                break;
            case 3:
                viewHolder.iv_menu_file_encrypt_state.setImageResource(R.drawable.ic_clear_black_48dp);
                break;

        }
        viewHolder.tv_menu_file_encrypt_inf.setText(encryptInf.getInf());
        return convertView;
    }

    final static class ViewHolder {
        ImageView iv_menu_file_encrypt_state;
        TextView tv_menu_file_encrypt_inf;
    }

}
