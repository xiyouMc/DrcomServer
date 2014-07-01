package com.mc.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mc.connectiondb.DBUtil;
import com.mc.util.HttpUtil;
import com.mc.util.Md5;

public class LoginServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String sessionID= request.getParameter("session");
		System.out.println(sessionID);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		/*String sql = "insert into drcom_user(?,?)";
		//将用户的账号和密码保存到数据库
		DBUtil dbutil = new DBUtil();
		Connection conn = null;
		try {
			conn = dbutil.openConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.execute();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			dbutil.closeConn(conn);
		}
		dbutil.closeConn(conn);*/
		String jiamiID = request.getParameter("sessionID");
		String serial = request.getParameter("serial");
		/*myThread mThread = new myThread(request.getParameter("username"),
//				request.getParameter("password"), request
						.getParameter("serial"),sessionID);
		mThread.start();*/
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", username);
		map.put("password", new Md5().secString(password+jiamiID));
		map.put("serial", serial);
		map.put("Submit", "%B5%C7%C2%BC");
		String result =  HttpUtil.http(HttpUtil.LOGIN_URL, map,sessionID);
		System.out.println("登录请求:"+result);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		if (result.equals("验证错误")) {
			result = "yanzhengerror";
		}else if(result.equals("账号密码有误，请重新登录")) {
			result = "loginerror";
		}else {
			result = result;//登录成功 返回个人信息
		}
		out.write(result);
//		System.out.println();
	}

	/*class myThread extends Thread {

		private String username;
		private String password;
		private String serial;
        private String sessionID ;
		public myThread(String username, String password, String serial,String sessionID) {
			// TODO Auto-generated constructor stub
			this.username = username;
			this.password = password;
			this.serial = serial;
			this.sessionID =sessionID;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

	}*/
}
