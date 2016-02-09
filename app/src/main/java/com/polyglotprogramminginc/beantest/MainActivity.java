package com.polyglotprogramminginc.beantest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<Bean> beans = new ArrayList<>();

    BeanDiscoveryListener listener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int rssi) {
            beans.add(bean);
        }

        @Override
        public void onDiscoveryComplete() {
            for (Bean bean : beans) {
                System.out.println(bean.getDevice().getName());   // "Bean"              (example)
                System.out.println(bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
            }

            final Bean bean = beans.get(0);

            BeanListener beanListener = new BeanListener() {
                @Override
                public void onConnected() {
                    System.out.println("connected to Bean!");
                    bean.readDeviceInfo(new Callback<DeviceInfo>() {
                        @Override
                        public void onResult(DeviceInfo deviceInfo) {
                            System.out.println(deviceInfo.hardwareVersion());
                            System.out.println(deviceInfo.firmwareVersion());
                            System.out.println(deviceInfo.softwareVersion());
                        }
                    });
                }

                @Override
                public void onError(BeanError error) {

                }

                @Override
                public void onScratchValueChanged(ScratchBank bank, byte[] value) {

                }

                @Override
                public void onConnectionFailed() {

                }

                @Override
                public void onDisconnected() {

                }

                @Override
                public void onSerialMessageReceived(byte[] data) {

                }

                // In practice you must implement the other Listener methods
            };

// Assuming you are in an Activity, use 'this' for the context
            bean.connect(getApplicationContext(), beanListener);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BeanManager.getInstance().startDiscovery(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
