/**
 *  Copyright 2011 ChinaSoft International Ltd. All rights reserved.
 */
package com.mc.util;
import java.security.*;
/**
 * <p>
 * Title: Md5
 * </p>
 * <p>
 * Description: 将字符串进行MD5不可逆加密的工具
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
public class Md5 {

	/**
	 * @param args

	 */
	
	MessageDigest md5;
	
	public Md5(){
		
		
		try {
			md5=MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error");
		}
		
	}
	
	
	/**
	 * 加密字符串的工具
	 * @param SrcString 源字符串
	 * @return 加密后的字符串
	 * */
	public String secString(String SrcString){
	
		
			
		String resultString="";		
		char[] charArray = SrcString.toCharArray();		 
		byte[] byteArray = new byte[charArray.length];		 
		for (int i=0; i<charArray.length; i++)
		       byteArray[i] = (byte) charArray[i];
		 
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i=0; i<md5Bytes.length; i++)
		    {
		       int val = ((int) md5Bytes[i] ) & 0xff; 
		       if (val < 16) hexValue.append("0");
		       hexValue.append(Integer.toHexString(val));
		    }
		resultString=hexValue.toString();		
		return resultString;
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Md5 md5=new Md5();
//System.out.println(args[0]);
	System.out.println(md5.secString("19930923f4ac1ef83490c399a9878c465dda44d8"));
	}
}
