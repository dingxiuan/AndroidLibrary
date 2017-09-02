package com.dxa.android.logger;

/**
 * log状态
 */
public enum LogLevel {
	VERBOSE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), NONE(6);

    private int level;

    private LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.valueOf(level);
    }
}
