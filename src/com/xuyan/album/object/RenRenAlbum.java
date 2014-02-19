package com.xuyan.album.object;

import java.util.List;

/**
 * 文件名称：RenRenAlbum.java 类说明：
 * 
 * @create 2014-2-19 下午5:04:08 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class RenRenAlbum {
	private String name;
	private String location;
	private String id;
	private String type;
	private String description;
	private String createTime;
	private String photoCount;
	private String accessControl;
	private List<Cover> cover;
	private String lastModifyTime;

	public RenRenAlbum() {
		super();
	}

	public RenRenAlbum(String name, String location, String id, String type,
			String description, String createTime, String photoCount,
			String accessControl, List<Cover> cover, String lastModifyTime) {
		super();
		this.name = name;
		this.location = location;
		this.id = id;
		this.type = type;
		this.description = description;
		this.createTime = createTime;
		this.photoCount = photoCount;
		this.accessControl = accessControl;
		this.cover = cover;
		this.lastModifyTime = lastModifyTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(String photoCount) {
		this.photoCount = photoCount;
	}

	public String getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(String accessControl) {
		this.accessControl = accessControl;
	}

	public List<Cover> getCover() {
		return cover;
	}

	public void setCover(List<Cover> cover) {
		this.cover = cover;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

}
