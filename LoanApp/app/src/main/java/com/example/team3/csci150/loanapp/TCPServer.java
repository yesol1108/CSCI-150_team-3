package com.example.team3.csci150.loanapp;

import android.app.ProgressDialog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yesol on 11/6/2017.
 */

public class TCPServer implements Runnable {
    ProgressDialog createProgress;
    public static final int ServerPort = 9999;
    public static final String ServerIp = "52.53.157.82";

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(ServerPort);

            while(true) {
                Socket client = serverSocket.accept();

                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
                    String str = in.readLine();
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream())), true);
                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    client.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
