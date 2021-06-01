package com.example.dummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dummy.Models.Client;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.example.dummy.Models.TimeFormat;
import com.example.dummy.Models.publishBackground;
import java.util.Date;

public class Publish extends AppCompatActivity {

    TextView topico;
    TextView valor;
    TextView pause;
    TextView conectionStatus;
    Button bt_publicar;
    Button bt_detener;
    Button bt_disc;
    TableLayout table;

    String s_log;
    Intent i_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        topico=(TextView)findViewById(R.id.input_topic);
        valor=(TextView)findViewById(R.id.input_value);
        pause=(TextView)findViewById(R.id.input_pause);
        conectionStatus=(TextView)findViewById(R.id.conectionStatus);
        bt_publicar=(Button)findViewById(R.id.bt_publish);
        bt_detener=(Button)findViewById(R.id.bt_stop);
        bt_disc=(Button)findViewById(R.id.bt_disc);
        table = (TableLayout) findViewById(R.id.tableLayout);
        valor.setEnabled(false);

        s_log="Conectado "+ TimeFormat.getFormatedDate(new Date());
        conectionStatus.setText(s_log);
        String [] cabecera = {"Hora","Tópico","Valor"};
        TableRow row = new TableRow(getBaseContext());
        TextView textView;
        for(int i=0;i<cabecera.length;i++){
            textView = new TextView(getBaseContext());
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setPadding(15,15,15,15);
            textView.setText(cabecera[i]);
            row.addView(textView);
        }
        table.addView(row);
    }
    public void publish(View v) {
        String s_topico = topico.getText().toString();
        if(s_topico.equals("")||pause.getText().toString().equals("")){
            Toast.makeText(getBaseContext(),"Por favor, llene todos los campos antes de continuar",Toast.LENGTH_LONG).show();
        }else{
            //String s_valor = valor.getText().toString();
            int time=Integer.parseInt(pause.getText().toString());
            topico.setEnabled(false);
            //valor.setEnabled(false);
            pause.setEnabled(false);
            bt_publicar.setEnabled(false);
            bt_detener.setEnabled(true);
            bt_disc.setEnabled(false);

            i_service = new Intent(getApplicationContext(),publishBackground.class);

            i_service.putExtra("milisPause",time);
            i_service.putExtra("topico",s_topico);
            //i_service.putExtra("valor",s_valor);
            startService(i_service);
        }
    }
    public void stop(View v){
        topico.setEnabled(true);
        //valor.setEnabled(true);
        pause.setEnabled(true);
        bt_publicar.setEnabled(true);
        bt_detener.setEnabled(false);
        bt_disc.setEnabled(true);
        publishBackground.setOn(false);
        stopService(i_service);
        Toast.makeText(getBaseContext(),"Servicio detenido",Toast.LENGTH_LONG).show();
    }
    public void disconect(View v){
        try {
            IMqttToken disconToken = Client.getClient().disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getBaseContext(),"Usted se ha desconectado",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Publish.this,MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
    String fecha,topi, temp;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            temp=""+intent.getIntExtra("temperatura",0);
            valor.setText("Temperatura:"+ temp);

            fecha=intent.getStringExtra("fecha");
            topi=intent.getStringExtra("topico");

            String [] data = {fecha,topi,temp};
            TableRow row = new TableRow(getBaseContext());
            TextView textView;
            for(int i=0;i<data.length;i++){
                textView = new TextView(getBaseContext());
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(15,15,15,15);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setText(data[i]);
                row.addView(textView);
            }
            table.addView(row);
        }
    };
}