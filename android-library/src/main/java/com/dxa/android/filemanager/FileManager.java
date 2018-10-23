package com.dxa.android.filemanager;

import java.io.File;
import java.io.IOException;

/**
 * 文件管理
 */

public class FileManager implements IFileManager {

    private final StringBuffer buffer = new StringBuffer();
    /**
     * 初始化状态
     */
    private boolean initialized = false;
    /**
     * 根目录
     */
    private File root;
    /**
     * 根目录路径
     */
    private String rootPath;

//    /**
//     * 缓存文件的 Map，通过弱引用
//     */
//    private final LinkedHashMap<String, WeakReference<File>> fileMap = new LinkedHashMap<>();

    public FileManager() {

    }

    public FileManager(File root) {
        this.root = root;
        setRoot(root, false);
    }

    @Override
    public boolean setRoot(File f, boolean force) {
        if (f == null) {
            throw new IllegalArgumentException("根目录不能为 null ！");
        }

        @SuppressWarnings("unused")
        boolean mkdirs = f.exists() || f.mkdirs();
        // 如果强制替换，直接替换掉，否则只有为 null 时替换
        root = (force || root == null) ? f : root;
        // 设置根目录路径
        this.rootPath = root.getAbsolutePath();
        initialized = true;

        return true;
    }

    protected String append(Object... os) {
        String s;
        synchronized (buffer) {
            buffer.setLength(0);
            for (Object o : os) {
                buffer.append(o);
            }
            s = buffer.toString();
        }
        return s;
    }

    protected String appendDirectory(String... parentFiles) {
        String s;
        synchronized (buffer) {
            buffer.setLength(0);
            for (String d : parentFiles) {
                buffer.append(d).append(File.separator);
            }
            s = buffer.toString();
        }
        return s;
    }

    @Override
    public File getRoot() {
        if (!initialized) {
            throw new IllegalStateException("还未设置根目录!");
        }
        return root;
    }

    @Override
    public boolean setRootPath(String rootPath, boolean force) {
        if (isEmpty(rootPath)) {
            throw new IllegalArgumentException("文件根目录的路径不能为空或 null !");
        }

        File f = new File(rootPath);
        return setRoot(f, force);
    }

    @Override
    public String getRootPath() {
        if (!initialized) {
            throw new IllegalStateException("还未设置根目录!");
        }
        return rootPath;
    }

    @Override
    public String getFilePath(String fileName) {
        char c = fileName.charAt(0);
        String separator = (c == '/' || c == '\\' ? "" : File.separator);
        return append(getRootPath(), separator, fileName);
    }

    @Override
    public File[] listFiles(String directoryName) {
        return getDirectory(directoryName).listFiles();
    }

//    private File getWeakReferenceFile(String absolutePath) {
//        WeakReference<File> reference = fileMap.get(absolutePath);
//        return reference != null ? reference.get() : null;
//    }
//
//    private void putFile(File file) {
//        fileMap.put(file.getAbsolutePath(), new WeakReference<>(file));
//    }
//
//    private boolean containsKey(String absolutePath) {
//        return fileMap.containsKey(absolutePath);
//    }
//
//    private boolean containsKey(File file) {
//        String absolutePath = file.getAbsolutePath();
//        return fileMap.containsKey(absolutePath);
//    }
//
//    private void removeFile(File file) {
//        fileMap.remove(file.getAbsolutePath());
//    }

    private void requiredNotNull(Object o, String error) {
        if (o == null) {
            throw new NullPointerException(error);
        }
    }

    private void requiredNotEmpty(String s, String error) {
        if (s == null || s.trim().isEmpty()) {
            throw new NullPointerException(error);
        }
    }

    @Override
    public boolean createDirectoryIfNotExist(File directory) {
        return directory.exists() || directory.mkdirs();
    }

    @Override
    public boolean createFileIfNotExist(File file) {
        try {
            return file.exists() || (file.getParentFile().mkdirs() && file.createNewFile());
        } catch (IOException e) {
            onThrowIOException(file, e);
            return false;
        }
    }

    @Override
    public File getDirectory(String directoryName) {
        requiredNotEmpty(directoryName, "目录名不能为空!");

        String filePath = getFilePath(directoryName);
        File directory = new File(filePath);
        if (!directory.exists()) {
            boolean mkdirs = directory.mkdirs();
            onNewFile(directory, mkdirs);
        }
        return directory;
    }

    @Override
    public File getDirectory(File parentFile, String directoryName) {
        requiredNotNull(parentFile, "父目录不能为 null !");
        requiredNotEmpty(directoryName, "目录名不能为空!");

        File directory = new File(parentFile, directoryName);
        createFileIfNotExist(directory);
        return directory;
    }

    @Override
    public File getFile(String fileName) {
        requiredNotEmpty(fileName, "文件名不能为空!");

        String filePath = getFilePath(fileName);
        File file = new File(filePath);
        createFileIfNotExist(file);
        return file;
    }

    @Override
    public File getFile(String directoryName, String fileName) {
        String filePath = getFilePath(directoryName);
        File file = new File(filePath, fileName);
        createFileIfNotExist(file);
        return file;
    }

    @Override
    public File getAbsoluteFile(File directory, String fileName) {
        requiredNotNull(directory, "父目录不能为 null !");
        requiredNotEmpty(fileName, "文件名不能为空!");

        File file = new File(directory, fileName);
        createFileIfNotExist(file);
        return file;
    }

    @Override
    public File getAbsoluteFile(String parentFile, String fileName) {
        File file = new File(parentFile, fileName);
        createFileIfNotExist(file);
        return file;
    }

    @Override
    public boolean deleteFile(String file) {
        String filePath = getFilePath(file);
        File f = new File(filePath);
        return f.exists() && f.isDirectory() && f.delete();
    }

    @Override
    public boolean deleteFile(String directoryName, String fileName) {
        if (isEmpty(directoryName)) {
            return false;
        }
        String filePath = getFilePath(directoryName);
        File f = new File(filePath, fileName);
        return f.exists() && f.delete();
    }

    @Override
    public int deleteDirectory(String directoryName) {
        String directory = getFilePath(directoryName);
        File f = new File(directory);
        return f.exists() && f.isDirectory() ? delete(f) : 0;
    }

    @Override
    public int delete(File file) {
        int count = 0;
        if (file != null && file.exists()) {
            if (file.isFile()) {
                count = file.delete() ? count + 1 : count;
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        count += delete(f);
                    }
                }
                // 删除目录
                file.delete();
            }
        }
        return count;
    }

    @Override
    public int deleteAll() {
        return delete(getRoot());
    }

    @Override
    public long length(String file) {
        String filePath = getFilePath(file);
        File f = new File(filePath);
        return f.length();
    }

    @Override
    public int count(String file) {
        String filePath = getFilePath(file);
        File f = new File(filePath);
        return count(f);
    }

    private int count(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (file.isDirectory()) {
            int count = 0;
            File[] files = file.listFiles();
            files = files != null ? files : new File[0];
            for (File f : files) {
                if (f.isDirectory()) {
                    count += count(f);
                } else {
                    count++;
                }
            }
            return count;
        }
        return 1;
    }

    @Override
    public boolean isFile(String fileName) {
        String filePath = getFilePath(fileName);
        return new File(filePath).isFile();
    }

    @Override
    public boolean isFile(String directoryName, String fileName) {
        if (isEmpty(directoryName) || isEmpty(fileName)) {
            return false;
        }

        String filePath = getFilePath(directoryName);
        File file = new File(filePath, fileName);
        return file.exists();
    }

    @Override
    public boolean isDirectory(String directoryName) {
        String filePath = getFilePath(directoryName);
        return new File(filePath).isDirectory();
    }

    @Override
    public boolean exist(String fileName) {
        String filePath = getFilePath(fileName);
        return new File(filePath).exists();
    }

    public void onNewFile(File file, boolean isNew) {

    }

    public void onThrowIOException(File file, IOException e) {
        e.printStackTrace();
    }

    private static boolean isEmpty(String s) {
        return s != null && s.trim().isEmpty();
    }


}
