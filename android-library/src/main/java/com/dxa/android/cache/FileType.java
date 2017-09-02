package com.dxa.android.cache;

/**
 * 文件类型
 */
public enum FileType {
	// ABCDEFGHIJKLMNOPQRSTUVWXY
	/**
	 * 缓存
	 */
	CACHE("cache"),
	/**
	 * 下载
	 */
	DOWNLOAD("download"),
	/**
	 * 图片
	 */
	IMAGE("image"),
	/**
	 * 日志
	 */
	LOG("log"),
	/**
	 * 音乐
	 */
	MUSIC("music"),
	/**
	 * 网络
	 */
	NETWORK("network"),
	/**
	 * 其他
	 */
	OTHER("other"),
	/**
	 * 相册
	 */
	PHOTO("photo"),
	/**
	 * picture
	 */
	PICTURE("picture"),
	/**
	 * 根目录
	 */
	ROOT("root"),
	/**
	 * 音频
	 */
	SOUND("sound"),
	/**
	 * 临时目录
	 */
	TEMP("temp"),
	/**
	 * 视频
	 */
	VIDEO("video"),
	
	;
	
	private String value;
	
	private FileType(String value){
		this.value = value;
	}

	public String getType() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
