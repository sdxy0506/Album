package com.xuyan.album.object;

/**
 * 文件名称：FriendInfoAvatar.java 类说明：好友头像类，有四种规格，TINY，HEAD，MAIN，LARGE
 * 
 * @create 2014-2-19 上午11:06:19 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class FriendInfoAvatar {
	private String url;
	private String size;

	public FriendInfoAvatar() {

	}

	public FriendInfoAvatar(String url, String size) {
		super();
		this.url = url;
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
