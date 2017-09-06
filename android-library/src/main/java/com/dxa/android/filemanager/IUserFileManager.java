package com.dxa.android.filemanager;

import java.io.File;

/**
 * 用户目录下的文件管理
 */
public interface IUserFileManager extends IFileManager {

    /**
     * 获取用户目录对象
     *
     * @return 返回用户目录对象
     */
    File getUserRoot();

    /**
     * 设置用户根目录
     *
     * @param rootName 根目录
     * @return 是否设置成功
     */
    boolean setUserRootName(String rootName);

    /**
     * 获取用户的根目录
     *
     * @return 返回用户的根目录
     */
    String getUserRootName();

    /**
     * 获取用户根目录的路径
     *
     * @return 返回用户根目录的路径
     */
    String getUserRootPath();

}
