package com.mc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.htmlparser.tags.Html;

/**
 * 
 * @author Administrator
 * @description 记得修改代码，当和服务器响应的时候需要 fall back 代码
 */
public class HttpUtil {
	// 基础URL
	// public static final String BASE_URL="http://10.0.2.2:8080/ShopServer/";
	// public static final String BASE_URL = "http://192.168.137.1:8080/TuoC/";
	public static final String BASE_URL = "http://124.89.91.246/SelfServiceLogin.jsp";
	public static final String LOGIN_URL = "http://124.89.91.246/SelfServiceTemp.jsp";
	public static final String CHANGE_PW_URL = "http://124.89.91.246/SelfServiceChangePW"; // 修改密码
	public static final String MAIN_URL = "http://124.89.91.246/SelfServiceMain.jsp?displaymode=";// 缴费记录
	// public static String SERVER_ADDRESS="192.168.1.103";
	/*
	 * public static String SERVER_ADDRESS="192.168.11.1"; public static int
	 * SERVER_PORT = 8080;
	 */
	private static String sessionid;
	public static String CONNECT_EXCEPTION = "网络异常！";

	/**
	 * 登錄請求/修改密碼
	 * 
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static String http(String url, Map<String, String> params,
			String cookie) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			// sb.substring(0, sb.length() - 1);
		}
		System.out.println("send_url:" + url);
		System.out.println("send_data:"
				+ sb.toString().substring(0, sb.length() - 1));
		// 尝试发送请求
		try {

			/*Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(
					"localhost", 8888));*/
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			// con.setRequestProperty("Content-Type",
			// "text/html;charset=ISO-8859-1");
			// con.setConnectTimeout(6000);// 最大延迟6000毫秒
			con.setRequestProperty("Cookie", cookie);
			con.setInstanceFollowRedirects(false);
			/*
			 * if (new Integer(con.getResponseCode()).toString().equals("302"))
			 * {//获取状态吗 String newurl = con.getHeaderField( "location" );
			 * con.disconnect(); String s = HttpUtil.gethttp(newurl,cookie);
			 * System.out.println("重定向"+newurl + "\n "+s); }else{
			 */
			/*
			 * String cookieval = con .getHeaderField("set-cookie");
			 * System.out.println(cookie+" "+cookieval);
			 */
			// con.setRequestProperty("Content-Length", "0");
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(con.getOutputStream());
			// 发送请求参数
			out.print(sb.toString().substring(0, sb.length() - 1).toString());
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			if (new Integer(con.getResponseCode()).toString().equals("302")) {// 获取状态吗
				String newurl = con.getHeaderField("location");
				con.disconnect();
				result = HttpUtil.gethttp(newurl, cookie, "NEWS");// 返回重定向的页面
				System.out.println("重定向" + newurl + "\n " + result);
			}
			// }
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String gethttp(String url, String cookie, String displaymode) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL u = null;
		HttpURLConnection con = null;
		System.out.println("send_url:" + url);
		// 尝试发送请求
		try {
			/*Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(
					"localhost", 8888));*/
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			// con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "text/html;charset=GB2312");
			// con.setConnectTimeout(6000);// 最大延迟6000毫秒
			con.setRequestProperty("Cookie", cookie);
			// if (url.equals(NEWS_URL)) {//如果是登录页面

			// }

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

			String s = HtmlUtil.getHtmlTittle(result);// 获取主题名字
			System.out.println("打印:" + s);

			if (s.equals("登录页面")) {// 回到主页 登录失败
				return "验证错误";
			} else {
				if (s.equals("selfserviceerror")) {// 密码错误 或者账号错误 如果是更改密码的话 返回
					String str = HtmlUtil.getHtmlBody(result).split("\\,")[0];// 获取
																				// 错误原因
					return str;
				} else if (s.equals("自服务网页")) {// 登录成功
					// 这里返回 自服务页面的 个人信息
					// HtmlUtil.parseHtmlTR(result);
					return HtmlUtil.parseHtmlTR(result, displaymode);
				}
			}

		} catch (Exception e) {
			System.out.println("发送 get 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 修改密碼
	 */
	
}
