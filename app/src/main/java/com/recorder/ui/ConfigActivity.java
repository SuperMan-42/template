package com.recorder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.core.base.CoreBaseActivity;
import com.recorder.App;
import com.recorder.R;
import com.recorder.log.LogService;
import com.recorder.util.Config;
import com.webserver.HookService;

import butterknife.BindView;

public class ConfigActivity extends CoreBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_config;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setStatusBarColor(getResources().getColor(R.color.colorPrimary), 0);
        setToolBar(toolbar, "Config");

        String h = App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces");
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        final String[] address = App.getmPrefs().getString(Config.SP_SERVER_INTERFACES, "--").split(",");
        ;
        for (int i = 0; i < address.length; i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(i);
            rdbtn.setText(address[i]);
            if (h.equals(address[i])) {
                rdbtn.setChecked(true);
            }
            radioGroup.addView(rdbtn);
        }
        ((ViewGroup) findViewById(R.id.radiogroup)).addView(radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                String host = address[index];
                SharedPreferences.Editor edit = App.getmPrefs().edit();
                edit.putString(Config.SP_SERVER_HOST, host);
                edit.apply();
            }
        });

        TextView txtPort = (TextView) findViewById(R.id.txtPort);
        txtPort.setText(String.valueOf(App.getmPrefs().getInt(Config.SP_SERVER_PORT, 8008)));

        TextView txtWSPort = (TextView) findViewById(R.id.txtWSPort);
        txtWSPort.setText(String.valueOf(App.getmPrefs().getInt(Config.SP_WSOCKET_PORT, 8887)));

        final Button button = (Button) findViewById(R.id.btnNewPort);
        button.setOnClickListener(v -> {
            TextView txtPort1 = (TextView) findViewById(R.id.txtPort);

            stopService();

            String host = null;
            if (!App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces").equals("All interfaces")) {
                host = App.getmPrefs().getString(Config.SP_SERVER_HOST, "All interfaces");
            }
            startService(host, Integer.parseInt(txtPort1.getText().toString()));

            TextView txtWSPort1 = (TextView) findViewById(R.id.txtWSPort);

            SharedPreferences.Editor edit = App.getmPrefs().edit();
            edit.putInt(Config.SP_SERVER_PORT, Integer.valueOf(txtPort1.getText().toString()));
            edit.putInt(Config.SP_WSOCKET_PORT, Integer.valueOf(txtWSPort1.getText().toString()));
            edit.apply();
        });
    }

    private void startService(String host, int port) {
        Intent i = new Intent(this, HookService.class);
        i.putExtra("port", port);
        i.putExtra("host", host);
        startService(i);
    }

    private void stopService() {
        stopService(new Intent(this, HookService.class));
        stopService(new Intent(this, LogService.class));
    }
}
