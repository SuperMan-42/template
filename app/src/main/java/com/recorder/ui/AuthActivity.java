package com.recorder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Switch;
import android.widget.TextView;

import com.core.base.CoreBaseActivity;
import com.recorder.App;
import com.recorder.R;
import com.recorder.log.LogService;
import com.recorder.util.Config;
import com.webserver.HookService;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/24.
 */

public class AuthActivity extends CoreBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_auth;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setStatusBarColor(getResources().getColor(R.color.colorPrimary), 0);
        setToolBar(toolbar, "AuthActivity");

        TextView txtLogin = (TextView) findViewById(R.id.txtLogin);
        TextView txtPass = (TextView) findViewById(R.id.txtPass);
        final Switch mSwitch = (Switch) findViewById(R.id.auth_switch);

        String login = App.getmPrefs().getString(Config.SP_USER_PASS, "");
        if (!login.trim().equals("")) {
            txtLogin.setText(login.split(":")[0]);
            txtPass.setText(login.split(":")[1]);
        }

        final Boolean sw = App.getmPrefs().getBoolean(Config.SP_SWITCH_AUTH, false);
        mSwitch.setChecked(sw);

        txtLogin.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mSwitch.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mSwitch.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            SharedPreferences.Editor edit = App.getmPrefs().edit();

            if (isChecked) {
                edit.putBoolean(Config.SP_SWITCH_AUTH, true);
            } else {
                edit.putBoolean(Config.SP_SWITCH_AUTH, false);
            }

            TextView txtLogin1 = (TextView) findViewById(R.id.txtLogin);
            TextView txtPass1 = (TextView) findViewById(R.id.txtPass);

            edit.putString(Config.SP_USER_PASS, txtLogin1.getText() + ":" + txtPass1.getText());
            edit.apply();

            stopService();

            String host = null;
            int port = App.getmPrefs().getInt(Config.SP_SERVER_PORT, 8008);
            if (!App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces").equals("All interfaces")) {
                host = App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces");
            }
            startService(host, port);
        });
    }

    public void startService(String host, int port) {
        Intent i = new Intent(this, HookService.class);
        i.putExtra("port", port);
        i.putExtra("host", host);
        startService(i);
    }

    public void stopService() {
        stopService(new Intent(this, HookService.class));
        stopService(new Intent(this, LogService.class));
    }

}