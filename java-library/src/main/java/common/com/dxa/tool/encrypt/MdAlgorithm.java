package com.dxa.tool.encrypt;

/**
 * 消息摘要算法名称
 */
public enum MdAlgorithm {
    MD5("MD5"),
    SHA128("SHA-128"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private final String algorithm;

    MdAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public String toString() {
        return this.algorithm;
    }
}
