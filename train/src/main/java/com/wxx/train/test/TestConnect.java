package com.wxx.train.test;


import java.io.*;
import java.net.*;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-08-14 18:03
 **/
public class TestConnect {
    public static ServerSocket socket = null;
    //创建线程池避免频繁创建
    static ThreadPoolExecutor threadPoolExecutor = new
            ThreadPoolExecutor(3, 6, 6000,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.AbortPolicy());


    public void initSocket(int port){
        try {
            socket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //线程对socket的链接后拿到数据进行处理
    static class TimerService implements Runnable{
        private  Socket socket = null;

        public TimerService(Socket socket) {
            this.socket = socket;

        }
        //处理
        @Override
        public void run() {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                //发送心跳包
                socket.sendUrgentData(0xFF);
                //设置长链接
                socket.setKeepAlive(true);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String message = bufferedReader.readLine();
                System.out.println("服务器收到消息："+message);
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println("现在是："+new Date());
                printWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //关闭资源
                if (null != inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }if(null != outputStream){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }if(null != socket){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        TestConnect testConnect = new TestConnect();
        testConnect.initSocket(8097);

        while(true){
            try {
                //serverSocket只起监听作用
                // 阻塞监听客户端的连接请求，有连接则返回
                Socket accept = socket.accept();
                //对socket数据进行处理
                threadPoolExecutor.execute(new TimerService(accept));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
//                if (socket != null){
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }

    }






}
