package com.redis.redis_springboot.util;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.redis.redis_springboot.confign.ConstantPropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 工具类--阿里云对象存储服务OSS
 * 问题排查路径：https://help.aliyun.com/zh/oss/developer-reference/error-handling-1?spm=a2c4g.11186623.0.0.738d38e0zPG3h9
 */
@Slf4j
@Configuration
public class AliyunOssUtil {
    // 查询文件个数默认值
    private static final int DEFAULT_OBJECT_MAX_KEYS = 100;
    // 查询文件个数最大值
    private static final int MAX_RETURNED_KEYS_LIMIT = 1000;
    // 删除文件个数默认值
    public static final int DELETE_OBJECTS_ONETIME_LIMIT = 1000;

    // 阿里云对象存储客户端
    private final OSS ossClient;
    // 存储空间名称
    private final String bucketName;

    public AliyunOssUtil(ConstantPropertiesConfig aliyunOssProperties) {
        // 创建OSSClient实例。
        this.ossClient = new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(), aliyunOssProperties.getKeyId(), aliyunOssProperties.getKeySecret());
        this.bucketName = aliyunOssProperties.getBucketName();
        // 判断存储空间是否存在
        if (this.doesBucketExist(this.bucketName)) {
            log.info("存储空间[{}]已存在，无需创建", this.bucketName);
            return;
        }
        // 创建存储空间
        this.createBucket(this.bucketName);
    }

    /**
     * 创建存储空间
     * @param bucketName 存储空间名称
     * 存储空间Bucket命名规范：1)只能包括小写字母，数字和短横线（-）；2)必须以小写字母或者数字开头；3)长度必须在 3-63 字节之间。
     */
    public void createBucket(String bucketName) {
        try {
            // 创建CreateBucketRequest对象。
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);

            // 如果创建存储空间的同时需要指定存储类型、存储空间的读写权限、数据容灾类型, 请参考如下代码。
            // 此处以设置存储空间的存储类型为标准存储为例介绍。
            //createBucketRequest.setStorageClass(StorageClass.Standard);
            // 数据容灾类型默认为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请设置为DataRedundancyType.ZRS。
            //createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS);
            // 设置存储空间读写权限为公共读，默认为私有。
            //createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);

            // 在支持资源组的地域创建Bucket时，您可以为Bucket配置资源组。
            //createBucketRequest.setResourceGroupId(rsId);
            this.ossClient.createBucket(createBucketRequest);
        } catch (OSSException e) {
            log.error("创建存储空间-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("创建存储空间-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 查看当前账户下的存储空间名称列表
     * @return 存储空间名称列表
     */
    public List<String> listBuckets() {
        List<String> bucketNameList = new LinkedList<>();
        try {
            // 返回当前帐户的所有 Bucket 实例
            List<Bucket> buckets = this.ossClient.listBuckets();
            for (Bucket bucket : buckets) {
                bucketNameList.add(bucket.getName());
            }
        } catch (OSSException e) {
            log.error("查看当前账户下的存储空间名称列表-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("查看当前账户下的存储空间名称列表-客户端异常：{}", e.getMessage());
            throw e;
        }
        return bucketNameList;
    }

    /**
     * 检查存储空间是否存在
     * @return 是否存在
     */
    public boolean doesBucketExist(String bucketName) {
        try {
            return this.ossClient.doesBucketExist(bucketName);
        } catch (OSSException e) {
            log.error("检查存储空间是否存在-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("检查存储空间是否存在-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 删除存储空间（需确保存储空间为空）
     * 删除不是自己的存储空间会报错：AccessDenied
     * 删除不存在的存储空间会报错：NoSuchBucket
     */
    public void deleteBucket(String bucketName) {
        try {
            this.ossClient.deleteBucket(bucketName);
        } catch (OSSException e) {
            log.error("删除存储空间-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("删除存储空间-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 上传文件（默认覆盖已存在的文件）
     * @param file 文件
     */
    public void putObject(File file) {
        this.putObject(file.getName(), file);
    }

    /**
     * 上传文件（默认覆盖已存在的文件）
     * @param objectName 文件名
     * @param file 文件
     */
    public void putObject(String objectName, File file) {
        this.putObject(objectName, file, true);
    }

    /**
     * 上传文件
     * @param file 文件
     * @param isCover 文件存在时是否覆盖
     */
    public void putObject(File file, boolean isCover) {
        this.putObject(file.getName(), file, isCover);
    }

    /**
     * 上传文件
     * @param objectName 文件名
     * @param file 文件
     * @param isCover 文件存在时是否覆盖
     */
    public void putObject(String objectName, File file, boolean isCover) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
//            throw e;
        }
        this.putObject(objectName, inputStream, isCover);
    }

    /**
     * 上传文件（默认覆盖已存在的文件）
     * @param objectName 文件名
     * @param inputStream 文件流
     */
    public void putObject(String objectName, InputStream inputStream) {
        this.putObject(objectName, inputStream, true);
    }

    /**
     * 上传文件
     * @param objectName 文件名
     * @param inputStream 文件流
     * @param isCover 文件存在时是否覆盖
     */
    public void putObject(String objectName, InputStream inputStream, boolean isCover) {
        // 判断是否覆盖已存在的文件
        if (!isCover && this.doesObjectExist(objectName)) {
            log.warn("文件[{}]已存在，无需上传", objectName);
            return;
        }
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, objectName, inputStream);

        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);
        try {
            // 上传
            this.ossClient.putObject(putObjectRequest);
        } catch (OSSException e) {
            log.error("上传文件-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("上传文件-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 下载文件
     * @param objectName 文件名
     * @return 文件流
     */
    public InputStream getObject(String objectName) {
        // 判断文件是否存在
        if (!this.doesObjectExist(objectName)) {
            throw new IllegalArgumentException(String.format("文件[%s]不存在", objectName));
        }
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元数据以及一个输入流。
            OSSObject ossObject = this.ossClient.getObject(this.bucketName, objectName);
            return ossObject.getObjectContent();
        } catch (OSSException e) {
            log.error("下载文件-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("下载文件-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 检查文件是否存在
     * @param objectName 文件名
     * @return 文件是否存在
     */
    public boolean doesObjectExist(String objectName) {
        try {
            // 判断文件是否存在。如果返回值为true，则文件存在，否则存储空间或者文件不存在。
            return this.ossClient.doesObjectExist(this.bucketName, objectName);
        } catch (OSSException e) {
            log.error("检查文件是否存在-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("检查文件是否存在-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取文件元数据
     * @param objectName 文件名
     * @return 文件元数据
     */
    public ObjectMetadata getObjectMetadata(String objectName) {
        // 判断文件是否存在
        if (!this.doesObjectExist(objectName)) {
            throw new IllegalArgumentException(String.format("文件[%s]不存在", objectName));
        }
        try {
            // 获取文件的全部元数据。
            return this.ossClient.getObjectMetadata(this.bucketName, objectName);
        } catch (OSSException e) {
            log.error("获取文件元数据-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("获取文件元数据-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 列举指定存储空间下的文件名（默认列举100个）
     * @return 文件名列表
     */
    public List<String> listObjects() {
        return this.listObjects("", "", DEFAULT_OBJECT_MAX_KEYS);
    }

    /**
     * 根据文件前缀列举指定存储空间下的所有文件名（默认列举100个）
     * @param objectPrefix 文件前缀
     * @return 文件名列表
     */
    public List<String> listObjects(String objectPrefix) {
        return this.listObjects(objectPrefix, "", DEFAULT_OBJECT_MAX_KEYS);
    }

    /**
     * 列举存储空间下指定个数文件名
     * @param objectCount 文件个数
     * @return 文件名列表
     */
    public List<String> listObjects(int objectCount) {
        return this.listObjects("", "", objectCount);
    }

    /**
     * 根据文件前缀列举指定存储空间下的所有文件名（默认列举100个）
     * @param objectPrefix 文件前缀
     * @param objectDelimiter 文件名分隔符
     * @return 文件名列表
     */
    public List<String> listObjects(String objectPrefix, String objectDelimiter) {
        return this.listObjects(objectPrefix, objectDelimiter, DEFAULT_OBJECT_MAX_KEYS);
    }

    /**
     * 根据文件名前缀列举指定存储空间下的指定个数文件名
     * @param objectPrefix 文件名前缀
     * @param objectDelimiter 文件名分隔符
     * @param objectCount 文件个数
     * @return 文件名列表
     */
    public List<String> listObjects(String objectPrefix, String objectDelimiter, int objectCount) {
        List<String> objectNameList;
        if (objectCount < 1 || objectCount > MAX_RETURNED_KEYS_LIMIT) {
            throw new IllegalArgumentException("文件个数超出范围，默认值：100，取值范围：(0, 1000]");
        }
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            listObjectsRequest.setPrefix(objectPrefix);
            listObjectsRequest.setMaxKeys(objectCount);
            listObjectsRequest.setDelimiter(objectDelimiter);
            ObjectListing objectListing = this.ossClient.listObjects(listObjectsRequest);
            List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            objectNameList = objectSummaries.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());
        } catch (OSSException e) {
            log.error("根据文件前缀列举指定存储空间下的指定个数文件名-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("根据文件前缀列举指定存储空间下的指定个数文件名-客户端异常：{}", e.getMessage());
            throw e;
        }
        return objectNameList;
    }

    /**
     * 复制文件（同存储空间复制）
     * @param sourceObjectName 源文件名
     * @param targetObjectName 目标文件名
     * @return 成功标志
     */
    public boolean copyObject(String sourceObjectName, String targetObjectName) {
        return this.copyObject(sourceObjectName, this.bucketName, targetObjectName);
    }

    /**
     * 复制文件
     * @param sourceObjectName 源文件名
     * @param targetBucketName 目标存储空间
     * @param targetObjectName 目标文件名
     * @return 成功标志
     */
    public boolean copyObject(String sourceObjectName, String targetBucketName, String targetObjectName) {
        // 判断文件是否存在
        if (!this.doesObjectExist(sourceObjectName)) {
            throw new IllegalArgumentException(String.format("文件[%s]不存在", sourceObjectName));
        }
        // 重命名名称相同
        if (sourceObjectName.equals(targetObjectName)) {
            log.warn("复制后名称相同[{}]", sourceObjectName);
            return true;
        }
        try {
            // 复制文件并重新命名
            this.ossClient.copyObject(this.bucketName, sourceObjectName, targetBucketName, targetObjectName);
            return this.doesObjectExist(targetObjectName);
        } catch (OSSException e) {
            log.error("复制文件-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("复制文件-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 重命名文件
     * @param sourceObjectName 源文件名
     * @param targetObjectName 目标文件名
     * @return 成功标志
     */
    public boolean renameObject(String sourceObjectName, String targetObjectName) {
        // 重命名名称相同
        if (sourceObjectName.equals(targetObjectName)) {
            log.warn("重命名名称相同[{}]", sourceObjectName);
            return true;
        }
        return this.moveObject(sourceObjectName, this.bucketName, targetObjectName);
    }

    /**
     * 移动文件
     * @param sourceObjectName 源文件名
     * @param targetBucketName 目标存储空间
     * @param targetObjectName 目标文件名
     * @return 成功标志
     */
    public boolean moveObject(String sourceObjectName, String targetBucketName, String targetObjectName) {
        // 复制文件
        boolean copyFlag = this.copyObject(sourceObjectName, targetBucketName, targetObjectName);
        if (copyFlag) {
            // 删除源文件
            return this.deleteObject(sourceObjectName);
        }
        return false;
    }

    /**
     * 删除的单个文件（如果是目录，需确保目录下为空）
     * @param objectName 文件名
     * @return 成功标志
     */
    public boolean deleteObject(String objectName) {
        // 判断文件是否存在
        if (!this.doesObjectExist(objectName)) {
            throw new IllegalArgumentException(String.format("文件[%s]不存在", objectName));
        }
        try {
            this.ossClient.deleteObject(this.bucketName, objectName);
            return this.doesObjectExist(objectName);
        } catch (OSSException e) {
            log.error("删除的单个文件-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("删除的单个文件-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 删除指定前缀或目录下的多个文件（默认删除1000个）
     * @param objectPrefix 文件名前缀
     * @return 已删除的文件名列表
     */
    public List<String> deleteObjectByPrefix(String objectPrefix) {
        List<String> objectNames = this.listObjects(objectPrefix, "", DELETE_OBJECTS_ONETIME_LIMIT);
        return this.deleteObjects(objectNames);
    }

    /**
     * 根据文件名称列表删除文件（每次最多删除1000个）
     * @param objectNames 要删除的文件名列表
     * @return 已删除的文件名列表
     */
    public List<String> deleteObjects(List<String> objectNames) {
        // 已删除文件名列表
        List<String> deletedObjectList = new ArrayList<>();
        if (objectNames == null || objectNames.isEmpty()) {
            log.warn("没有查询到要删除的文件");
            return deletedObjectList;
        }

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.withKeys(objectNames);
        deleteObjectsRequest.withEncodingType("url");
        try {
            DeleteObjectsResult deleteObjectsResult = this.ossClient.deleteObjects(deleteObjectsRequest);
            deletedObjectList = deleteObjectsResult.getDeletedObjects();
        } catch (OSSException e) {
            log.error("根据文件名称列表删除文件-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("根据文件名称列表删除文件-客户端异常：{}", e.getMessage());
            throw e;
        }
        return deletedObjectList;
    }

    /**
     * 生成文件预签名url（默认有效10分钟）
     * @param objectName 文件名
     * @return url
     */
    public String generatePresignedUrl(String objectName) {
        return this.generatePresignedUrl(objectName, 1000 * 60 * 10);
    }

    /**
     * 生成文件预签名url
     * @param objectName 文件名
     * @param expiration 有效时长（毫秒）
     * @return url
     */
    public String generatePresignedUrl(String objectName, long expiration) {
        // 判断文件是否存在
        if (!this.doesObjectExist(objectName)) {
            throw new IllegalArgumentException(String.format("文件[%s]不存在", objectName));
        }
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(this.bucketName, objectName, HttpMethod.GET);
        request.setExpiration(new Date(System.currentTimeMillis() + expiration));
        try {
            return this.ossClient.generatePresignedUrl(request).toString();
        } catch (OSSException e) {
            log.error("生成文件预签名url-服务端异常：{}", e.getMessage());
            throw e;
        } catch (ClientException e) {
            log.error("生成文件预签名url-客户端异常：{}", e.getMessage());
            throw e;
        }
    }

    // 将 MultipartFile 转换为 File，并使用原始文件名
    public File convertMultipartFileToFile(MultipartFile multipartFile) throws Exception {
        // 获取原始文件名
        String originalFilename = multipartFile.getOriginalFilename();

        // 这里可以创建一个临时文件夹来存储文件
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + originalFilename);
        multipartFile.transferTo(file);
        return file;
    }
}

