package dhu.cst.zjm.encrypt.Views.Fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dhu.cst.zjm.encrypt.Adapter.Menu_File_Adapter;
import dhu.cst.zjm.encrypt.Models.ServerFile;
import dhu.cst.zjm.encrypt.Models.User;
import dhu.cst.zjm.encrypt.R;

/**
 * Created by admin on 2017/1/3.
 */

public class UI_Menu_File_List extends ListFragment {

    private View view;
    private List<ServerFile> souceServerFileList;
    private Menu_File_Adapter menu_file_adapter;
    private PullToRefreshView ptrv_menu_file_list;
    private Menu_File_List_Interface menu_file_list_interface;
    private ImageView iv_menu_toolbar;
    private CollapsingToolbarLayout ctl_menu;
    private User user;

    public UI_Menu_File_List(User user){
        this.user=user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.ui_menu_file_list, container, false);
        }

        if (getActivity() instanceof Menu_File_List_Interface) {
            menu_file_list_interface = (Menu_File_List_Interface) getActivity();
        }

        setupView(view);
        setupActivityView();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupView(View view) {


        ptrv_menu_file_list = (PullToRefreshView) view.findViewById(R.id.ptrv_menu_file_list);
        ptrv_menu_file_list.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ptrv_menu_file_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        menu_file_list_interface.getSourceList();
                        ptrv_menu_file_list.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        souceServerFileList = new ArrayList<ServerFile>();
        menu_file_list_interface.getSourceList();

        menu_file_adapter = new Menu_File_Adapter(getActivity(), souceServerFileList);
        menu_file_adapter.setOnItemClickListener(new Menu_File_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                menu_file_list_interface.fileListItemClick(souceServerFileList.get(postion));
            }
        });
        setListAdapter(menu_file_adapter);
    }

    private void setupActivityView(){
        iv_menu_toolbar = (ImageView) getActivity().findViewById(R.id.iv_menu_toolbar);
        iv_menu_toolbar.setVisibility(View.VISIBLE);

        ctl_menu = (CollapsingToolbarLayout) getActivity().findViewById(R.id.ctl_menu);
        ctl_menu.setContentScrimColor(getResources().getColor(R.color.transparent));
        ctl_menu.setTitle(user.getName());
    }

    public interface Menu_File_List_Interface {
        void getSourceList();

        void fileListItemClick(ServerFile serverFile);
    }

    public void updateSouceMenuFileList(List<ServerFile> list) {
        souceServerFileList.clear();
        for (ServerFile sf : list) {
            souceServerFileList.add(sf);
        }
        menu_file_adapter.notifyDataSetChanged();
    }
}
