package com.xuyan.album.object;

/**
 * 文件名称：Cover.java 类说明：
 * 
 * @create 2014-2-19 下午5:06:17 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class Cover {
	private String size;
	private String url;

	public Cover() {
		super();
	}

	public Cover(String size, String url) {
		super();
		this.size = size;
		this.url = url;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
