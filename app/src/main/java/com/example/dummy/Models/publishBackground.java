package com.example.dummy.Models;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.dummy.R;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

public class publishBackground extends IntentService {

    static boolean on;

    public publishBackground() {
        super("Publicar");
        on=true;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String s_topico=intent.getStringExtra("topico");
        Random random=new Random();
        int temperatura=random.nextInt(40);
        //String s_value=intent.getStringExtra("valor");
        int pause=intent.getIntExtra("milisPause",0);
        String mensajeBroadcast;
        String fechaBroadCast, topicoBroadCast, temperaturaBroadCast;


        while(on){
            try{
                Intent i = new Intent("broadcast");

                byte[] encodedPayload = new byte[0];
                try {
                    temperatura+=random.nextInt(7)-3;
                    String jsonString = "{'"+s_topico+"':"+temperatura+"}";
                    JSONObject json = new JSONObject(jsonString);
                    byte[] jsonAsBytes = json.toString().getBytes("UTF-8");
                    Client.getClient().publish("v1/devices/me/telemetry",jsonAsBytes,1,true);

                } catch (UnsupportedEncodingException | MqttException | JSONException e) {
                    e.printStackTrace();
                }
                fechaBroadCast = TimeFormat.getFormatedDate(new Date());
                topicoBroadCast =  intent.getStringExtra("topico");
                i.putExtra("fecha",fechaBroadCast);
                i.putExtra("topico",topicoBroadCast);
                i.putExtra("temperatura",temperatura);

                LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setOn(boolean new_on){
        on=new_on;
    }
}
