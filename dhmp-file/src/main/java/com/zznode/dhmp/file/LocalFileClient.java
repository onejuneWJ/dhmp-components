package com.zznode.dhmp.file;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.core.message.DhmpMessageSource;
import com.zznode.dhmp.file.constant.FileErrorCode;
import com.zznode.dhmp.file.exception.FileIOException;
import com.zznode.dhmp.file.orm.FileInfoManager;
import com.zznode.dhmp.file.util.FileUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件客户端
 * <p> 文件上传下载
 *
 * @author 王俊
 */
public class LocalFileClient extends DefaultFileClient {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileClient.class);

    private final String basePath;

    private final FileInfoManager fileInfoManager;


    public LocalFileClient(String basePath, FileInfoManager fileInfoManager) {
        Assert.notNull(fileInfoManager, "fileInfoService cannot be null");
        this.basePath = basePath;
        this.fileInfoManager = fileInfoManager;
    }

    @Override
    public FileInfo saveFile(String bucket, String objectName, InputStream inputStream) throws IOException {
        return saveFile(bucket, objectName, inputStream, false);
    }

    @Override
    public FileInfo saveFile(String bucket, String objectName, InputStream inputStream, boolean randomName) throws IOException {

        Path rootPath = Paths.get(this.basePath, bucket);
        Path namePath = FileUtil.getNamePath(objectName, randomName);
        Path path = rootPath.resolve(namePath);
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            IoUtil.copy(inputStream, outputStream);
        }
        FileInfo fileInfo = FileInfo.of(bucket, namePath);

        return fileInfoManager.saveFileInfo(fileInfo);

    }

    /**
     * 获取文件信息
     *
     * @param uid 文件唯一id
     * @return FileResponse
     */
    @Override
    public FileResponse getFile(String uid) {
        FileInfo fileInfo = getFileInfo(uid, false);
        if (fileInfo == null) {
            return FileResponse.failed(null, this.getMessages().getMessage(FileErrorCode.FILE_INFO_NOT_FOUND, uid));
        }
        try {
            InputStream inputStream = getFile(fileInfo.getBucket(), fileInfo.getObjectName());
            return FileResponse.success(fileInfo, inputStream);
        } catch (IOException e) {
            logger.error("Error getting file", e);
            return FileResponse.failed(fileInfo, e.getLocalizedMessage());
        }
    }

    /**
     * 获取文件的输入流
     *
     * @param bucket     桶名
     * @param objectName 文件名. 可以包含文件夹<p> e.g. test.txt  /path/file.txt
     * @return InputStream
     * @throws FileIOException 文件不存在之类的。
     */
    @Override
    public InputStream getFile(String bucket, String objectName) throws IOException {
        Path path = Path.of(basePath, bucket, objectName);
        return Files.newInputStream(path);

    }

    @Override
    public void download(String bucket, String objectName, OutputStream outputStream) throws FileIOException {
        InputStream inputStream = null;
        try {
            inputStream = getFile(bucket, objectName);
            IoUtil.copy(inputStream, outputStream);
        } catch (IORuntimeException ioRuntimeException) {
            if (ioRuntimeException.causeInstanceOf(ClientAbortException.class)) {
                // 连接中断报的异常直接吞掉
                logger.error("连接中断.", ioRuntimeException.getCause());
            } else {
                // 其他不知道会报什么异常，比如下载过程中删除了源文件等，或者宕机了？
                throw new FileIOException(ioRuntimeException);
            }
        } catch (IOException e) {
            throw new FileIOException(e);
        } finally {
            IoUtil.close(inputStream);
        }
    }

    @Override
    public void download(String uid, OutputStream outputStream) throws FileIOException {

        FileInfo fileInfo = getFileInfo(uid, false);
        if (fileInfo == null) {
            throw new FileIOException(String.format("fileInfo not found. uid: %s", uid));
        }
        download(fileInfo.getBucket(), fileInfo.getObjectName(), outputStream);
    }

    @Override
    public void download(String bucket, String objectName, HttpServletResponse response) {
        String filename = Path.of(objectName).getFileName().toString();
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, Charset.defaultCharset()));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            download(bucket, objectName, outputStream);
        } catch (FileIOException fileIOException) {
            response.setHeader("Content-Disposition", "");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            throw new CommonException(FileErrorCode.FILE_DOWNLOAD_ERROR, fileIOException);
        } catch (IOException e) {
            // 获取响应流报的异常，不做处理，打印日志即可
            logger.error("Error downloading file", e);
        }
    }

    @Override
    public void download(String uid, HttpServletResponse response) {
        FileInfo fileInfo = getFileInfo(uid);

        download(fileInfo.getBucket(), fileInfo.getObjectName(), response);

    }

    /**
     * 获取不为空的文件对象信息,如果为空，直接抛异常
     *
     * @param uid uid
     * @return 文件对象信息
     */
    @NonNull
    private FileInfo getFileInfo(String uid) {
        return getFileInfo(uid, true);
    }

    /**
     * 获取文件对象信息
     *
     * @param uid            uid
     * @param throwException 如果为空，是否直接抛出异常给前端
     * @return 文件对象信息
     */
    private FileInfo getFileInfo(String uid, boolean throwException) {

        FileInfo fileInfo = this.fileInfoManager.getFileInfo(uid);
        if (fileInfo == null && throwException) {
            throw new CommonException(FileErrorCode.FILE_INFO_NOT_FOUND, uid);
        }
        return fileInfo;
    }


    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {

        this.messages = new MessageSourceAccessor(messageSource);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DhmpMessageSource.addBasename("messages.messages_file");
    }

    public static final class Builder {
        private String basePath;

        private FileInfoManager fileInfoManager;

        public Builder basePath(String path) {
            this.basePath = path;
            return this;
        }

        public Builder fileInfoManager(FileInfoManager fileInfoManager) {
            this.fileInfoManager = fileInfoManager;
            return this;
        }

        public LocalFileClient build() {
            Assert.notNull(basePath, "basePath cannot be null");
            Assert.notNull(fileInfoManager, "fileInfoService cannot be null");
            return new LocalFileClient(basePath, fileInfoManager);
        }
    }

}
