package com.dxa.utils.unit;

public class Units {

	/**
	 * 秒
	 */
	public static final long SECOND = 1000;
	/**
	 * 分钟
	 */
	public static final long MINUTE = SECOND * 60;
	/**
	 * 小时
	 */
	public static final long HOUR = MINUTE * 60;
	/**
	 * 天
	 */
	public static final long DAY = HOUR * 24;
	
	/*********************************************************/
	
	/**
	 * KB
	 */
	public static final long KB = 1024;
	
	/**
	 * MB
	 */
	public static final long MB = 1024 * KB;
	
	/**
	 * GB
	 */
	public static final long GB = 1024 * MB;

	/**
	 * TB
	 */
	public static final long TB = 1024 * GB;
	
//	public static void main(String[] args) throws IOException {
//		File file = new File("E:/temp/abcd.txt");
//		System.out.println(file.length());
//		FileInputStream fis = FileUtils.getFileInputStream(file);
//		String content = FileUtils.readString(fis, "GBK");
//		System.out.println(content.length());
//	}
}
