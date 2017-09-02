package com.dxa.android.cache;

import java.io.File;

/**
 * 文件的基本数据
 */
public class FileModel {
	
	private File file;
	private String path;
	private String simpleName;
	private long length;
	private boolean directory;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("FileModel [file:").append(file )
				.append( ", path:" ).append( path )
				.append(", simpleName:" ).append( simpleName )
				.append(", length:" ).append( length)
				.append(", directory:" ).append(directory ).append("]")
				.toString();
	}
	
	public static class Builder {
		
		private final FileModel model;
		
		public Builder() {
			model = new FileModel();
		}

		public void setFile(File file) {
			model.setFile(file);
		}

		public void setPath(String path) {
			model.setPath(path);
		}

		public void setSimpleName(String simpleName) {
			model.setSimpleName(simpleName);
		}

		public void setLength(long length) {
			model.setLength(length);
		}

		public void setDirectory(boolean directory) {
			model.setDirectory(directory);
		}
		
		public FileModel build(){
			return model;
		}

	}
	
}
