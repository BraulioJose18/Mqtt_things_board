package com.example.dummy.Models;

import java.io.Serializable;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class Client implements Serializable {
    static MqttAndroidClient client;
    public static void setClient(MqttAndroidClient client_in){
        client=client_in;
    }
    public static MqttAndroidClient getClient(){
        return client;
    }
}
