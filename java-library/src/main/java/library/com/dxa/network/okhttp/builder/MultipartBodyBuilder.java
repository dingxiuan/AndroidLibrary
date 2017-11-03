package com.dxa.network.okhttp.builder;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 上传表单时的MultipartBody
 */
public class MultipartBodyBuilder {
    private static final MediaType TYPE_MULTI_FORM = MediaType.parse("multipart/form-data");

    private MultipartBody.Builder builder;

    public MultipartBodyBuilder() {
        builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
    }

    public MultipartBodyBuilder addParams(String name, String value) {
        builder.addFormDataPart(name, value);
        return this;
    }

    /**
     * 添加参数
     */
    public MultipartBodyBuilder addParams(Map<String, String> params) {
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        return this;
    }

    /**
     * 添加文件
     */
    public MultipartBodyBuilder addFile(String name, Map<String, File> nameAndFiles) {
        File file;
        RequestBody requestBody;
        for (String fileName : nameAndFiles.keySet()) {
            file = nameAndFiles.get(fileName);
            requestBody = RequestBody.create(TYPE_MULTI_FORM, file);
            builder.addFormDataPart(name, fileName, requestBody);
        }
        return this;
    }

    /**
     * 添加文件
     */
    public MultipartBodyBuilder addFiles(String name, FilePart... parts) {
        String fileName;
        File file;
        RequestBody requestBody;
        for (FilePart part : parts) {
            fileName = part.getName();
            file = part.getFile();
            requestBody = RequestBody.create(TYPE_MULTI_FORM, file);
            builder.addFormDataPart(name, fileName, requestBody);
        }
        return this;
    }

    /**
     * 添加文件
     */
    public MultipartBodyBuilder addFiles(String name, File... files) {
        String fileName;
        RequestBody requestBody;
        for (File f : files) {
            fileName = f.getName();
            requestBody = RequestBody.create(TYPE_MULTI_FORM, f);
            builder.addFormDataPart(name, fileName, requestBody);
        }
        return this;
    }

    public MultipartBody build() {
        return builder.build();
    }


}
