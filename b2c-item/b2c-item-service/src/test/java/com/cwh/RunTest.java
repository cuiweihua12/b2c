package com.cwh;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-22 08:55
 */
public class RunTest {
    public static void main(String[] args) {
        new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }
        ).start();
    }

}
