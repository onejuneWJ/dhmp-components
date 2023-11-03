package com.zznode.dhmp.file.orm;

import com.zznode.dhmp.file.FileInfo;

/**
 * 文件服务，用于保存获取文件信息
 *
 * @author 王俊
 */
public interface FileInfoManager {

    FileInfo saveFileInfo(FileInfo fileInfo);

    FileInfo getFileInfo(String uid);

    FileInfo getFileInfo(Long id);
}
