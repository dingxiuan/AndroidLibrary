package com.dxa.android.filemanager;

import java.io.File;

/**
 * 用戶文件管理
 */

public class UserFileManager extends FileManager implements IUserFileManager {

    /**
     * 用户根目录
     */
    private String userRootName = "";

    private File userRootFile;

    /**
     * 用户目录的文件对象
     */
    public UserFileManager() {
    }

    public UserFileManager(File root) {
        super(root);
    }

    @Override
    public File getUserRoot() {
        return userRootFile;
    }

    @Override
    public boolean setUserRootName(String rootName) {
        if (isEmpty(rootName))
            throw new IllegalArgumentException("用户根目录名不能为空!");
        this.userRootName = rootName;
        String userRootPath = getUserRootPath();
        this.userRootFile = new File(userRootPath);
        return true;
    }

    @Override
    public String getUserRootName() {
        if (isEmpty(userRootName))
            throw new IllegalStateException("还未设置用户目录");
        return userRootName;
    }

    @Override
    public String getUserRootPath() {
        String userRootName = getUserRootName();
        return super.getFilePath(userRootName);
    }

    @Override
    public String getFilePath(String fileName) {
        fileName = append(getUserRootName(), File.separator, fileName);
        return super.getFilePath(fileName);
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
