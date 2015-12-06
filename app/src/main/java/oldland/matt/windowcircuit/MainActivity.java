package oldland.matt.windowcircuit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Pubnub pubnub;

    private String smartPanelCh = "WindowCircuit";
    private String publishKey;
    private String subscribeKey;

    String UUID = "Samed-Nexus6P";

    Button on, off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        publishKey = getString(R.string.publish_key);
        subscribeKey = getString(R.string.subscribe_key);

        on = (Button) findViewById(R.id.on);
        off = (Button) findViewById(R.id.off);

        on.setOnClickListener(this);
        off.setOnClickListener(this);

        connectToPubnub();
    }

    private void connectToPubnub() {

        pubnub = new Pubnub(publishKey, subscribeKey);
        pubnub.setUUID(UUID);

        try {
            pubnub.subscribe(smartPanelCh, callback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }

    Callback callback = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            log("CONNECT on channel:" + channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            log("DISCONNECT on channel:" + channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void reconnectCallback(String channel, Object message) {
            log("RECONNECT on channel:" + channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
           log(channel + " : "
                    + message.getClass() + " : " + message.toString());
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
           log("ERROR on channel " + channel
                    + " : " + error.toString());
        }
    };

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.on:
                sendMessage("on");
                break;
            case R.id.off:
                sendMessage("off");
                break;
        }

    }

    private void sendMessage(String message) {
        log("sendMessage:" + message);
        pubnub.publish(smartPanelCh, message, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                log("Success: " + message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                log("Error: " + error);
            }
        });
    }

    private void log(String message) {
        Log.i(MainActivity.this.getClass().getSimpleName(), message);
    }
}

