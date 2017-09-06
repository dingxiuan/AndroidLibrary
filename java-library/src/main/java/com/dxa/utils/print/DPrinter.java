package com.dxa.utils.print;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日志打印
 */
public class DPrinter {

	public static final DPrinter printer = new DPrinter();

	private static boolean debug = true;

	public static void setDebug(boolean d) {
		debug = d;
	}

	/**
	 * 对象锁
	 */
	private final Object lock = new Object();
	/**
	 * 缓存，用于拼接字符串
	 */
	private final StringBuffer buffer = new StringBuffer();
	/**
	 * 格式化日期
	 */
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
	/**
	 * 标签
	 */
	private String tag = "D-Printer";

	private PrintLevel level = PrintLevel.INFO;

	public DPrinter() {
	}
	
	public DPrinter(String tag) {
		this.tag = tag;
	}

	public DPrinter(PrintLevel level) {
		this.level = level;
	}

	public DPrinter(String tag, PrintLevel level) {
		this.tag = tag;
		this.level = level;
	}

	public String getTag() {
		return tag;
	}

	/**
	 * 标签名
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	public PrintLevel getLevel() {
		return level;
	}

	/**
	 * 设置打印日志的水平
	 */
	public void setLevel(PrintLevel level) {
		this.level = level;
	}

	/**
	 * 获取日期格式化的模式
	 */
	public String getPattern() {
		return sdf.toPattern();
	}
	
	/**
	 * 设置日期格式化的样式
	 */
	public void applyPattern(String pattern) {
		sdf.applyPattern(pattern);
	}
	
	/**
	 * 打印
	 */
	public void i(String s) {
		println(PrintLevel.INFO, tag, s);
	}

	/**
	 * 打印
	 */
	public void e(String s) {
		println(PrintLevel.ERROR, tag, s);
	}

	/**
	 * 打印
	 */
	private void println(PrintLevel level, String tag, String s) {
		if (isPrint(level)) {
			buffer.setLength(0);
			buffer.append(tag).append(">>>: ").append(s).append("......")
					.append(sdf.format(System.currentTimeMillis()));
			println(level, buffer.toString());
		}
	}

	private void println(PrintLevel level, String s) {
		synchronized (lock) {
			switch (level) {
				case INFO:
					System.out.println(s);
					break;
				case ERROR:
					System.err.println(s);
					break;
				default:
					break;
			}
		}
	}

	public void t(PrintLevel level, String s) {
		if (isPrint(level)) {
			buffer.setLength(0);
			buffer.append(tag).append(">>>: ").append(s).append("\n")
				.append("thread: ").append(getThreadName()).append("\n")
				.append("time: ").append(sdf.format(System.currentTimeMillis()));
			println(level, buffer.toString());
		}
	}

	public void t(PrintLevel level, String tag, String s) {
		if (isPrint(level)) {
			println(level, tag, s);
		}
	}

	private boolean isPrint(PrintLevel level) {
		return debug && level != PrintLevel.NONE;
	}

	/**
	 * 获取当前线程的名字
	 */
	public static String getThreadName() {
		return Thread.currentThread().getName();
	}
}
