package com.dxa.network.okhttp.builder;

import java.io.File;

/**
 * 文件
 */
public final class FilePart {
    /**
     * 创建
     */
    public static FilePart create(String name, File file) {
        return new FilePart(name, file);
    }

    private String name;
    private File file;

    public FilePart(File file) {
        this(file.getName(), file);
    }

    public FilePart(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}
