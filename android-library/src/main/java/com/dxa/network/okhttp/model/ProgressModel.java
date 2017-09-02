package com.dxa.network.okhttp.model;

import java.io.Serializable;

/**
 * UI进度回调实体类
 */
public class ProgressModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 632985641270493673L;

	private static final StringBuffer BUFFER = new StringBuffer();

    //当前读取字节长度
    private long bytesLength;
    //总字节长度
    private long contentLength;
    //是否读取完成
    private boolean done;

    public ProgressModel(long bytesLength, long contentLength, boolean done) {
        this.bytesLength = bytesLength;
        this.contentLength = contentLength;
        this.done = done;
    }

    public long getBytesLength() {
        return bytesLength;
    }

    public void setCurrentBytes(long bytesLength) {
        this.bytesLength = bytesLength;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        BUFFER.setLength(0);
        BUFFER.append("-------- ProgressModel --------")
            .append("\ncontent length: ").append(contentLength)
            .append("\nbytes length: ").append(bytesLength)
            .append("\ndone: ").append(done);
        return BUFFER.toString();
    }
}
