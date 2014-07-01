package com.mc.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mc.util.HttpUtil;
import com.mc.util.Md5;

public class SelfServiceChangePW extends HttpServlet {
	public SelfServiceChangePW() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		 String session = request.getParameter("session");// session
		 String username = request.getParameter("username");
		 String password_o = request.getParameter("password_o");
		 String password_n = request.getParameter("password_n");//新密码
		 Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("password_o", password_o);
			map.put("password_n", password_n);
			map.put("password_c", password_n);
			map.put("Submit", "%CC%E1%BD%BB");
			String result =  HttpUtil.http(HttpUtil.CHANGE_PW_URL, map,session);
			System.out.println(result);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			if (result.split("\\，")[0].equals("密码修改成功")) {
				out.write("change_success");
			}else {
				out.write("0");
			}
	}

}
