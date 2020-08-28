package com.cwh;


import com.cwh.jopo.UserInfo;
import com.cwh.utils.JwtUtils;
import com.cwh.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTokenTest {

    //公钥位置
    private static final String pubKeyPath = "D:\\temp\\rea.pub";
    //私钥位置
    private static final String priKeyPath = "D:\\temp\\rea.pri";
    //公钥对象
    private PublicKey publicKey;
    //私钥对象
    private PrivateKey privateKey;


    /**
     * 生成公钥私钥 根据密文
     * @throws Exception
     */
    @Test
    public void genRsaKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "9527");
    }


    /**
     * 从文件中读取公钥私钥
     * @throws Exception
     */
    @Before
    public void getKeyByRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 根据用户信息结合私钥生成token
     * @throws Exception
     */
    @Test
    public void genToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(1l, "tomcat"), privateKey, 2);
        System.out.println("user-token = " + token);
    }


    /**
     * 结合公钥解析token
     * @throws Exception
     */
    @Test
    public void parseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MzEsInVzZXJuYW1lIjoiY3Vpd2VpaHVhIiwiZXhwIjoxNTk4NTU5MzYzfQ.NH7_Z4Qo-y07EaMknN-WPTlbel4Iw46_lh2oqZzk493C9WQ0QJLGHbyRakUSwsCRoF7nOR5AW75LCzrps75v-mMCedXcwslQb7APjVSMv1fB_YCKgD8gfcMTY9PR-gy9P1JSWXu2Lo0WL78IXa_2kuoH3bfjlOQwTyO26QvKiCQ";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}


