package com.wxx.train.test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-08-17 15:26
 **/

public class TestClient {
    static Socket socket = null;

    public void init(){
        try {
            socket = new Socket("localhost",8097);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestClient testClient = new TestClient();
        testClient.init();

        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            //发送心跳包
            socket.sendUrgentData(0xFF);
            //设置长链接
            socket.setKeepAlive(true);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println("what's time ?");
            printWriter.flush();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s = bufferedReader.readLine();
            System.out.println("======"+s);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != socket){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }







}
