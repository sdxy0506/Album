package com.xuyan.album.object;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名称：RenRenAlbumList.java 类说明：
 * 
 * @create 2014-2-20 上午10:57:53 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class RenRenAlbumPhotoList {
	private String id;
	private String description;
	private String createTime;
	private List<Cover> images = new ArrayList<Cover>();
	private String albumId;
	private String viewCount;
	private String commentCount;
	private String ownerId;

	public RenRenAlbumPhotoList() {
		super();
	}

	public RenRenAlbumPhotoList(String id, String description, String createTime,
			List<Cover> images, String albumId, String viewCount,
			String commentCount, String ownerId) {
		super();
		this.id = id;
		this.description = description;
		this.createTime = createTime;
		this.images = images;
		this.albumId = albumId;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.ownerId = ownerId;
	}

	public String getLargeUrl() {
		for (int i = 0; i < images.size(); i++) {
			Cover cover = images.get(i);
			if (cover.getSize().equals("LARGE")) {
				return cover.getUrl();
			} else if (cover.getSize().equals("MAIN")) {
				return cover.getUrl();
			} else if (cover.getSize().equals("HEAD")) {
				return cover.getUrl();
			} else if (cover.getSize().equals("TINY")) {
				return cover.getUrl();
			}
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Cover> getImages() {
		return images;
	}

	public void setImages(List<Cover> images) {
		this.images = images;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
