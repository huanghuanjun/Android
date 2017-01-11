package dhu.cst.zjm.encrypt.Views;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import dhu.cst.zjm.encrypt.Action.ActionsCreator;
import dhu.cst.zjm.encrypt.Adapter.Menu_File_Encrypt_Adapter;
import dhu.cst.zjm.encrypt.Base.MapKey.EncryptMap;
import dhu.cst.zjm.encrypt.Base.PathAndKey;
import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Models.EncryptFile;
import dhu.cst.zjm.encrypt.Models.EncryptInf;
import dhu.cst.zjm.encrypt.Models.EncryptType;
import dhu.cst.zjm.encrypt.Models.ServerFile;
import dhu.cst.zjm.encrypt.Models.User;
import dhu.cst.zjm.encrypt.R;
import dhu.cst.zjm.encrypt.Service.Menu_Service;
import dhu.cst.zjm.encrypt.Stores.ChangePoint;
import dhu.cst.zjm.encrypt.Stores.LoginStore;
import dhu.cst.zjm.encrypt.Stores.MenuStore;
import dhu.cst.zjm.encrypt.Views.Fragment.UI_Menu_File_List;
import dhu.cst.zjm.encrypt.Views.Fragment.UI_Menu_File_Type;

import static dhu.cst.zjm.encrypt.Util.Get_File_From_Uri.getPath;

/**
 * Created by lenovo on 2016/11/30.
 */

public class UI_Menu extends AppCompatActivity implements UI_Menu_File_List.Menu_File_List_Interface, UI_Menu_File_Type.Menu_File_Type_Interface {
    private Dispatcher dispatcher;
    private MenuStore menuStore;
    private LoginStore loginStore;
    private ActionsCreator actionsCreator;
    private UI_Menu instance;
    private Uri fileUri;
    private ServiceConnection menu_service_connection;
    private boolean isBind = false;
    private Menu_Service.MyBinder menu_service_binder;

    private User user;
    private boolean isLogin = false;

    //Ui相关
    private DrawerLayout dl_ui_menu;
    private FragmentManager fm_menu_main;
    private FragmentTransaction ft_menu_main;
    private UI_Menu_File_List ui_menu_file_list;
    private UI_Menu_File_Type ui_menu_file_type;
    private NavigationView nv_menu_person;
    private Toolbar tb_menu_title;
    private ProgressDialog pd_file_progress;
    private CollapsingToolbarLayout ctl_menu;
    private android.support.v7.app.AlertDialog.Builder adb_menu_file_encrypt, adb_others;
    private ListView lv_menu_file_encrypt;
    private EditText et_menu_file_encrypt_exinf;
    private List<EncryptInf> sourceEncryptInfList = new ArrayList<EncryptInf>();
    private View v_menu_file_encrypt;
    private Menu_File_Encrypt_Adapter menu_file_encrypt_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_menu);
        init();
        setupService();
        setupView();
    }

    private void init() {
        instance = this;
        dispatcher = Dispatcher.get(EventBus.getDefault());
        actionsCreator = ActionsCreator.get(dispatcher);
        menuStore = MenuStore.get(dispatcher);
        loginStore = LoginStore.get(dispatcher);
        user = new User();
        PathAndKey.setMainPath(Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.i("a",PathAndKey.getPath());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatcher.register(this);
        dispatcher.register(loginStore);
        dispatcher.register(menuStore);

        if (!isLogin) {
            getUserInf();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dispatcher.unregister(this);
        dispatcher.unregister(loginStore);
        dispatcher.unregister(menuStore);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            dl_ui_menu.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }

    private void setupService() {
        menu_service_connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                menu_service_binder = (Menu_Service.MyBinder) service;
                menu_service_binder.getService().setID(user.getId());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        if (!isBind) {
            Intent menu_service = new Intent(UI_Menu.this, Menu_Service.class);
            bindService(menu_service, menu_service_connection, Context.BIND_AUTO_CREATE);
            isBind = true;
        }
    }

    private void setupFragment() {
        fm_menu_main = getFragmentManager();
        ft_menu_main = fm_menu_main.beginTransaction();
        ui_menu_file_list = new UI_Menu_File_List(user);
        ft_menu_main.add(R.id.rl_Menu_Main, ui_menu_file_list);
        ft_menu_main.commit();
    }

    private void setupView() {
        dl_ui_menu = (DrawerLayout) findViewById(R.id.dl_ui_menu);

        tb_menu_title = (Toolbar) findViewById(R.id.tb_menu_title);
        setSupportActionBar(tb_menu_title);
        ctl_menu = (CollapsingToolbarLayout) findViewById(R.id.ctl_menu);
        ctl_menu.setTitle(getResources().getString(R.string.app_name));
        ctl_menu.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        ctl_menu.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色


        nv_menu_person = (NavigationView) findViewById(R.id.nv_menu_person);
        nv_menu_person.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            private MenuItem mPreMenuItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (mPreMenuItem != null) {
                    mPreMenuItem.setCheckable(false);
                }
                item.setChecked(true);
                dl_ui_menu.closeDrawers();
                mPreMenuItem = item;

                navigationViewClick(item);

                return true;
            }
        });


        pd_file_progress = new ProgressDialog(instance);
        pd_file_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd_file_progress.setMax(UI_Menu_Action.PROGRESS_MAX);
        pd_file_progress.setIndeterminate(false);
        pd_file_progress.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        pd_file_progress.setCancelable(false);
        pd_file_progress.setCanceledOnTouchOutside(false);


    }

    private void setupEncryptExinfDialog() {
        LayoutInflater inflater = getLayoutInflater();
        v_menu_file_encrypt = inflater.inflate(R.layout.ui_menu_file_encrypt_exinf, (ViewGroup) findViewById(R.id.ll_menu_file_encrypt_exinf));
        et_menu_file_encrypt_exinf = (EditText) v_menu_file_encrypt.findViewById(R.id.et_menu_file_encrypt_exinf);
        adb_menu_file_encrypt = new android.support.v7.app.AlertDialog.Builder(this);
        adb_menu_file_encrypt.setView(v_menu_file_encrypt);
    }

    private void setupEncryptProgressDialog() {
        LayoutInflater inflater = getLayoutInflater();
        v_menu_file_encrypt = inflater.inflate(R.layout.ui_menu_file_encrypt, (ViewGroup) findViewById(R.id.rl_menu_file_encrypt));
        menu_file_encrypt_adapter = new Menu_File_Encrypt_Adapter(this, sourceEncryptInfList);
        lv_menu_file_encrypt = (ListView) v_menu_file_encrypt.findViewById(R.id.lv_menu_file_encrypt);
        lv_menu_file_encrypt.setAdapter(menu_file_encrypt_adapter);
        adb_menu_file_encrypt = new android.support.v7.app.AlertDialog.Builder(this);
        adb_menu_file_encrypt.setView(v_menu_file_encrypt);
    }

    private void setupDialog() {
        adb_others = new android.support.v7.app.AlertDialog.Builder(this);
    }

    private void chooseFile() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(chooseFile, UI_Menu_Action.UPLOAD_FILE);
    }

    private void navigationViewClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nV_Menu_Item_Home:
                ft_menu_main = fm_menu_main.beginTransaction();
                ft_menu_main.replace(R.id.rl_Menu_Main, ui_menu_file_list);
                ft_menu_main.commit();
                break;
            case R.id.nV_Menu_Item_Upload_File:
                item.setChecked(false);
                chooseFile();
                break;
        }
    }

    private void getUserInf() {
        actionsCreator.Get_User_Inf();
    }

    private void updateUserInf(LoginStore.LoginStoreChangeEvent event) {
        user = new User(event.getLoginInternetID(), event.getLoginInternetName());
        this.isLogin = event.getIsLoginInternet();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupFragment();

                RelativeLayout nv_menu_header = (RelativeLayout) nv_menu_person.inflateHeaderView(R.layout.nv_menu_header);
                nv_menu_person.removeHeaderView(nv_menu_person.getHeaderView(0));
                TextView tv_nv_menu_id = (TextView) nv_menu_header.findViewById(R.id.tv_nv_menu_id);
                TextView tv_nv_menu_name = (TextView) nv_menu_header.findViewById(R.id.tv_nv_menu_name);
                tv_nv_menu_id.setText(user.getId() + "");
                tv_nv_menu_name.setText(user.getName());
                ctl_menu.setTitle(user.getName());
            }
        });

    }

    private void confirmConnect(boolean isConnect) {
        if (isConnect) {
            Snackbar snackbar = Snackbar.make(dl_ui_menu, "Connection Success!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            connectFailed();
        }
    }

    private void connectFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(dl_ui_menu, "Connection failed!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Reconnect", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            menu_service_binder.getService().Try_Connect_Menu();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
                snackbar.show();
            }
        });
    }

    private void updateMenuFileList(List<ServerFile> list) {
        final List<ServerFile> l = list;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ui_menu_file_list.updateSouceMenuFileList(l);
            }
        });
    }

    private void updateMenuFileType(List<EncryptType> list) {
        final List<EncryptType> l = list;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ui_menu_file_type.updateSouceMenuFileType(l);
            }
        });
    }

    private void menuConnectError(Exception e) {
        e.printStackTrace();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(dl_ui_menu, "Connection Error!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Reconnect", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            menu_service_binder.getService().Try_Connect_Menu();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
                snackbar.show();
            }
        });
    }

    private void menuConnectClose(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(dl_ui_menu, "Connection Close : " + reason, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Reconnect", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            menu_service_binder.getService().Try_Connect_Menu();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
                snackbar.show();
            }
        });
    }

    private void menuConnectMessage(String message) {
        final String s = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EncryptInf encryptInf = new EncryptInf(1, s);
                encryptInf.setState(1);
                sourceEncryptInfList.add(encryptInf);
                menu_file_encrypt_adapter.notifyDataSetChanged();
            }
        });

    }

    private void uploadFileError() {
        Toast.makeText(instance, "Upload File error!",
                Toast.LENGTH_SHORT).show();
    }

    private void uploadFileSuccess(Intent data) {
        fileUri = data.getData();//得到uri，后面就是将uri转化成file的过程。
        String path = getPath(instance, fileUri);

        pd_file_progress.setTitle("Upload File");
        pd_file_progress.setMessage("Uploading");
        pd_file_progress.setProgress(UI_Menu_Action.PROGRESS_START);
        pd_file_progress.show();

        menu_service_binder.getService().Upload_File(user.getId(), path);
    }

    private void updateUploadFileProgress(int progress) {
        final int p = progress;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd_file_progress.setProgress(p);
            }
        });
    }

    private void updateDownloadFileProgress(int progress) {
        final int p = progress;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd_file_progress.setProgress(p);
            }
        });
    }

    private String encryptFileToJson(EncryptFile encryptFile) {
        Gson gson = new Gson();
        return gson.toJson(encryptFile);
    }

    private void startEncrypt(String json) {
        final String s = json;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupEncryptProgressDialog();
                adb_menu_file_encrypt.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                sourceEncryptInfList.clear();
                adb_menu_file_encrypt.show();
                actionsCreator.Encrypt_File(s);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UI_Menu_Action.UPLOAD_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    uploadFileSuccess(data);
                } else {
                    uploadFileError();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (isBind) {
            unbindService(menu_service_connection);
            isBind = false;
        }
        super.onDestroy();
    }

    @Subscribe
    public void onLoginStoreChange(LoginStore.LoginStoreChangeEvent event) {
        switch (event.getChangePoint()) {
            case ChangePoint.GET_USER_INF:
                updateUserInf(event);
                break;
        }
    }


    @Subscribe
    public void onMenuStoreChange(MenuStore.MenuStoreChangeEvent event) {
        switch (event.getChangePoint()) {
            case ChangePoint.IS_MENU_TRY_CONNECT:
                confirmConnect(event.getIsMenuTryConnect());
                break;
            case ChangePoint.MENU_CONNECT_MESSAGE:
                menuConnectMessage(event.getConnectMessageInf());
                break;
            case ChangePoint.MENU_CONNECT_CLOSE:
                menuConnectClose(event.getConnectCloseReason());
                break;
            case ChangePoint.MENU_CONNECT_ERROR:
                menuConnectError(event.getConnectErrorExc());
                break;
            case ChangePoint.GET_MENU_FILE_LIST:
                updateMenuFileList(event.getSouceServerFileList());
                break;
            case ChangePoint.UPLOAD_FILE_PROGRESS:
                updateUploadFileProgress(event.getUploadFileProgress());
                break;
            case ChangePoint.DOWNLOAD_FILE_PROGRESS:
                updateDownloadFileProgress(event.getDownloadFileProgress());
                break;
            case ChangePoint.GET_MENU_FILE_TYPE:
                updateMenuFileType(event.getSouceServerTypeList());
                break;
            case ChangePoint.SET_ENCRYPT_FILE:
                startEncrypt(event.getEncryptFileJson());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStackImmediate();
    }


    @Override
    public void getSourceList() {
        actionsCreator.Get_Menu_File_List(user.getId());
    }

    @Override
    public void fileListItemClick(ServerFile serverFile) {
        fm_menu_main = getFragmentManager();
        ft_menu_main = fm_menu_main.beginTransaction();
        ui_menu_file_type = new UI_Menu_File_Type(serverFile);
        ft_menu_main.replace(R.id.rl_Menu_Main, ui_menu_file_type);
        ft_menu_main.addToBackStack(null);
        ft_menu_main.commit();
    }

//    @Override
//    public void typeListItemClick(ServerFile serverFile, EncryptType encryptType) {
//        fm_menu_main = getFragmentManager();
//        ft_menu_main = fm_menu_main.beginTransaction();
//        Log.i("a", serverFile.getName());
//        ui_menu_file_encrypt = new UI_Menu_File_Encrypt(serverFile, encryptType);
//        ft_menu_main.replace(R.id.rl_Menu_Main, ui_menu_file_encrypt);
//        ft_menu_main.addToBackStack(null);
//        ft_menu_main.commit();
//    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void downloadClick(EncryptFile encryptFile) {
        pd_file_progress.setTitle("Download File");
        pd_file_progress.setMessage("Downloading");
        pd_file_progress.setProgress(UI_Menu_Action.PROGRESS_START);
        pd_file_progress.show();
        menu_service_binder.getService().Download_File(encryptFileToJson(encryptFile));
    }

    @Override
    public void encryptClick(EncryptFile encryptFile) {
        switch (encryptFile.getTypeId()) {
            case EncryptMap.BASE:
                setupEncryptExinfDialog();
                final EncryptFile ef = encryptFile;
                adb_menu_file_encrypt.setTitle("输入DES密钥！");
                adb_menu_file_encrypt.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ef.setExInf(et_menu_file_encrypt_exinf.getText().toString());
                        actionsCreator.Set_Encrypt_Inf(encryptFileToJson(ef));
                        dialog.dismiss();
                    }
                });
                adb_menu_file_encrypt.show();

                break;
        }
    }

    @Override
    public void detailsClick(EncryptType encryptType) {
        setupDialog();
        adb_others.setTitle(encryptType.getName());
        adb_others.setMessage(encryptType.getInf());
        adb_others.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb_others.show();
    }

    @Override
    public void decryptClick(EncryptFile encryptFile) {
        switch (encryptFile.getTypeId()) {
            case EncryptMap.BASE:
                setupEncryptProgressDialog();
                adb_menu_file_encrypt.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                sourceEncryptInfList.clear();
                adb_menu_file_encrypt.show();
                actionsCreator.Decrypt_File(encryptFileToJson(encryptFile));
                break;
        }
    }

    @Override
    public void getSourceType() {
        actionsCreator.Get_Menu_File_Type();
    }
}
