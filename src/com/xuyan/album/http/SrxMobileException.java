/**
 *@title SrxMobileException.java 
 *@package com.srx.utility 
 *@author Johnbin Wang (code my life) 
 *@mail johnbin.wang@gmail.com 
 *@date 2012-8-14 
 *@version V1.0
 */
package com.xuyan.album.http;

import com.xuyan.util.SharpParameter;

public class SrxMobileException extends Exception {

	/**
	 * @param e
	 */
	public SrxMobileException(Exception e) {
		e.printStackTrace();
		RequestAid.setRequestHeader("Cookie", SharpParameter.cookie);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param format
	 * @param errCode
	 */
	public SrxMobileException(String format, int errCode) {
		RequestAid.setRequestHeader("Cookie", SharpParameter.cookie);
		// TODO Auto-generated constructor stub
	}

}
