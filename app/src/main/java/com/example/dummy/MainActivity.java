package com.example.dummy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dummy.Models.Client;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView host;
    TextView usuario;
    TextView contraseña;

    static String MQTTHOST="tcp://demo.thingsboard.io:1883";
    static String USERNAME="W5QDTdk9eRn5KyvgsRZP";
    static String PASSWORD="";
    static String topicStr="v1/devices/me/telemetry";
    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        host=(TextView)findViewById(R.id.input_host);
        usuario=(TextView)findViewById(R.id.input_user);
        contraseña=(TextView)findViewById(R.id.input_pass);

    }
    public void conect(View v){
        String s_host= host.getText().toString();
        String s_user= usuario.getText().toString();
        String s_password= contraseña.getText().toString();

        String clientId = MqttClient.generateClientId();
        client= new MqttAndroidClient(this.getApplicationContext(),s_host,clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(s_user);
        options.setPassword(s_password.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getBaseContext(),"Se ha conectado",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,Publish.class);
                    Client.setClient(client);
                    startActivity(intent);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getBaseContext(),"Fallo en la conexión",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            Log.e("MyActivity",e.getMessage());
            e.printStackTrace();
        }

    }
    public void autofill(View v){
        host.setText("tcp://demo.thingsboard.io:1883");
        usuario.setText("W5QDTdk9eRn5KyvgsRZP");
        contraseña.setText("");
        Toast.makeText(getBaseContext(),"Contraseña no requerida",Toast.LENGTH_SHORT).show();
    }
    public void closeApp(View v){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}