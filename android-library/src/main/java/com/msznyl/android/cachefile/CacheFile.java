package com.msznyl.android.cachefile;

/**
 *  缓存文件
 */

interface CacheFile {
    /**
     * 心率
     */
    int TYPE_HEART_RATE = 1;
    /**
     * 心电
     */
    int TYPE_RAW = 2;
    /**
     * 接触皮肤状态
     */
    int TYPE_POOR_SIGNAL = 3;

    String _COMMA = ", ";
    String _NEW_LINE = "\n";
}
