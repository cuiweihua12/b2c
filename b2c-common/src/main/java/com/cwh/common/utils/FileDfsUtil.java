package com.cwh.common.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Component
public class FileDfsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDfsUtil.class);
    @Resource
    private FastFileStorageClient storageClient ;
    /**
     * 上传文件,并生成缩略图
     */
    public String uploadImageAndCrtThumbImage(MultipartFile multipartFile) throws Exception{
        //获取后缀名
        String originalFilename = multipartFile.getOriginalFilename().
                                  substring(multipartFile.getOriginalFilename().
                                  lastIndexOf(".") + 1);
        //获取上传后的路径
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                              multipartFile.getInputStream(),
                              multipartFile.getSize(),originalFilename , null);
        return storePath.getFullPath() ;
    }

    /**
     * 上传文件
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public String upload(MultipartFile multipartFile) throws Exception{
        //获取后缀名
        String originalFilename = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().
                        lastIndexOf(".") + 1);
        //获取上传后的路径
        StorePath storePath = this.storageClient.uploadFile(
                multipartFile.getInputStream(),
                multipartFile.getSize(),originalFilename , null);
        return storePath.getFullPath() ;
    }

    /**
     * 删除文件
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            LOGGER.info("fileUrl == >>文件路径为空...");
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param fileUrl
     * @return
     */
    public byte[] download(String fileUrl){
        //获取分组
        String filePath = fileUrl.substring(fileUrl.indexOf("group"));
        String group = filePath.substring(0,filePath.indexOf("/"));
        String path = filePath.substring(filePath.indexOf("/")+1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = storageClient.downloadFile(group, path, downloadByteArray);
        return bytes;
    }
}
