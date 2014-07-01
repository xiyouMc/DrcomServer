package com.mc.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Session;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import com.mc.jsonutil.JSONUtil;
import com.mc.util.FilePathUtil;
import com.mc.util.HtmlUtil;
import com.mc.util.HttpUtil;

public class GetPic extends HttpServlet {

	static String cookie = "";
	static String jiamiSessionID = "";
	/**
	 * Constructor of the object.
	 */
	public GetPic() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		getSessionID();
		saveImage();
		
		Session session = new Session();
        session.setCookieSessionID(cookie);
        session.setSessionID(jiamiSessionID);
        session.setPicurl(FilePathUtil.picPath);
        PrintWriter out = response.getWriter();
        out.write(JSONUtil.toJSON(session));
	}

	//得到图片
	public static InputStream getInputStream() {
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;
		try {
		
			URL url = new URL(
					"http://124.89.91.246/CreateImage?Image=aa&Rgb=255|0|0");
			if (url != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection();
				String sessionid = null;
				httpURLConnection.setConnectTimeout(6000);// 最大延迟6000毫秒
				httpURLConnection.setRequestProperty("Cookie", cookie);
				int responseCode = httpURLConnection.getResponseCode();
				if (responseCode == 200) {
					inputStream = httpURLConnection.getInputStream();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;

	}

	/**
	 * 获取 对密码进行加密的sessionid
	 */
	private static String getSessionID (){
		 String script = "error";
		 HttpURLConnection httpURLConnection = null;
			try {
//				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("localhost", 8888));
				URL url = new URL(
						HttpUtil.BASE_URL);
				if (url != null) {
					httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setDoOutput(true);
					httpURLConnection.setDoInput(true);
					httpURLConnection.setUseCaches(false);
					httpURLConnection.setConnectTimeout(6000);// 最大延迟6000毫秒
					String sessionid = null;
					String cookieval = httpURLConnection
					.getHeaderField("set-cookie");
			if (cookieval != null) {
				sessionid = cookieval.substring(0, cookieval.indexOf(";"));
//				System.out.println(sessionid);
			}
			cookie = sessionid;
				}
				script = HtmlUtil.getHtmlTag(httpURLConnection, "script");
     /*   System.out.println("========================================");
//        System.out.println(script);
        System.out.println("========================================");
        System.out.println(script.split("\\}")[script.split("\\}").length-2].trim().split("\\;")[0].split("\"")[1]);*/
        //获取 sessionID
        script =  script.split("\\}")[script.split("\\}").length-2].trim().split("\\;")[0].split("\"")[1];
        jiamiSessionID = script;
			}
    catch( Exception e ) {     
        e.printStackTrace();
    }
    return script;
	}
/**
 * 保存图片
 */
	public static void saveImage() {
		InputStream inputStream = getInputStream();
		FileOutputStream fileOutputStream = null;
		byte[] data = new byte[1024];
		int len = 0;
		try {
			fileOutputStream = new FileOutputStream(FilePathUtil.tomcatPath+"\\2.jpg");
			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
