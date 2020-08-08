package com.cwh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-19 14:46
 */
public class PathTest implements ApplicationListener<WebServerInitializedEvent> {
    private Integer serverPort;
    public static void main(String[] args) {
        String s = System.getProperty("user.dir");
        System.out.println(s+"\\upload");


    }
    @Override
    public  void onApplicationEvent(WebServerInitializedEvent event) {
        serverPort = event.getWebServer().getPort();
    }

    @Test
    public void pathTest(){
        try {
            InetAddress add = InetAddress.getLocalHost();
            System.out.println(add);
            System.out.println("http://"+add.getHostAddress() +":"+serverPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }



}
