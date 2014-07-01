package com.mc.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mc.util.HttpUtil;

public class ServiceMain extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ServiceMain() {
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

		String displaymode = request.getParameter("displaymode");// 获取请求类型
		String session = request.getParameter("session");// session
	    String url = HttpUtil.MAIN_URL+displaymode;
		/*if (displaymode.equals("PAID")) {// 缴费记录

		}
		if (displaymode.equals("LD")) {// 登录记录

		}
		if (displaymode.equals("ChangePW")) {// 修改密码

		}
		if (displaymode.equals("UD")) {// 詳細記錄

		}*/
	    System.out.println("****************************************");
//	    HttpUtil.gethttp(url, session,displaymode);
	    String result = HttpUtil.gethttp(url, session,displaymode);
	    response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    out.write(result);
		  System.out.println("****************************************");
	}

}
