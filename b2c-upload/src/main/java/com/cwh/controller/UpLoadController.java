package com.cwh.controller;

import com.cwh.common.enums.ExceptionEnums;
import com.cwh.common.exception.MyException;
import com.cwh.common.utils.FileDfsUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: b2c
 * @description: 上传文件
 * @author: cuiweihua
 * @create: 2020-07-19 14:30
 */
@RestController
@RequestMapping("/upload")
@Api(tags = "上传文件服务相关")
public class UpLoadController {

    private static final Logger logger = LoggerFactory.getLogger(UpLoadController.class);
    @Autowired
    private FileDfsUtil fileDfsUtil;

    @Value("${fdfs.thumb-image.width}x${fdfs.thumb-image.height}")
    private String smallImageSize;

    @Value("${image.server}")
    private String SERVERNAME;

    @PostMapping("image")
    @ApiOperation("上传图片")
    @ApiResponse(code = 400,message = "上传文件为空或类型错误")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){
        String filePath = null;
        if (file.isEmpty()){
            throw new MyException(ExceptionEnums.FILE_IS_NOTNULL);
        }
        logger.info("上传文件 =============>"+file.getOriginalFilename());
        try {
            filePath = fileDfsUtil.upload(file);
        } catch (Exception e) {
            logger.info("文件上传失败");
            e.printStackTrace();
        }

        return ResponseEntity.ok(SERVERNAME+filePath);
    }

    @PostMapping("smallImage")
    @ApiOperation(value = "上传图片并生成缩略图")
    @ApiResponse(code = 400,message = "上传文件为空或类型错误")
    public Map<String,String> uploada(@RequestParam("file") MultipartFile file){
        String filePath = null;
        HashMap<String, String> map = null;
        if (file.isEmpty()){
            throw new MyException(ExceptionEnums.FILE_IS_NOTNULL);
        }
        map = new HashMap<>();
        logger.info("上传文件 =============>"+file.getOriginalFilename());
        try {
            filePath = fileDfsUtil.uploadImageAndCrtThumbImage(file);
        } catch (Exception e) {
            logger.info("文件上传失败");
            e.printStackTrace();
        }
        map.put("img",SERVERNAME+filePath);
        map.put("small",SERVERNAME+StringUtils.stripFilenameExtension(filePath)+'_' + smallImageSize + '.' + StringUtils.getFilenameExtension(filePath));
        return map;
    }

    @DeleteMapping
    @ApiOperation("删除上传文件")
    @ApiParam(name = "fielUrl",value = "文件路径",required = true,type = "String")
    public String fileDel(String fielUrl){
        logger.info("删除文件 =================>"+fielUrl);
        fileDfsUtil.deleteFile(fielUrl);
        return "success";
    }

    @GetMapping("down")
    @ApiOperation("下载文件")
    @ApiParam(name = "fileUrl",value = "文件路径",required = true)
    public void download(String fileUrl, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        byte[] bytes = fileDfsUtil.download(fileUrl);
        //获取文件名加后缀
        String suffix = StringUtils.unqualify(fileUrl,'/');
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(suffix, "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
