package dhu.cst.zjm.encrypt.views.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

import dhu.cst.zjm.encrypt.adapter.Menu_File_Type_Adapter;
import dhu.cst.zjm.encrypt.models.EncryptFile;
import dhu.cst.zjm.encrypt.models.EncryptType;
import dhu.cst.zjm.encrypt.models.ServerFile;
import dhu.cst.zjm.encrypt.models.User;
import dhu.cst.zjm.encrypt.R;

/**
 * Created by zjm on 2017/1/8.
 */

public class UI_Menu_File_Type extends Fragment {

    private View view;
    private Menu_File_Type_Interface menu_file_type_interface;
    private RecyclerView rcv_menu_file_type;
    private List<EncryptType> sourceEncryptTypeList;
    private Menu_File_Type_Adapter menu_file_type_adapter;
    private CollapsingToolbarLayout ctl_menu;
    private ImageView iv_menu_toolbar;
    private ServerFile serverFile;
    private TextView tv_menu_toolbar;

    public UI_Menu_File_Type(){}

    public UI_Menu_File_Type(ServerFile serverFile) {
        this.serverFile = serverFile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.ui_menu_file_type, container, false);
        }

        if (getActivity() instanceof Menu_File_Type_Interface) {
            menu_file_type_interface = (Menu_File_Type_Interface) getActivity();
        }

        setupView(view);
        setupActivityView();

        return view;
    }

    private void setupView(View view) {
        sourceEncryptTypeList = new ArrayList<EncryptType>();
        menu_file_type_interface.getSourceType();

        menu_file_type_adapter = new Menu_File_Type_Adapter(getActivity(), sourceEncryptTypeList);
        menu_file_type_adapter.setMode(Attributes.Mode.Single);
        menu_file_type_adapter.setDownloadClickListener(new Menu_File_Type_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EncryptFile encryptFile = new EncryptFile(menu_file_type_interface.getUser().getId(), serverFile.getId(), sourceEncryptTypeList.get(position).getId());
                encryptFile.setFileName(serverFile.getName());
                menu_file_type_interface.downloadClick(encryptFile);
            }
        });
        menu_file_type_adapter.setRightClickListener(new Menu_File_Type_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EncryptFile encryptFile = new EncryptFile(menu_file_type_interface.getUser().getId(), serverFile.getId(), sourceEncryptTypeList.get(position).getId());
                encryptFile.setFileName(serverFile.getName());
                menu_file_type_interface.encryptClick(encryptFile);
            }
        });
        menu_file_type_adapter.setClickListener(new Menu_File_Type_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                menu_file_type_interface.detailsClick(sourceEncryptTypeList.get(position));
            }
        });

        menu_file_type_adapter.setDecryptClickListener(new Menu_File_Type_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EncryptFile encryptFile = new EncryptFile(menu_file_type_interface.getUser().getId(), serverFile.getId(), sourceEncryptTypeList.get(position).getId());
                encryptFile.setFileName(serverFile.getName());
                menu_file_type_interface.decryptClick(encryptFile);
            }
        });

        rcv_menu_file_type = (RecyclerView) view.findViewById(R.id.rcv_menu_file_type);
        rcv_menu_file_type.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rcv_menu_file_type.setAdapter(menu_file_type_adapter);


    }

    private void setupActivityView() {
        iv_menu_toolbar = (ImageView) getActivity().findViewById(R.id.iv_menu_toolbar);
        iv_menu_toolbar.setVisibility(View.INVISIBLE);

        ctl_menu = (CollapsingToolbarLayout) getActivity().findViewById(R.id.ctl_menu);
        ctl_menu.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        ctl_menu.setTitle(serverFile.getName());

        tv_menu_toolbar = (TextView) getActivity().findViewById(R.id.tv_menu_toolbar);
        tv_menu_toolbar.setText(serverFile.getSize() + "\n \n" + serverFile.getUploadTime());
    }

    public void updateSourceMenuFileType(List<EncryptType> list) {
        sourceEncryptTypeList.clear();
        for (EncryptType et : list) {
            sourceEncryptTypeList.add(et);
        }
        menu_file_type_adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface Menu_File_Type_Interface {
        User getUser();

        void downloadClick(EncryptFile encryptFile);

        void encryptClick(EncryptFile encryptFile);

        void detailsClick(EncryptType encryptType);

        void decryptClick(EncryptFile encryptFile);

        void getSourceType();
    }
}
