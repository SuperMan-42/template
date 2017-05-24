package com.recorder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.core.base.CoreBaseActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jaeger.library.StatusBarUtil;
import com.recorder.App;
import com.recorder.R;
import com.recorder.util.Config;
import com.recorder.util.FileUtil;
import com.recorder.util.PackageDetail;
import com.recorder.util.Util;
import com.webserver.HookService;
import com.webserver.WebServer;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends CoreBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionsMenu menuMultipleActions;
    private PackageDetail pd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final View actionB = findViewById(R.id.action_b);
        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setSize(FloatingActionButton.SIZE_MINI);
        actionC.setOnClickListener(v -> actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));
        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(view -> actionA.setTitle("Action A clicked"));
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        StatusBarUtil.setColorForDrawerLayout(this, drawer, getResources().getColor(R.color.colorPrimary), 0);


        try {
            String host = null;
            if (!App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces").equals("All interfaces")) {
                host = App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces");
            }
            startService(host, App.getmPrefs().getInt(Config.SP_SERVER_PORT, 8008));

        } catch (Exception e) {
            e.printStackTrace();
        }

        ExpandableListView mExpandableList = (ExpandableListView) findViewById(R.id.appsListView);

        loadListView();

        TextView txtModule = (TextView) findViewById(R.id.txtModule);
        if (WebServer.isModuleEnabled()) {
            txtModule.setText(R.string.module_enabled);
            txtModule.setBackgroundColor(Color.TRANSPARENT);
        }

        TextView txtServer = (TextView) findViewById(R.id.txtServer);
        if (Util.isMyServiceRunning(this, HookService.class)) {
            txtServer.setText(R.string.server_started);
            txtServer.setBackgroundColor(Color.TRANSPARENT);
        }

        mExpandableList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            TextView txtPackage = (TextView) v.findViewById(R.id.txtListPkg);
            TextView txtAppName = (TextView) v.findViewById(R.id.txtListItem);


            loadSelectedApp(txtPackage.getText().toString());

            TextView txtAppSelected = (TextView) findViewById(R.id.txtAppSelected);
            txtAppSelected.setText(">>> " + txtPackage.getText().toString());

            Toast.makeText(this, "" + txtAppName.getText().toString(), Toast.LENGTH_SHORT).show();
            loadListView();

            return true;
        });

        Switch mSwitch = (Switch) findViewById(R.id.only_user_app_switch);
        Boolean sw = App.getmPrefs().getBoolean(Config.SP_SWITCH_OUA, true);
        mSwitch.setChecked(sw);
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            SharedPreferences.Editor edit = App.getmPrefs().edit();

            //Only User App
            if (isChecked) {
                edit.putBoolean(Config.SP_SWITCH_OUA, true);
            } else {
                edit.putBoolean(Config.SP_SWITCH_OUA, false);
            }
            edit.apply();
            loadListView();
        });

        final Button button = (Button) findViewById(R.id.btnLaunchApp);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pd == null) {
                    pd = new PackageDetail(mContext, App.getmPrefs().getString(Config.SP_PACKAGE, ""));
                }
                Intent i = pd.getLaunchIntent();
                if (i != null) {
                    startActivity(i);
                } else {
                    Toast.makeText(mContext, "Launch Intent not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadInterfaces();

        String scheme = "http://";
        if (App.getmPrefs().getBoolean(Config.SP_SWITCH_AUTH, false)) {
            scheme = "https://";
        }

        String port = String.valueOf(App.getmPrefs().getInt(Config.SP_SERVER_PORT, 8008));
        String host = "";
        if (App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces").equals("All interfaces")) {
            String[] adds = App.getmPrefs().getString(Config.SP_SERVER_INTERFACES, "--").split(",");
            for (int i = 0; i < adds.length; i++) {
                if (!adds[i].equals("All interfaces"))
                    host = host + scheme + adds[i] + ":" + port + "\n";
            }
        } else {
            String ip = App.getmPrefs().getString(Config.SP_SERVER_HOST, "127.0.0.1");
            host = scheme + ip + ":" + port;

            SharedPreferences.Editor edit = App.getmPrefs().edit();
            edit.putString(Config.SP_SERVER_IP, ip);
            edit.apply();
        }

        TextView txtHost = (TextView) findViewById(R.id.txtHost);
        txtHost.setText(host);

        TextView txtAdb = (TextView) findViewById(R.id.txtAdb);
        txtAdb.setText("adb forward tcp:" + port + " tcp:" + port);

        TextView txtAppSelected = (TextView) findViewById(R.id.txtAppSelected);
        txtAppSelected.setText(">>> " + App.getmPrefs().getString(Config.SP_PACKAGE, "..."));
    }

    public void loadInterfaces() {

        StringBuilder sb = new StringBuilder();
        sb.append("All interfaces,");
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface netInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    String address = inetAddress.getHostAddress();
                    boolean isIPv4 = address.indexOf(':') < 0;
                    if (isIPv4) {
                        sb.append(address + ",");
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Inspeckage_Error", ex.toString());
        }
        SharedPreferences.Editor edit = App.getmPrefs().edit();
        edit.putString(Config.SP_SERVER_INTERFACES, sb.toString().substring(0, sb.length() - 1));
        edit.apply();
    }

    private ArrayList<ExpandableListItem> getInstalledApps() {
        ArrayList<ExpandableListItem> appsList = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {

            android.content.pm.PackageInfo p = packs.get(i);
            // Installed by user
            if (App.getmPrefs().getBoolean(Config.SP_SWITCH_OUA, true) ? (p.applicationInfo.flags & 129) == 0 : true) {
                ExpandableListItem pInfo = new ExpandableListItem();
                pInfo.setAppName(p.applicationInfo.loadLabel(getPackageManager()).toString());
                pInfo.setPckName(p.packageName);
                pInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));

                String pack = App.getmPrefs().getString(Config.SP_PACKAGE, "");

                if (p.packageName.trim().equals(pack.trim())) {
                    pInfo.setSelected(true);
                }

                appsList.add(pInfo);
            }
        }
        return appsList;
    }

    private void loadListView() {
        List<String> mListDataHeader = new ArrayList<String>();
        mListDataHeader.add(getString(R.string.activity_config_choose));

        HashMap<String, List<ExpandableListItem>> mListDataChild = new HashMap<String, List<ExpandableListItem>>();

        ArrayList<ExpandableListItem> mApps = getInstalledApps();
        Collections.sort(mApps, (o1, o2) -> o1.getAppName().compareTo(o2.getAppName()));

        ExpandableListView appList = (ExpandableListView) findViewById(R.id.appsListView);

        mListDataChild.put(mListDataHeader.get(0), mApps);
        appList.setAdapter(new ExpandableListAdapter(this, mListDataHeader, mListDataChild));

    }

    private void loadSelectedApp(String pkg) {

        SharedPreferences.Editor edit = App.getmPrefs().edit();
        //this put has to come before the PackageDetail
        edit.putString(Config.SP_PACKAGE, pkg);

        pd = new PackageDetail(mContext, pkg);

        edit.putBoolean(Config.SP_HAS_W_PERMISSION, false);
        if (pd.getRequestedPermissions().contains("android.permission.WRITE_EXTERNAL_STORAGE") &&
                Build.VERSION.SDK_INT < 23) {
            edit.putBoolean(Config.SP_HAS_W_PERMISSION, true);
        }

        edit.putString(Config.SP_APP_NAME, pd.getAppName());
        edit.putString(Config.SP_APP_ICON_BASE64, pd.getIconBase64());
        edit.putString(Config.SP_PROCESS_NAME, pd.getProcessName());
        edit.putString(Config.SP_APP_VERSION, pd.getVersion());
        edit.putString(Config.SP_DEBUGGABLE, pd.isDebuggable());
        edit.putString(Config.SP_ALLOW_BACKUP, pd.allowBackup());
        edit.putString(Config.SP_APK_DIR, pd.getApkDir());
        edit.putString(Config.SP_UID, pd.getUID());
        edit.putString(Config.SP_GIDS, pd.getGIDs());
        edit.putString(Config.SP_DATA_DIR, pd.getDataDir());

        edit.putString(Config.SP_REQ_PERMISSIONS, pd.getRequestedPermissions());
        edit.putString(Config.SP_APP_PERMISSIONS, pd.getAppPermissions());

        edit.putString(Config.SP_EXP_ACTIVITIES, pd.getExportedActivities());
        edit.putString(Config.SP_N_EXP_ACTIVITIES, pd.getNonExportedActivities());

        edit.putString(Config.SP_EXP_SERVICES, pd.getExportedServices());
        edit.putString(Config.SP_N_EXP_SERVICES, pd.getNonExportedServices());

        edit.putString(Config.SP_EXP_BROADCAST, pd.getExportedBroadcastReceivers());
        edit.putString(Config.SP_N_EXP_BROADCAST, pd.getNonExportedBroadcastReceivers());

        edit.putString(Config.SP_EXP_PROVIDER, pd.getExportedContentProvider());
        edit.putString(Config.SP_N_EXP_PROVIDER, pd.getNonExportedContentProvider());

        edit.putString(Config.SP_SHARED_LIB, pd.getSharedLibraries());

        edit.putBoolean(Config.SP_APP_IS_RUNNING, false);
        edit.putString(Config.SP_DATA_DIR_TREE, "");

        //test
        //edit.putString(Config.SP_REPLACE_SP, "limitEventUsage,true");

        edit.apply();

        //resolve this problem
        if (pd.getRequestedPermissions().contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
            pd.extractInfoToFile();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            stopService();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clear) {
            clearAll();
            TextView txtAppSelected = (TextView) findViewById(R.id.txtAppSelected);
            if (txtAppSelected != null) {
                txtAppSelected.setText("... ");
            }
        } else if (id == R.id.nav_close) {
            clearAll();
            stopService();
            super.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else if (id == R.id.nav_config) {
            startActivity(ConfigActivity.class);
        } else if (id == R.id.nav_auth) {
            startActivity(AuthActivity.class);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/SuperMan42/007");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void stopService() {
        stopService(new Intent(this, HookService.class));
    }

    public void startService(String host, int port) {
        Intent i = new Intent(this, HookService.class);
        i.putExtra("port", port);
        i.putExtra("host", host);
        startService(i);
    }

    private void clearAll() {
        SharedPreferences.Editor edit = App.getmPrefs().edit();

        String appPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!App.getmPrefs().getBoolean(Config.SP_HAS_W_PERMISSION, false)) {
            appPath = App.getmPrefs().getString(Config.SP_DATA_DIR, "");
        }

        edit.putString(Config.SP_PROXY_HOST, "");
        edit.putString(Config.SP_PROXY_PORT, "");
        edit.putBoolean(Config.SP_SWITCH_PROXY, false);
        edit.putBoolean(Config.SP_FLAG_SECURE, false);
        edit.putBoolean(Config.SP_UNPINNING, false);
        edit.putBoolean(Config.SP_EXPORTED, false);
        edit.putBoolean(Config.SP_HAS_W_PERMISSION, true);
        edit.putString(Config.SP_SERVER_HOST, null);
        edit.putString(Config.SP_SERVER_PORT, null);
        edit.putString(Config.SP_SERVER_IP, null);
        edit.putString(Config.SP_SERVER_INTERFACES, "");

        edit.putString(Config.SP_PACKAGE, "");
        edit.putString(Config.SP_APP_NAME, "");
        edit.putString(Config.SP_APP_VERSION, "");
        edit.putString(Config.SP_DEBUGGABLE, "");
        edit.putString(Config.SP_APK_DIR, "");
        edit.putString(Config.SP_UID, "");
        edit.putString(Config.SP_GIDS, "");
        edit.putString(Config.SP_DATA_DIR, "");
        //white img
        edit.putString(Config.SP_APP_ICON_BASE64, "iVBORw0KGgoAAAANSUhEUgAAABoAAAAbCAIAAADtdAg8AAAAA3NCSVQICAjb4U/gAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAJUlEQVRIiWP8//8/A/UAExXNGjVu1LhR40aNGzVu1LhR44aScQDKygMz8IbG2QAAAABJRU5ErkJggg==");

        edit.putString(Config.SP_EXP_ACTIVITIES, "");
        edit.putString(Config.SP_N_EXP_ACTIVITIES, "");
        edit.putString(Config.SP_REQ_PERMISSIONS, "");
        edit.putString(Config.SP_APP_PERMISSIONS, "");
        edit.putString(Config.SP_N_EXP_PROVIDER, "");
        edit.putString(Config.SP_N_EXP_SERVICES, "");
        edit.putString(Config.SP_N_EXP_BROADCAST, "");

        edit.putString(Config.SP_EXP_SERVICES, "");
        edit.putString(Config.SP_EXP_BROADCAST, "");
        edit.putString(Config.SP_EXP_PROVIDER, "");
        edit.putString(Config.SP_SHARED_LIB, "");

        edit.putBoolean(Config.SP_APP_IS_RUNNING, false);
        edit.putString(Config.SP_DATA_DIR_TREE, "");

        edit.putString(Config.SP_USER_HOOKS, "");

        edit.apply();

        File root = new File(appPath + Config.P_ROOT);
        FileUtil.deleteRecursive(root);
    }
}
