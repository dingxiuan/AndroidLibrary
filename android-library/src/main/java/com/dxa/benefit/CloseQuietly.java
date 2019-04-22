package com.dxa.benefit;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * 关闭流
 *
 * @author DINGXIUAN
 *
 */
public final class CloseQuietly {
    private CloseQuietly() {
    }

    /**
     * 关闭流
     *
     * @param closeable
     */
    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                /* ignore */}
        }
    }

    /**
     * 关闭流
     *
     * @param closeable
     */
    public static void close(final Closeable... closeable) {
        for (Closeable c : closeable) {
            close(c);
        }
    }
}
