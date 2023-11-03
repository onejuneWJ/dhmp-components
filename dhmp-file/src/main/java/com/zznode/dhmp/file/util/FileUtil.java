package com.zznode.dhmp.file.util;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.RandomUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 描述
 *
 * @author 王俊
 */
public class FileUtil {

    public static Path getNamePath(String objectName) {
        return getNamePath(objectName, false);
    }

    /**
     * 根据入参文件名字符串获得对应的Path对象
     *
     * @param objectName 对象名称，可以包含文件夹。如：file.txt、/path/to/file.txt
     * @param randomName 是否将文件名随机化. 当为true时，文件名会转换为8位随机的由[数字、字母]组成的字符串
     *                   file.txt -> uoQPnP6k.txt
     * @return path
     * @see Path
     */
    public static Path getNamePath(String objectName, boolean randomName) {
        if (objectName.startsWith("/")) {
            objectName = objectName.substring(1);
        }
        Path namePath = Path.of(objectName);
        if (randomName) {
            Path parent = namePath.getParent();
            Path fileName = namePath.getFileName();
            Path randomFileName = randomFileName(fileName);
            return Objects.isNull(parent) ? randomFileName : parent.resolve(randomFileName);
        }

        return namePath;

    }

    /**
     * 随机文件名
     *
     * @param fileName 文件名
     * @return 随机文件名
     */
    public static Path randomFileName(Path fileName) {
        String fileType = FileNameUtil.getSuffix(fileName.toFile());
        String randomStr = RandomUtil.randomString(6);
        return Paths.get(randomStr + "." + fileType);
    }

}
