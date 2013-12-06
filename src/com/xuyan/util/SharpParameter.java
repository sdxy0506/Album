package com.xuyan.util;

import com.xuyan.album.http.RequestAid;
import com.xuyan.album.tools.SharpDES;

import android.content.Context;
import android.content.SharedPreferences;

public class SharpParameter {
	public static final int TaskResult_OK = 99;
	public static final int TaskResult_NO = 101;
	public static final int TIMEINT = 60 * 1000;
	public static String http = "http://api.3ren.cn";
	public static String loginUrl = http + "/account/login.do";
	public static String getHomeUrl = http
			+ "/activities/friends_timeline.json";
	public static String getOtherHomeUrl = http
			+ "/activities/user_timeline.json";
	public static String getCommentUrl = http + "/activities/single.json";
	public static String postSaytUrl = http + "/activities/publish.json";
	public static String transferSayUrl = http + "/activities/repost.json";
	// 评论的list
	// 1
	public static String getSayCommentUrl = http + "/comments/list.json";
	// 2
	public static String getDiaryClassCommentUrl = http
			+ "/comments/class/list.json";
	// 3
	public static String getFenXiangCommentUrl = http
			+ "/share/comments/list.json";
	// 4
	public static String getFenXiangUserCommentUrl = http
			+ "/comments/user/photo/list.json";
	// 5
	public static String getFenXiangClassCommentUrl = http
			+ "/comments/class/photo/list.json";

	// post评论
	// 1
	public static String postCommentUrl = http + "/comments/comment.json";
	// 2
	public static String postShareClassDiaryCommentUrl = http
			+ "/comments/class/comment.json";
	// 3
	public static String postShareCommentUrl = http + "/share/comment.json";
	// 4
	public static String postPhotoPersonCommentUrl = http
			+ "/comments/user/photo/comment.json";
	// 5
	public static String postPhotoClassCommentUrl = http
			+ "/comments/class/photo/comment.json";

	public static String getGreadeItem = http + "/relation/myclasses.json";
	public static String getTalk = http + "/jx/talks.json";
	public static String getHomeWork = http + "/jx/homework.json";
	public static String getInfo = http + "/jx/broadcast.json";
	public static String getStudents = http + "/jx/students.json";
	public static String getTeachers = http + "/jx/teachers.json";
	public static String getJxComment = http + "/jx/msg/comments.json";
	public static String postTalkToTalk = http + "/jx/msg/comment.json";
	public static String postTalk = http + "/jx/talk/publish.json";
	public static String postHomwInfo = http + "/jx/msg/publish.json";
	public static String postCheckIn = http + "/class/score/checkin.json";
	public static String getCheckIn = http + "/class/score/haschecked.json";

	public static String getNotificationNum = "http://macaca.3ren.cn/mnotice";
	public static String getAtMe = http + "/activities/atme.json";
	public static String getRemind = http + "/comments/to_me.json";
	public static String postResgister = http + "/account/register.json";
	public static String getMobileCode = http + "/code/mobile/r.json";
	public static String getRecord = http + "/jx/audio.json";
	public static String getVersion = http + "/common/ver.json";

	public static String dataBase = "SharedPreferences";
	public static String APP_SESSION_ID_NAME = "APPSESSIONID";
	public static String avatar = "";
	public static String nickname = "";

	public static String isImageUrl(String url) {
		// if (url != null &&
		// !url.contains("http://static.jsyxw.cn")&&!url.contains("http://www.srxing.com"))
		// {
		// url = "http://www.srxing.com" + url;
		// }
		return url;
	}

	public static String imageToSmall(String url) {
		/***
		 * 大图换小图
		 */
		// if (url != null && !url.equals("null")) {
		// if (url.contains("_l")) {
		// url = url.replace("_l", "_s");
		// } else if (url.contains("_o")) {
		// url = url.replace("_o", "_s");
		// }
		// }

		return url;
	}

	public static String headPic_Process(String url) {
		// url = isImageUrl(url);
		// url = imageToSmall(url);
		return url;
	}

	public static String desKey = "1563##$*ni@*#456#$%122";
	public static SharpDES sharpDES = new SharpDES(desKey);
	public static String cookie = null;
	public static long nowDate = System.currentTimeMillis();
	public static String UId = "null";
	public static String sessionId = null;
	public static String SharedPreferencesName = "SharedPreferences";

	/**
	 * 检测UId是否丢失
	 * 
	 * @author xuyan
	 * 
	 * @param context
	 */
	public static void setCookie(Context context) {
		if (SharpParameter.UId.equals("null")) {
			// Log.i("coolie", "重新设置cookie");
			RequestAid.setRequestHeader(SharpParameter.APP_SESSION_ID_NAME,
					getRandomString(12));
			String cookie = "id=402063; access_token=826637fab76b717370c5499d5147f978#1386234047458";
			SharpParameter.UId = "402063";
			SharpParameter.cookie = cookie;
			RequestAid.setRequestHeader("Cookie", cookie);
		}
	}

	public static String getRandomString(int length) {
		// 定义验证码的字符表
		String chars = "3323456789ABCDEFGHHJKLMNNPQRSTUVWXYZ";
		char[] rands = new char[length];
		for (int i = 0; i < length; i++) {
			int rand = (int) (Math.random() * 36);
			rands[i] = chars.charAt(rand);
		}
		return new String(rands);
	}

}
