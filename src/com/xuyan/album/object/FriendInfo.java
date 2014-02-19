package com.xuyan.album.object;

import java.util.List;

/**
 * 创建时间：2014-2-19 上午10:54:21 项目名称：MyAlbum
 * 
 * @author xuyan
 * @version 1.0
 * 
 *          文件名称：FriendInfo.java 类说明：人人好友列表中，好友详情信息
 */
public class FriendInfo {
	private String work;
	private String id;
	private String name;
	private String emotionalState;
	private String star;
	private String like;
	private String education;
	private String basicInformation;
	private List<FriendInfoAvatar> avatars;

	public FriendInfo() {

	}

	public FriendInfo(String work, String id, String name,
			String emotionalState, String star, String like, String education,
			String basicInformation, List<FriendInfoAvatar> avatars) {
		super();
		this.work = work;
		this.id = id;
		this.name = name;
		this.emotionalState = emotionalState;
		this.star = star;
		this.like = like;
		this.education = education;
		this.basicInformation = basicInformation;
		this.avatars = avatars;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmotionalState() {
		return emotionalState;
	}

	public void setEmotionalState(String emotionalState) {
		this.emotionalState = emotionalState;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getBasicInformation() {
		return basicInformation;
	}

	public void setBasicInformation(String basicInformation) {
		this.basicInformation = basicInformation;
	}

	public List<FriendInfoAvatar> getAvatars() {
		return avatars;
	}

	public void setAvatars(List<FriendInfoAvatar> avatars) {
		this.avatars = avatars;
	}

}
