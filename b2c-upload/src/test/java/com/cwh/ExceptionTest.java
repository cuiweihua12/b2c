package com.cwh;

import com.cwh.common.enums.ExceptionEnums;
import com.cwh.common.exception.MyException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-22 17:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExceptionTest {

    @Value("${fdfs.thumb-image.width}")
    public Integer SMALLIMAGE_WIDTH;

    @Value("${fdfs.thumb-image.height}")
    public Integer SMALLIMAGE_HEIGHT;

    @Value("${fdfs.thumb-image.width}x${fdfs.thumb-image.height}")
    public String ss;

    @Test
    public void testss(){
        System.out.println(ss);
    }

    @Test
    public void gets(){

    }

    @Test
    public void run() throws Exception {
        if (true){
            throw  new MyException(ExceptionEnums.FILE_IS_NOTNULL);
        }
        System.out.println(1);
    }

    @Test
    public void small(){
        String img = "group1/M00/00/00/wKiKgF8Y-nSATFWEAAD0R1yjqYk662.png";
        System.out.println(StringUtils.stripFilenameExtension(img));
        System.out.println(StringUtils.getFilenameExtension(img));
        System.out.println(StringUtils.stripFilenameExtension(img)+'_' + ss + '.' + StringUtils.getFilenameExtension(img));
    }

    @Test
    public void test(){
        Integer integer = new Integer(1);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(1);
        String path = "http://image.b2c.com/group1/M00/00/00/wKiKgF8WsrqAHS7rAAAc2fgbaGc58.jpeg";
        path = path.substring(path.indexOf("group"));
        String substring = path.substring(0, path.indexOf("/"));
        System.out.println(path);
        System.out.println(path.substring(path.indexOf("/")+1));
        String substring1 = path.substring(path.indexOf("/"));
        System.out.println(substring1.substring(substring1.indexOf("/")+1));
//        System.out.println(path.substring(path.indexOf("/")));
//        System.out.println(StringUtils.unqualify(path,'/'));
//        System.out.println(path.substring(path.length()-10));

    }
}
