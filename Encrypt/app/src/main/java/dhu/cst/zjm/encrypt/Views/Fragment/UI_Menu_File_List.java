package dhu.cst.zjm.encrypt.views.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import dhu.cst.zjm.encrypt.adapter.Menu_File_Adapter;
import dhu.cst.zjm.encrypt.models.ServerFile;
import dhu.cst.zjm.encrypt.models.User;
import dhu.cst.zjm.encrypt.R;
import dhu.cst.zjm.encrypt.util.appbarlayout.SwipyAppBarScrollListener;

/**
 * Created by zjm on 2017/1/3.
 */

public class UI_Menu_File_List extends Fragment {


    private List<ServerFile> sourceServerFileList;
    private User user;
    private Menu_File_Adapter menu_file_adapter;
    private Menu_File_List_Interface menu_file_list_interface;
    private View view;
    private RecyclerView rcv_menu_file_list;
    private ImageView iv_menu_toolbar;
    private PullToRefreshView ptrv_menu_file_list;
    private AppBarLayout abl_ui_menu;
    private CollapsingToolbarLayout ctl_menu;


    public UI_Menu_File_List(){

    }

    public UI_Menu_File_List(User user) {
        this.user = user;
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

        sourceServerFileList = new ArrayList<ServerFile>();
        menu_file_list_interface.getSourceList();

        rcv_menu_file_list = (RecyclerView) view.findViewById(R.id.rcv_menu_file_list);
        rcv_menu_file_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        menu_file_adapter = new Menu_File_Adapter(getActivity(), sourceServerFileList);
        menu_file_adapter.setOnItemClickListener(new Menu_File_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                menu_file_list_interface.fileListItemClick(sourceServerFileList.get(position));
            }
        });
        rcv_menu_file_list.setAdapter(menu_file_adapter);
    }

    private void setupActivityView() {
        abl_ui_menu = (AppBarLayout) getActivity().findViewById(R.id.abl_ui_menu);

        iv_menu_toolbar = (ImageView) getActivity().findViewById(R.id.iv_menu_toolbar);
        iv_menu_toolbar.setVisibility(View.VISIBLE);

        ctl_menu = (CollapsingToolbarLayout) getActivity().findViewById(R.id.ctl_menu);
        ctl_menu.setContentScrimColor(getResources().getColor(R.color.transparent));
        ctl_menu.setTitle(user.getName());

        rcv_menu_file_list.addOnScrollListener(new SwipyAppBarScrollListener(abl_ui_menu, ptrv_menu_file_list, rcv_menu_file_list));
    }

    public interface Menu_File_List_Interface {
        void getSourceList();

        void fileListItemClick(ServerFile serverFile);
    }

    public void updateSourceMenuFileList(List<ServerFile> list) {
        sourceServerFileList.clear();
        for (ServerFile sf : list) {
            sourceServerFileList.add(sf);
        }
        menu_file_adapter.notifyDataSetChanged();
    }
}
