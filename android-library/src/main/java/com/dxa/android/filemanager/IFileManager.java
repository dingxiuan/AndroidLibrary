package com.dxa.android.filemanager;

import java.io.File;

/**
 * 文件管理
 */
public interface IFileManager {


    /**
     * 设置目录
     *
     * @param rootFile 根目录文件夹
     * @param force    当已经存在一个根目录的File对象时是否强制替换
     * @return 是否设置成功
     */
    boolean setRoot(File rootFile, boolean force);

    /**
     * 获取缓存的根目录
     */
    File getRoot();

    /**
     * 设置目录
     *
     * @param rootPath 根目录路径
     * @param force    当已经存在一个根目录时是否强制替换
     * @return 是否设置成功
     */
    boolean setRootPath(String rootPath, boolean force);

    /**
     * 获取根目录路径
     *
     * @return 返回根目录的路径
     */
    String getRootPath();

    /**
     * 获取文件路径
     *
     * @param fileName 文件名
     * @return 返回文件的绝对路径
     */
    String getFilePath(String fileName);

    /**
     * 创建目录
     *
     * @param directory 目录
     * @return 返回是否存在或创建成功
     */
    boolean createDirectoryIfNotExist(File directory);

    /**
     * 创建文件如果不存在
     *
     * @param file 文件
     * @return 返回是否存在或创建成功
     */
    boolean createFileIfNotExist(File file);

    /**
     * 列出目录下的全部文件
     *
     * @param directoryName 目录
     * @return 返回 File 数组
     */
    File[] listFiles(String directoryName);

    /**
     * 创建目录，如果存在直接返回 File 对象
     *
     * @param directoryName 目录名
     * @return 返回创建的目录文件
     */
    File getDirectory(String directoryName);

    /**
     * 创建目录，如果存在直接返回 File 对象
     *
     * @param parentFile    父目录
     * @param directoryName 目录名
     * @return 返回创建的目录文件
     */
    File getDirectory(File parentFile, String directoryName);

    /**
     * 创建文件，如果存在直接返回 File 对象
     *
     * @param fileName 文件名
     * @return 返回创建的文件
     */
    File getFile(String fileName);

    /**
     * 创建文件，如果存在直接返回 File 对象
     *
     * @param fileName 文件名
     * @return 返回创建的文件
     */
    File getFile(String directoryName, String fileName);

    /**
     * 获取绝对路径的文件
     *
     * @param directory 父目录
     * @param fileName  文件名
     * @return 返回创建的文件
     */
    File getAbsoluteFile(File directory, String fileName);

    /**
     * 获取绝对路径的文件
     *
     * @param parentFile 父目录
     * @param fileName   文件名
     * @return 返回创建的文件
     */
    File getAbsoluteFile(String parentFile, String fileName);

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 是否删除
     */
    boolean deleteFile(String fileName);

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 是否删除
     */
    boolean deleteFile(String directoryName, String fileName);

    /**
     * 删除目录
     *
     * @param directoryName 目录名
     * @return 删除的文件数量
     */
    int deleteDirectory(String directoryName);

//    /**
//     * 删除匹配的文件
//     *
//     * @param directoryName 目录名
//     * @param pattern       匹配格式
//     * @return 删除的数量
//     */
//    int deleteMatch(String directoryName, String pattern);

    /**
     * 删除文件或目录
     *
     * @param file 文件
     * @return 删除的文件数量
     */
    int delete(File file);

    /**
     * 删除全部文件和目录
     *
     * @return 删除的文件数量
     */
    int deleteAll();

    /**
     * 查看文件或目录的大小
     *
     * @param file 文件或目录
     * @return 返回文件或目录的大小
     */
    long length(String file);

    /**
     * 统计某个目录的数量，如果传入的是文件，则返回 1 ，否则返回统计的数量
     *
     * @param directoryName 目录
     * @return 返回统计的数量
     */
    int count(String directoryName);

    /**
     * 是否为文件
     *
     * @param fileName 文件名
     * @return 如果是文件返回 true，否则返回 false
     */
    boolean isFile(String fileName);

    /**
     * 是否为文件
     *
     * @param directoryName 目录名
     * @param fileName      文件名
     * @return 如果是文件返回 true，否则返回 false
     */
    boolean isFile(String directoryName, String fileName);

    /**
     * 是否为目录
     *
     * @param directoryName 目录名
     * @return 如果是目录返回 true，否则返回 false
     */
    boolean isDirectory(String directoryName);

    /**
     * 文件或目录是否存在
     *
     * @param fileName 文件名
     * @return 如果存在返回 true ，否则返回 false
     */
    boolean exist(String fileName);

}
